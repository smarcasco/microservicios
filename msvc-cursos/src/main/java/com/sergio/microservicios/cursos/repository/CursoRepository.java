package com.sergio.microservicios.cursos.repository;

import com.sergio.microservicios.cursos.models.entity.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {
}
