package kr.co.teamo.auth.service;

import kr.co.teamo.auth.dto.*;
import kr.co.teamo.auth.mapper.AuthMapper;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.code.UserErrorCode;
import kr.co.teamo.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
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

        UserInsertDto dto = new UserInsertDto();
        dto.setEmail(email);
        dto.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        dto.setStatus("ACTIVE");
        dto.setName(req.getName().trim());
        dto.setPhone(req.getPhone().trim());

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

        return new LoginResponse(accessToken, refreshToken);
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

        return new RefreshResponse(newAccessToken, newRefreshToken);
    }

    // 회원 탈퇴
    @Transactional
    public void withdrawUser(Long userId) {

        authMapper.withdrawUser(userId);

        refreshTokenRedisService.delete(userId);
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
            String providerUserId
    ) {
        SocialAccount account = authMapper.findSocialAccount(provider, providerUserId);

        Long userId;
        boolean isNew = false;

        if (account != null) {
            userId = account.getUserId();
        } else {
            LoginDto user = authMapper.findByEmail(email);

            if (user != null) {
                userId = user.getUserId();

                authMapper.insertSocialAccount(
                        userId,
                        provider,
                        providerUserId
                );

            } else {
                UserInsertDto dto = new UserInsertDto();
                dto.setEmail(email);
                dto.setPasswordHash("SOCIAL_LOGIN");
                dto.setStatus("ACTIVE");
                dto.setName(name);
                dto.setPhone(null);

                authMapper.insertUser(dto);
                userId = dto.getUserId();

                authMapper.insertSocialAccount(
                        userId,
                        provider,
                        providerUserId
                );

                isNew = true;
            }
        }

        String accessToken = jwtTokenUtil.createAccessToken(userId);
        String refreshToken = jwtTokenUtil.createRefreshToken(userId);

        refreshTokenRedisService.save(userId, refreshToken);

        return new SocialLoginResponse(accessToken, refreshToken, isNew);
    }
}