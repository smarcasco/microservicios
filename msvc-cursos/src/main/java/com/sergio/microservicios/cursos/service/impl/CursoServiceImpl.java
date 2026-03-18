package com.sergio.microservicios.cursos.service.impl;

import com.sergio.microservicios.cursos.entity.Curso;
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
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Curso> buscarCursoPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public Curso guardar(Curso usuario) {
        return repository.save(usuario);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
