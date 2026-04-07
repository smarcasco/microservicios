package com.sergio.msvc.springcloud.services;

import com.sergio.msvc.springcloud.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Objects;

public class UsuarioService implements UserDetailsService {

    @Autowired
    private WebClient client;

    @Override
    public UserDetails loadUserByUsername(String email) {
        try {
            Usuario usuario = client.get()
                    .uri("http://msvc-usuarios/byEmail/{email}", uri -> uri.queryParam("email", email).build())
                    .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Usuario.class)
                    .block();
            return new User(email, usuario.getPassword(),
                    true, true, true,
                    true, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        }catch (RuntimeException e) {
            throw new UsernameNotFoundException("No existe el usuario con email: " + email);
        }

    }

}
