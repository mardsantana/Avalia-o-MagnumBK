package br.com.testmagnumbk.magnum_bk_teste.v1.application.api;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.request.LoginRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.request.RegisterRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.AuthResponse;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.service.AuthApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthApplicationService authApplicationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.debug("[start] AuthController - login");
        AuthResponse response = authApplicationService.login(request);
        log.debug("[finish] AuthController - login");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.debug("[start] AuthController - register");
        AuthResponse response = authApplicationService.register(request);
        log.debug("[finish] AuthController - register");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth service is running!");
    }
}
