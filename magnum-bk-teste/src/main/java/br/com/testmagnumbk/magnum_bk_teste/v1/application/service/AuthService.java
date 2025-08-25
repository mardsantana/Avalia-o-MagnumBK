package br.com.testmagnumbk.magnum_bk_teste.v1.application.service;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.request.LoginRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.request.RegisterRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.AuthResponse;
import br.com.testmagnumbk.magnum_bk_teste.v1.domain.model.Usuario;
import br.com.testmagnumbk.magnum_bk_teste.v1.domain.repository.UsuarioRepository;
import br.com.testmagnumbk.magnum_bk_teste.v1.infra.exception.AuthenticationException;
import br.com.testmagnumbk.magnum_bk_teste.v1.infra.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse login(LoginRequest request) {
        log.debug("[start] AuthService - login para usuário: {}", request.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String token = jwtTokenProvider.generateToken(authentication);
            Usuario usuario = (Usuario) authentication.getPrincipal();

            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setUserId(usuario.getId());
            response.setUsername(usuario.getUsername());
            response.setEmail(usuario.getEmail());
            response.setRole(usuario.getRole().name());
            response.setExpiresAt(LocalDateTime.now().plusHours(1));

            log.debug("[finish] AuthService - login bem-sucedido para usuário: {}", request.getUsername());
            return response;

        } catch (Exception e) {
            log.error("Erro no login para usuário {}: {}", request.getUsername(), e.getMessage());
            throw new AuthenticationException("Credenciais inválidas");
        }
    }

    public AuthResponse register(RegisterRequest request) {
        log.debug("[start] AuthService - register para usuário: {}", request.getUsername());

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new AuthenticationException("Username já está em uso");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationException("Email já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRole(Usuario.Role.USER);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            savedUsuario, null, savedUsuario.getAuthorities()
        );

        String token = jwtTokenProvider.generateToken(authentication);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(savedUsuario.getId());
        response.setUsername(savedUsuario.getUsername());
        response.setEmail(savedUsuario.getEmail());
        response.setRole(savedUsuario.getRole().name());
        response.setExpiresAt(LocalDateTime.now().plusHours(1));

        log.debug("[finish] AuthService - register bem-sucedido para usuário: {}", request.getUsername());
        return response;
    }
}
