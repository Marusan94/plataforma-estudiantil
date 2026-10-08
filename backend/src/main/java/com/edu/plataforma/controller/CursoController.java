package com.edu.plataforma.controller;

import com.edu.plataforma.dto.CursoDTO;
import com.edu.plataforma.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<CursoDTO> crearCurso(@Valid @RequestBody CursoDTO dto) {
        return new ResponseEntity<>(cursoService.crearCurso(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        return ResponseEntity.ok(cursoService.listarCursos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> actualizarCurso(@PathVariable Long id, @Valid @RequestBody CursoDTO dto) {
        return ResponseEntity.ok(cursoService.actualizarCurso(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.noContent().build();
    }
}
