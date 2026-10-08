package com.edu.plataforma.controller;

import com.edu.plataforma.dto.EvaluacionDTO;
import com.edu.plataforma.service.EvaluacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    public EvaluacionController(EvaluacionService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    @PostMapping
    public ResponseEntity<EvaluacionDTO> crearEvaluacion(@Valid @RequestBody EvaluacionDTO dto) {
        return new ResponseEntity<>(evaluacionService.crearEvaluacion(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EvaluacionDTO>> listarEvaluaciones(@RequestParam(required = false) Long cursoId) {
        if (cursoId != null) {
            return ResponseEntity.ok(evaluacionService.listarPorCurso(cursoId));
        }
        return ResponseEntity.ok(evaluacionService.listarEvaluaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(evaluacionService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluacionDTO> actualizarEvaluacion(@PathVariable Long id, @Valid @RequestBody EvaluacionDTO dto) {
        return ResponseEntity.ok(evaluacionService.actualizarEvaluacion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvaluacion(@PathVariable Long id) {
        evaluacionService.eliminarEvaluacion(id);
        return ResponseEntity.noContent().build();
    }
}
