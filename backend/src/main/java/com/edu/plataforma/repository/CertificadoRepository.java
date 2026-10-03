package com.edu.plataforma.repository;

import com.edu.plataforma.model.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
    List<Certificado> findByPerfilId(Long perfilId);
    List<Certificado> findByPerfilEstudianteId(Long estudianteId);
}