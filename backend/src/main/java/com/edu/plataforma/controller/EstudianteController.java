package com.edu.plataforma.controller;

import com.edu.plataforma.dto.EstudianteDTO;
import com.edu.plataforma.dto.NotaPromedioDTO;
import com.edu.plataforma.service.EstudianteService;
import com.edu.plataforma.service.NotaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;
    private final NotaService notaService;

    public EstudianteController(EstudianteService estudianteService, NotaService notaService) {
        this.estudianteService = estudianteService;
        this.notaService = notaService;
    }

    @PostMapping
    public ResponseEntity<EstudianteDTO> crearEstudiante(@Valid @RequestBody EstudianteDTO dto) {
        return new ResponseEntity<>(estudianteService.crearEstudiante(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> listarEstudiantes() {
        return ResponseEntity.ok(estudianteService.listarEstudiantes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EstudianteDTO>> buscar(@RequestParam String query) {
        return ResponseEntity.ok(estudianteService.buscarPorNombreOCodigo(query));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteDTO> actualizarEstudiante(@PathVariable Long id, @Valid @RequestBody EstudianteDTO dto) {
        return ResponseEntity.ok(estudianteService.actualizarEstudiante(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarEstudiante(@PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "Estudiante eliminado exitosamente");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/resumen-academico")
    public ResponseEntity<Map<String, Object>> obtenerResumenAcademico(@PathVariable Long id) {
        EstudianteDTO est = estudianteService.obtenerPorId(id);
        NotaPromedioDTO prom = notaService.calcularPromedioEstudiante(id);

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("estudiante", est);
        resumen.put("promedioGeneral", prom.getPromedio());
        resumen.put("materiasCursadas", prom.getNumeroMaterias());
        resumen.put("ultimasNotas", notaService.listarNotasPorEstudiante(id));
        return ResponseEntity.ok(resumen);
    }
}