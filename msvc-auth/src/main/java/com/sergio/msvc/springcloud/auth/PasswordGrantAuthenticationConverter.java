package com.sergio.msvc.springcloud.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.*;

public class PasswordGrantAuthenticationConverter implements AuthenticationConverter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public Authentication convert(HttpServletRequest request) {
        // Solo actúa para grant_type=password
        if (!"password".equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE))) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);
        String scope    = request.getParameter(OAuth2ParameterNames.SCOPE);

        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(scope.split(" ")));
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        Collections.list(request.getParameterNames()).forEach(param -> {
            if (!List.of(OAuth2ParameterNames.GRANT_TYPE,
                         USERNAME,
                         PASSWORD,
                         OAuth2ParameterNames.SCOPE).contains(param)) {
                additionalParameters.put(param, request.getParameter(param));
            }
        });

        return new PasswordGrantAuthenticationToken(
                username, password, clientPrincipal, requestedScopes, additionalParameters);
    }
}
