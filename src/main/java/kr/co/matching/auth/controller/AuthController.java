package kr.co.matching.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.matching.auth.dto.SignupRequest;
import kr.co.matching.auth.dto.SignupResponse;
import kr.co.matching.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody SignupRequest req) {
        return authService.signup(req);
    }
}
