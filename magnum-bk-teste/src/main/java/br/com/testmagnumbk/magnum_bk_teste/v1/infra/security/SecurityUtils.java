package br.com.testmagnumbk.magnum_bk_teste.v1.infra.security;

import org.springframework.util.StringUtils;

public class SecurityUtils {

    public static String getJwtFromRequest(String request) {
        if (StringUtils.hasText(request) && request.startsWith("Bearer ")) {
            return request.substring(7);
        }
        return null;
    }

    public static String getJwtFromRequest(jakarta.servlet.http.HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return getJwtFromRequest(bearerToken);
    }
}
