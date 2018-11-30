package ar.edu.um.programacion2.cine.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.edu.um.programacion2.cine.domain.Pelicula;
import ar.edu.um.programacion2.cine.repository.PeliculaRepository;
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
 * REST controller for managing Pelicula.
 */
@RestController
@RequestMapping("/api")
public class PeliculaResource {

    private final Logger log = LoggerFactory.getLogger(PeliculaResource.class);

    private static final String ENTITY_NAME = "pelicula";

    private final PeliculaRepository peliculaRepository;

    public PeliculaResource(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    /**
     * POST  /peliculas : Create a new pelicula.
     *
     * @param pelicula the pelicula to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pelicula, or with status 400 (Bad Request) if the pelicula has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/peliculas")
    @Timed
    public ResponseEntity<Pelicula> createPelicula(@Valid @RequestBody Pelicula pelicula) throws URISyntaxException {
        log.debug("REST request to save Pelicula : {}", pelicula);
        if (pelicula.getId() != null) {
            throw new BadRequestAlertException("A new pelicula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pelicula result = peliculaRepository.save(pelicula);
        return ResponseEntity.created(new URI("/api/peliculas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /peliculas : Updates an existing pelicula.
     *
     * @param pelicula the pelicula to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pelicula,
     * or with status 400 (Bad Request) if the pelicula is not valid,
     * or with status 500 (Internal Server Error) if the pelicula couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/peliculas")
    @Timed
    public ResponseEntity<Pelicula> updatePelicula(@Valid @RequestBody Pelicula pelicula) throws URISyntaxException {
        log.debug("REST request to update Pelicula : {}", pelicula);
        if (pelicula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Pelicula result = peliculaRepository.save(pelicula);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pelicula.getId().toString()))
            .body(result);
    }

    /**
     * GET  /peliculas : get all the peliculas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of peliculas in body
     */
    @GetMapping("/peliculas")
    @Timed
    public List<Pelicula> getAllPeliculas() {
        log.debug("REST request to get all Peliculas");
        return peliculaRepository.findAll();
    }

    /**
     * GET  /peliculas/:id : get the "id" pelicula.
     *
     * @param id the id of the pelicula to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pelicula, or with status 404 (Not Found)
     */
    @GetMapping("/peliculas/{id}")
    @Timed
    public ResponseEntity<Pelicula> getPelicula(@PathVariable Long id) {
        log.debug("REST request to get Pelicula : {}", id);
        Optional<Pelicula> pelicula = peliculaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pelicula);
    }

    /**
     * DELETE  /peliculas/:id : delete the "id" pelicula.
     *
     * @param id the id of the pelicula to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/peliculas/{id}")
    @Timed
    public ResponseEntity<Void> deletePelicula(@PathVariable Long id) {
        log.debug("REST request to delete Pelicula : {}", id);

        peliculaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
