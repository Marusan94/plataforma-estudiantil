package com.edu.plataforma.service;

import com.edu.plataforma.dto.EvaluacionDTO;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.Curso;
import com.edu.plataforma.model.Evaluacion;
import com.edu.plataforma.repository.CursoRepository;
import com.edu.plataforma.repository.EvaluacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final CursoRepository cursoRepository;

    public EvaluacionService(EvaluacionRepository evaluacionRepository, CursoRepository cursoRepository) {
        this.evaluacionRepository = evaluacionRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public EvaluacionDTO crearEvaluacion(EvaluacionDTO dto) {
        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + dto.getCursoId()));
        Evaluacion e = new Evaluacion();
        e.setCurso(curso);
        e.setTitulo(dto.getTitulo());
        e.setDescripcion(dto.getDescripcion());
        e.setTipoEvaluacion(dto.getTipoEvaluacion() != null ? dto.getTipoEvaluacion() : "Quiz");
        e.setPuntajeMaximo(dto.getPuntajeMaximo() != null ? dto.getPuntajeMaximo() : 100);
        e.setPreguntasJson(dto.getPreguntasJson());
        return toDTO(evaluacionRepository.save(e));
    }

    @Transactional(readOnly = true)
    public List<EvaluacionDTO> listarEvaluaciones() {
        return evaluacionRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EvaluacionDTO> listarPorCurso(Long cursoId) {
        return evaluacionRepository.findByCursoId(cursoId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EvaluacionDTO obtenerPorId(Long id) {
        Evaluacion e = evaluacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluacion no encontrada con ID: " + id));
        return toDTO(e);
    }

    @Transactional
    public EvaluacionDTO actualizarEvaluacion(Long id, EvaluacionDTO dto) {
        Evaluacion e = evaluacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluacion no encontrada con ID: " + id));
        if (dto.getCursoId() != null) {
            Curso curso = cursoRepository.findById(dto.getCursoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + dto.getCursoId()));
            e.setCurso(curso);
        }
        e.setTitulo(dto.getTitulo());
        e.setDescripcion(dto.getDescripcion());
        if (dto.getTipoEvaluacion() != null) e.setTipoEvaluacion(dto.getTipoEvaluacion());
        if (dto.getPuntajeMaximo() != null) e.setPuntajeMaximo(dto.getPuntajeMaximo());
        if (dto.getPreguntasJson() != null) e.setPreguntasJson(dto.getPreguntasJson());
        return toDTO(evaluacionRepository.save(e));
    }

    @Transactional
    public void eliminarEvaluacion(Long id) {
        if (!evaluacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evaluacion no encontrada con ID: " + id);
        }
        evaluacionRepository.deleteById(id);
    }

    public EvaluacionDTO toDTO(Evaluacion e) {
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setId(e.getId());
        if (e.getCurso() != null) dto.setCursoId(e.getCurso().getId());
        dto.setTitulo(e.getTitulo());
        dto.setDescripcion(e.getDescripcion());
        dto.setTipoEvaluacion(e.getTipoEvaluacion());
        dto.setPuntajeMaximo(e.getPuntajeMaximo());
        dto.setPreguntasJson(e.getPreguntasJson());
        dto.setFechaCreacion(e.getFechaCreacion());
        return dto;
    }
}
