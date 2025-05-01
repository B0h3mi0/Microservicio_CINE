package com.cine.cine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import com.cine.cine.model.Pelicula;
import com.cine.cine.service.PeliculaService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;






@RestController
@RequestMapping("/peliculas")
public class PeliculaController {
    @Autowired
    private PeliculaService peliculaService;

    private static final Logger log = LoggerFactory.getLogger(PeliculaController.class);

    /*  @GetMapping                           
    public List<Pelicula> getAllPeliculas(){
        return peliculaService.getAllPeliculas();
    } */

    @GetMapping
    public CollectionModel<EntityModel<Pelicula>> getAllPeliculas() {
        List<Pelicula> peliculas = peliculaService.getAllPeliculas();
        log.info("GET /peliculas");
        log.info("Retornando todos los peliculas");
        List<EntityModel<Pelicula>> peliculasResources = peliculas.stream()
            .map( pelicula -> EntityModel.of(pelicula,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPeliculaById(pelicula.getId())).withSelfRel()
            ))
            .collect(Collectors.toList());

        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllPeliculas());
        CollectionModel<EntityModel<Pelicula>> resources = CollectionModel.of(peliculasResources, linkTo.withRel("peliculas"));

        return resources;
    }
        
    /* @GetMapping("/{id}") 
    public Optional<Pelicula> getPeliculaById(@PathVariable Long id) {
        return peliculaService.getPeliculaById(id);
    } */

    @GetMapping("/{id}")
    public EntityModel<Pelicula> getPeliculaById(@PathVariable Long id) {
        Optional<Pelicula> pelicula = peliculaService.getPeliculaById(id);

        if (pelicula.isPresent()) {
            return EntityModel.of(pelicula.get(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPeliculaById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllPeliculas()).withRel("all-peliculas"));
        } else {
            throw new PeliculaNotFoundException("Pelicula not found with id: " + id);
        }
    }

    /* @PostMapping 
    public Pelicula creaPelicula(@RequestBody Pelicula pelicula) {
        return peliculaService.createPelicula(pelicula);
    } */
    
    @PostMapping
    public EntityModel<Pelicula> createPelicula(@Validated @RequestBody Pelicula pelicula) {
        Pelicula createdPelicula = peliculaService.createPelicula(pelicula);
            return EntityModel.of(createdPelicula,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPeliculaById(createdPelicula.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllPeliculas()).withRel("all-peliculas"));

    }

                            
    /* @PutMapping("/{id}")
    public Pelicula updatePelicula(@PathVariable Long id, @RequestBody Pelicula pelicula) {
        return peliculaService.updatePelicula(id, pelicula);
    } */

    @PutMapping("/{id}")
    public EntityModel<Pelicula> updatePelicula(@PathVariable Long id, @RequestBody Pelicula pelicula) {
        Pelicula updatedPelicula = peliculaService.updatePelicula(id, pelicula);
        return EntityModel.of(updatedPelicula,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPeliculaById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllPeliculas()).withRel("all-peliculas"));

    }

    /* @DeleteMapping("/{id}")  
    public void deletePelicula(@PathVariable Long id){
        peliculaService.deletePelicula(id);
    } */

    @DeleteMapping("/{id}")
    public void deletePelicula(@PathVariable Long id){
        peliculaService.deletePelicula(id);
    } 

    static class ErrorResponse {
        private final String message;
    
        public ErrorResponse(String message) {
            this.message = message;
        }
    
        public String getMessage() {
            return message;
        }
    }
}










/*
@RestController
public class PeliculaController {

    private List<Pelicula> peliculas = new ArrayList<>();
}

    
    public PeliculaController(){
        // PELICULA ID 1  //
        peliculas.add(new Pelicula(1,"Los 8 más odiados",2015,"Quentin Tarantino", "Wéstern/Acción","Wyoming, poco después de acabar la Guerra Civil estadounidense. Un cazarrecompensas, su preso, otro cazarrecompensas y un supuesto sheriff quedan atrapados junto a cuatro desconocidos en una fonda durante una tormenta de nieve. Entre los ocho individuos hay viejas cuentas pendientes que saldar, y probablemente cuando pase la tormenta no todos saldrán con vida del edificio."));
        // PELICULA ID 2  //
        peliculas.add(new Pelicula(2,"Bastardos sin gloria",2009,"Quentin Tarantino", "Bélico/Acción","Es el primer año de la ocupación alemana de Francia. El oficial aliado, teniente Aldo Raine, ensambla un equipo de soldados judíos para cometer actos violentos en contra de los nazis, incluyendo la toma de cabelleras. Él y sus hombres unen fuerzas con Bridget von Hammersmark, una actriz alemana y agente encubierto, para derrocar a los líderes del Tercer Reich. Sus destinos convergen con la dueña de teatro Shosanna Dreyfus, quien busca vengar la ejecución de su familia."));
        // PELICULA ID 3  //
        peliculas.add(new Pelicula(3,"Fragmentado",2015,"M. Night Shyamalan", "Terror/Misterio","Kevin, un hombre psicótico con 23 personalidades, secuestra a 3 chicas jóvenes y las mantiene retenidas en un sótano. A medida que una de sus personalidades va imponiéndose al resto, la vida de las chicas, y la del propio Kevin, peligra cada vez más."));
        // PELICULA ID 4  //
        peliculas.add(new Pelicula(4,"El club de la pelea",1999,"David Fincher", "Acción/Crimen ","Un empleado de oficina insomne, harto de su vida, se cruza con un vendedor peculiar. Ambos crean un club de lucha clandestino como forma de terapia y, poco a poco, la organización crece y sus objetivos toman otro rumbo."));
        // PELICULA ID 5  //
        peliculas.add(new Pelicula(5,"La isla siniestra",2010,"Martin Scorsese", "Suspenso/Misterio","Un alguacil de los años cincuenta persigue a una asesina que escapó de un sanatorio en una isla remota, y hay pistas de hechos macabros cometidos en su interior."));

    }

    @GetMapping("/peliculas")
    public List<Pelicula> getPeliculas() {
        return peliculas;
    }
    
    @GetMapping("/peliculas/{id}")
    public Pelicula getPeliculaById(@PathVariable int id) {
        for(Pelicula pelicula : peliculas){
            if (pelicula.getId() == id){
                return pelicula;
            }
        }
        return null;
    }
    

}*/








