package com.sergio.microservicios.cursos.clients;

import com.sergio.microservicios.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-usuarios", url = "http://localhost:8001")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    public Usuario findById(@PathVariable Long id);

    @PostMapping("/")
    public Usuario create(@RequestBody Usuario usuario);
}
