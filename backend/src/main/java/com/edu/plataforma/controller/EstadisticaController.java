package com.edu.plataforma.controller;

import com.edu.plataforma.dto.AlertaTempranaDTO;
import com.edu.plataforma.dto.DashboardIndicadoresDTO;
import com.edu.plataforma.dto.ResumenMateriaDTO;
import com.edu.plataforma.service.AsistenciaService;
import com.edu.plataforma.service.BienestarService;
import com.edu.plataforma.service.EstadisticaService;
import com.edu.plataforma.service.NotaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class EstadisticaController {

    private final EstadisticaService estadisticaService;
    private final AsistenciaService asistenciaService;
    private final NotaService notaService;
    private final BienestarService bienestarService;

    public EstadisticaController(EstadisticaService estadisticaService,
                                 AsistenciaService asistenciaService,
                                 NotaService notaService,
                                 BienestarService bienestarService) {
        this.estadisticaService = estadisticaService;
        this.asistenciaService = asistenciaService;
        this.notaService = notaService;
        this.bienestarService = bienestarService;
    }

    @GetMapping("/dashboard/indicadores")
    public ResponseEntity<DashboardIndicadoresDTO> obtenerDashboard() {
        return ResponseEntity.ok(estadisticaService.obtenerDashboard());
    }

    @GetMapping("/estadisticas/asistencia/grupo/{id}")
    public ResponseEntity<List<Map<String, Object>>> estadisticaAsistenciaGrupo(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.obtenerResumenPorGrupo(id));
    }

    @GetMapping("/estadisticas/notas/promedio")
    public ResponseEntity<Map<String, Object>> promedioGeneral() {
        DashboardIndicadoresDTO d = estadisticaService.obtenerDashboard();
        Map<String, Object> res = new HashMap<>();
        res.put("promedioGeneral", d.getPromedioGeneral());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/estadisticas/ranking")
    public ResponseEntity<Map<String, Object>> rankingGlobal() {
        DashboardIndicadoresDTO d = estadisticaService.obtenerDashboard();
        Map<String, Object> res = new HashMap<>();
        res.put("materias", d.getResumenMaterias());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/estadisticas/evolucion/{idEstudiante}")
    public ResponseEntity<List<Map<String, Object>>> evolucionEstudiante(@PathVariable Long idEstudiante) {
        return ResponseEntity.ok(notaService.obtenerEvolucionEstudiante(idEstudiante));
    }

    @GetMapping("/estadisticas/asistencia-mensual")
    public ResponseEntity<?> asistenciaMensual() {
        return ResponseEntity.ok(asistenciaService.obtenerResumenPorMes());
    }

    @GetMapping("/estadisticas/reportes-bienestar")
    public ResponseEntity<?> reportesBienestar() {
        return ResponseEntity.ok(bienestarService.reporteNecesidadesPorTipo());
    }

    @GetMapping("/estadisticas/bajo-rendimiento")
    public ResponseEntity<?> bajoRendimiento(@RequestParam(defaultValue = "3.0") Double umbral) {
        return ResponseEntity.ok(notaService.obtenerNotasBajoRendimiento());
    }

    @GetMapping("/estadisticas/graficos-asistencia")
    public ResponseEntity<?> graficosAsistencia() {
        return ResponseEntity.ok(asistenciaService.obtenerResumenPorMes());
    }

    @GetMapping("/estadisticas/resumen-materia")
    public ResponseEntity<List<ResumenMateriaDTO>> resumenMateria() {
        return ResponseEntity.ok(estadisticaService.obtenerResumenMaterias());
    }

    @GetMapping("/estadisticas/estudiantes-dificultades")
    public ResponseEntity<?> estudiantesDificultades() {
        return ResponseEntity.ok(estadisticaService.obtenerAlertasTempranas());
    }

    @GetMapping("/estadisticas/familiares/uso")
    public ResponseEntity<?> usoFamiliares() {
        return ResponseEntity.ok(estadisticaService.usoFamiliar());
    }

    @GetMapping("/estadisticas/inasistencia-mensual")
    public ResponseEntity<?> inasistenciaMensual() {
        return ResponseEntity.ok(asistenciaService.obtenerResumenPorMes());
    }

    @GetMapping("/estadisticas/solicitudes-bienestar-tipo")
    public ResponseEntity<?> solicitudesBienestarTipo() {
        return ResponseEntity.ok(bienestarService.reporteNecesidadesPorTipo());
    }

    @GetMapping("/estadisticas/cohorte")
    public ResponseEntity<?> resumenCohorte() {
        return ResponseEntity.ok(estadisticaService.resumenPorCohorte());
    }

    @GetMapping("/estadisticas/genero")
    public ResponseEntity<?> distribucionGenero() {
        DashboardIndicadoresDTO d = estadisticaService.obtenerDashboard();
        return ResponseEntity.ok(d.getDistribucionPorGenero());
    }

    @GetMapping("/estadisticas/alertas")
    public ResponseEntity<List<AlertaTempranaDTO>> alertasTempranas() {
        return ResponseEntity.ok(estadisticaService.obtenerAlertasTempranas());
    }

    @GetMapping("/estadisticas/exportar")
    public ResponseEntity<String> exportarEstadisticas(@RequestParam(defaultValue = "csv") String formato) {
        String csvData = estadisticaService.exportarCSV();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_estadisticas.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvData);
    }
}