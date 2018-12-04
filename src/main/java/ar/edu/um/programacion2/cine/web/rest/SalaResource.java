package ar.edu.um.programacion2.cine.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.edu.um.programacion2.cine.domain.Sala;
import ar.edu.um.programacion2.cine.repository.SalaRepository;
import ar.edu.um.programacion2.cine.web.rest.errors.BadRequestAlertException;
import ar.edu.um.programacion2.cine.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Sala.
 */
@RestController
@RequestMapping("/api")
public class SalaResource {

    private final Logger log = LoggerFactory.getLogger(SalaResource.class);

    private static final String ENTITY_NAME = "sala";

    private final SalaRepository salaRepository;

    public SalaResource(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    /**
     * POST  /salas : Create a new sala.
     *
     * @param sala the sala to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sala, or with status 400 (Bad Request) if the sala has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/salas")
    @Timed
    public ResponseEntity<Sala> createSala(@Valid @RequestBody Sala sala) throws URISyntaxException {
        log.debug("REST request to save Sala : {}", sala);
        if (sala.getId() != null) {
            throw new BadRequestAlertException("A new sala cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        sala.setCreated(ZonedDateTime.now());
        sala.setUpdated(ZonedDateTime.now());
        Sala result = salaRepository.save(sala);
        
        return ResponseEntity.created(new URI("/api/salas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /salas : Updates an existing sala.
     *
     * @param sala the sala to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sala,
     * or with status 400 (Bad Request) if the sala is not valid,
     * or with status 500 (Internal Server Error) if the sala couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/salas")
    @Timed
    public ResponseEntity<Sala> updateSala(@Valid @RequestBody Sala sala) throws URISyntaxException {
        log.debug("REST request to update Sala : {}", sala);
        if (sala.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        sala.setUpdated(ZonedDateTime.now());
        Sala result = salaRepository.save(sala);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sala.getId().toString()))
            .body(result);
    }

    /**
     * GET  /salas : get all the salas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of salas in body
     */
    @GetMapping("/salas")
    @Timed
    public List<Sala> getAllSalas() {
        log.debug("REST request to get all Salas");
        return salaRepository.findAll();
    }

    /**
     * GET  /salas/:id : get the "id" sala.
     *
     * @param id the id of the sala to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sala, or with status 404 (Not Found)
     */
    @GetMapping("/salas/{id}")
    @Timed
    public ResponseEntity<Sala> getSala(@PathVariable Long id) {
        log.debug("REST request to get Sala : {}", id);
        Optional<Sala> sala = salaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sala);
    }

    /**
     * DELETE  /salas/:id : delete the "id" sala.
     *
     * @param id the id of the sala to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/salas/{id}")
    @Timed
    public ResponseEntity<Void> deleteSala(@PathVariable Long id) {
        log.debug("REST request to delete Sala : {}", id);

        salaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
