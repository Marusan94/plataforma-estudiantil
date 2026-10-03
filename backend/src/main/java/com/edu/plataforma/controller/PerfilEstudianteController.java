package com.edu.plataforma.controller;

import com.edu.plataforma.dto.*;
import com.edu.plataforma.service.PerfilEstudianteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class PerfilEstudianteController {

    private final PerfilEstudianteService perfilService;

    public PerfilEstudianteController(PerfilEstudianteService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping("/perfiles")
    public ResponseEntity<PerfilEstudianteDTO> crearPerfil(@Valid @RequestBody PerfilEstudianteDTO dto) {
        return new ResponseEntity<>(perfilService.crearPerfil(dto), HttpStatus.CREATED);
    }

    @GetMapping("/perfiles")
    public ResponseEntity<Page<PerfilEstudianteDTO>> listarPerfiles(
            @RequestParam(required = false) String programa,
            @RequestParam(required = false) Integer semestre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(perfilService.listarPerfiles(programa, semestre, pageable));
    }

    @GetMapping("/perfiles/{id}")
    public ResponseEntity<PerfilEstudianteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(perfilService.obtenerPorId(id));
    }

    @GetMapping("/perfil-estudiante/{id}")
    public ResponseEntity<PerfilEstudianteDTO> obtenerPorEstudianteId(@PathVariable Long id) {
        return ResponseEntity.ok(perfilService.obtenerPorEstudianteId(id));
    }

    @GetMapping("/perfiles/busqueda")
    public ResponseEntity<List<PerfilEstudianteDTO>> buscarPorPalabraClave(@RequestParam String query) {
        return ResponseEntity.ok(perfilService.buscarPorPalabraClave(query));
    }

    @PutMapping("/perfiles/{id}")
    public ResponseEntity<PerfilEstudianteDTO> actualizarPerfil(@PathVariable Long id, @RequestBody PerfilEstudianteDTO dto) {
        return ResponseEntity.ok(perfilService.actualizarPerfil(id, dto));
    }

    @DeleteMapping("/perfiles/{id}")
    public ResponseEntity<Map<String, String>> eliminarPerfil(@PathVariable Long id) {
        perfilService.eliminarPerfil(id);
        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "Perfil eliminado correctamente");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/perfiles/{id}/exportar-pdf")
    public ResponseEntity<Map<String, Object>> exportarBorradorPDF(@PathVariable Long id) {
        return ResponseEntity.ok(perfilService.exportarPerfilBorrador(id));
    }

    // Gestion de sub-elementos
    @PostMapping("/perfiles/{id}/habilidades")
    public ResponseEntity<HabilidadDTO> agregarHabilidad(@PathVariable Long id, @Valid @RequestBody HabilidadDTO dto) {
        return new ResponseEntity<>(perfilService.agregarHabilidad(id, dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/habilidades/{id}")
    public ResponseEntity<Map<String, String>> eliminarHabilidad(@PathVariable Long id) {
        perfilService.eliminarHabilidad(id);
        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "Habilidad eliminada correctamente");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/perfiles/{id}/proyectos")
    public ResponseEntity<ProyectoDTO> agregarProyecto(@PathVariable Long id, @Valid @RequestBody ProyectoDTO dto) {
        return new ResponseEntity<>(perfilService.agregarProyecto(id, dto), HttpStatus.CREATED);
    }

    @PutMapping("/proyectos/{id}")
    public ResponseEntity<ProyectoDTO> actualizarProyecto(@PathVariable Long id, @Valid @RequestBody ProyectoDTO dto) {
        return ResponseEntity.ok(perfilService.actualizarProyecto(id, dto));
    }

    @PostMapping("/perfiles/{id}/certificados")
    public ResponseEntity<CertificadoDTO> agregarCertificado(@PathVariable Long id, @Valid @RequestBody CertificadoDTO dto) {
        return new ResponseEntity<>(perfilService.agregarCertificado(id, dto), HttpStatus.CREATED);
    }
}