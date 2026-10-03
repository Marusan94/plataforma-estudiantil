package com.edu.plataforma.controller;

import com.edu.plataforma.dto.*;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.AccesoFamiliar;
import com.edu.plataforma.model.Familiar;
import com.edu.plataforma.repository.AccesoFamiliarRepository;
import com.edu.plataforma.repository.FamiliarRepository;
import com.edu.plataforma.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/familiares")
public class FamiliarController {

    private final FamiliarService familiarService;
    private final VinculoFamiliarService vinculoService;
    private final PerfilEstudianteService perfilService;
    private final NotaService notaService;
    private final AsistenciaService asistenciaService;
    private final BienestarService bienestarService;
    private final FamiliarRepository familiarRepository;
    private final AccesoFamiliarRepository accesoFamiliarRepository;

    public FamiliarController(FamiliarService familiarService,
                              VinculoFamiliarService vinculoService,
                              PerfilEstudianteService perfilService,
                              NotaService notaService,
                              AsistenciaService asistenciaService,
                              BienestarService bienestarService,
                              FamiliarRepository familiarRepository,
                              AccesoFamiliarRepository accesoFamiliarRepository) {
        this.familiarService = familiarService;
        this.vinculoService = vinculoService;
        this.perfilService = perfilService;
        this.notaService = notaService;
        this.asistenciaService = asistenciaService;
        this.bienestarService = bienestarService;
        this.familiarRepository = familiarRepository;
        this.accesoFamiliarRepository = accesoFamiliarRepository;
    }

    @PostMapping
    public ResponseEntity<FamiliarDTO> crearFamiliar(@Valid @RequestBody FamiliarDTO dto) {
        return new ResponseEntity<>(familiarService.registrarFamiliar(dto), HttpStatus.CREATED);
    }

    @PostMapping("/registro")
    public ResponseEntity<FamiliarDTO> registroPublico(@Valid @RequestBody FamiliarDTO dto) {
        return new ResponseEntity<>(familiarService.registrarFamiliar(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FamiliarDTO>> listarFamiliares() {
        return ResponseEntity.ok(familiarService.listarFamiliares());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamiliarDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(familiarService.obtenerPorId(id));
    }

    @GetMapping("/correo/{email}")
    public ResponseEntity<Map<String, Object>> verificarCorreo(@PathVariable String email) {
        boolean existe = familiarService.verificarExistePorCorreo(email);
        Map<String, Object> res = new HashMap<>();
        res.put("correo", email);
        res.put("existe", existe);
        res.put("mensaje", existe ? "El familiar ya se encuentra registrado" : "El correo esta disponible");
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamiliarDTO> actualizarFamiliar(@PathVariable Long id, @Valid @RequestBody FamiliarDTO dto) {
        return ResponseEntity.ok(familiarService.actualizarFamiliar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarFamiliar(@PathVariable Long id) {
        familiarService.eliminarFamiliar(id);
        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "Familiar eliminado correctamente");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/estudiantes")
    public ResponseEntity<List<VinculoDTO>> listarEstudiantesAsociados(@PathVariable Long id) {
        registrarLogAcceso(id, "Consulta lista de estudiantes asociados");
        return ResponseEntity.ok(vinculoService.listarPorFamiliar(id));
    }

    @GetMapping("/{id}/perfil-estudiante")
    public ResponseEntity<PerfilEstudianteDTO> consultarPerfilEstudiante(@PathVariable Long id, @RequestParam(required = false) Long estudianteId) {
        registrarLogAcceso(id, "Consulta hoja de vida de estudiante");
        Long targetEstudianteId = resolverEstudianteId(id, estudianteId);
        return ResponseEntity.ok(perfilService.obtenerPorEstudianteId(targetEstudianteId));
    }

    @GetMapping("/{id}/notas")
    public ResponseEntity<List<NotaDTO>> consultarNotasEstudiante(@PathVariable Long id, @RequestParam(required = false) Long estudianteId) {
        registrarLogAcceso(id, "Consulta de calificaciones de estudiante");
        Long targetEstudianteId = resolverEstudianteId(id, estudianteId);
        return ResponseEntity.ok(notaService.listarNotasPorEstudiante(targetEstudianteId));
    }

    @GetMapping("/{id}/asistencia")
    public ResponseEntity<List<AsistenciaDTO>> consultarAsistenciaEstudiante(@PathVariable Long id, @RequestParam(required = false) Long estudianteId) {
        registrarLogAcceso(id, "Consulta de registros de asistencia de estudiante");
        Long targetEstudianteId = resolverEstudianteId(id, estudianteId);
        return ResponseEntity.ok(asistenciaService.listarPorEstudiante(targetEstudianteId));
    }

    @GetMapping("/{id}/alertas")
    public ResponseEntity<List<NotaDTO>> consultarAlertasEstudiante(@PathVariable Long id, @RequestParam(required = false) Long estudianteId) {
        registrarLogAcceso(id, "Consulta de alertas academicas");
        Long targetEstudianteId = resolverEstudianteId(id, estudianteId);
        List<NotaDTO> todas = notaService.listarNotasPorEstudiante(targetEstudianteId);
        List<NotaDTO> bajoRendimiento = new ArrayList<>();
        for (NotaDTO n : todas) {
            if (n.getValor() < 3.0) bajoRendimiento.add(n);
        }
        return ResponseEntity.ok(bajoRendimiento);
    }

    @GetMapping("/{id}/solicitudes-bienestar")
    public ResponseEntity<List<NecesidadApoyoDTO>> consultarSolicitudesBienestar(@PathVariable Long id, @RequestParam(required = false) Long estudianteId) {
        registrarLogAcceso(id, "Consulta solicitudes de apoyo bienestar");
        Long targetEstudianteId = resolverEstudianteId(id, estudianteId);
        return ResponseEntity.ok(bienestarService.listarPorEstudiante(targetEstudianteId));
    }

    @GetMapping("/{id}/resumen-academico")
    public ResponseEntity<Map<String, Object>> consultarResumenAcademico(@PathVariable Long id, @RequestParam(required = false) Long estudianteId) {
        registrarLogAcceso(id, "Consulta resumen integral de rendimiento");
        Long targetEstudianteId = resolverEstudianteId(id, estudianteId);
        NotaPromedioDTO prom = notaService.calcularPromedioEstudiante(targetEstudianteId);
        Map<String, Object> pctAsist = asistenciaService.calcularPorcentajeEstudiante(targetEstudianteId);

        Map<String, Object> res = new HashMap<>();
        res.put("familiarId", id);
        res.put("estudianteId", targetEstudianteId);
        res.put("promedioGeneral", prom.getPromedio());
        res.put("materiasCursadas", prom.getNumeroMaterias());
        res.put("asistencia", pctAsist);
        res.put("tieneAlertas", prom.getPromedio() < 3.0 || (Double) pctAsist.get("porcentajeAsistencia") < 80.0);
        return ResponseEntity.ok(res);
    }

    private Long resolverEstudianteId(Long familiarId, Long estudianteId) {
        if (estudianteId != null) {
            return estudianteId;
        }
        List<VinculoDTO> vinculos = vinculoService.listarPorFamiliar(familiarId);
        if (!vinculos.isEmpty()) {
            return vinculos.get(0).getIdEstudiante();
        }
        throw new ResourceNotFoundException("El familiar no tiene ningun estudiante vinculado");
    }

    private void registrarLogAcceso(Long familiarId, String accion) {
        Familiar fam = familiarRepository.findById(familiarId).orElse(null);
        if (fam != null) {
            accesoFamiliarRepository.save(new AccesoFamiliar(fam, accion));
        }
    }
}