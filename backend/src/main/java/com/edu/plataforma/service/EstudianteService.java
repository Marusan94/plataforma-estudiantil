package com.edu.plataforma.service;

import com.edu.plataforma.dto.EstudianteDTO;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.Estudiante;
import com.edu.plataforma.model.Usuario;
import com.edu.plataforma.repository.EstudianteRepository;
import com.edu.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;

    public EstudianteService(EstudianteRepository estudianteRepository, UsuarioRepository usuarioRepository) {
        this.estudianteRepository = estudianteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public EstudianteDTO crearEstudiante(EstudianteDTO dto) {
        Usuario usuario;
        if (dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getUsuarioId()));
        } else {
            usuario = new Usuario();
            usuario.setNombre(dto.getNombre());
            usuario.setCorreo(dto.getCorreo());
            usuario.setContrasena("123456");
            usuario.setRol("ESTUDIANTE");
            usuario.setEstado("ACTIVO");
            usuario = usuarioRepository.save(usuario);
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setUsuario(usuario);
        estudiante.setPrograma(dto.getPrograma());
        estudiante.setSemestre(dto.getSemestre() != null ? dto.getSemestre() : 1);
        estudiante.setFechaNacimiento(dto.getFechaNacimiento());
        estudiante.setCodigoEstudiante(dto.getCodigoEstudiante() != null ? dto.getCodigoEstudiante() : "EST-" + usuario.getId());
        estudiante.setGenero(dto.getGenero() != null ? dto.getGenero() : "Otro");

        return toDTO(estudianteRepository.save(estudiante));
    }

    @Transactional(readOnly = true)
    public List<EstudianteDTO> listarEstudiantes() {
        return estudianteRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstudianteDTO obtenerPorId(Long id) {
        Estudiante est = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + id));
        return toDTO(est);
    }

    @Transactional(readOnly = true)
    public EstudianteDTO obtenerPorUsuarioId(Long usuarioId) {
        Estudiante est = estudianteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con usuario ID: " + usuarioId));
        return toDTO(est);
    }

    @Transactional(readOnly = true)
    public List<EstudianteDTO> buscarPorNombreOCodigo(String query) {
        return estudianteRepository.findByUsuarioNombreContainingIgnoreCaseOrCodigoEstudianteContainingIgnoreCase(query, query)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public EstudianteDTO actualizarEstudiante(Long id, EstudianteDTO dto) {
        Estudiante est = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + id));

        est.setPrograma(dto.getPrograma());
        if (dto.getSemestre() != null) est.setSemestre(dto.getSemestre());
        if (dto.getFechaNacimiento() != null) est.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getGenero() != null) est.setGenero(dto.getGenero());

        if (dto.getNombre() != null && est.getUsuario() != null) {
            est.getUsuario().setNombre(dto.getNombre());
            usuarioRepository.save(est.getUsuario());
        }

        return toDTO(estudianteRepository.save(est));
    }

    @Transactional
    public void eliminarEstudiante(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estudiante no encontrado con ID: " + id);
        }
        estudianteRepository.deleteById(id);
    }

    public EstudianteDTO toDTO(Estudiante e) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(e.getId());
        if (e.getUsuario() != null) {
            dto.setUsuarioId(e.getUsuario().getId());
            dto.setNombre(e.getUsuario().getNombre());
            dto.setCorreo(e.getUsuario().getCorreo());
        }
        dto.setPrograma(e.getPrograma());
        dto.setSemestre(e.getSemestre());
        dto.setFechaNacimiento(e.getFechaNacimiento());
        dto.setCodigoEstudiante(e.getCodigoEstudiante());
        dto.setGenero(e.getGenero());
        return dto;
    }
}