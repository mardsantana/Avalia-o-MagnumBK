package br.com.testmagnumbk.magnum_bk_teste.authentic;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.request.LoginRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.request.RegisterRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.AuthResponse;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.service.AuthApplicationService;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthApplicationServiceTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthApplicationService authApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_deveChamarAuthServiceEretornarResponse() {
        // Arrange
        LoginRequest request = new LoginRequest("user", "pass");
        AuthResponse expectedResponse = AuthResponse.builder()
                .token("token123")
                .tokenType("Bearer")
                .userId(UUID.randomUUID())
                .username("user")
                .email("user@test.com")
                .role("ROLE_USER")
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        when(authService.login(request)).thenReturn(expectedResponse);
        // Act
        AuthResponse result = authApplicationService.login(request);
        // Assert
        assertThat(result).isEqualTo(expectedResponse);
        verify(authService, times(1)).login(request);
    }

    @Test
    void register_deveChamarAuthServiceEretornarResponse() {
        // Arrange
        RegisterRequest request = new RegisterRequest("user", "email@test.com", "pass");
        AuthResponse expectedResponse = AuthResponse.builder()
                .token("token456")
                .tokenType("Bearer")
                .userId(UUID.randomUUID())
                .username("user")
                .email("email@test.com")
                .role("ROLE_USER")
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        when(authService.register(request)).thenReturn(expectedResponse);
        // Act
        AuthResponse result = authApplicationService.register(request);
        // Assert
        assertThat(result).isEqualTo(expectedResponse);
        verify(authService, times(1)).register(request);
    }
}
