package com.edu.plataforma.service;

import com.edu.plataforma.dto.DocenteDTO;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.Docente;
import com.edu.plataforma.model.Usuario;
import com.edu.plataforma.repository.DocenteRepository;
import com.edu.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocenteService {

    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;

    public DocenteService(DocenteRepository docenteRepository, UsuarioRepository usuarioRepository) {
        this.docenteRepository = docenteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public DocenteDTO crearDocente(DocenteDTO dto) {
        Usuario usuario;
        if (dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getUsuarioId()));
        } else {
            usuario = new Usuario();
            usuario.setNombre(dto.getNombre());
            usuario.setCorreo(dto.getCorreo());
            usuario.setContrasena("123456");
            usuario.setRol("DOCENTE");
            usuario.setEstado("ACTIVO");
            usuario = usuarioRepository.save(usuario);
        }

        Docente doc = new Docente();
        doc.setUsuario(usuario);
        doc.setEspecialidad(dto.getEspecialidad());
        doc.setNivelAcademico(dto.getNivelAcademico());
        doc.setDepartamento(dto.getDepartamento());

        return toDTO(docenteRepository.save(doc));
    }

    @Transactional(readOnly = true)
    public List<DocenteDTO> listarDocentes() {
        return docenteRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DocenteDTO obtenerPorId(Long id) {
        Docente doc = docenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con ID: " + id));
        return toDTO(doc);
    }

    @Transactional
    public DocenteDTO actualizarDocente(Long id, DocenteDTO dto) {
        Docente doc = docenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con ID: " + id));

        doc.setEspecialidad(dto.getEspecialidad());
        doc.setNivelAcademico(dto.getNivelAcademico());
        doc.setDepartamento(dto.getDepartamento());

        if (dto.getNombre() != null && doc.getUsuario() != null) {
            doc.getUsuario().setNombre(dto.getNombre());
            usuarioRepository.save(doc.getUsuario());
        }

        return toDTO(docenteRepository.save(doc));
    }

    @Transactional
    public void eliminarDocente(Long id) {
        if (!docenteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Docente no encontrado con ID: " + id);
        }
        docenteRepository.deleteById(id);
    }

    public DocenteDTO toDTO(Docente d) {
        DocenteDTO dto = new DocenteDTO();
        dto.setId(d.getId());
        if (d.getUsuario() != null) {
            dto.setUsuarioId(d.getUsuario().getId());
            dto.setNombre(d.getUsuario().getNombre());
            dto.setCorreo(d.getUsuario().getCorreo());
        }
        dto.setEspecialidad(d.getEspecialidad());
        dto.setNivelAcademico(d.getNivelAcademico());
        dto.setDepartamento(d.getDepartamento());
        return dto;
    }
}