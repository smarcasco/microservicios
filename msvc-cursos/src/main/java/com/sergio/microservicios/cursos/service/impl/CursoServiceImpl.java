package com.sergio.microservicios.cursos.service.impl;

import com.sergio.microservicios.cursos.clients.UsuarioClientRest;
import com.sergio.microservicios.cursos.models.entity.Curso;
import com.sergio.microservicios.cursos.models.entity.CursoUsuario;
import com.sergio.microservicios.cursos.models.Usuario;
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

    @Autowired
    private UsuarioClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> search() {
        return (List<Curso>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Curso> findCourseById(Long id) {

        Optional<Curso> result = repository.findById(id);
        if (result.isPresent()) {
            Curso curso = result.get();
            if (!curso.getCursoUsuarios().isEmpty()) {

                List<Long> ids = curso.getCursoUsuarios().stream().map(CursoUsuario::getUsuarioId).toList();
                List<Usuario> usuarios = client.findAllByCourse(ids);
                curso.setUsuarios(usuarios);

            }
        }
        return result;
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

    @Transactional
    @Override
    public Optional<Usuario> assignCourse(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = repository.findById(cursoId);
        if (cursoOptional.isPresent()) {
            Usuario usuarioMsvc = client.findById(usuario.getId());
            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Usuario> createUser(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = repository.findById(cursoId);
        if (cursoOptional.isPresent()) {
            Usuario usuarioMsvc = client.create(usuario);
            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Usuario> deleteUserFromCourse(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = repository.findById(cursoId);
        if (cursoOptional.isPresent()) {
            Usuario usuarioMsvc = client.findById(usuario.getId());
            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.removeCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void deleteUser(Long usuarioId) {
        repository.deleteUserFromCourses(usuarioId);
    }
}
