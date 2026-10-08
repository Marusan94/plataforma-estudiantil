package com.edu.plataforma.controller;

import com.edu.plataforma.dto.RecursoBibliotecaDTO;
import com.edu.plataforma.service.RecursoBibliotecaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {

    private final RecursoBibliotecaService recursoService;

    public BibliotecaController(RecursoBibliotecaService recursoService) {
        this.recursoService = recursoService;
    }

    @PostMapping
    public ResponseEntity<RecursoBibliotecaDTO> crearRecurso(@Valid @RequestBody RecursoBibliotecaDTO dto) {
        return new ResponseEntity<>(recursoService.crearRecurso(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecursoBibliotecaDTO>> listarRecursos() {
        return ResponseEntity.ok(recursoService.listarRecursos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoBibliotecaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(recursoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoBibliotecaDTO> actualizarRecurso(@PathVariable Long id, @Valid @RequestBody RecursoBibliotecaDTO dto) {
        return ResponseEntity.ok(recursoService.actualizarRecurso(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecurso(@PathVariable Long id) {
        recursoService.eliminarRecurso(id);
        return ResponseEntity.noContent().build();
    }
}
