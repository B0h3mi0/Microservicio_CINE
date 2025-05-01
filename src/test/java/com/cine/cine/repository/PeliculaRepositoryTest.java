package com.cine.cine.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import com.cine.cine.model.Pelicula;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class PeliculaRepositoryTest {
    @Autowired
    private PeliculaRepository peliculaRepository;


    @Test
    public void guardarPeliculaTest(){
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo("La bella y la bestia");
        Pelicula resultado = peliculaRepository.save(pelicula);
        assertNotNull(resultado.getId());
        assertEquals( "La bella y la bestia", resultado.getTitulo());
    }
    
}
