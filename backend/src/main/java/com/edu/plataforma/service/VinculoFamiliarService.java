package com.edu.plataforma.service;

import com.edu.plataforma.dto.VinculoDTO;
import com.edu.plataforma.exception.ConflictException;
import com.edu.plataforma.exception.ResourceNotFoundException;
import com.edu.plataforma.model.Estudiante;
import com.edu.plataforma.model.Familiar;
import com.edu.plataforma.model.VinculoFamiliarEstudiante;
import com.edu.plataforma.repository.EstudianteRepository;
import com.edu.plataforma.repository.FamiliarRepository;
import com.edu.plataforma.repository.VinculoFamiliarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VinculoFamiliarService {

    private final VinculoFamiliarRepository vinculoRepository;
    private final FamiliarRepository familiarRepository;
    private final EstudianteRepository estudianteRepository;

    public VinculoFamiliarService(VinculoFamiliarRepository vinculoRepository,
                                  FamiliarRepository familiarRepository,
                                  EstudianteRepository estudianteRepository) {
        this.vinculoRepository = vinculoRepository;
        this.familiarRepository = familiarRepository;
        this.estudianteRepository = estudianteRepository;
    }

    @Transactional
    public VinculoDTO crearVinculo(VinculoDTO dto) {
        Familiar familiar = familiarRepository.findById(dto.getIdFamiliar())
                .orElseThrow(() -> new ResourceNotFoundException("Familiar no encontrado con ID: " + dto.getIdFamiliar()));

        Estudiante estudiante = estudianteRepository.findById(dto.getIdEstudiante())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + dto.getIdEstudiante()));

        if (vinculoRepository.findByFamiliarIdAndEstudianteId(dto.getIdFamiliar(), dto.getIdEstudiante()).isPresent()) {
            throw new ConflictException("Este vinculo familiar ya se encuentra registrado");
        }

        VinculoFamiliarEstudiante vinculo = new VinculoFamiliarEstudiante(familiar, estudiante, dto.getAutorizado());
        return toDTO(vinculoRepository.save(vinculo));
    }

    @Transactional(readOnly = true)
    public List<VinculoDTO> listarTodos() {
        return vinculoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VinculoDTO> listarPorEstudiante(Long estudianteId) {
        return vinculoRepository.findByEstudianteId(estudianteId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VinculoDTO> listarPorFamiliar(Long familiarId) {
        return vinculoRepository.findByFamiliarId(familiarId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void eliminarVinculo(Long id) {
        if (!vinculoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vinculo no encontrado con ID: " + id);
        }
        vinculoRepository.deleteById(id);
    }

    public boolean estaAutorizado(Long familiarId, Long estudianteId) {
        return vinculoRepository.findByFamiliarIdAndEstudianteId(familiarId, estudianteId)
                .map(VinculoFamiliarEstudiante::getAutorizado)
                .orElse(false);
    }

    public VinculoDTO toDTO(VinculoFamiliarEstudiante v) {
        VinculoDTO dto = new VinculoDTO();
        dto.setId(v.getId());
        dto.setIdFamiliar(v.getFamiliar().getId());
        if (v.getFamiliar().getUsuario() != null) {
            dto.setNombreFamiliar(v.getFamiliar().getUsuario().getNombre());
        }
        dto.setIdEstudiante(v.getEstudiante().getId());
        if (v.getEstudiante().getUsuario() != null) {
            dto.setNombreEstudiante(v.getEstudiante().getUsuario().getNombre());
        }
        dto.setPrograma(v.getEstudiante().getPrograma());
        dto.setSemestre(v.getEstudiante().getSemestre());
        dto.setAutorizado(v.getAutorizado());
        return dto;
    }
}