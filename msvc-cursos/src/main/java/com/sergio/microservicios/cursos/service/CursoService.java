package com.sergio.microservicios.cursos.service;

import com.sergio.microservicios.cursos.models.entity.Curso;
import com.sergio.microservicios.cursos.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    // Métodos para gestionar cursos
    List<Curso> search();

    Optional<Curso> findCourseById(Long id);

    Curso save(Curso usuario);

    void delete(Long id);

    // Métodos para gestionar usuarios en cursos
    Optional<Usuario> assignCourse(Usuario usuario, Long cursoId);
    Optional<Usuario> createUser(Usuario usuario, Long cursoId);
    Optional<Usuario> deleteUser(Usuario usuario, Long cursoId);


}
