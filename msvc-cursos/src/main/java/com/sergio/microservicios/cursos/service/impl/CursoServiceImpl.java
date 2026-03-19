package com.sergio.microservicios.cursos.service.impl;

import com.sergio.microservicios.cursos.models.entity.Curso;
import com.sergio.microservicios.cursos.models.entity.Usuario;
import com.sergio.microservicios.cursos.repository.CursoRepository;
import com.sergio.microservicios.cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> search() {
        return (List<Curso>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Curso> findCourseById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public Curso save(Curso usuario) {
        return repository.save(usuario);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Usuario> assignCourse(Usuario usuario, Long cursoId) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> createUser(Usuario usuario, Long cursoId) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> deleteUser(Usuario usuario, Long cursoId) {
        return Optional.empty();
    }
}
