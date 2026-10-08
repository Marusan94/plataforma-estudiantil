package com.edu.plataforma.service;

import com.edu.plataforma.dto.ProgresoCursoDTO;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.ProgresoCurso;
import com.edu.plataforma.repository.ProgresoCursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgresoCursoService {

    private final ProgresoCursoRepository progresoRepository;

    public ProgresoCursoService(ProgresoCursoRepository progresoRepository) {
        this.progresoRepository = progresoRepository;
    }

    @Transactional
    public ProgresoCursoDTO crearProgreso(ProgresoCursoDTO dto) {
        ProgresoCurso p = new ProgresoCurso();
        p.setEstudianteId(dto.getEstudianteId());
        p.setCursoId(dto.getCursoId());
        p.setPorcentaje(dto.getPorcentaje() != null ? dto.getPorcentaje() : 0);
        p.setEstado(dto.getEstado() != null ? dto.getEstado() : "NO_INICIADO");
        return toDTO(progresoRepository.save(p));
    }

    @Transactional(readOnly = true)
    public List<ProgresoCursoDTO> listarProgreso() {
        return progresoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProgresoCursoDTO obtenerPorId(Long id) {
        ProgresoCurso p = progresoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progreso no encontrado con ID: " + id));
        return toDTO(p);
    }

    @Transactional(readOnly = true)
    public List<ProgresoCursoDTO> listarPorEstudiante(Long estudianteId) {
        return progresoRepository.findByEstudianteId(estudianteId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ProgresoCursoDTO actualizarProgreso(Long id, ProgresoCursoDTO dto) {
        ProgresoCurso p = progresoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progreso no encontrado con ID: " + id));
        if (dto.getEstudianteId() != null) p.setEstudianteId(dto.getEstudianteId());
        if (dto.getCursoId() != null) p.setCursoId(dto.getCursoId());
        if (dto.getPorcentaje() != null) p.setPorcentaje(dto.getPorcentaje());
        if (dto.getEstado() != null) p.setEstado(dto.getEstado());
        p.setFechaActualizacion(LocalDateTime.now());
        return toDTO(progresoRepository.save(p));
    }

    @Transactional
    public ProgresoCursoDTO upsertProgreso(ProgresoCursoDTO dto) {
        ProgresoCurso p = progresoRepository
                .findByEstudianteIdAndCursoId(dto.getEstudianteId(), dto.getCursoId())
                .orElseGet(ProgresoCurso::new);
        p.setEstudianteId(dto.getEstudianteId());
        p.setCursoId(dto.getCursoId());
        if (dto.getPorcentaje() != null) p.setPorcentaje(dto.getPorcentaje());
        if (dto.getEstado() != null) p.setEstado(dto.getEstado());
        if (p.getPorcentaje() == null) p.setPorcentaje(0);
        if (p.getEstado() == null) p.setEstado("NO_INICIADO");
        p.setFechaActualizacion(LocalDateTime.now());
        return toDTO(progresoRepository.save(p));
    }

    @Transactional
    public void eliminarProgreso(Long id) {
        if (!progresoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Progreso no encontrado con ID: " + id);
        }
        progresoRepository.deleteById(id);
    }

    public ProgresoCursoDTO toDTO(ProgresoCurso p) {
        ProgresoCursoDTO dto = new ProgresoCursoDTO();
        dto.setId(p.getId());
        dto.setEstudianteId(p.getEstudianteId());
        dto.setCursoId(p.getCursoId());
        dto.setPorcentaje(p.getPorcentaje());
        dto.setEstado(p.getEstado());
        dto.setFechaCreacion(p.getFechaCreacion());
        dto.setFechaActualizacion(p.getFechaActualizacion());
        return dto;
    }
}
