package kr.co.teamo.auth.service;

import kr.co.teamo.auth.dto.*;
import kr.co.teamo.auth.mapper.AuthMapper;
import kr.co.teamo.auth.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public SignupResponse signup(SignupRequest req) {

        String email = req.getEmail();
        String password = req.getPassword();
        String name = req.getName();
        String phone = req.getPhone();

        email = email.trim().toLowerCase();

        if (authMapper.countByEmail(email) > 0) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        UserInsertDto dto = new UserInsertDto();
        dto.setEmail(email);
        dto.setPasswordHash(passwordEncoder.encode(password));
        dto.setStatus("ACTIVE");
        dto.setName(name.trim());
        dto.setPhone(phone.trim());

        authMapper.insertUser(dto);

        return SignupResponse.builder()
                .userId(dto.getUserId())
                .email(dto.getEmail())
                .message("회원가입 완료")
                .build();
    }

    public LoginResponse login(LoginRequest req) {
        Map<String,Object> user = authMapper.findByEmail(req.getEmail());

        String passwordHash = (String) user.get("PASSWORD_HASH");
        if (!passwordEncoder.matches(req.getPassword(), passwordHash)) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        Long userId =  ((Number) user.get("USER_ID")).longValue();

        String accessToken = jwtTokenUtil.createAccessToken(userId);
        String refreshToken = jwtTokenUtil.createRefreshToken(userId);

        refreshTokenRedisService.save(userId, refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }

    public RefreshResponse refreshToken(RefreshRequest req) {

        String refreshToken = req.getRefreshToken();

        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new RuntimeException("refresh token이 없습니다.");
        }

        if (!jwtTokenUtil.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 refresh token 입니다.");
        }

        Long userId = jwtTokenUtil.getUserId(refreshToken);

        String savedToken = refreshTokenRedisService.findByUserId(userId);

        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new RuntimeException("로그아웃된 토큰입니다.");
        }

        String newAccessToken = jwtTokenUtil.createAccessToken(userId);
        String newRefreshToken = jwtTokenUtil.createRefreshToken(userId);

        refreshTokenRedisService.save(userId, newRefreshToken);

        return new RefreshResponse(newAccessToken, newRefreshToken);
    }


    @Transactional
    public void withdrawUser(Long userId){
        authMapper.withdrawUser(userId);
        refreshTokenRedisService.delete(userId);
    }

    public boolean existsByEmail(String email) {
        return authMapper.existsEmail(email) > 0;
    }

    public User getMe(){
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();

        User user = authMapper.findById(userId);

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        return new User(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getRole(),
                user.getProfileImageUrl()
        );
    }

    public void logout(Long userId){
        redisTemplate.delete("refresh:" + userId);
    }
}
