package ar.edu.um.programacion2.cine.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.edu.um.programacion2.cine.domain.Ocupacion;
import ar.edu.um.programacion2.cine.repository.OcupacionRepository;
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
 * REST controller for managing Ocupacion.
 */
@RestController
@RequestMapping("/api")
public class OcupacionResource {

    private final Logger log = LoggerFactory.getLogger(OcupacionResource.class);

    private static final String ENTITY_NAME = "ocupacion";

    private final OcupacionRepository ocupacionRepository;

    public OcupacionResource(OcupacionRepository ocupacionRepository) {
        this.ocupacionRepository = ocupacionRepository;
    }

    /**
     * POST  /ocupacions : Create a new ocupacion.
     *
     * @param ocupacion the ocupacion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ocupacion, or with status 400 (Bad Request) if the ocupacion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ocupacions")
    @Timed
    public ResponseEntity<Ocupacion> createOcupacion(@Valid @RequestBody Ocupacion ocupacion) throws URISyntaxException {
        log.debug("REST request to save Ocupacion : {}", ocupacion);
        if (ocupacion.getId() != null) {
            throw new BadRequestAlertException("A new ocupacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        // Verificar ocupacion de butacas
        List<Ocupacion> ocupaciones = ocupacionRepository.findAllByFuncionAndButacaNotNull(ocupacion.getFuncion());
        
        for (int i=0; i < ocupaciones.size(); i++) {
        	if (ocupaciones.get(i).getButaca().equals(ocupacion.getButaca())) {
        		throw new BadRequestAlertException("No se puede asignar esta butaca porque esta ocupada", ENTITY_NAME, "idexists");
        	}
        }
        
        ocupacion.setCreated(ZonedDateTime.now());
        ocupacion.setUpdated(ZonedDateTime.now());
        Ocupacion result = ocupacionRepository.save(ocupacion);
        
        return ResponseEntity.created(new URI("/api/ocupacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ocupacions : Updates an existing ocupacion.
     *
     * @param ocupacion the ocupacion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ocupacion,
     * or with status 400 (Bad Request) if the ocupacion is not valid,
     * or with status 500 (Internal Server Error) if the ocupacion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ocupacions")
    @Timed
    public ResponseEntity<Ocupacion> updateOcupacion(@Valid @RequestBody Ocupacion ocupacion) throws URISyntaxException {
        log.debug("REST request to update Ocupacion : {}", ocupacion);
        if (ocupacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        ocupacion.setUpdated(ZonedDateTime.now());
        Ocupacion result = ocupacionRepository.save(ocupacion);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ocupacion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ocupacions : get all the ocupacions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ocupacions in body
     */
    @GetMapping("/ocupacions")
    @Timed
    public List<Ocupacion> getAllOcupacions() {
        log.debug("REST request to get all Ocupacions");
        return ocupacionRepository.findAll();
    }

    /**
     * GET  /ocupacions/:id : get the "id" ocupacion.
     *
     * @param id the id of the ocupacion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ocupacion, or with status 404 (Not Found)
     */
    @GetMapping("/ocupacions/{id}")
    @Timed
    public ResponseEntity<Ocupacion> getOcupacion(@PathVariable Long id) {
        log.debug("REST request to get Ocupacion : {}", id);
        Optional<Ocupacion> ocupacion = ocupacionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ocupacion);
    }

    /**
     * DELETE  /ocupacions/:id : delete the "id" ocupacion.
     *
     * @param id the id of the ocupacion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ocupacions/{id}")
    @Timed
    public ResponseEntity<Void> deleteOcupacion(@PathVariable Long id) {
        log.debug("REST request to delete Ocupacion : {}", id);

        ocupacionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
