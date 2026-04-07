package com.sergio.msvc.springcloud.services;

import com.sergio.msvc.springcloud.models.Usuario;
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

import java.util.Collections;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private WebClient client;

    @Value("${msvc.usuarios.url:http://localhost:8001}")
    private String usuariosUrl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Usuario usuario = client.get()
                    .uri(usuariosUrl + "/byEmail/{email}", email)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Usuario.class)
                    .block();

            if (usuario == null) {
                throw new UsernameNotFoundException("No existe el usuario con email: " + email);
            }

            return new User(
                    email,
                    usuario.getPassword(),
                    true, true, true, true,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new UsernameNotFoundException("No existe el usuario con email: " + email, e);
        }
    }
}
