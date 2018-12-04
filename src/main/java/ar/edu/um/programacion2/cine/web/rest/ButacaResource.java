package ar.edu.um.programacion2.cine.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.edu.um.programacion2.cine.domain.Butaca;
import ar.edu.um.programacion2.cine.domain.Sala;
import ar.edu.um.programacion2.cine.repository.ButacaRepository;
import ar.edu.um.programacion2.cine.repository.SalaRepository;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Butaca.
 */
@RestController
@RequestMapping("/api")
public class ButacaResource {

    private final Logger log = LoggerFactory.getLogger(ButacaResource.class);

    private static final String ENTITY_NAME = "butaca";

    private final ButacaRepository butacaRepository;

    @Autowired
    private SalaRepository salaRepository;
    
    public ButacaResource(ButacaRepository butacaRepository) {
        this.butacaRepository = butacaRepository;
    }

    @PostMapping("/butacas/{id_sala}/{butacas}")
    @Timed
    public void createButaca(@PathVariable Long id_sala, @PathVariable int butacas) throws URISyntaxException {
        log.debug("REST request to save Butaca : {}");
        
        Optional<Sala> sala = salaRepository.findById(id_sala);
        String[] filas = {"A", "B", "C", "D", "E"};
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j <= (butacas/5); j++) {
                Butaca butaca = new Butaca();
                butaca.setCreated(ZonedDateTime.now());
                butaca.setUpdated(ZonedDateTime.now());
                butaca.setSala(sala.get());
                butaca.setFila(filas[i]);
                butaca.setNumero(j);
                butaca.setDescripcion("Butaca: S: " + Long.toString(id_sala) + " F: " + filas[i] + " N: " + Integer.toString(j));
                butacaRepository.save(butaca);
            }
        }
    }
    
    /**
     * POST  /butacas : Create a new butaca.
     *
     * @param butaca the butaca to create
     * @return the ResponseEntity with status 201 (Created) and with body the new butaca, or with status 400 (Bad Request) if the butaca has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/butacas")
    @Timed
    public ResponseEntity<Butaca> createButaca(@Valid @RequestBody Butaca butaca) throws URISyntaxException {
        log.debug("REST request to save Butaca : {}", butaca);
        if (butaca.getId() != null) {
            throw new BadRequestAlertException("A new butaca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        butaca.setCreated(ZonedDateTime.now());
        butaca.setUpdated(ZonedDateTime.now());
        Butaca result = butacaRepository.save(butaca);
        
        return ResponseEntity.created(new URI("/api/butacas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /butacas : Updates an existing butaca.
     *
     * @param butaca the butaca to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated butaca,
     * or with status 400 (Bad Request) if the butaca is not valid,
     * or with status 500 (Internal Server Error) if the butaca couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/butacas")
    @Timed
    public ResponseEntity<Butaca> updateButaca(@Valid @RequestBody Butaca butaca) throws URISyntaxException {
        log.debug("REST request to update Butaca : {}", butaca);
        if (butaca.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        butaca.setUpdated(ZonedDateTime.now());
        Butaca result = butacaRepository.save(butaca);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, butaca.getId().toString()))
            .body(result);
    }

    /**
     * GET  /butacas : get all the butacas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of butacas in body
     */
    @GetMapping("/butacas")
    @Timed
    public List<Butaca> getAllButacas() {
        log.debug("REST request to get all Butacas");
        return butacaRepository.findAll();
    }

    /**
     * GET  /butacas/:id : get the "id" butaca.
     *
     * @param id the id of the butaca to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the butaca, or with status 404 (Not Found)
     */
    @GetMapping("/butacas/{id}")
    @Timed
    public ResponseEntity<Butaca> getButaca(@PathVariable Long id) {
        log.debug("REST request to get Butaca : {}", id);
        Optional<Butaca> butaca = butacaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(butaca);
    }

    /**
     * DELETE  /butacas/:id : delete the "id" butaca.
     *
     * @param id the id of the butaca to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/butacas/{id}")
    @Timed
    public ResponseEntity<Void> deleteButaca(@PathVariable Long id) {
        log.debug("REST request to delete Butaca : {}", id);

        butacaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
