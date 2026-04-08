package com.sergio.msvc.springcloud.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;

public class PasswordGrantAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    public PasswordGrantAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this.userDetailsService    = userDetailsService;
        this.passwordEncoder       = passwordEncoder;
        this.authorizationService  = authorizationService;
        this.tokenGenerator        = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PasswordGrantAuthenticationToken authToken =
                (PasswordGrantAuthenticationToken) authentication;

        // ── 1. Verificar que el cliente está autenticado ──────────────────
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClient(authToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        if (registeredClient == null || !registeredClient.getAuthorizationGrantTypes()
                .contains(PasswordGrantAuthenticationToken.PASSWORD)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        // ── 2. Cargar usuario y verificar contraseña ──────────────────────
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(authToken.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        if (!userDetails.isEnabled() || !userDetails.isAccountNonExpired()
                || !userDetails.isAccountNonLocked() || !userDetails.isCredentialsNonExpired()) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        if (!passwordEncoder.matches(authToken.getPassword(), userDetails.getPassword())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        // ── 3. Determinar scopes autorizados ──────────────────────────────
        Set<String> authorizedScopes = new LinkedHashSet<>(registeredClient.getScopes());
        if (authToken.getScopes() != null && !authToken.getScopes().isEmpty()) {
            for (String scope : authToken.getScopes()) {
                if (!registeredClient.getScopes().contains(scope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(authToken.getScopes());
        }

        // ── 4. Crear principal del usuario ────────────────────────────────
        UsernamePasswordAuthenticationToken userPrincipal =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // ── 5. Generar access token ───────────────────────────────────────
        var tokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(userPrincipal)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(PasswordGrantAuthenticationToken.PASSWORD)
                .authorizationGrant(authToken)
                .build();

        OAuth2Token generatedToken = tokenGenerator.generate(tokenContext);
        if (generatedToken == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.SERVER_ERROR);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                generatedToken.getTokenValue(),
                generatedToken.getIssuedAt(),
                generatedToken.getExpiresAt(),
                authorizedScopes);

        // ── 6. Guardar autorización ───────────────────────────────────────
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(userDetails.getUsername())
                .authorizationGrantType(PasswordGrantAuthenticationToken.PASSWORD)
                .authorizedScopes(authorizedScopes)
                .attribute(Principal.class.getName(), userPrincipal);

        if (generatedToken instanceof ClaimAccessor claimAccessor) {
            authorizationBuilder.token(accessToken, meta ->
                    meta.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        authorizationService.save(authorizationBuilder.build());

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClient(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2ClientAuthenticationToken clientToken
                && clientToken.isAuthenticated()) {
            return clientToken;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }
}

