package com.sergio.msvc.usuarios.repository;

import com.sergio.msvc.usuarios.model.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
}
