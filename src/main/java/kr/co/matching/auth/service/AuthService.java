package kr.co.matching.auth.service;

import kr.co.matching.auth.dto.*;
import kr.co.matching.auth.mapper.AuthMapper;
import kr.co.matching.auth.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;;
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

    @Transactional
    public SignupResponse signup(SignupRequest req) {

        String email = req.getEmail();
        String password = req.getPassword();
        String name = req.getName();
        String phone = req.getPhone();

        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("email은 필수입니다.");
        if (password == null || password.trim().isEmpty()) throw new IllegalArgumentException("password는 필수입니다.");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("name은 필수입니다.");
        if (phone == null || phone.trim().isEmpty()) throw new IllegalArgumentException("phone은 필수입니다.");

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
        if(user == null){
            throw new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String passwordHash = (String) user.get("PASSWORD_HASH");
        if (!passwordEncoder.matches(req.getPassword(), passwordHash)) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        Long userId =  ((Number) user.get("USER_ID")).longValue();

        String accessToken = jwtTokenUtil.createAccessToken(userId);
        String refreshToken = jwtTokenUtil.createRefreshToken(userId);

        authMapper.upsertRefreshToken(userId, refreshToken);

        return new LoginResponse(userId, accessToken, refreshToken);
    }

    public RefreshResponse refreshToken(RefreshRequest req){

        String refreshToken = req.getRefreshToken();

        if(!jwtTokenUtil.validateToken(refreshToken)){
            throw new RuntimeException("유효하지 않은 refresh token 입니다.");
        }

        Long userId = jwtTokenUtil.getUserId(refreshToken);

        String savedToken = authMapper.findRefreshToken(userId);

        if(savedToken == null || !savedToken.equals(refreshToken)){
            throw new RuntimeException("로그아웃된 토큰입니다.");
        }

        String newAccessToken = jwtTokenUtil.createAccessToken(userId);
        String newRefreshToken = jwtTokenUtil.createRefreshToken(userId);

        authMapper.upsertRefreshToken(userId,newRefreshToken);

        return new RefreshResponse(newAccessToken,newRefreshToken);
    }

    @Transactional
    public void withdrawUser(Long userId){
        authMapper.withdrawUser(userId);
        authMapper.deleteRefreshToken(userId);
    }
}
