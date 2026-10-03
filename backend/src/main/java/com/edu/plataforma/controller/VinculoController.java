package com.edu.plataforma.controller;

import com.edu.plataforma.dto.VinculoDTO;
import com.edu.plataforma.service.VinculoFamiliarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vinculos")
public class VinculoController {

    private final VinculoFamiliarService vinculoService;

    public VinculoController(VinculoFamiliarService vinculoService) {
        this.vinculoService = vinculoService;
    }

    @PostMapping
    public ResponseEntity<VinculoDTO> crearVinculo(@Valid @RequestBody VinculoDTO dto) {
        return new ResponseEntity<>(vinculoService.crearVinculo(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VinculoDTO>> listarTodos() {
        return ResponseEntity.ok(vinculoService.listarTodos());
    }

    @GetMapping("/estudiante/{id}")
    public ResponseEntity<List<VinculoDTO>> listarPorEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(vinculoService.listarPorEstudiante(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarVinculo(@PathVariable Long id) {
        vinculoService.eliminarVinculo(id);
        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "Vinculo eliminado exitosamente");
        return ResponseEntity.ok(res);
    }
}