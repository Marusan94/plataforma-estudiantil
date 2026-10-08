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
import com.edu.plataforma.repository.ProgresoCursoRepository;
import com.edu.plataforma.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final ProgresoCursoRepository progresoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          EstudianteRepository estudianteRepository,
                          FamiliarRepository familiarRepository,
                          DocenteRepository docenteRepository,
                          ProgresoCursoRepository progresoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
        this.familiarRepository = familiarRepository;
        this.docenteRepository = docenteRepository;
        this.progresoRepository = progresoRepository;
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

    @Transactional(readOnly = true)
    public UsuarioDTO login(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo == null ? "" : correo.trim())
                .orElseThrow(() -> new BadRequestException("Credenciales invalidas: correo o contrasena incorrectos"));
        if (contrasena == null || !contrasena.equals(usuario.getContrasena())) {
            throw new BadRequestException("Credenciales invalidas: correo o contrasena incorrectos");
        }
        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            throw new BadRequestException("Usuario inactivo. Contacte al administrador");
        }
        return toDTO(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado para eliminar con ID: " + id));
        try {
            String rol = usuario.getRol() == null ? "" : usuario.getRol().toUpperCase();
            if ("ESTUDIANTE".equals(rol)) {
                estudianteRepository.findByUsuarioId(id).ifPresent(est -> {
                    progresoRepository.findByEstudianteId(est.getId())
                            .forEach(p -> progresoRepository.deleteById(p.getId()));
                    estudianteRepository.deleteById(est.getId());
                });
            } else if ("DOCENTE".equals(rol)) {
                docenteRepository.findByUsuarioId(id).ifPresent(d -> docenteRepository.deleteById(d.getId()));
            } else if ("FAMILIAR".equals(rol)) {
                familiarRepository.findByUsuarioId(id).ifPresent(f -> familiarRepository.deleteById(f.getId()));
            }
            usuarioRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("No se puede eliminar: el usuario tiene historial academico asociado (notas, asistencias o matriculas)");
        }
    }

    public UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNombre(), u.getCorreo(), u.getRol(), u.getEstado(), u.getFechaRegistro());
    }
}