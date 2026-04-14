package kr.co.teamo.auth.service;

import kr.co.teamo.auth.dto.*;
import kr.co.teamo.auth.mapper.AuthMapper;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.code.UserErrorCode;
import kr.co.teamo.common.exception.CustomException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Builder
public class AuthService {

    private final AuthMapper authMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRedisService refreshTokenRedisService;
    private final RedisTemplate<Object, Object> redisTemplate;

    // 회원가입
    @Transactional
    public SignupResponse signup(SignupRequest req) {

        String email = req.getEmail().trim().toLowerCase();

        if (authMapper.countByEmail(email) > 0) {
            throw new CustomException(UserErrorCode.EMAIL_DUPLICATED);
        }

        UserInsertDto dto = UserInsertDto.builder()
        .email(email)
        .passwordHash(passwordEncoder.encode(req.getPassword()))
        .status("ACTIVE")
        .name(req.getName().trim())
        .phone(req.getPhone().trim())
        .build();

        authMapper.insertUser(dto);

        Long userId = dto.getUserId();

        if (req.getDtlCdIds() != null) {
            for (String dtlCdId : req.getDtlCdIds()) {
                authMapper.insertUserLanguage(userId, dtlCdId);
            }
        }

        return SignupResponse.builder()
                .userId(userId)
                .email(email)
                .message("회원가입 완료")
                .build();
    }

    // 로그인
    public LoginResponse login(LoginRequest req) {

        LoginDto user = authMapper.findByEmail(req.getEmail());

        if (user == null) {
            throw new CustomException(UserErrorCode.INVALID_INFO);
        }

        String passwordHash = user.getPasswordHash();

        if (!passwordEncoder.matches(req.getPassword(), passwordHash)) {
            throw new CustomException(UserErrorCode.INVALID_PASSWORD);
        }

        Long userId = user.getUserId();

        String accessToken = jwtTokenUtil.createAccessToken(userId);
        String refreshToken = jwtTokenUtil.createRefreshToken(userId);

        refreshTokenRedisService.save(userId, refreshToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 재발급
    public RefreshResponse refreshToken(RefreshRequest req) {

        String refreshToken = req.getRefreshToken();

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new CustomException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!jwtTokenUtil.validateToken(refreshToken)) {
            throw new CustomException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtTokenUtil.getUserId(refreshToken);

        String savedToken = refreshTokenRedisService.findByUserId(userId);

        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new CustomException(UserErrorCode.INVALID_LOGOUT_TOKEN);
        }

        String newAccessToken = jwtTokenUtil.createAccessToken(userId);
        String newRefreshToken = jwtTokenUtil.createRefreshToken(userId);

        refreshTokenRedisService.save(userId, newRefreshToken);

        return RefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    // 회원 탈퇴
    @Transactional
    public void withdrawUser(Long userId, String currentPassword) {

        User user = authMapper.findById(userId);

    // 일반 로그인만 비밀번호 검증
        if (!user.getProvider().equals("GOOGLE") &&
            !user.getProvider().equals("KAKAO") &&
            !user.getProvider().equals("GITHUB")) {

            if (currentPassword == null || currentPassword.isEmpty()) {
                throw new IllegalArgumentException("현재 비밀번호를 입력해주세요.");
            }

            if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }

        // 소셜이면 UNLINK 먼저
        if(user.getProvider().equals("GOOGLE") ||
            user.getProvider().equals("KAKAO") ||
            user.getProvider().equals("GITHUB")) {

            SocialUnlinkDto account = authMapper.findSocialByUserId(userId);

            String provider = account.getProvider();
            String token = account.getProviderAccessToken();
            unlink(provider,token);
        }

        // 탈퇴 처리
        authMapper.withdrawUser(userId);

        // 토큰 삭제
        refreshTokenRedisService.delete(userId);
    }

    private void unlink(String provider, String token) {
        switch (provider) {
            case "KAKAO":
                unlinkKakao(token);
                break;
            case "GOOGLE":
                unlinkGoogle(token);
                break;
        }
    }

    public void unlinkKakao(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);

        restTemplate.postForEntity(
                "https://kapi.kakao.com/v1/user/unlink",
                request,
                String.class
        );
    }

    public void unlinkGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://oauth2.googleapis.com/revoke?token=" + accessToken;

        restTemplate.postForEntity(url, null, String.class);
    }

    // 로그인 사용자 조회
    public User getMe() {

        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();

        User user = authMapper.findById(userId);

        if (user == null) {
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }

        return user;
    }

    // 로그아웃
    public void logout(Long userId) {

        redisTemplate.delete("refresh:" + userId);
    }

    // 이메일 중복 체크
    public Boolean existsByEmail(String email) {
        return authMapper.existsEmail(email) > 0;
    }

    // 기술 스택 조회
    public List<TechStackResponse> getTechStacks(){
        return authMapper.findAll();
    }
    
    // 마이페이지 수정
    @Transactional
    public void updateMe(Long userId, UpdateUserRequest request){
        // 🔥 1. 소셜 로그인 여부 체크
        boolean isSocialUser = authMapper.existsByUserId(userId);

        if (isSocialUser && request.getPasswordHash() != null && !request.getPasswordHash().isBlank()) {
            throw new CustomException(UserErrorCode.SOCIAL_PW_BLOCKED);
        }

        // 2. 비밀번호 인코딩
        String encodedPassword = null;
        if(request.getPasswordHash() != null && !request.getPasswordHash().isBlank()){
            encodedPassword = passwordEncoder.encode(request.getPasswordHash());
        }

        // 3. 유저 정보 수정
        authMapper.updateUser(userId, request.getName(), encodedPassword);

        // 4. 기술스택 초기화
        authMapper.deleteUserLanguage(userId);

        // 5. 기술스택 재등록
        if(request.getDtlCdIds() != null){
            for(String dtlCdId : request.getDtlCdIds()){
                authMapper.insertUserLanguage(userId, dtlCdId);
            }
        }
    }

    // 소셜 로그인 추가(google)
    @Transactional
    public SocialLoginResponse socialLogin(
            String email,
            String name,
            String provider,
            String providerUserId,
            String providerAccessToken
    ) {
        SocialAccount account = authMapper.findSocialAccount(provider, providerUserId);

        Long userId;
        boolean isNew = false;

        if (account != null) {
            userId = account.getUserId();

            User user = authMapper.findById(userId);

            if ("DEACTIVATE".equals(user.getStatus())) {
                authMapper.reactivateUser(userId);
            }
        } else {
            LoginDto user = authMapper.findByEmail(email);

            if (user != null) {
                userId = user.getUserId();

                authMapper.insertSocialAccount(
                        userId,
                        provider,
                        providerUserId,
                        providerAccessToken
                );

            } else {
                UserInsertDto dto = UserInsertDto.builder()
                        .email(email)
                        .passwordHash("SOCIAL_LOGIN")
                        .status("ACTIVE")
                        .name(name)
                        .phone(null)
                        .build();

                authMapper.insertUser(dto);
                userId = dto.getUserId();

                authMapper.insertSocialAccount(
                        userId,
                        provider,
                        providerUserId,
                        providerAccessToken
                );

                isNew = true;
            }
        }

        String accessToken = jwtTokenUtil.createAccessToken(userId);
        String refreshToken = jwtTokenUtil.createRefreshToken(userId);

        refreshTokenRedisService.save(userId, refreshToken);

        return SocialLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isNew(isNew)
                .build();
    }
}