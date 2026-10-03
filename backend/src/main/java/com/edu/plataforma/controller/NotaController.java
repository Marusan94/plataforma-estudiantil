package com.edu.plataforma.controller;

import com.edu.plataforma.dto.NotaDTO;
import com.edu.plataforma.dto.NotaPromedioDTO;
import com.edu.plataforma.dto.RankingEstudianteDTO;
import com.edu.plataforma.service.NotaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notas")
public class NotaController {

    private final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    @PostMapping
    public ResponseEntity<NotaDTO> registrarNota(@Valid @RequestBody NotaDTO dto) {
        return new ResponseEntity<>(notaService.registrarNota(dto), HttpStatus.CREATED);
    }

    @GetMapping("/estudiante/{id}")
    public ResponseEntity<List<NotaDTO>> listarPorEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(notaService.listarNotasPorEstudiante(id));
    }

    @GetMapping("/materia/{materiaId}/grupo/{grupoId}")
    public ResponseEntity<List<NotaDTO>> listarPorMateriaYGrupo(@PathVariable Long materiaId, @PathVariable Long grupoId) {
        return ResponseEntity.ok(notaService.listarNotasPorMateriaYGrupo(materiaId, grupoId));
    }

    @GetMapping("/promedio/estudiante/{id}")
    public ResponseEntity<NotaPromedioDTO> calcularPromedioEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(notaService.calcularPromedioEstudiante(id));
    }

    @GetMapping("/bajo-rendimiento")
    public ResponseEntity<List<NotaDTO>> listarBajoRendimiento() {
        return ResponseEntity.ok(notaService.obtenerNotasBajoRendimiento());
    }

    @GetMapping("/ranking/grupo/{id}")
    public ResponseEntity<List<RankingEstudianteDTO>> rankingPorGrupo(@PathVariable Long id) {
        return ResponseEntity.ok(notaService.obtenerRankingGrupo(id));
    }

    @GetMapping("/evolucion/estudiante/{id}")
    public ResponseEntity<List<Map<String, Object>>> evolucionEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(notaService.obtenerEvolucionEstudiante(id));
    }

    @GetMapping("/docente/{id}")
    public ResponseEntity<List<NotaDTO>> listarPorDocente(@PathVariable Long id, @RequestParam(required = false) Long materiaId) {
        return ResponseEntity.ok(notaService.listarNotasPorDocente(id, materiaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaDTO> actualizarNota(@PathVariable Long id, @Valid @RequestBody NotaDTO dto) {
        return ResponseEntity.ok(notaService.actualizarNota(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNota(@PathVariable Long id) {
        notaService.eliminarNota(id);
        return ResponseEntity.noContent().build();
    }
}