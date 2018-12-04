package ar.edu.um.programacion2.cine.web.rest;

import com.codahale.metrics.annotation.Timed;

import ar.edu.um.programacion2.cine.domain.Butaca;
import ar.edu.um.programacion2.cine.domain.Funcion;
import ar.edu.um.programacion2.cine.domain.Ocupacion;
import ar.edu.um.programacion2.cine.repository.ButacaRepository;
import ar.edu.um.programacion2.cine.repository.FuncionRepository;
import ar.edu.um.programacion2.cine.repository.OcupacionRepository;
import ar.edu.um.programacion2.cine.web.rest.errors.BadRequestAlertException;
import ar.edu.um.programacion2.cine.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Funcion.
 */
@RestController
@RequestMapping("/api")
public class FuncionResource {

    private final Logger log = LoggerFactory.getLogger(FuncionResource.class);

    private static final String ENTITY_NAME = "funcion";

    private final FuncionRepository funcionRepository;
    
    @Autowired
    private OcupacionRepository ocupacionRepository;

    @Autowired
    private ButacaRepository butacaRepository;
    
    public FuncionResource(FuncionRepository funcionRepository) {
        this.funcionRepository = funcionRepository;
    }

    /**
     * POST  /funcions : Create a new funcion.
     *
     * @param funcion the funcion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new funcion, or with status 400 (Bad Request) if the funcion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/funcions")
    @Timed
    public ResponseEntity<Funcion> createFuncion(@Valid @RequestBody Funcion funcion) throws URISyntaxException {
        log.debug("REST request to save Funcion : {}", funcion);
        if (funcion.getId() != null) {
            throw new BadRequestAlertException("A new funcion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        funcion.setCreated(ZonedDateTime.now());
        funcion.setUpdated(ZonedDateTime.now());
        Funcion result = funcionRepository.save(funcion);
        
        return ResponseEntity.created(new URI("/api/funcions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /funcions : Updates an existing funcion.
     *
     * @param funcion the funcion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated funcion,
     * or with status 400 (Bad Request) if the funcion is not valid,
     * or with status 500 (Internal Server Error) if the funcion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/funcions")
    @Timed
    public ResponseEntity<Funcion> updateFuncion(@Valid @RequestBody Funcion funcion) throws URISyntaxException {
        log.debug("REST request to update Funcion : {}", funcion);
        if (funcion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        funcion.setUpdated(ZonedDateTime.now());
        Funcion result = funcionRepository.save(funcion);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, funcion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /funcions : get all the funcions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of funcions in body
     */
    @GetMapping("/funcions")
    @Timed
    public List<Funcion> getAllFuncions() {
        log.debug("REST request to get all Funcions");
        return funcionRepository.findAll();
    }

    /**
     * GET  /funcions/:id : get the "id" funcion.
     *
     * @param id the id of the funcion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the funcion, or with status 404 (Not Found)
     */
    @GetMapping("/funcions/{id}")
    @Timed
    public ResponseEntity<Funcion> getFuncion(@PathVariable Long id) {
        log.debug("REST request to get Funcion : {}", id);
        Optional<Funcion> funcion = funcionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(funcion);
    }

    /**
     * DELETE  /funcions/:id : delete the "id" funcion.
     *
     * @param id the id of the funcion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/funcions/{id}")
    @Timed
    public ResponseEntity<Void> deleteFuncion(@PathVariable Long id) {
        log.debug("REST request to delete Funcion : {}", id);

        funcionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * GET /funcions/{id}/disponibles busca todas las butacas disponibles por funcion
     * 
     * @param id el id de funcion
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/funcions/{id}/disponibles")
    @Timed
    public List<Butaca> getAllButacasDisponibles(@PathVariable Long id) {
        log.debug("Peticion REST de todas las butacas disponibles por funcion : {}", id);
        
        Optional<Funcion> funcion = funcionRepository.findById(id);
        List<Ocupacion> ocupaciones = ocupacionRepository.findAllByFuncionAndButacaNotNull(funcion.get());
        
        if(ocupaciones.isEmpty()) {
        	return butacaRepository.findAllBySala(funcion.get().getSala());
        }
        
        List<Long> ocupadas = new ArrayList<>();
        
        for (int i = 0; i < ocupaciones.size(); i++) {
        	ocupadas.add(ocupaciones.get(i).getButaca().getId());
        }
        
        return butacaRepository.findAllBySalaAndIdNotIn(funcion.get().getSala(), ocupadas);
    }
    
    /**
     * GET /funcions/{id}/butacas busca todas las butacas
     * 
     * @param id el id de funcion
     * @return the ResponseEntity with status 200 (OK)
     */
    
    @GetMapping("/funcions/{id}/butacas")
    @Timed
    public Set<Butaca> getAllButacas(@PathVariable Long id) {
        log.debug("REST request to get Butacas de una funcion : {}", id);

        Optional<Funcion> funcion = funcionRepository.findById(id);

        return funcion.get().getSala().getButacas();
    }
    
}
