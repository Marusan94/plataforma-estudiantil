package com.edu.plataforma.repository;

import com.edu.plataforma.model.NotificacionFamiliar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionFamiliarRepository extends JpaRepository<NotificacionFamiliar, Long> {
    List<NotificacionFamiliar> findByFamiliarIdOrderByFechaEnvioDesc(Long familiarId);
}