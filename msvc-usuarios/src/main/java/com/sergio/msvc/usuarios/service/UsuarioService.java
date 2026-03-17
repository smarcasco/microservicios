package com.sergio.msvc.usuarios.service;

import com.sergio.msvc.usuarios.model.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listar();

    Optional<Usuario> buscarUsuarioPorId(Long id);

    Usuario guardar (Usuario usuario);

    void eliminar (Long id);

}
