package br.com.testmagnumbk.magnum_bk_teste.v1.application.service;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.request.LoginRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.request.RegisterRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthApplicationService {

    private final AuthService authService;

    public AuthResponse login(LoginRequest request) {
        log.debug("[start] AuthApplicationService - login");
        AuthResponse response = authService.login(request);
        log.debug("[finish] AuthApplicationService - login");
        return response;
    }

    public AuthResponse register(RegisterRequest request) {
        log.debug("[start] AuthApplicationService - register");
        AuthResponse response = authService.register(request);
        log.debug("[finish] AuthApplicationService - register");
        return response;
    }
}
