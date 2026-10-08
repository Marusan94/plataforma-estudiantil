package com.edu.plataforma.service;

import com.edu.plataforma.dto.ActividadDTO;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.Actividad;
import com.edu.plataforma.model.Curso;
import com.edu.plataforma.repository.ActividadRepository;
import com.edu.plataforma.repository.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final CursoRepository cursoRepository;

    public ActividadService(ActividadRepository actividadRepository, CursoRepository cursoRepository) {
        this.actividadRepository = actividadRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public ActividadDTO crearActividad(ActividadDTO dto) {
        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + dto.getCursoId()));
        Actividad a = new Actividad();
        a.setCurso(curso);
        a.setTitulo(dto.getTitulo());
        a.setDescripcion(dto.getDescripcion());
        a.setTipo(dto.getTipo());
        a.setContenido(dto.getContenido());
        a.setOrden(dto.getOrden() != null ? dto.getOrden() : 0);
        a.setPuntos(dto.getPuntos() != null ? dto.getPuntos() : 10);
        return toDTO(actividadRepository.save(a));
    }

    @Transactional(readOnly = true)
    public List<ActividadDTO> listarActividades() {
        return actividadRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ActividadDTO> listarPorCurso(Long cursoId) {
        return actividadRepository.findByCursoId(cursoId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ActividadDTO obtenerPorId(Long id) {
        Actividad a = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con ID: " + id));
        return toDTO(a);
    }

    @Transactional
    public ActividadDTO actualizarActividad(Long id, ActividadDTO dto) {
        Actividad a = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con ID: " + id));
        if (dto.getCursoId() != null) {
            Curso curso = cursoRepository.findById(dto.getCursoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + dto.getCursoId()));
            a.setCurso(curso);
        }
        a.setTitulo(dto.getTitulo());
        a.setDescripcion(dto.getDescripcion());
        if (dto.getTipo() != null) a.setTipo(dto.getTipo());
        if (dto.getContenido() != null) a.setContenido(dto.getContenido());
        if (dto.getOrden() != null) a.setOrden(dto.getOrden());
        if (dto.getPuntos() != null) a.setPuntos(dto.getPuntos());
        return toDTO(actividadRepository.save(a));
    }

    @Transactional
    public void eliminarActividad(Long id) {
        if (!actividadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Actividad no encontrada con ID: " + id);
        }
        actividadRepository.deleteById(id);
    }

    public ActividadDTO toDTO(Actividad a) {
        ActividadDTO dto = new ActividadDTO();
        dto.setId(a.getId());
        if (a.getCurso() != null) dto.setCursoId(a.getCurso().getId());
        dto.setTitulo(a.getTitulo());
        dto.setDescripcion(a.getDescripcion());
        dto.setTipo(a.getTipo());
        dto.setContenido(a.getContenido());
        dto.setOrden(a.getOrden());
        dto.setPuntos(a.getPuntos());
        dto.setFechaCreacion(a.getFechaCreacion());
        return dto;
    }
}
