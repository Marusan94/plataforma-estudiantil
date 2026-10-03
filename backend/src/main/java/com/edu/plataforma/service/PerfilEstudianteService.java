package com.edu.plataforma.service;

import com.edu.plataforma.dto.*;
import com.edu.plataforma.exception.ConflictException;
import com.edu.plataforma.exception.PerfilNoEncontradoException;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.*;
import com.edu.plataforma.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PerfilEstudianteService {

    private final PerfilEstudianteRepository perfilRepository;
    private final EstudianteRepository estudianteRepository;
    private final HabilidadRepository habilidadRepository;
    private final ProyectoRepository proyectoRepository;
    private final CertificadoRepository certificadoRepository;

    public PerfilEstudianteService(PerfilEstudianteRepository perfilRepository,
                                  EstudianteRepository estudianteRepository,
                                  HabilidadRepository habilidadRepository,
                                  ProyectoRepository proyectoRepository,
                                  CertificadoRepository certificadoRepository) {
        this.perfilRepository = perfilRepository;
        this.estudianteRepository = estudianteRepository;
        this.habilidadRepository = habilidadRepository;
        this.proyectoRepository = proyectoRepository;
        this.certificadoRepository = certificadoRepository;
    }

    @Transactional
    public PerfilEstudianteDTO crearPerfil(PerfilEstudianteDTO dto) {
        if (dto.getEstudianteId() == null) {
            // Contexto simulado: toma el primer estudiante si no se suministra
            List<Estudiante> todos = estudianteRepository.findAll();
            if (!todos.isEmpty()) {
                dto.setEstudianteId(todos.get(0).getId());
            } else {
                throw new ResourceNotFoundException("No existen estudiantes para asociar el perfil");
            }
        }

        if (perfilRepository.existsByEstudianteId(dto.getEstudianteId())) {
            throw new ConflictException("El estudiante ya cuenta con una hoja de vida registrada.");
        }

        Estudiante est = estudianteRepository.findById(dto.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + dto.getEstudianteId()));

        PerfilEstudiante perfil = new PerfilEstudiante();
        perfil.setEstudiante(est);
        perfil.setResumen(dto.getResumen());
        perfil.setIntereses(dto.getIntereses());
        perfil.setExperiencia(dto.getExperiencia());
        perfil.setFotoUrl(dto.getFotoUrl());

        PerfilEstudiante guardado = perfilRepository.save(perfil);
        return toDTO(guardado);
    }

    @Transactional(readOnly = true)
    public PerfilEstudianteDTO obtenerPorEstudianteId(Long estudianteId) {
        PerfilEstudiante perfil = perfilRepository.findByEstudianteId(estudianteId)
                .orElseThrow(() -> new PerfilNoEncontradoException("Hoja de vida no encontrada para el estudiante: " + estudianteId));
        return toDTO(perfil);
    }

    @Transactional(readOnly = true)
    public PerfilEstudianteDTO obtenerPorId(Long id) {
        PerfilEstudiante perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new PerfilNoEncontradoException("Hoja de vida no encontrada con ID: " + id));
        return toDTO(perfil);
    }

    @Transactional(readOnly = true)
    public Page<PerfilEstudianteDTO> listarPerfiles(String programa, Integer semestre, Pageable pageable) {
        return perfilRepository.filtrarPerfiles(programa, semestre, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<PerfilEstudianteDTO> buscarPorPalabraClave(String query) {
        return perfilRepository.buscarPorPalabraClave(query).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public PerfilEstudianteDTO actualizarPerfil(Long id, PerfilEstudianteDTO dto) {
        PerfilEstudiante perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new PerfilNoEncontradoException("Perfil no encontrado con ID: " + id));

        perfil.setResumen(dto.getResumen());
        perfil.setIntereses(dto.getIntereses());
        perfil.setExperiencia(dto.getExperiencia());
        if (dto.getFotoUrl() != null) {
            perfil.setFotoUrl(dto.getFotoUrl());
        }

        return toDTO(perfilRepository.save(perfil));
    }

    @Transactional
    public void eliminarPerfil(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new PerfilNoEncontradoException("Perfil no encontrado con ID: " + id);
        }
        perfilRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> exportarPerfilBorrador(Long id) {
        PerfilEstudianteDTO dto = obtenerPorId(id);
        Map<String, Object> resume = new HashMap<>();
        resume.put("titulo", "Hoja de Vida Academica - EduPortal");
        resume.put("estudiante", dto.getNombreEstudiante());
        resume.put("programa", dto.getPrograma() + " - Semestre " + dto.getSemestre());
        resume.put("correo", dto.getCorreoEstudiante());
        resume.put("resumenProfesional", dto.getResumen());
        resume.put("intereses", dto.getIntereses());
        resume.put("experiencia", dto.getExperiencia());
        resume.put("totalHabilidades", dto.getHabilidades().size());
        resume.put("totalProyectos", dto.getProyectos().size());
        resume.put("totalCertificados", dto.getCertificados().size());
        resume.put("proyectos", dto.getProyectos());
        resume.put("certificados", dto.getCertificados());
        resume.put("habilidades", dto.getHabilidades());
        return resume;
    }

    // Sub-recursos Habilidades, Proyectos, Certificados
    @Transactional
    public HabilidadDTO agregarHabilidad(Long perfilId, HabilidadDTO dto) {
        PerfilEstudiante perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new PerfilNoEncontradoException("Perfil no encontrado con ID: " + perfilId));
        Habilidad h = new Habilidad(dto.getNombre(), dto.getNivel(), dto.getTipo(), perfil);
        Habilidad guardada = habilidadRepository.save(h);
        return new HabilidadDTO(guardada.getId(), perfilId, guardada.getNombre(), guardada.getNivel(), guardada.getTipo());
    }

    @Transactional
    public void eliminarHabilidad(Long habilidadId) {
        if (!habilidadRepository.existsById(habilidadId)) {
            throw new ResourceNotFoundException("Habilidad no encontrada con ID: " + habilidadId);
        }
        habilidadRepository.deleteById(habilidadId);
    }

    @Transactional
    public ProyectoDTO agregarProyecto(Long perfilId, ProyectoDTO dto) {
        PerfilEstudiante perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new PerfilNoEncontradoException("Perfil no encontrado con ID: " + perfilId));
        Proyecto p = new Proyecto();
        p.setPerfil(perfil);
        p.setTitulo(dto.getTitulo());
        p.setDescripcion(dto.getDescripcion());
        p.setUrl(dto.getUrl());
        p.setTecnologias(dto.getTecnologias());
        Proyecto guardado = proyectoRepository.save(p);
        dto.setId(guardado.getId());
        dto.setPerfilId(perfilId);
        return dto;
    }

    @Transactional
    public ProyectoDTO actualizarProyecto(Long proyectoId, ProyectoDTO dto) {
        Proyecto p = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));
        p.setTitulo(dto.getTitulo());
        p.setDescripcion(dto.getDescripcion());
        p.setUrl(dto.getUrl());
        p.setTecnologias(dto.getTecnologias());
        Proyecto guardado = proyectoRepository.save(p);
        dto.setId(guardado.getId());
        dto.setPerfilId(p.getPerfil().getId());
        return dto;
    }

    @Transactional
    public CertificadoDTO agregarCertificado(Long perfilId, CertificadoDTO dto) {
        PerfilEstudiante perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new PerfilNoEncontradoException("Perfil no encontrado con ID: " + perfilId));
        Certificado c = new Certificado();
        c.setPerfil(perfil);
        c.setNombre(dto.getNombre());
        c.setInstitucion(dto.getInstitucion());
        c.setFecha(dto.getFecha());
        c.setUrlArchivo(dto.getUrlArchivo());
        Certificado guardado = certificadoRepository.save(c);
        dto.setId(guardado.getId());
        dto.setPerfilId(perfilId);
        return dto;
    }

    public PerfilEstudianteDTO toDTO(PerfilEstudiante p) {
        PerfilEstudianteDTO dto = new PerfilEstudianteDTO();
        dto.setId(p.getId());
        if (p.getEstudiante() != null) {
            dto.setEstudianteId(p.getEstudiante().getId());
            if (p.getEstudiante().getUsuario() != null) {
                dto.setNombreEstudiante(p.getEstudiante().getUsuario().getNombre());
                dto.setCorreoEstudiante(p.getEstudiante().getUsuario().getCorreo());
            }
            dto.setPrograma(p.getEstudiante().getPrograma());
            dto.setSemestre(p.getEstudiante().getSemestre());
        }
        dto.setResumen(p.getResumen());
        dto.setIntereses(p.getIntereses());
        dto.setExperiencia(p.getExperiencia());
        dto.setFotoUrl(p.getFotoUrl());

        if (p.getHabilidades() != null) {
            dto.setHabilidades(p.getHabilidades().stream()
                    .map(h -> new HabilidadDTO(h.getId(), p.getId(), h.getNombre(), h.getNivel(), h.getTipo()))
                    .collect(Collectors.toList()));
        }
        if (p.getProyectos() != null) {
            dto.setProyectos(p.getProyectos().stream().map(pr -> {
                ProyectoDTO prDto = new ProyectoDTO();
                prDto.setId(pr.getId());
                prDto.setPerfilId(p.getId());
                prDto.setTitulo(pr.getTitulo());
                prDto.setDescripcion(pr.getDescripcion());
                prDto.setUrl(pr.getUrl());
                prDto.setTecnologias(pr.getTecnologias());
                return prDto;
            }).collect(Collectors.toList()));
        }
        if (p.getCertificados() != null) {
            dto.setCertificados(p.getCertificados().stream().map(c -> {
                CertificadoDTO cDto = new CertificadoDTO();
                cDto.setId(c.getId());
                cDto.setPerfilId(p.getId());
                cDto.setNombre(c.getNombre());
                cDto.setInstitucion(c.getInstitucion());
                cDto.setFecha(c.getFecha());
                cDto.setUrlArchivo(c.getUrlArchivo());
                return cDto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }
}