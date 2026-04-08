package com.sergio.msvc.springcloud.services;

import com.sergio.msvc.springcloud.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;

@Service
public class UsuarioService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private WebClient client;

    @Value("${msvc.usuarios.url:http://localhost:8001}")
    private String usuariosUrl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Buscando usuario por email: {}", email);
        log.debug("URL de msvc-usuarios: {}", usuariosUrl);

        try {
            Usuario usuario = client.get()
                    .uri(usuariosUrl + "/byEmail/{email}", email)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Usuario.class)
                    .block();

            if (usuario == null) {
                log.warn("Usuario no encontrado para email: {}", email);
                throw new UsernameNotFoundException("No existe el usuario con email: " + email);
            }

            log.debug("Usuario encontrado: id={}, email={}", usuario.getId(), usuario.getEmail());

            String password = usuario.getPassword();
            if (password != null && !password.startsWith("{") && !password.startsWith("$2a$")) {
                log.debug("Contrasena en texto plano detectada, annadiendo prefijo {{noop}}");
                password = "{noop}" + password;
            } else {
                log.debug("Contrasena ya codificada (prefijo: {})",
                        password != null && password.length() > 7 ? password.substring(0, 7) : "?");
            }

            return new User(
                    email,
                    password,
                    true, true, true, true,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            );

        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (WebClientResponseException e) {
            log.error("Error HTTP al llamar a msvc-usuarios: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new UsernameNotFoundException("Error al buscar usuario: " + email, e);
        } catch (RuntimeException e) {
            log.error("Error al conectar con msvc-usuarios en {}: {}", usuariosUrl, e.getMessage());
            throw new UsernameNotFoundException("No existe el usuario con email: " + email, e);
        }
    }
}
