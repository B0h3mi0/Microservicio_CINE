package com.cine.cine.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.annotation.Repeatable;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.cine.cine.model.Pelicula;
import com.cine.cine.service.PeliculaService;

@WebMvcTest(PeliculaController.class)
public class PeliculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PeliculaService peliculaServiceMock;

    @Test
    public void obtenerTodosTest() throws Exception {
        Pelicula peli1 = new Pelicula();
        peli1.setTitulo("Xmens");
        peli1.setId(1L);
        Pelicula peli2 = new Pelicula();
        peli2.setTitulo("Terminator");
        peli2.setId(2L);
        List<Pelicula> peliculas = List.of(peli1, peli2);

        List<EntityModel<Pelicula>> peliculasResources = peliculas.stream()
            .map(pelicula -> EntityModel.of(pelicula))
            .collect(Collectors.toList());

        when(peliculaServiceMock.getAllPeliculas()).thenReturn(peliculas);

        mockMvc.perform(get("/peliculas"))
                .andExpect(status().isOk())
                // Here, use direct JSON path matching without Matchers
                .andExpect(jsonPath("$._embedded.peliculas.length()").value(2))
                .andExpect(jsonPath("$._embedded.peliculas[0].titulo").value("Xmens"))
                .andExpect(jsonPath("$._embedded.peliculas[1].titulo").value("Terminator"))
                .andExpect(jsonPath("$._embedded.peliculas[0]._links.self.href").value("http://localhost/peliculas/1"))
                .andExpect(jsonPath("$._embedded.peliculas[1]._links.self.href").value("http://localhost/peliculas/2"));
    }
}

