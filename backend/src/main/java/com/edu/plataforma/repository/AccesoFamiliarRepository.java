package com.edu.plataforma.repository;

import com.edu.plataforma.model.AccesoFamiliar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccesoFamiliarRepository extends JpaRepository<AccesoFamiliar, Long> {
    List<AccesoFamiliar> findByFamiliarId(Long familiarId);
}