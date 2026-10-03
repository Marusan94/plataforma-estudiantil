package com.edu.plataforma.controller;

import com.edu.plataforma.dto.*;
import com.edu.plataforma.service.BienestarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BienestarController {

    private final BienestarService bienestarService;

    public BienestarController(BienestarService bienestarService) {
        this.bienestarService = bienestarService;
    }

    // Necesidades de Apoyo
    @PostMapping("/necesidades")
    public ResponseEntity<NecesidadApoyoDTO> registrarNecesidad(@Valid @RequestBody NecesidadApoyoDTO dto) {
        return new ResponseEntity<>(bienestarService.registrarNecesidad(dto), HttpStatus.CREATED);
    }

    @GetMapping("/necesidades")
    public ResponseEntity<List<NecesidadApoyoDTO>> listarNecesidades() {
        return ResponseEntity.ok(bienestarService.listarTodas());
    }

    @GetMapping("/necesidades/{id}")
    public ResponseEntity<NecesidadApoyoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bienestarService.obtenerPorId(id));
    }

    @GetMapping("/necesidades/estudiante/{id}")
    public ResponseEntity<List<NecesidadApoyoDTO>> listarPorEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(bienestarService.listarPorEstudiante(id));
    }

    @GetMapping("/necesidades/categoria/{id}")
    public ResponseEntity<List<NecesidadApoyoDTO>> listarPorCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(bienestarService.listarPorCategoria(id));
    }

    @PutMapping("/necesidades/{id}")
    public ResponseEntity<NecesidadApoyoDTO> actualizarNecesidad(@PathVariable Long id, @RequestBody NecesidadApoyoDTO dto) {
        return ResponseEntity.ok(bienestarService.actualizarNecesidad(id, dto));
    }

    @DeleteMapping("/necesidades/{id}")
    public ResponseEntity<Map<String, String>> eliminarNecesidad(@PathVariable Long id) {
        bienestarService.eliminarNecesidad(id);
        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "Solicitud de apoyo eliminada correctamente");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/necesidades/{id}/asignar-profesional")
    public ResponseEntity<NecesidadApoyoDTO> asignarProfesional(@PathVariable Long id, @RequestParam Long idProfesional) {
        return ResponseEntity.ok(bienestarService.asignarProfesional(id, idProfesional));
    }

    @PutMapping("/necesidades/{id}/estado")
    public ResponseEntity<Map<String, Object>> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        NecesidadApoyoDTO act = bienestarService.cambiarEstado(id, estado);
        Map<String, Object> res = new HashMap<>();
        res.put("mensaje", "Estado actualizado a: " + act.getEstado());
        res.put("necesidad", act);
        return ResponseEntity.ok(res);
    }

    // Seguimientos
    @PostMapping("/seguimientos")
    public ResponseEntity<SeguimientoDTO> registrarSeguimiento(@Valid @RequestBody SeguimientoDTO dto) {
        return new ResponseEntity<>(bienestarService.registrarSeguimiento(dto), HttpStatus.CREATED);
    }

    @GetMapping("/necesidades/{id}/seguimientos")
    public ResponseEntity<List<SeguimientoDTO>> listarSeguimientos(@PathVariable Long id) {
        return ResponseEntity.ok(bienestarService.listarSeguimientos(id));
    }

    // Tipos de Apoyo
    @PostMapping("/tipos-apoyo")
    public ResponseEntity<TipoApoyoDTO> registrarTipoApoyo(@Valid @RequestBody TipoApoyoDTO dto) {
        return new ResponseEntity<>(bienestarService.registrarTipoApoyo(dto), HttpStatus.CREATED);
    }

    @GetMapping("/tipos-apoyo")
    public ResponseEntity<List<TipoApoyoDTO>> listarTiposApoyo() {
        return ResponseEntity.ok(bienestarService.listarTiposApoyo());
    }

    // Categorias de Necesidad
    @PostMapping("/categorias-necesidad")
    public ResponseEntity<CategoriaNecesidadDTO> registrarCategoria(@Valid @RequestBody CategoriaNecesidadDTO dto) {
        return new ResponseEntity<>(bienestarService.registrarCategoria(dto), HttpStatus.CREATED);
    }

    @GetMapping("/categorias-necesidad")
    public ResponseEntity<List<CategoriaNecesidadDTO>> listarCategorias() {
        return ResponseEntity.ok(bienestarService.listarCategorias());
    }

    // Profesionales
    @PostMapping("/profesionales")
    public ResponseEntity<ProfesionalDTO> registrarProfesional(@Valid @RequestBody ProfesionalDTO dto) {
        return new ResponseEntity<>(bienestarService.registrarProfesional(dto), HttpStatus.CREATED);
    }

    @GetMapping("/profesionales")
    public ResponseEntity<List<ProfesionalDTO>> listarProfesionales() {
        return ResponseEntity.ok(bienestarService.listarProfesionales());
    }

    @GetMapping("/profesionales/{id}/necesidades")
    public ResponseEntity<List<NecesidadApoyoDTO>> listarPorProfesional(@PathVariable Long id) {
        return ResponseEntity.ok(bienestarService.listarPorProfesional(id));
    }

    @PutMapping("/profesionales/{id}")
    public ResponseEntity<ProfesionalDTO> actualizarProfesional(@PathVariable Long id, @Valid @RequestBody ProfesionalDTO dto) {
        return ResponseEntity.ok(bienestarService.actualizarProfesional(id, dto));
    }

    @DeleteMapping("/profesionales/{id}")
    public ResponseEntity<Map<String, String>> eliminarProfesional(@PathVariable Long id) {
        bienestarService.eliminarProfesional(id);
        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "Profesional eliminado exitosamente");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/reportes/necesidades-tipo")
    public ResponseEntity<List<Map<String, Object>>> reportePorTipo() {
        return ResponseEntity.ok(bienestarService.reporteNecesidadesPorTipo());
    }
}