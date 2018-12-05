package ar.edu.um.programacion2.cine.web.rest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import ar.edu.um.programacion2.cine.domain.Funcion;
import ar.edu.um.programacion2.cine.domain.Ocupacion;
import ar.edu.um.programacion2.cine.domain.Ticket;
import ar.edu.um.programacion2.cine.repository.FuncionRepository;
import ar.edu.um.programacion2.cine.repository.OcupacionRepository;
import ar.edu.um.programacion2.cine.repository.TicketRepository;
import ar.edu.um.programacion2.cine.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Cliente.
 */
@RestController
@RequestMapping("/api")
public class EstadisticaResource {

    private final Logger log = LoggerFactory.getLogger(EstadisticaResource.class);

    private static final String ENTITY_NAME = "estadisticas";
    
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OcupacionRepository ocupacionRepository;
    
    @Autowired
    private FuncionRepository funcionRepository;
    
    public EstadisticaResource() { }

    /**
     * GET  /clientes : get all the clientes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clientes in body
     */
    @GetMapping("/estadisticas/recaudacion")
    @Timed
    public String getAllPagos() {
        log.debug("REST request to get all Pagos al cine");
        
        BigDecimal recaudacion = BigDecimal.ZERO;
        
        List<Ticket> tickets = ticketRepository.findAll();
        
        for (int i = 0; i < tickets.size(); i++) {
        	recaudacion = tickets.get(i).getImporte().add(recaudacion);
		}
        
        return recaudacion.toString();
    }

}
