package br.com.testmagnumbk.magnum_bk_teste.v1.infra.security;

import br.com.testmagnumbk.magnum_bk_teste.v1.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado: {}", username);
                    return new UsernameNotFoundException("Usuário não encontrado: " + username);
                });
    }
}
