package com.edu.plataforma.repository;

import com.edu.plataforma.model.Empresario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmpresarioRepository extends JpaRepository<Empresario, Long> {
    Optional<Empresario> findByUsuarioId(Long usuarioId);
}