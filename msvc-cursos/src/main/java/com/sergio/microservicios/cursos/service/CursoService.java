package com.sergio.microservicios.cursos.service;

import com.sergio.microservicios.cursos.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();

    Optional<Curso> buscarCursoPorId(Long id);

    Curso guardar (Curso usuario);

    void eliminar (Long id);

}
