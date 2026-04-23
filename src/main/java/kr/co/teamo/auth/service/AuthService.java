package kr.co.teamo.auth.service;

import kr.co.teamo.auth.dto.*;
import kr.co.teamo.auth.mapper.AuthMapper;
import kr.co.teamo.auth.util.AesEncryptor;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.code.UserErrorCode;
import kr.co.teamo.common.exception.CustomException;
import kr.co.teamo.common.file.dto.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRedisService refreshTokenRedisService;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final AesEncryptor aesEncryptor;
    @Value("${file.upload.path}")
    private String uploadPath;

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

        String accessToken = jwtTokenUtil.createAccessToken(userId);
        String refreshToken = jwtTokenUtil.createRefreshToken(userId);

        refreshTokenRedisService.save(userId, refreshToken);

        return SignupResponse.builder()
                .userId(userId)
                .email(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .message("회원가입 및 로그인 완료")
                .build();
    }

    // 로그인
    public LoginResponse login(LoginRequest req) {

        LoginDto user = authMapper.findByEmail(req.getEmail());

        if (user == null) {
            throw new CustomException(UserErrorCode.INVALID_INFO);
        }

        if (!"ACTIVE".equals(user.getStatus())) {
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

        String provider = user.getProvider() == null ? "LOCAL" : user.getProvider();

        boolean isSocial = !"LOCAL".equals(provider);
        // 일반 로그인
        if(!isSocial) {
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("현재 비밀번호를 입력해주세요.");
            }

            if (!passwordEncoder.matches(currentPassword.trim(), user.getPasswordHash())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }

        // 소셜 로그인
        if(isSocial) {
            SocialUnlinkDto account = authMapper.findSocialByUserId(userId);

            if(account == null) {
                throw new CustomException(UserErrorCode.SOCIAL_ACCOUNT_NOT_FOUND);
            }

            if (account.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new CustomException(UserErrorCode.SOCIAL_TOKEN_EXPIRED);
            }

            //복호화
            String token = aesEncryptor.decrypt(account.getProviderAccessToken());

            //unlink
            unlink(account.getProvider(),token);
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

        if (user.getFilePath() != null && user.getSaveFileNm() != null) {
            user.setProfileImageUrl(
                    user.getFilePath() + "/" + user.getSaveFileNm()
            );
        }

        return user;
    }

    // 로그아웃
    public void logout(Long userId, String accessToken) {

        // 1. refresh token 삭제 (서비스 통해서)
        refreshTokenRedisService.delete(userId);

        // 2. access token 블랙리스트 등록
        long expiration = jwtTokenUtil.getRemainingTime(accessToken);

        redisTemplate.opsForValue().set(
                "blacklist:" + accessToken,
                "logout",
                Duration.ofMillis(expiration)
        );
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
        User user = authMapper.findById(userId);

        String provider = user.getProvider() == null ? "LOCAL" : user.getProvider();
        boolean isSocialUser = !"LOCAL".equals(provider);

        // 소셜 유저 비밀번호 변경 차단
        if (isSocialUser && request.getPassword() != null && !request.getPassword().isBlank()) {
            throw new CustomException(UserErrorCode.SOCIAL_PW_BLOCKED);
        }

        // 비밀번호 인코딩
        String encodedPassword = null;
        if(request.getPassword() != null && !request.getPassword().isBlank()){
            encodedPassword = passwordEncoder.encode(request.getPassword());
        }

        // 유저 정보 수정
        authMapper.updateUser(userId, request.getName(), encodedPassword);

        // 기술스택 초기화
        authMapper.deleteUserLanguage(userId);

        // 기술스택 재등록
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

            String encryptedToken = aesEncryptor.encrypt(providerAccessToken);

            authMapper.updateSocialAccessToken(
                    userId,
                    encryptedToken,
                    LocalDateTime.now().plusHours(1)
            );
        } else {
            LoginDto user = authMapper.findByEmail(email);

            if (user != null) {
                userId = user.getUserId();

                authMapper.insertSocialAccount(
                        userId,
                        provider,
                        providerUserId,
                        providerAccessToken,
                        LocalDateTime.now().plusHours(1)
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

                String encryptedToken = aesEncryptor.encrypt(providerAccessToken);

                authMapper.insertSocialAccount(
                        userId,
                        provider,
                        providerUserId,
                        encryptedToken,
                        LocalDateTime.now().plusHours(1)
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

    private String getExt(MultipartFile file) {
        String name = file.getOriginalFilename();

        if (name == null || !name.contains(".")) {
            return "";
        }

        return name.substring(name.lastIndexOf(".") + 1);
    }

    public Long upload(MultipartFile file) {

        String savedName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        LocalDate now = LocalDate.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String baseDir = Paths.get(uploadPath, "profile").toString();
        String fullPath = Paths.get(baseDir, datePath).toString();

        File dir = new File(fullPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new RuntimeException("디렉토리 생성 실패: " + fullPath);
            }
        }

        try {
            File dest = new File(fullPath + "/" + savedName);
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        FileDto dto = new FileDto();
        dto.setOrgFileNm(file.getOriginalFilename());
        dto.setSaveFileNm(savedName);

        dto.setFilePath("/upload/profile/" + datePath);

        dto.setFileSize(file.getSize());
        dto.setFileExt(getExt(file));

        authMapper.insert(dto);

        return dto.getFileId();
    }

    @Transactional
    public void updateProfileImage(Long userId, Long fileId) {

        // 1. 기존 프로필 이미지 조회
        Long oldFileId = authMapper.findProfileFileId(userId);

        // 2. USERS 업데이트
        authMapper.updateProfileImage(userId, fileId);

        // 3. 기존 파일 처리 (soft delete)
        if (oldFileId != null && !oldFileId.equals(fileId)) {
            authMapper.softDeleteFile(oldFileId);
        }
    }
}