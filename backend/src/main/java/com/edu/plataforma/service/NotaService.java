package com.edu.plataforma.service;

import com.edu.plataforma.dto.NotaDTO;
import com.edu.plataforma.dto.NotaPromedioDTO;
import com.edu.plataforma.dto.RankingEstudianteDTO;
import com.edu.plataforma.exception.BadRequestException;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.*;
import com.edu.plataforma.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;
    private final GrupoRepository grupoRepository;

    public NotaService(NotaRepository notaRepository,
                       EstudianteRepository estudianteRepository,
                       MateriaRepository materiaRepository,
                       GrupoRepository grupoRepository) {
        this.notaRepository = notaRepository;
        this.estudianteRepository = estudianteRepository;
        this.materiaRepository = materiaRepository;
        this.grupoRepository = grupoRepository;
    }

    @Transactional
    public NotaDTO registrarNota(NotaDTO dto) {
        if (dto.getValor() == null || dto.getValor() < 0.0 || dto.getValor() > 5.0) {
            throw new BadRequestException("La calificacion debe estar en el rango de 0.0 a 5.0");
        }

        Estudiante estudiante = estudianteRepository.findById(dto.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + dto.getEstudianteId()));

        Materia materia = materiaRepository.findById(dto.getMateriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con ID: " + dto.getMateriaId()));

        Grupo grupo = null;
        if (dto.getGrupoId() != null) {
            grupo = grupoRepository.findById(dto.getGrupoId()).orElse(null);
        }

        Nota nota = new Nota();
        nota.setEstudiante(estudiante);
        nota.setMateria(materia);
        nota.setGrupo(grupo);
        nota.setValor(Math.round(dto.getValor() * 100.0) / 100.0);
        nota.setTipoEvaluacion(dto.getTipoEvaluacion() != null ? dto.getTipoEvaluacion() : "Seguimiento");
        nota.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
        nota.setComentario(dto.getComentario());

        Nota guardada = notaRepository.save(nota);
        return toDTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<NotaDTO> listarNotasPorEstudiante(Long estudianteId) {
        return notaRepository.findByEstudianteIdOrderByFechaDesc(estudianteId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotaDTO> listarNotasPorMateriaYGrupo(Long materiaId, Long grupoId) {
        return notaRepository.findByMateriaIdAndGrupoId(materiaId, grupoId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotaDTO> listarNotasPorDocente(Long docenteId, Long materiaId) {
        if (materiaId != null) {
            return notaRepository.findByMateriaDocenteIdAndMateriaId(docenteId, materiaId).stream().map(this::toDTO).collect(Collectors.toList());
        }
        return notaRepository.findByMateriaDocenteId(docenteId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotaPromedioDTO calcularPromedioEstudiante(Long estudianteId) {
        Estudiante est = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + estudianteId));

        List<Nota> notas = notaRepository.findByEstudianteIdOrderByFechaDesc(estudianteId);
        if (notas.isEmpty()) {
            return new NotaPromedioDTO(estudianteId, est.getUsuario() != null ? est.getUsuario().getNombre() : "Estudiante", 0.0, 0);
        }

        double promedio = notas.stream().mapToDouble(Nota::getValor).average().orElse(0.0);
        long materiasUnicas = notas.stream().map(n -> n.getMateria().getId()).distinct().count();

        return new NotaPromedioDTO(estudianteId, est.getUsuario() != null ? est.getUsuario().getNombre() : "Estudiante",
                Math.round(promedio * 100.0) / 100.0, (int) materiasUnicas);
    }

    @Transactional(readOnly = true)
    public List<NotaDTO> obtenerNotasBajoRendimiento() {
        return notaRepository.findByValorLessThan(3.0).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RankingEstudianteDTO> obtenerRankingGrupo(Long grupoId) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado con ID: " + grupoId));

        List<Object[]> resultados = notaRepository.rankingPorGrupo(grupoId);
        List<RankingEstudianteDTO> ranking = new ArrayList<>();
        int pos = 1;
        for (Object[] row : resultados) {
            Long estId = (Long) row[0];
            Double prom = (Double) row[1];
            Estudiante e = estudianteRepository.findById(estId).orElse(null);
            String nombre = (e != null && e.getUsuario() != null) ? e.getUsuario().getNombre() : "Estudiante " + estId;
            ranking.add(new RankingEstudianteDTO(estId, nombre, grupoId, grupo.getNombre(), Math.round(prom * 100.0) / 100.0, pos++));
        }
        return ranking;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerEvolucionEstudiante(Long estudianteId) {
        List<Nota> notas = notaRepository.findByEstudianteIdOrderByFechaDesc(estudianteId);
        // Agrupar por fecha/periodo ordenado cronológicamente
        return notas.stream()
                .sorted(Comparator.comparing(Nota::getFecha))
                .map(n -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("fecha", n.getFecha().toString());
                    map.put("materia", n.getMateria().getNombre());
                    map.put("tipo", n.getTipoEvaluacion());
                    map.put("nota", n.getValor());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public NotaDTO actualizarNota(Long id, NotaDTO dto) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nota no encontrada con ID: " + id));

        if (dto.getValor() != null) {
            if (dto.getValor() < 0.0 || dto.getValor() > 5.0) {
                throw new BadRequestException("La calificacion debe estar entre 0.0 y 5.0");
            }
            nota.setValor(Math.round(dto.getValor() * 100.0) / 100.0);
        }
        if (dto.getTipoEvaluacion() != null) nota.setTipoEvaluacion(dto.getTipoEvaluacion());
        if (dto.getFecha() != null) nota.setFecha(dto.getFecha());
        if (dto.getComentario() != null) nota.setComentario(dto.getComentario());

        return toDTO(notaRepository.save(nota));
    }

    @Transactional
    public void eliminarNota(Long id) {
        if (!notaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Nota no encontrada para eliminar con ID: " + id);
        }
        notaRepository.deleteById(id);
    }

    public NotaDTO toDTO(Nota n) {
        NotaDTO dto = new NotaDTO();
        dto.setId(n.getId());
        dto.setValor(n.getValor());
        dto.setTipoEvaluacion(n.getTipoEvaluacion());
        dto.setFecha(n.getFecha());
        dto.setComentario(n.getComentario());
        if (n.getEstudiante() != null) {
            dto.setEstudianteId(n.getEstudiante().getId());
            if (n.getEstudiante().getUsuario() != null) {
                dto.setNombreEstudiante(n.getEstudiante().getUsuario().getNombre());
            }
        }
        if (n.getMateria() != null) {
            dto.setMateriaId(n.getMateria().getId());
            dto.setNombreMateria(n.getMateria().getNombre());
        }
        if (n.getGrupo() != null) {
            dto.setGrupoId(n.getGrupo().getId());
            dto.setNombreGrupo(n.getGrupo().getNombre());
        }

        // Recomendación pedagógica si nota < 3.0
        if (n.getValor() < 3.0) {
            dto.setRecomendacion("Alerta Academica: Se sugiere solicitar asesoria personalizada en Bienestar Estudiantil, asistir a monitorias de " 
                    + (n.getMateria() != null ? n.getMateria().getNombre() : "la materia") + " y reforzar temas previos al proximo parcial.");
        } else {
            dto.setRecomendacion("Rendimiento satisfactorio. Continuar con el plan de estudio y participacion activa en clase.");
        }

        return dto;
    }
}