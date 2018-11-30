package ar.edu.um.programacion2.cine.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.edu.um.programacion2.cine.domain.Calificacion;
import ar.edu.um.programacion2.cine.repository.CalificacionRepository;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Calificacion.
 */
@RestController
@RequestMapping("/api")
public class CalificacionResource {

    private final Logger log = LoggerFactory.getLogger(CalificacionResource.class);

    private static final String ENTITY_NAME = "calificacion";

    private final CalificacionRepository calificacionRepository;

    public CalificacionResource(CalificacionRepository calificacionRepository) {
        this.calificacionRepository = calificacionRepository;
    }

    /**
     * POST  /calificacions : Create a new calificacion.
     *
     * @param calificacion the calificacion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new calificacion, or with status 400 (Bad Request) if the calificacion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/calificacions")
    @Timed
    public ResponseEntity<Calificacion> createCalificacion(@Valid @RequestBody Calificacion calificacion) throws URISyntaxException {
        log.debug("REST request to save Calificacion : {}", calificacion);
        if (calificacion.getId() != null) {
            throw new BadRequestAlertException("A new calificacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Calificacion result = calificacionRepository.save(calificacion);
        return ResponseEntity.created(new URI("/api/calificacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /calificacions : Updates an existing calificacion.
     *
     * @param calificacion the calificacion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated calificacion,
     * or with status 400 (Bad Request) if the calificacion is not valid,
     * or with status 500 (Internal Server Error) if the calificacion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/calificacions")
    @Timed
    public ResponseEntity<Calificacion> updateCalificacion(@Valid @RequestBody Calificacion calificacion) throws URISyntaxException {
        log.debug("REST request to update Calificacion : {}", calificacion);
        if (calificacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Calificacion result = calificacionRepository.save(calificacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, calificacion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /calificacions : get all the calificacions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of calificacions in body
     */
    @GetMapping("/calificacions")
    @Timed
    public List<Calificacion> getAllCalificacions() {
        log.debug("REST request to get all Calificacions");
        return calificacionRepository.findAll();
    }

    /**
     * GET  /calificacions/:id : get the "id" calificacion.
     *
     * @param id the id of the calificacion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the calificacion, or with status 404 (Not Found)
     */
    @GetMapping("/calificacions/{id}")
    @Timed
    public ResponseEntity<Calificacion> getCalificacion(@PathVariable Long id) {
        log.debug("REST request to get Calificacion : {}", id);
        Optional<Calificacion> calificacion = calificacionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(calificacion);
    }

    /**
     * DELETE  /calificacions/:id : delete the "id" calificacion.
     *
     * @param id the id of the calificacion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/calificacions/{id}")
    @Timed
    public ResponseEntity<Void> deleteCalificacion(@PathVariable Long id) {
        log.debug("REST request to delete Calificacion : {}", id);

        calificacionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
