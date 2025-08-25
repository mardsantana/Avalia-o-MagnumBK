package br.com.testmagnumbk.magnum_bk_teste.v1.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private UUID userId;
    private String username;
    private String email;
    private String role;
    private LocalDateTime expiresAt;
}
