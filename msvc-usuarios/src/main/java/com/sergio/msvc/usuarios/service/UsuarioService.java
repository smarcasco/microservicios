package com.sergio.msvc.usuarios.service;

import com.sergio.msvc.usuarios.model.entity.Usuario;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> findAll();

    List<Usuario> findAllById(Iterable<Long> ids);

    Optional<Usuario> findUserById(Long id);

    Optional<Usuario> findUserByEmail(String email);

    Usuario saveUser(Usuario usuario);

    void deleteUser(Long id);
}
