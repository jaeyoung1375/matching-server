package kr.co.matching.auth.service;

import kr.co.matching.auth.dto.SignupRequest;
import kr.co.matching.auth.dto.SignupResponse;
import kr.co.matching.auth.dto.UserInsertDto;
import kr.co.matching.auth.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;
    private final BCryptPasswordEncoder passwordEncoder;

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
}
