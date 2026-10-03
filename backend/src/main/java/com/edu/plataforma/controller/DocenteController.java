package com.edu.plataforma.controller;

import com.edu.plataforma.dto.DocenteDTO;
import com.edu.plataforma.service.DocenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/docentes")
public class DocenteController {

    private final DocenteService docenteService;

    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @PostMapping
    public ResponseEntity<DocenteDTO> crearDocente(@Valid @RequestBody DocenteDTO dto) {
        return new ResponseEntity<>(docenteService.crearDocente(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DocenteDTO>> listarDocentes() {
        return ResponseEntity.ok(docenteService.listarDocentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocenteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(docenteService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocenteDTO> actualizarDocente(@PathVariable Long id, @Valid @RequestBody DocenteDTO dto) {
        return ResponseEntity.ok(docenteService.actualizarDocente(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarDocente(@PathVariable Long id) {
        docenteService.eliminarDocente(id);
        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "Docente eliminado correctamente");
        return ResponseEntity.ok(res);
    }
}