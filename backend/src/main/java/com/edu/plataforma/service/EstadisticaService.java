package com.edu.plataforma.service;

import com.edu.plataforma.dto.AlertaTempranaDTO;
import com.edu.plataforma.dto.DashboardIndicadoresDTO;
import com.edu.plataforma.dto.ResumenMateriaDTO;
import com.edu.plataforma.model.Estudiante;
import com.edu.plataforma.model.Materia;
import com.edu.plataforma.model.Nota;
import com.edu.plataforma.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadisticaService {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final DocenteRepository docenteRepository;
    private final FamiliarRepository familiarRepository;
    private final NotaRepository notaRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final NecesidadApoyoRepository necesidadRepository;
    private final MateriaRepository materiaRepository;
    private final AccesoFamiliarRepository accesoFamiliarRepository;

    public EstadisticaService(UsuarioRepository usuarioRepository,
                              EstudianteRepository estudianteRepository,
                              DocenteRepository docenteRepository,
                              FamiliarRepository familiarRepository,
                              NotaRepository notaRepository,
                              AsistenciaRepository asistenciaRepository,
                              NecesidadApoyoRepository necesidadRepository,
                              MateriaRepository materiaRepository,
                              AccesoFamiliarRepository accesoFamiliarRepository) {
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
        this.docenteRepository = docenteRepository;
        this.familiarRepository = familiarRepository;
        this.notaRepository = notaRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.necesidadRepository = necesidadRepository;
        this.materiaRepository = materiaRepository;
        this.accesoFamiliarRepository = accesoFamiliarRepository;
    }

    @Transactional(readOnly = true)
    public DashboardIndicadoresDTO obtenerDashboard() {
        DashboardIndicadoresDTO d = new DashboardIndicadoresDTO();
        d.setTotalUsuarios(usuarioRepository.count());
        d.setTotalEstudiantes(estudianteRepository.count());
        d.setTotalDocentes(docenteRepository.count());
        d.setTotalFamiliares(familiarRepository.count());

        Double prom = notaRepository.calcularPromedioGlobal();
        d.setPromedioGeneral(prom != null ? Math.round(prom * 100.0) / 100.0 : 0.0);

        Long totalSesiones = asistenciaRepository.contarTotalRegistros();
        Long presentes = asistenciaRepository.contarTotalPresentes();
        double pctAsistencia = (totalSesiones != null && totalSesiones > 0) ? ((double) presentes / totalSesiones) * 100.0 : 100.0;
        d.setPorcentajeAsistencia(Math.round(pctAsistencia * 100.0) / 100.0);

        d.setTotalSolicitudesBienestar(necesidadRepository.count());
        d.setSolicitudesPendientes(necesidadRepository.countByEstado("PENDIENTE"));

        List<AlertaTempranaDTO> alertas = obtenerAlertasTempranas();
        d.setEstudiantesEnRiesgo(alertas.size());

        d.setResumenMaterias(obtenerResumenMaterias());

        // Distribucion por genero
        Map<String, Long> generos = estudianteRepository.findAll().stream()
                .collect(Collectors.groupingBy(e -> e.getGenero() != null ? e.getGenero() : "No especificado", Collectors.counting()));
        d.setDistribucionPorGenero(generos);

        // Solicitudes bienestar por tipo
        Map<String, Long> tiposBienestar = new HashMap<>();
        for (Object[] row : necesidadRepository.contarPorTipo()) {
            tiposBienestar.put((String) row[0], (Long) row[1]);
        }
        d.setSolicitudesPorTipo(tiposBienestar);

        return d;
    }

    @Transactional(readOnly = true)
    public List<ResumenMateriaDTO> obtenerResumenMaterias() {
        List<Materia> materias = materiaRepository.findAll();
        List<Nota> todasNotas = notaRepository.findAll();

        List<ResumenMateriaDTO> resumen = new ArrayList<>();
        for (Materia m : materias) {
            List<Nota> notasMateria = todasNotas.stream().filter(n -> n.getMateria().getId().equals(m.getId())).collect(Collectors.toList());
            if (notasMateria.isEmpty()) {
                resumen.add(new ResumenMateriaDTO(m.getId(), m.getNombre(), m.getCodigo(), 0.0, 0.0, 0.0, 0.0, 0, 0.0));
                continue;
            }

            double prom = notasMateria.stream().mapToDouble(Nota::getValor).average().orElse(0.0);
            double min = notasMateria.stream().mapToDouble(Nota::getValor).min().orElse(0.0);
            double max = notasMateria.stream().mapToDouble(Nota::getValor).max().orElse(0.0);
            long reprobados = notasMateria.stream().filter(n -> n.getValor() < 3.0).count();
            double pctReprobacion = ((double) reprobados / notasMateria.size()) * 100.0;

            // Desviación estándar
            double varianza = notasMateria.stream().mapToDouble(n -> Math.pow(n.getValor() - prom, 2)).average().orElse(0.0);
            double desviacion = Math.sqrt(varianza);

            long totalEst = notasMateria.stream().map(n -> n.getEstudiante().getId()).distinct().count();

            resumen.add(new ResumenMateriaDTO(
                    m.getId(),
                    m.getNombre(),
                    m.getCodigo(),
                    Math.round(prom * 100.0) / 100.0,
                    Math.round(desviacion * 100.0) / 100.0,
                    Math.round(min * 100.0) / 100.0,
                    Math.round(max * 100.0) / 100.0,
                    totalEst,
                    Math.round(pctReprobacion * 100.0) / 100.0
            ));
        }
        return resumen;
    }

    @Transactional(readOnly = true)
    public List<AlertaTempranaDTO> obtenerAlertasTempranas() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        List<AlertaTempranaDTO> alertas = new ArrayList<>();

        for (Estudiante e : estudiantes) {
            List<Nota> notas = notaRepository.findByEstudianteIdOrderByFechaDesc(e.getId());
            double prom = notas.stream().mapToDouble(Nota::getValor).average().orElse(5.0);

            Long totalSesiones = asistenciaRepository.contarTotalSesionesPorEstudiante(e.getId());
            Long presentes = asistenciaRepository.contarPresentesPorEstudiante(e.getId());
            double pctAsist = (totalSesiones > 0) ? ((double) presentes / totalSesiones) * 100.0 : 100.0;

            boolean riesgoNotas = prom < 3.0;
            boolean riesgoAsistencia = pctAsist < 80.0;

            if (riesgoNotas || riesgoAsistencia) {
                String nivel = (riesgoNotas && riesgoAsistencia) ? "ALTO" : "MEDIO";
                String motivo = "";
                if (riesgoNotas && riesgoAsistencia) {
                    motivo = "Bajo rendimiento academico (" + (Math.round(prom * 10.0) / 10.0) + ") y alto ausentismo (" + (Math.round(pctAsist)) + "% de asistencia)";
                } else if (riesgoNotas) {
                    motivo = "Promedio acumulado critico: " + (Math.round(prom * 10.0) / 10.0) + " (inferior a 3.0)";
                } else {
                    motivo = "Inasistencias reiteradas: " + (Math.round(pctAsist)) + "% de asistencia (inferior a 80%)";
                }

                alertas.add(new AlertaTempranaDTO(
                        e.getId(),
                        e.getUsuario() != null ? e.getUsuario().getNombre() : "Estudiante " + e.getId(),
                        e.getPrograma(),
                        Math.round(prom * 100.0) / 100.0,
                        Math.round(pctAsist * 100.0) / 100.0,
                        nivel,
                        motivo
                ));
            }
        }
        alertas.sort(Comparator.comparing(AlertaTempranaDTO::getNivelRiesgo));
        return alertas;
    }

    @Transactional(readOnly = true)
    public String exportarCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append("ID_Estudiante,Nombre,Programa,Semestre,PromedioNotas,PorcentajeAsistencia,EstadoRiesgo\n");

        List<Estudiante> estudiantes = estudianteRepository.findAll();
        for (Estudiante e : estudiantes) {
            List<Nota> notas = notaRepository.findByEstudianteIdOrderByFechaDesc(e.getId());
            double prom = notas.stream().mapToDouble(Nota::getValor).average().orElse(0.0);

            Long totalSesiones = asistenciaRepository.contarTotalSesionesPorEstudiante(e.getId());
            Long presentes = asistenciaRepository.contarPresentesPorEstudiante(e.getId());
            double pctAsist = (totalSesiones > 0) ? ((double) presentes / totalSesiones) * 100.0 : 100.0;

            String riesgo = (prom < 3.0 || pctAsist < 80.0) ? "EN_RIESGO" : "NORMAL";

            csv.append(e.getId()).append(",")
                    .append("\"").append(e.getUsuario() != null ? e.getUsuario().getNombre() : "Estudiante").append("\",")
                    .append("\"").append(e.getPrograma()).append("\",")
                    .append(e.getSemestre()).append(",")
                    .append(Math.round(prom * 100.0) / 100.0).append(",")
                    .append(Math.round(pctAsist * 100.0) / 100.0).append(",")
                    .append(riesgo).append("\n");
        }
        return csv.toString();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> resumenPorCohorte() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        Map<Integer, Long> porSemestre = estudiantes.stream()
                .collect(Collectors.groupingBy(Estudiante::getSemestre, Collectors.counting()));
        Map<String, Object> res = new HashMap<>();
        res.put("distribucionSemestres", porSemestre);
        res.put("totalMatriculados", estudiantes.size());
        return res;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> usoFamiliar() {
        long totalAccesos = accesoFamiliarRepository.count();
        Map<String, Object> map = new HashMap<>();
        map.put("totalAccesosRegistrados", totalAccesos);
        map.put("promedioAccesosPorFamiliar", totalAccesos > 0 ? (double) totalAccesos / Math.max(1, familiarRepository.count()) : 0.0);
        return map;
    }
}