package com.sergio.msvc.usuarios.repository;

import com.sergio.msvc.usuarios.model.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    /**
     * Busca un usuario por su email.
     * @param email El email del usuario a buscar.
     * @return El usuario encontrado o null si no se encuentra ningún usuario con ese email.
     */
    Usuario findByEmail(String email);
}
