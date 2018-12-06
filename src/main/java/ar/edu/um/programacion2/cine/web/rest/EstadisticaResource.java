package ar.edu.um.programacion2.cine.web.rest;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import ar.edu.um.programacion2.cine.domain.Funcion;
import ar.edu.um.programacion2.cine.domain.Ocupacion;
import ar.edu.um.programacion2.cine.domain.Pelicula;
import ar.edu.um.programacion2.cine.domain.Ticket;
import ar.edu.um.programacion2.cine.repository.FuncionRepository;
import ar.edu.um.programacion2.cine.repository.OcupacionRepository;
import ar.edu.um.programacion2.cine.repository.PeliculaRepository;
import ar.edu.um.programacion2.cine.repository.TicketRepository;
import ar.edu.um.programacion2.cine.web.rest.errors.BadRequestAlertException;


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
    private PeliculaRepository peliculaRepository ;
    
    @Autowired
    private FuncionRepository funcionRepository;
    
    public EstadisticaResource() { }

    @GetMapping("/estadisticas/recaudacion")
    @Timed
    public String getAllPagos() throws URISyntaxException {
        log.debug("REST request to get all Pagos al cine");
        
        BigDecimal recaudacion = BigDecimal.ZERO;
        
        List<Ticket> tickets = ticketRepository.findAll();
        
        for (int i = 0; i < tickets.size(); i++) {
        	recaudacion = tickets.get(i).getImporte().add(recaudacion);
		}
        
        return recaudacion.toString();
    }

    @GetMapping("/estadisticas/funcion/{idFuncion}")
    @Timed
    public String getFuncionPagos(@PathVariable Long idFuncion) throws URISyntaxException {
        log.debug("REST request to get all Pagos de funcion: {}", idFuncion);

        if (funcionRepository.findById(idFuncion) == null){
            throw new BadRequestAlertException("ID de funcion incorrecto", ENTITY_NAME, "idexists");
        }

        BigDecimal recaudacion = BigDecimal.ZERO;

        Optional<Funcion> funcion = funcionRepository.findById(idFuncion);

        Set<Ocupacion> ocupacions = funcion.get().getOcupacions();
        List<Ocupacion> ocupacionList = new ArrayList<>(ocupacions);

        for (int i = 0; i < ocupacionList.size(); i++) {
            recaudacion = ocupacionList.get(i).getValor().add(recaudacion);
        }

        return recaudacion.toString();
    }
    
    @GetMapping("/estadisticas/pelicula/{idPelicula}")
    @Timed
    public String getPeliculaPagos(@PathVariable Long idPelicula) throws URISyntaxException {
        log.debug("REST request to get all Pagos de pelicula: {}", idPelicula);

        if (funcionRepository.findById(idPelicula) == null){
            throw new BadRequestAlertException("ID de pelicula incorrecto", ENTITY_NAME, "idexists");
        }

        BigDecimal recaudacion = BigDecimal.ZERO;

        Optional<Pelicula> pelicula = peliculaRepository.findById(idPelicula);

        Set<Funcion> funcions = pelicula.get().getFuncions();
        List<Funcion> funcionList = new ArrayList<>(funcions);

        for (int i = 0; i < funcionList.size(); i++) {

            Set<Ocupacion> ocupacions = funcionList.get(i).getOcupacions();
            List<Ocupacion> ocupacionList = new ArrayList<>(ocupacions);

            for (int j = 0; j < ocupacionList.size(); j++) {
                recaudacion = ocupacionList.get(i).getValor().add(recaudacion);
            }
        }

        return recaudacion.toString();
    }
    
}
