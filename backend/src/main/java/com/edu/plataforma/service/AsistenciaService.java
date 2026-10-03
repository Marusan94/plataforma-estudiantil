package com.edu.plataforma.service;

import com.edu.plataforma.dto.*;
import com.edu.plataforma.exception.BadRequestException;
import com.edu.plataforma.exception.ConflictException;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.Asistencia;
import com.edu.plataforma.model.Estudiante;
import com.edu.plataforma.model.Grupo;
import com.edu.plataforma.repository.AsistenciaRepository;
import com.edu.plataforma.repository.EstudianteRepository;
import com.edu.plataforma.repository.GrupoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final EstudianteRepository estudianteRepository;
    private final GrupoRepository grupoRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository,
                             EstudianteRepository estudianteRepository,
                             GrupoRepository grupoRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.estudianteRepository = estudianteRepository;
        this.grupoRepository = grupoRepository;
    }

    @Transactional
    public AsistenciaDTO registrarAsistencia(AsistenciaDTO dto) {
        if (dto.getFecha().isAfter(LocalDate.now())) {
            throw new BadRequestException("No es posible registrar asistencias con fechas futuras");
        }

        if (asistenciaRepository.findByEstudianteIdAndGrupoIdAndFecha(dto.getEstudianteId(), dto.getGrupoId(), dto.getFecha()).isPresent()) {
            throw new ConflictException("Ya existe un registro de asistencia para este estudiante en la fecha indicada: " + dto.getFecha());
        }

        Estudiante estudiante = estudianteRepository.findById(dto.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + dto.getEstudianteId()));

        Grupo grupo = grupoRepository.findById(dto.getGrupoId())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado con ID: " + dto.getGrupoId()));

        Asistencia a = new Asistencia(
                dto.getFecha(),
                dto.getEstado().toUpperCase(),
                dto.getObservaciones(),
                dto.getJustificada() != null ? dto.getJustificada() : "JUSTIFICADO".equalsIgnoreCase(dto.getEstado()),
                estudiante,
                grupo
        );

        return toDTO(asistenciaRepository.save(a));
    }

    @Transactional
    public List<AsistenciaDTO> registrarAsistenciaLote(AsistenciaLoteDTO loteDTO) {
        if (loteDTO.getFecha().isAfter(LocalDate.now())) {
            throw new BadRequestException("No es posible registrar asistencias con fechas futuras");
        }

        Grupo grupo = grupoRepository.findById(loteDTO.getGrupoId())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado con ID: " + loteDTO.getGrupoId()));

        List<AsistenciaDTO> guardadas = new ArrayList<>();
        for (AsistenciaLoteDTO.AsistenciaItemDTO item : loteDTO.getAsistencias()) {
            Optional<Asistencia> previa = asistenciaRepository.findByEstudianteIdAndGrupoIdAndFecha(
                    item.getEstudianteId(), loteDTO.getGrupoId(), loteDTO.getFecha()
            );

            Asistencia a;
            if (previa.isPresent()) {
                a = previa.get();
                a.setEstado(item.getEstado().toUpperCase());
                a.setObservaciones(item.getObservaciones());
                a.setJustificada(item.getJustificada() != null ? item.getJustificada() : "JUSTIFICADO".equalsIgnoreCase(item.getEstado()));
            } else {
                Estudiante est = estudianteRepository.findById(item.getEstudianteId())
                        .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + item.getEstudianteId()));
                a = new Asistencia(
                        loteDTO.getFecha(),
                        item.getEstado().toUpperCase(),
                        item.getObservaciones(),
                        item.getJustificada() != null ? item.getJustificada() : "JUSTIFICADO".equalsIgnoreCase(item.getEstado()),
                        est,
                        grupo
                );
            }
            guardadas.add(toDTO(asistenciaRepository.save(a)));
        }
        return guardadas;
    }

    @Transactional(readOnly = true)
    public List<AsistenciaDTO> listarPorEstudiante(Long estudianteId) {
        return asistenciaRepository.findByEstudianteIdOrderByFechaDesc(estudianteId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AsistenciaDTO> listarPorGrupoYFecha(Long grupoId, LocalDate fecha) {
        return asistenciaRepository.findByGrupoIdAndFechaOrderByEstudianteUsuarioNombreAsc(grupoId, fecha)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public AsistenciaDTO actualizarEstado(Long id, String nuevoEstado, Boolean justificada) {
        Asistencia a = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia no encontrada con ID: " + id));

        a.setEstado(nuevoEstado.toUpperCase());
        if (justificada != null) a.setJustificada(justificada);
        if ("JUSTIFICADO".equalsIgnoreCase(nuevoEstado)) a.setJustificada(true);

        return toDTO(asistenciaRepository.save(a));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> calcularPorcentajeEstudiante(Long estudianteId) {
        Estudiante est = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + estudianteId));

        Long total = asistenciaRepository.contarTotalSesionesPorEstudiante(estudianteId);
        Long presentes = asistenciaRepository.contarPresentesPorEstudiante(estudianteId);

        double porcentaje = (total > 0) ? ((double) presentes / total) * 100.0 : 100.0;
        porcentaje = Math.round(porcentaje * 100.0) / 100.0;

        Map<String, Object> res = new HashMap<>();
        res.put("estudianteId", estudianteId);
        res.put("nombreEstudiante", est.getUsuario() != null ? est.getUsuario().getNombre() : "Estudiante");
        res.put("totalSesiones", total);
        res.put("sesionesPresente", presentes);
        res.put("sesionesAusente", total - presentes);
        res.put("porcentajeAsistencia", porcentaje);
        return res;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerResumenPorGrupo(Long grupoId) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado con ID: " + grupoId));

        List<Asistencia> asistencias = asistenciaRepository.findByGrupoIdOrderByFechaDesc(grupoId);
        Map<Estudiante, List<Asistencia>> porEstudiante = asistencias.stream().collect(Collectors.groupingBy(Asistencia::getEstudiante));

        List<Map<String, Object>> resumen = new ArrayList<>();
        for (Map.Entry<Estudiante, List<Asistencia>> entry : porEstudiante.entrySet()) {
            Estudiante e = entry.getKey();
            List<Asistencia> list = entry.getValue();
            long total = list.size();
            long pres = list.stream().filter(a -> "PRESENTE".equalsIgnoreCase(a.getEstado())).count();
            double pct = total > 0 ? ((double) pres / total) * 100.0 : 0.0;

            Map<String, Object> item = new HashMap<>();
            item.put("estudianteId", e.getId());
            item.put("nombreEstudiante", e.getUsuario() != null ? e.getUsuario().getNombre() : "Estudiante " + e.getId());
            item.put("codigo", e.getCodigoEstudiante());
            item.put("totalSesiones", total);
            item.put("asistencias", pres);
            item.put("ausencias", total - pres);
            item.put("porcentaje", Math.round(pct * 100.0) / 100.0);
            resumen.add(item);
        }

        resumen.sort(Comparator.comparingDouble(m -> (Double) m.get("porcentaje")));
        return resumen;
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResumenDTO> obtenerResumenPorMes() {
        List<Asistencia> todas = asistenciaRepository.findAll();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");

        Map<String, List<Asistencia>> porMes = todas.stream()
                .collect(Collectors.groupingBy(a -> a.getFecha().format(fmt)));

        return porMes.entrySet().stream().map(entry -> {
            String mes = entry.getKey();
            List<Asistencia> list = entry.getValue();
            long pres = list.stream().filter(a -> "PRESENTE".equalsIgnoreCase(a.getEstado())).count();
            long aus = list.stream().filter(a -> "AUSENTE".equalsIgnoreCase(a.getEstado())).count();
            long just = list.stream().filter(a -> "JUSTIFICADO".equalsIgnoreCase(a.getEstado()) || Boolean.TRUE.equals(a.getJustificada())).count();
            double pct = list.size() > 0 ? ((double) pres / list.size()) * 100.0 : 0.0;
            return new AsistenciaResumenDTO(mes, pres, aus, just, Math.round(pct * 100.0) / 100.0);
        }).sorted(Comparator.comparing(AsistenciaResumenDTO::getMes)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AsistenciaDTO> listarPorFecha(LocalDate fecha) {
        return asistenciaRepository.findByFecha(fecha).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AsistenciaDTO> filtrarPorRango(LocalDate inicio, LocalDate fin) {
        if (inicio.isAfter(fin)) {
            throw new BadRequestException("La fecha inicial debe ser anterior a la fecha final");
        }
        return asistenciaRepository.findByFechaBetween(inicio, fin).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void eliminarAsistencia(Long id) {
        if (!asistenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Asistencia no encontrada para eliminar con ID: " + id);
        }
        asistenciaRepository.deleteById(id);
    }

    @Transactional
    public Map<String, Object> simularAsistenciaAutomatica() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        List<Grupo> grupos = grupoRepository.findAll();
        int creados = 0;
        LocalDate hoy = LocalDate.now();

        for (Grupo g : grupos) {
            for (Estudiante e : estudiantes) {
                if (!asistenciaRepository.findByEstudianteIdAndGrupoIdAndFecha(e.getId(), g.getId(), hoy).isPresent()) {
                    Asistencia a = new Asistencia(hoy, "AUSENTE", "Generada automaticamente para control", false, e, g);
                    asistenciaRepository.save(a);
                    creados++;
                }
            }
        }
        Map<String, Object> res = new HashMap<>();
        res.put("mensaje", "Asistencias automaticas generadas con exito");
        res.put("nuevosRegistros", creados);
        return res;
    }

    public AsistenciaDTO toDTO(Asistencia a) {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setId(a.getId());
        dto.setFecha(a.getFecha());
        dto.setEstado(a.getEstado());
        dto.setObservaciones(a.getObservaciones());
        dto.setJustificada(a.getJustificada());
        if (a.getEstudiante() != null) {
            dto.setEstudianteId(a.getEstudiante().getId());
            if (a.getEstudiante().getUsuario() != null) {
                dto.setNombreEstudiante(a.getEstudiante().getUsuario().getNombre());
            }
        }
        if (a.getGrupo() != null) {
            dto.setGrupoId(a.getGrupo().getId());
            dto.setNombreGrupo(a.getGrupo().getNombre());
        }
        return dto;
    }
}