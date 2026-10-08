package com.edu.plataforma.service;

import com.edu.plataforma.dto.RecursoBibliotecaDTO;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.RecursoBiblioteca;
import com.edu.plataforma.repository.RecursoBibliotecaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecursoBibliotecaService {

    private final RecursoBibliotecaRepository recursoRepository;

    public RecursoBibliotecaService(RecursoBibliotecaRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    @Transactional
    public RecursoBibliotecaDTO crearRecurso(RecursoBibliotecaDTO dto) {
        RecursoBiblioteca r = new RecursoBiblioteca();
        r.setTitulo(dto.getTitulo());
        r.setDescripcion(dto.getDescripcion());
        r.setAutor(dto.getAutor());
        r.setTipo(dto.getTipo());
        r.setCategoria(dto.getCategoria());
        r.setUrlArchivo(dto.getUrlArchivo());
        r.setTamanioMb(dto.getTamanioMb());
        return toDTO(recursoRepository.save(r));
    }

    @Transactional(readOnly = true)
    public List<RecursoBibliotecaDTO> listarRecursos() {
        return recursoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecursoBibliotecaDTO obtenerPorId(Long id) {
        RecursoBiblioteca r = recursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso no encontrado con ID: " + id));
        return toDTO(r);
    }

    @Transactional
    public RecursoBibliotecaDTO actualizarRecurso(Long id, RecursoBibliotecaDTO dto) {
        RecursoBiblioteca r = recursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso no encontrado con ID: " + id));
        r.setTitulo(dto.getTitulo());
        r.setDescripcion(dto.getDescripcion());
        if (dto.getAutor() != null) r.setAutor(dto.getAutor());
        if (dto.getTipo() != null) r.setTipo(dto.getTipo());
        if (dto.getCategoria() != null) r.setCategoria(dto.getCategoria());
        if (dto.getUrlArchivo() != null) r.setUrlArchivo(dto.getUrlArchivo());
        if (dto.getTamanioMb() != null) r.setTamanioMb(dto.getTamanioMb());
        return toDTO(recursoRepository.save(r));
    }

    @Transactional
    public void eliminarRecurso(Long id) {
        if (!recursoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso no encontrado con ID: " + id);
        }
        recursoRepository.deleteById(id);
    }

    public RecursoBibliotecaDTO toDTO(RecursoBiblioteca r) {
        RecursoBibliotecaDTO dto = new RecursoBibliotecaDTO();
        dto.setId(r.getId());
        dto.setTitulo(r.getTitulo());
        dto.setDescripcion(r.getDescripcion());
        dto.setAutor(r.getAutor());
        dto.setTipo(r.getTipo());
        dto.setCategoria(r.getCategoria());
        dto.setUrlArchivo(r.getUrlArchivo());
        dto.setTamanioMb(r.getTamanioMb());
        dto.setFechaCreacion(r.getFechaCreacion());
        return dto;
    }
}
