package ar.edu.um.programacion2.cine.web.rest;

import com.codahale.metrics.annotation.Timed;

import ar.edu.um.programacion2.cine.domain.Butaca;
import ar.edu.um.programacion2.cine.domain.Entrada;
import ar.edu.um.programacion2.cine.domain.Funcion;
import ar.edu.um.programacion2.cine.domain.Ocupacion;
import ar.edu.um.programacion2.cine.repository.ButacaRepository;
import ar.edu.um.programacion2.cine.repository.EntradaRepository;
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

/**
 * REST controller for managing Ocupacion.
 */
@RestController
@RequestMapping("/api")
public class OcupacionResource {

    private final Logger log = LoggerFactory.getLogger(OcupacionResource.class);

    private static final String ENTITY_NAME = "ocupacion";

    private final OcupacionRepository ocupacionRepository;
    
    @Autowired
    private FuncionRepository funcionRepository;
    
    @Autowired
    private ButacaRepository butacaRepository;
    
    @Autowired
    private EntradaRepository entradaRepository;

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
    
    /**
     * POST  /ocupacions : Create a new ocupacion.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new ocupacion, or with status 400 (Bad Request) if the ocupacion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ocupacions/{idFuncion}/{idButacas}/{idEntradas}")
    @Timed
    public List<Ocupacion> createOcupacion(@PathVariable Long idFuncion, @PathVariable String idButacas, @PathVariable String idEntradas) throws URISyntaxException {
        log.debug("REST request to save Ocupacion : {}");

        if (funcionRepository.findById(idFuncion) == null) {
            throw new BadRequestAlertException("No existe la funcion solicitada", ENTITY_NAME, "idnull");
        }

        Optional<Funcion> funcion= funcionRepository.findById(idFuncion);
        String[] butacas = idButacas.split("-");
        String[] entradas = idEntradas.split("-");
        List<Ocupacion> ocupacions= new ArrayList<>();

        Iterable<Long> butacas_id = new ArrayList<>();
        Iterable<Long> entradas_id = new ArrayList<>();

        for(int i = 0;i<butacas.length;i++) {
            ((ArrayList<Long>) butacas_id).add(Long.parseLong(butacas[i]));
            ((ArrayList<Long>) entradas_id).add(Long.parseLong(entradas[i]));
        }

        List<Butaca> butacasList = butacaRepository.findAllById(butacas_id);
        List<Entrada> entradasList = entradaRepository.findAllById(entradas_id);

        List<Ocupacion> ocupaciones = ocupacionRepository.findAllByFuncionAndButacaNotNull(funcion.get());

        List<Butaca> ocupadas = new ArrayList<>();

        for (int i = 0; i < ocupaciones.size(); i++) {
            ocupadas.add(ocupaciones.get(i).getButaca());
        }

        for (int i = 0; i < butacasList.size(); i++) {
            for (int j = 0; j < ocupadas.size(); j++) {
                if (butacasList.get(i).equals(ocupadas.get(j))){
                    throw new BadRequestAlertException("Una butaca solicitada esta ocupada", ENTITY_NAME, "idnull");
                }
            }
        }

        for(int indice = 0;indice<butacasList.size();indice++) {
            Ocupacion ocupacion=new Ocupacion();
            ocupacion.setButaca(butacasList.get(indice));
            ocupacion.setFuncion(funcion.get());
            ocupacion.setEntrada(entradasList.get(indice));
            ocupacion.setValor(ocupacion.getEntrada().getValor());
            ocupacion.setCreated(ZonedDateTime.now());
            ocupacion.setUpdated(ZonedDateTime.now());
            ocupacions.add(ocupacion);
        }

        List<Ocupacion> result_ocupacion = ocupacionRepository.saveAll(ocupacions);

        return result_ocupacion;
    }
    
}
