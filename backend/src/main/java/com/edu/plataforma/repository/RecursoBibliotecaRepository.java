package com.edu.plataforma.repository;

import com.edu.plataforma.model.RecursoBiblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoBibliotecaRepository extends JpaRepository<RecursoBiblioteca, Long> {
}
