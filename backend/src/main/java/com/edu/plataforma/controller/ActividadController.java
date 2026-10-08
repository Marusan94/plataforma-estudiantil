package com.edu.plataforma.controller;

import com.edu.plataforma.dto.ActividadDTO;
import com.edu.plataforma.service.ActividadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @PostMapping
    public ResponseEntity<ActividadDTO> crearActividad(@Valid @RequestBody ActividadDTO dto) {
        return new ResponseEntity<>(actividadService.crearActividad(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ActividadDTO>> listarActividades(@RequestParam(required = false) Long cursoId) {
        if (cursoId != null) {
            return ResponseEntity.ok(actividadService.listarPorCurso(cursoId));
        }
        return ResponseEntity.ok(actividadService.listarActividades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(actividadService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadDTO> actualizarActividad(@PathVariable Long id, @Valid @RequestBody ActividadDTO dto) {
        return ResponseEntity.ok(actividadService.actualizarActividad(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarActividad(@PathVariable Long id) {
        actividadService.eliminarActividad(id);
        return ResponseEntity.noContent().build();
    }
}
