package com.sergio.microservicios.cursos.clients;

import com.sergio.microservicios.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "${clients.usuarios.url}")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    public Usuario findById(@PathVariable Long id);

    @GetMapping("/byIds")
    public List<Usuario> findAllByCourse(@RequestParam List<Long> ids);

    @PostMapping("/")
    public Usuario create(@RequestBody Usuario usuario);
}
