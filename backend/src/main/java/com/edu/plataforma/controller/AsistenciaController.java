package com.edu.plataforma.controller;

import com.edu.plataforma.dto.AsistenciaDTO;
import com.edu.plataforma.dto.AsistenciaLoteDTO;
import com.edu.plataforma.dto.AsistenciaResumenDTO;
import com.edu.plataforma.service.AsistenciaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/asistencias")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @PostMapping
    public ResponseEntity<AsistenciaDTO> registrarAsistencia(@Valid @RequestBody AsistenciaDTO dto) {
        return new ResponseEntity<>(asistenciaService.registrarAsistencia(dto), HttpStatus.CREATED);
    }

    @PostMapping("/lote")
    public ResponseEntity<List<AsistenciaDTO>> registrarLote(@Valid @RequestBody AsistenciaLoteDTO dto) {
        return new ResponseEntity<>(asistenciaService.registrarAsistenciaLote(dto), HttpStatus.CREATED);
    }

    @GetMapping("/estudiante/{id}")
    public ResponseEntity<List<AsistenciaDTO>> listarPorEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.listarPorEstudiante(id));
    }

    @GetMapping("/grupo/{id}")
    public ResponseEntity<List<AsistenciaDTO>> listarPorGrupoYFecha(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate f = fecha != null ? fecha : LocalDate.now();
        return ResponseEntity.ok(asistenciaService.listarPorGrupoYFecha(id, f));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<AsistenciaDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado,
            @RequestParam(required = false) Boolean justificada) {
        return ResponseEntity.ok(asistenciaService.actualizarEstado(id, estado, justificada));
    }

    @GetMapping("/porcentaje/estudiante/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorcentajeEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.calcularPorcentajeEstudiante(id));
    }

    @GetMapping("/resumen/grupo/{id}")
    public ResponseEntity<List<Map<String, Object>>> obtenerResumenGrupo(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.obtenerResumenPorGrupo(id));
    }

    @GetMapping("/resumen-mes")
    public ResponseEntity<List<AsistenciaResumenDTO>> obtenerResumenPorMes() {
        return ResponseEntity.ok(asistenciaService.obtenerResumenPorMes());
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<AsistenciaDTO>> listarPorFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(asistenciaService.listarPorFecha(fecha));
    }

    @GetMapping("/rango")
    public ResponseEntity<List<AsistenciaDTO>> filtrarPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(asistenciaService.filtrarPorRango(fechaInicio, fechaFin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarAsistencia(@PathVariable Long id) {
        asistenciaService.eliminarAsistencia(id);
        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "Registro de asistencia eliminado con exito");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/generar-automaticas")
    public ResponseEntity<Map<String, Object>> generarAutomaticas() {
        return ResponseEntity.ok(asistenciaService.simularAsistenciaAutomatica());
    }
}