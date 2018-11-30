package ar.edu.um.programacion2.cine.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.edu.um.programacion2.cine.domain.Entrada;
import ar.edu.um.programacion2.cine.repository.EntradaRepository;
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
 * REST controller for managing Entrada.
 */
@RestController
@RequestMapping("/api")
public class EntradaResource {

    private final Logger log = LoggerFactory.getLogger(EntradaResource.class);

    private static final String ENTITY_NAME = "entrada";

    private final EntradaRepository entradaRepository;

    public EntradaResource(EntradaRepository entradaRepository) {
        this.entradaRepository = entradaRepository;
    }

    /**
     * POST  /entradas : Create a new entrada.
     *
     * @param entrada the entrada to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entrada, or with status 400 (Bad Request) if the entrada has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entradas")
    @Timed
    public ResponseEntity<Entrada> createEntrada(@Valid @RequestBody Entrada entrada) throws URISyntaxException {
        log.debug("REST request to save Entrada : {}", entrada);
        if (entrada.getId() != null) {
            throw new BadRequestAlertException("A new entrada cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Entrada result = entradaRepository.save(entrada);
        return ResponseEntity.created(new URI("/api/entradas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entradas : Updates an existing entrada.
     *
     * @param entrada the entrada to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entrada,
     * or with status 400 (Bad Request) if the entrada is not valid,
     * or with status 500 (Internal Server Error) if the entrada couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entradas")
    @Timed
    public ResponseEntity<Entrada> updateEntrada(@Valid @RequestBody Entrada entrada) throws URISyntaxException {
        log.debug("REST request to update Entrada : {}", entrada);
        if (entrada.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Entrada result = entradaRepository.save(entrada);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entrada.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entradas : get all the entradas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of entradas in body
     */
    @GetMapping("/entradas")
    @Timed
    public List<Entrada> getAllEntradas() {
        log.debug("REST request to get all Entradas");
        return entradaRepository.findAll();
    }

    /**
     * GET  /entradas/:id : get the "id" entrada.
     *
     * @param id the id of the entrada to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entrada, or with status 404 (Not Found)
     */
    @GetMapping("/entradas/{id}")
    @Timed
    public ResponseEntity<Entrada> getEntrada(@PathVariable Long id) {
        log.debug("REST request to get Entrada : {}", id);
        Optional<Entrada> entrada = entradaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(entrada);
    }

    /**
     * DELETE  /entradas/:id : delete the "id" entrada.
     *
     * @param id the id of the entrada to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entradas/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntrada(@PathVariable Long id) {
        log.debug("REST request to delete Entrada : {}", id);

        entradaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
