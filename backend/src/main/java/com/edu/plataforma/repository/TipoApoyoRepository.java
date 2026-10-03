package com.edu.plataforma.repository;

import com.edu.plataforma.model.TipoApoyo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TipoApoyoRepository extends JpaRepository<TipoApoyo, Long> {
    Optional<TipoApoyo> findByNombre(String nombre);
}