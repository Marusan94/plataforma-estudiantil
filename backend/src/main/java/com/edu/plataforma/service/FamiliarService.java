package com.edu.plataforma.service;

import com.edu.plataforma.dto.FamiliarDTO;
import com.edu.plataforma.exception.ConflictException;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.Familiar;
import com.edu.plataforma.model.Usuario;
import com.edu.plataforma.repository.FamiliarRepository;
import com.edu.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FamiliarService {

    private final FamiliarRepository familiarRepository;
    private final UsuarioRepository usuarioRepository;

    public FamiliarService(FamiliarRepository familiarRepository, UsuarioRepository usuarioRepository) {
        this.familiarRepository = familiarRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public FamiliarDTO registrarFamiliar(FamiliarDTO dto) {
        if (dto.getCorreo() != null && usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new ConflictException("Ya existe un usuario con este correo: " + dto.getCorreo());
        }

        Usuario usuario;
        if (dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getUsuarioId()));
        } else {
            usuario = new Usuario();
            usuario.setNombre(dto.getNombre());
            usuario.setCorreo(dto.getCorreo());
            usuario.setContrasena("123456");
            usuario.setRol("FAMILIAR");
            usuario.setEstado("ACTIVO");
            usuario = usuarioRepository.save(usuario);
        }

        Familiar fam = new Familiar();
        fam.setUsuario(usuario);
        fam.setParentesco(dto.getParentesco());
        fam.setTelefono(dto.getTelefono());
        fam.setDireccion(dto.getDireccion());

        return toDTO(familiarRepository.save(fam));
    }

    @Transactional(readOnly = true)
    public List<FamiliarDTO> listarFamiliares() {
        return familiarRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FamiliarDTO obtenerPorId(Long id) {
        Familiar fam = familiarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Familiar no encontrado con ID: " + id));
        return toDTO(fam);
    }

    @Transactional(readOnly = true)
    public boolean verificarExistePorCorreo(String correo) {
        return familiarRepository.findByUsuarioCorreo(correo).isPresent();
    }

    @Transactional
    public FamiliarDTO actualizarFamiliar(Long id, FamiliarDTO dto) {
        Familiar fam = familiarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Familiar no encontrado con ID: " + id));

        fam.setParentesco(dto.getParentesco());
        fam.setTelefono(dto.getTelefono());
        fam.setDireccion(dto.getDireccion());

        if (dto.getNombre() != null && fam.getUsuario() != null) {
            fam.getUsuario().setNombre(dto.getNombre());
            usuarioRepository.save(fam.getUsuario());
        }

        return toDTO(familiarRepository.save(fam));
    }

    @Transactional
    public void eliminarFamiliar(Long id) {
        if (!familiarRepository.existsById(id)) {
            throw new ResourceNotFoundException("Familiar no encontrado con ID: " + id);
        }
        familiarRepository.deleteById(id);
    }

    public FamiliarDTO toDTO(Familiar f) {
        FamiliarDTO dto = new FamiliarDTO();
        dto.setId(f.getId());
        if (f.getUsuario() != null) {
            dto.setUsuarioId(f.getUsuario().getId());
            dto.setNombre(f.getUsuario().getNombre());
            dto.setCorreo(f.getUsuario().getCorreo());
        }
        dto.setParentesco(f.getParentesco());
        dto.setTelefono(f.getTelefono());
        dto.setDireccion(f.getDireccion());
        return dto;
    }
}