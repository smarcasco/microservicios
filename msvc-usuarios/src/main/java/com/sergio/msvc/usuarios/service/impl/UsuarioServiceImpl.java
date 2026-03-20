package com.sergio.msvc.usuarios.service.impl;

import com.sergio.msvc.usuarios.clients.CursoClientRest;
import com.sergio.msvc.usuarios.model.entity.Usuario;
import com.sergio.msvc.usuarios.repository.UsuarioRepository;
import com.sergio.msvc.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private CursoClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return (List<Usuario>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllById(Iterable<Long> ids) {
        return (List<Usuario>) repository.findAllById(ids);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Usuario> findUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Usuario> findUserByEmail(String email) {
        return Optional.ofNullable(repository.findByEmail(email));
    }

    @Transactional
    @Override
    public Usuario saveUser(Usuario usuario) {
        return repository.save(usuario);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        client.eliminarUsuario(id);
        repository.deleteById(id);
    }
}
