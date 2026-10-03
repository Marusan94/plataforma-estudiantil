package com.edu.plataforma.service;

import com.edu.plataforma.dto.*;
import com.edu.plataforma.exception.BadRequestException;
import com.edu.plataforma.exception.ConflictException;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.*;
import com.edu.plataforma.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BienestarService {

    private final NecesidadApoyoRepository necesidadRepository;
    private final EstudianteRepository estudianteRepository;
    private final ProfesionalRepository profesionalRepository;
    private final CategoriaNecesidadRepository categoriaRepository;
    private final TipoApoyoRepository tipoApoyoRepository;
    private final SeguimientoRepository seguimientoRepository;

    public BienestarService(NecesidadApoyoRepository necesidadRepository,
                            EstudianteRepository estudianteRepository,
                            ProfesionalRepository profesionalRepository,
                            CategoriaNecesidadRepository categoriaRepository,
                            TipoApoyoRepository tipoApoyoRepository,
                            SeguimientoRepository seguimientoRepository) {
        this.necesidadRepository = necesidadRepository;
        this.estudianteRepository = estudianteRepository;
        this.profesionalRepository = profesionalRepository;
        this.categoriaRepository = categoriaRepository;
        this.tipoApoyoRepository = tipoApoyoRepository;
        this.seguimientoRepository = seguimientoRepository;
    }

    @Transactional
    public NecesidadApoyoDTO registrarNecesidad(NecesidadApoyoDTO dto) {
        Estudiante est = estudianteRepository.findById(dto.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + dto.getEstudianteId()));

        NecesidadApoyo n = new NecesidadApoyo();
        n.setEstudiante(est);
        n.setTipo(dto.getTipo());
        n.setDescripcion(dto.getDescripcion());
        n.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
        
        String prio = dto.getPrioridad() != null ? dto.getPrioridad().toUpperCase() : "MEDIA";
        if (!Arrays.asList("ALTA", "MEDIA", "BAJA").contains(prio)) {
            throw new BadRequestException("La prioridad debe ser ALTA, MEDIA o BAJA");
        }
        n.setPrioridad(prio);
        n.setEstado("PENDIENTE");

        if (dto.getCategoriaId() != null) {
            CategoriaNecesidad cat = categoriaRepository.findById(dto.getCategoriaId()).orElse(null);
            n.setCategoria(cat);
        }

        if (dto.getProfesionalId() != null) {
            Profesional prof = profesionalRepository.findById(dto.getProfesionalId()).orElse(null);
            n.setProfesional(prof);
        }

        return toDTO(necesidadRepository.save(n));
    }

    @Transactional(readOnly = true)
    public List<NecesidadApoyoDTO> listarTodas() {
        return necesidadRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NecesidadApoyoDTO> listarPorEstudiante(Long estudianteId) {
        return necesidadRepository.findByEstudianteIdOrderByFechaDesc(estudianteId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NecesidadApoyoDTO obtenerPorId(Long id) {
        NecesidadApoyo n = necesidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad de apoyo no encontrada con ID: " + id));
        return toDTO(n);
    }

    @Transactional
    public NecesidadApoyoDTO actualizarNecesidad(Long id, NecesidadApoyoDTO dto) {
        NecesidadApoyo n = necesidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada con ID: " + id));

        if (dto.getTipo() != null) n.setTipo(dto.getTipo());
        if (dto.getDescripcion() != null) n.setDescripcion(dto.getDescripcion());
        if (dto.getPrioridad() != null) {
            String prio = dto.getPrioridad().toUpperCase();
            if (!Arrays.asList("ALTA", "MEDIA", "BAJA").contains(prio)) {
                throw new BadRequestException("Prioridad invalida. Use ALTA, MEDIA o BAJA");
            }
            n.setPrioridad(prio);
        }
        return toDTO(necesidadRepository.save(n));
    }

    @Transactional
    public void eliminarNecesidad(Long id) {
        if (!necesidadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Necesidad no encontrada para eliminar con ID: " + id);
        }
        necesidadRepository.deleteById(id);
    }

    @Transactional
    public NecesidadApoyoDTO asignarProfesional(Long necesidadId, Long profesionalId) {
        NecesidadApoyo n = necesidadRepository.findById(necesidadId)
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada con ID: " + necesidadId));
        Profesional p = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + profesionalId));

        n.setProfesional(p);
        if ("PENDIENTE".equalsIgnoreCase(n.getEstado())) {
            n.setEstado("EN_ATENCION");
        }
        return toDTO(necesidadRepository.save(n));
    }

    @Transactional
    public NecesidadApoyoDTO cambiarEstado(Long id, String nuevoEstado) {
        NecesidadApoyo n = necesidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada con ID: " + id));

        String est = nuevoEstado.toUpperCase();
        if (!Arrays.asList("PENDIENTE", "EN_ATENCION", "CERRADA").contains(est)) {
            throw new BadRequestException("Estado invalido. Opciones permitidas: PENDIENTE, EN_ATENCION, CERRADA");
        }

        n.setEstado(est);
        return toDTO(necesidadRepository.save(n));
    }

    @Transactional(readOnly = true)
    public List<NecesidadApoyoDTO> listarPorProfesional(Long profesionalId) {
        return necesidadRepository.findByProfesionalIdOrderByFechaDesc(profesionalId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NecesidadApoyoDTO> listarPorCategoria(Long categoriaId) {
        return necesidadRepository.findByCategoriaId(categoriaId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public SeguimientoDTO registrarSeguimiento(SeguimientoDTO dto) {
        NecesidadApoyo n = necesidadRepository.findById(dto.getNecesidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada con ID: " + dto.getNecesidadId()));

        Seguimiento s = new Seguimiento(n, dto.getFecha() != null ? dto.getFecha() : LocalDate.now(), dto.getDescripcion(), dto.getEstado());
        Seguimiento guardado = seguimientoRepository.save(s);

        if ("CERRADA".equalsIgnoreCase(dto.getEstado())) {
            n.setEstado("CERRADA");
            necesidadRepository.save(n);
        }

        dto.setId(guardado.getId());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<SeguimientoDTO> listarSeguimientos(Long necesidadId) {
        return seguimientoRepository.findByNecesidadIdOrderByFechaDesc(necesidadId).stream().map(s -> {
            SeguimientoDTO dto = new SeguimientoDTO();
            dto.setId(s.getId());
            dto.setNecesidadId(s.getNecesidad().getId());
            dto.setFecha(s.getFecha());
            dto.setDescripcion(s.getDescripcion());
            dto.setEstado(s.getEstado());
            return dto;
        }).collect(Collectors.toList());
    }

    // Gestion de Tipos de Apoyo
    @Transactional
    public TipoApoyoDTO registrarTipoApoyo(TipoApoyoDTO dto) {
        if (tipoApoyoRepository.findByNombre(dto.getNombre()).isPresent()) {
            throw new ConflictException("Ya existe este tipo de apoyo");
        }
        TipoApoyo t = new TipoApoyo(dto.getNombre(), dto.getDescripcion());
        TipoApoyo g = tipoApoyoRepository.save(t);
        return new TipoApoyoDTO(g.getId(), g.getNombre(), g.getDescripcion());
    }

    @Transactional(readOnly = true)
    public List<TipoApoyoDTO> listarTiposApoyo() {
        return tipoApoyoRepository.findAll().stream()
                .map(t -> new TipoApoyoDTO(t.getId(), t.getNombre(), t.getDescripcion()))
                .collect(Collectors.toList());
    }

    // Gestion de Categorias
    @Transactional
    public CategoriaNecesidadDTO registrarCategoria(CategoriaNecesidadDTO dto) {
        if (categoriaRepository.findByNombre(dto.getNombre()).isPresent()) {
            throw new ConflictException("Ya existe esta categoria");
        }
        CategoriaNecesidad c = new CategoriaNecesidad(dto.getNombre());
        CategoriaNecesidad g = categoriaRepository.save(c);
        return new CategoriaNecesidadDTO(g.getId(), g.getNombre());
    }

    @Transactional(readOnly = true)
    public List<CategoriaNecesidadDTO> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(c -> new CategoriaNecesidadDTO(c.getId(), c.getNombre()))
                .collect(Collectors.toList());
    }

    // Gestion de Profesionales
    @Transactional
    public ProfesionalDTO registrarProfesional(ProfesionalDTO dto) {
        Profesional p = new Profesional(dto.getNombre(), dto.getArea(), dto.getCargo(), dto.getCorreo());
        Profesional g = profesionalRepository.save(p);
        dto.setId(g.getId());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<ProfesionalDTO> listarProfesionales() {
        return profesionalRepository.findAll().stream().map(p -> {
            ProfesionalDTO dto = new ProfesionalDTO();
            dto.setId(p.getId());
            dto.setNombre(p.getNombre());
            dto.setArea(p.getArea());
            dto.setCargo(p.getCargo());
            dto.setCorreo(p.getCorreo());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public ProfesionalDTO actualizarProfesional(Long id, ProfesionalDTO dto) {
        Profesional p = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + id));
        p.setNombre(dto.getNombre());
        p.setArea(dto.getArea());
        p.setCargo(dto.getCargo());
        p.setCorreo(dto.getCorreo());
        Profesional g = profesionalRepository.save(p);
        dto.setId(g.getId());
        return dto;
    }

    @Transactional
    public void eliminarProfesional(Long id) {
        if (!profesionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profesional no encontrado con ID: " + id);
        }
        if (necesidadRepository.existsByProfesionalId(id)) {
            throw new BadRequestException("No se puede eliminar el profesional porque tiene casos asignados");
        }
        profesionalRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> reporteNecesidadesPorTipo() {
        List<Object[]> rows = necesidadRepository.contarPorTipo();
        List<Map<String, Object>> res = new ArrayList<>();
        for (Object[] r : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("tipo", r[0]);
            map.put("cantidad", r[1]);
            res.add(map);
        }
        return res;
    }

    public NecesidadApoyoDTO toDTO(NecesidadApoyo n) {
        NecesidadApoyoDTO dto = new NecesidadApoyoDTO();
        dto.setId(n.getId());
        dto.setTipo(n.getTipo());
        dto.setDescripcion(n.getDescripcion());
        dto.setFecha(n.getFecha());
        dto.setPrioridad(n.getPrioridad());
        dto.setEstado(n.getEstado());
        if (n.getEstudiante() != null) {
            dto.setEstudianteId(n.getEstudiante().getId());
            if (n.getEstudiante().getUsuario() != null) {
                dto.setNombreEstudiante(n.getEstudiante().getUsuario().getNombre());
            }
            dto.setProgramaEstudiante(n.getEstudiante().getPrograma());
        }
        if (n.getCategoria() != null) {
            dto.setCategoriaId(n.getCategoria().getId());
            dto.setNombreCategoria(n.getCategoria().getNombre());
        }
        if (n.getProfesional() != null) {
            dto.setProfesionalId(n.getProfesional().getId());
            dto.setNombreProfesional(n.getProfesional().getNombre());
        }
        if (n.getSeguimientos() != null) {
            dto.setSeguimientos(n.getSeguimientos().stream().map(s -> {
                SeguimientoDTO sDto = new SeguimientoDTO();
                sDto.setId(s.getId());
                sDto.setNecesidadId(n.getId());
                sDto.setFecha(s.getFecha());
                sDto.setDescripcion(s.getDescripcion());
                sDto.setEstado(s.getEstado());
                return sDto;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}