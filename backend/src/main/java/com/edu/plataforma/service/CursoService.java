package com.edu.plataforma.service;

import com.edu.plataforma.dto.CursoDTO;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.Curso;
import com.edu.plataforma.repository.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public CursoDTO crearCurso(CursoDTO dto) {
        Curso curso = new Curso();
        curso.setTitulo(dto.getTitulo());
        curso.setDescripcion(dto.getDescripcion());
        curso.setCodigo(dto.getCodigo());
        curso.setCategoria(dto.getCategoria());
        curso.setNivel(dto.getNivel());
        curso.setHoras(dto.getHoras());
        curso.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return toDTO(cursoRepository.save(curso));
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> listarCursos() {
        return cursoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CursoDTO obtenerPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + id));
        return toDTO(curso);
    }

    @Transactional
    public CursoDTO actualizarCurso(Long id, CursoDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + id));
        curso.setTitulo(dto.getTitulo());
        curso.setDescripcion(dto.getDescripcion());
        if (dto.getCodigo() != null) curso.setCodigo(dto.getCodigo());
        if (dto.getCategoria() != null) curso.setCategoria(dto.getCategoria());
        if (dto.getNivel() != null) curso.setNivel(dto.getNivel());
        if (dto.getHoras() != null) curso.setHoras(dto.getHoras());
        if (dto.getActivo() != null) curso.setActivo(dto.getActivo());
        return toDTO(cursoRepository.save(curso));
    }

    @Transactional
    public void eliminarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso no encontrado con ID: " + id);
        }
        cursoRepository.deleteById(id);
    }

    public CursoDTO toDTO(Curso c) {
        CursoDTO dto = new CursoDTO();
        dto.setId(c.getId());
        dto.setTitulo(c.getTitulo());
        dto.setDescripcion(c.getDescripcion());
        dto.setCodigo(c.getCodigo());
        dto.setCategoria(c.getCategoria());
        dto.setNivel(c.getNivel());
        dto.setHoras(c.getHoras());
        dto.setActivo(c.getActivo());
        dto.setFechaCreacion(c.getFechaCreacion());
        return dto;
    }
}
