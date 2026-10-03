package com.edu.plataforma.service;

import com.edu.plataforma.dto.UsuarioCreateDTO;
import com.edu.plataforma.dto.UsuarioDTO;
import com.edu.plataforma.dto.UsuarioUpdateDTO;
import com.edu.plataforma.exception.BadRequestException;
import com.edu.plataforma.exception.ConflictException;
import com.edu.plataforma.exception.UsuarioNoEncontradoException;
import com.edu.plataforma.model.Docente;
import com.edu.plataforma.model.Estudiante;
import com.edu.plataforma.model.Familiar;
import com.edu.plataforma.model.Usuario;
import com.edu.plataforma.repository.DocenteRepository;
import com.edu.plataforma.repository.EstudianteRepository;
import com.edu.plataforma.repository.FamiliarRepository;
import com.edu.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final FamiliarRepository familiarRepository;
    private final DocenteRepository docenteRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          EstudianteRepository estudianteRepository,
                          FamiliarRepository familiarRepository,
                          DocenteRepository docenteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
        this.familiarRepository = familiarRepository;
        this.docenteRepository = docenteRepository;
    }

    @Transactional
    public UsuarioDTO crearUsuario(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new ConflictException("Ya existe un usuario registrado con el correo: " + dto.getCorreo());
        }
        if (dto.getContrasena() == null || dto.getContrasena().length() < 6) {
            throw new BadRequestException("La contrasena debe tener minimo 6 caracteres");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasena(dto.getContrasena());
        usuario.setRol(dto.getRol().toUpperCase());
        usuario.setEstado("ACTIVO");

        Usuario saved = usuarioRepository.save(usuario);

        // Auto-crear entidad de rol según corresponda
        if ("ESTUDIANTE".equalsIgnoreCase(saved.getRol())) {
            Estudiante est = new Estudiante();
            est.setUsuario(saved);
            est.setPrograma(dto.getPrograma() != null ? dto.getPrograma() : "Desarrollo de Software");
            est.setSemestre(dto.getSemestre() != null ? dto.getSemestre() : 1);
            est.setCodigoEstudiante("EST-" + saved.getId());
            estudianteRepository.save(est);
        } else if ("FAMILIAR".equalsIgnoreCase(saved.getRol())) {
            Familiar fam = new Familiar();
            fam.setUsuario(saved);
            fam.setParentesco(dto.getParentesco() != null ? dto.getParentesco() : "Acudiente");
            fam.setTelefono(dto.getTelefono());
            familiarRepository.save(fam);
        } else if ("DOCENTE".equalsIgnoreCase(saved.getRol())) {
            Docente doc = new Docente();
            doc.setUsuario(saved);
            doc.setEspecialidad(dto.getEspecialidad() != null ? dto.getEspecialidad() : "Docente de Area");
            docenteRepository.save(doc);
        }

        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id));
        return toDTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con correo: " + correo));
        return toDTO(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarPorRol(String rol) {
        return usuarioRepository.findByRol(rol.toUpperCase()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id));

        if (!usuario.getCorreo().equalsIgnoreCase(dto.getCorreo())) {
            if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
                throw new ConflictException("El correo ya esta en uso por otro usuario: " + dto.getCorreo());
            }
            usuario.setCorreo(dto.getCorreo());
        }

        usuario.setNombre(dto.getNombre());
        if (dto.getEstado() != null) {
            usuario.setEstado(dto.getEstado().toUpperCase());
        }

        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado para eliminar con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNombre(), u.getCorreo(), u.getRol(), u.getEstado(), u.getFechaRegistro());
    }
}