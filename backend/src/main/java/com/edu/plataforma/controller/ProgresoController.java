package com.edu.plataforma.controller;

import com.edu.plataforma.dto.ProgresoCursoDTO;
import com.edu.plataforma.service.ProgresoCursoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progreso")
public class ProgresoController {

    private final ProgresoCursoService progresoService;

    public ProgresoController(ProgresoCursoService progresoService) {
        this.progresoService = progresoService;
    }

    @PostMapping
    public ResponseEntity<ProgresoCursoDTO> crearProgreso(@Valid @RequestBody ProgresoCursoDTO dto) {
        return new ResponseEntity<>(progresoService.crearProgreso(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProgresoCursoDTO>> listarProgreso() {
        return ResponseEntity.ok(progresoService.listarProgreso());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgresoCursoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(progresoService.obtenerPorId(id));
    }

    @GetMapping("/estudiante/{id}")
    public ResponseEntity<List<ProgresoCursoDTO>> listarPorEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(progresoService.listarPorEstudiante(id));
    }

    @PutMapping
    public ResponseEntity<ProgresoCursoDTO> upsertProgreso(@Valid @RequestBody ProgresoCursoDTO dto) {
        return ResponseEntity.ok(progresoService.upsertProgreso(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgresoCursoDTO> actualizarProgreso(@PathVariable Long id, @Valid @RequestBody ProgresoCursoDTO dto) {
        return ResponseEntity.ok(progresoService.actualizarProgreso(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProgreso(@PathVariable Long id) {
        progresoService.eliminarProgreso(id);
        return ResponseEntity.noContent().build();
    }
}
