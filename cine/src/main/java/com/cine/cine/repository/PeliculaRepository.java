package com.cine.cine.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cine.cine.model.Pelicula;

public interface PeliculaRepository extends JpaRepository <Pelicula, Long> {
} 