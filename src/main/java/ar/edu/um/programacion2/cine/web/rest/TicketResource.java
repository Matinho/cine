package ar.edu.um.programacion2.cine.web.rest;

import com.codahale.metrics.annotation.Timed;

import ar.edu.um.programacion2.cine.domain.Butaca;
import ar.edu.um.programacion2.cine.domain.Cliente;
import ar.edu.um.programacion2.cine.domain.Ocupacion;
import ar.edu.um.programacion2.cine.domain.Ticket;
import ar.edu.um.programacion2.cine.repository.ClienteRepository;
import ar.edu.um.programacion2.cine.repository.OcupacionRepository;
import ar.edu.um.programacion2.cine.repository.TicketRepository;
import ar.edu.um.programacion2.cine.web.rest.errors.BadRequestAlertException;
import ar.edu.um.programacion2.cine.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Ticket.
 */
@RestController
@RequestMapping("/api")
public class TicketResource {

    private final Logger log = LoggerFactory.getLogger(TicketResource.class);

    private static final String ENTITY_NAME = "ticket";

    private final TicketRepository ticketRepository;
    
    @Autowired
    private OcupacionRepository ocupacionRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    public TicketResource(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * POST  /tickets : Create a new ticket.
     *
     * @param ticket the ticket to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ticket, or with status 400 (Bad Request) if the ticket has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tickets")
    @Timed
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) throws URISyntaxException {
        log.debug("REST request to save Ticket : {}", ticket);
        if (ticket.getId() != null) {
            throw new BadRequestAlertException("A new ticket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        ticket.setFechaTransaccion(ZonedDateTime.now());
        ticket.setCreated(ZonedDateTime.now());
        ticket.setUpdated(ZonedDateTime.now());
        Ticket result = ticketRepository.save(ticket);
        
        return ResponseEntity.created(new URI("/api/tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tickets : Updates an existing ticket.
     *
     * @param ticket the ticket to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ticket,
     * or with status 400 (Bad Request) if the ticket is not valid,
     * or with status 500 (Internal Server Error) if the ticket couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tickets")
    @Timed
    public ResponseEntity<Ticket> updateTicket(@Valid @RequestBody Ticket ticket) throws URISyntaxException {
        log.debug("REST request to update Ticket : {}", ticket);
        if (ticket.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        ticket.setFechaTransaccion(ZonedDateTime.now());
        ticket.setUpdated(ZonedDateTime.now());
        Ticket result = ticketRepository.save(ticket);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ticket.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tickets : get all the tickets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tickets in body
     */
    @GetMapping("/tickets")
    @Timed
    public List<Ticket> getAllTickets() {
        log.debug("REST request to get all Tickets");
        return ticketRepository.findAll();
    }

    /**
     * GET  /tickets/:id : get the "id" ticket.
     *
     * @param id the id of the ticket to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ticket, or with status 404 (Not Found)
     */
    @GetMapping("/tickets/{id}")
    @Timed
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        log.debug("REST request to get Ticket : {}", id);
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ticket);
    }

    /**
     * DELETE  /tickets/:id : delete the "id" ticket.
     *
     * @param id the id of the ticket to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tickets/{id}")
    @Timed
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        log.debug("REST request to delete Ticket : {}", id);

        ticketRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * POST   /tickets/:id_ocupaciones/:id_cliente/:tarjeta : creo el ticket con las ocupaciones asignadas 
     * */
    @PostMapping("/tickets/{id_ocupaciones}/{id_cliente}/{tarjeta}")//crear ticket consumiendo rest de validacion de la parte de pago
    @Timed
    public String hacerCompra(@PathVariable String id_ocupaciones, @PathVariable Long id_cliente, @PathVariable String tarjeta)throws IOException, java.io.IOException {
        log.debug("REST request to save Ticket : {}", id_ocupaciones);

        String[] ocupaciones = id_ocupaciones.split("-");
        
        List<Long> ocupacionList = new ArrayList<>();
        
        Ticket ticket = new Ticket();
        for(int i = 0; i < ocupaciones.length ; i++)
        {
            ((ArrayList<Long>) ocupacionList).add(Long.parseLong(ocupaciones[i]));
        }
        
        List<Ocupacion> ocupacionesList = ocupacionRepository.findAllById(ocupacionList);
        
        ticket.setImporte(BigDecimal.ZERO);
        
        for (int i = 0; i < ocupaciones.length; i++) {
        	ticket.setImporte(ocupacionesList.get(i).getValor());
		} 
        
        URL url = new URL("http://localhost:8090/api/pagos/" + tarjeta + "/" + ticket.getImporte());//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTU0Mzk4ODI5OH0.XzUZIrrMSRTeZ1cvm3rEaOm-4MhAcXAaIMWrorggKGL8Y28Ja9PbLADhq6jmd5E701XLurDvrlM83XUgceWj7A");
        
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                + conn.getResponseCode() 
                + "Ticket : " + ticket.getImporte() 
                + "Tarjeta: " + tarjeta);
        }
        
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        
        String output = br.readLine();
        
        Optional<Cliente> cliente_ticket = clienteRepository.findById(id_cliente);
        
        ticket.setCliente(cliente_ticket.get());
        ticket.setPagoUuid(output);
        ticket.setFechaTransaccion(ZonedDateTime.now());
        ticket.setCreated(ZonedDateTime.now());
        ticket.setUpdated(ZonedDateTime.now());
        ticket.setButacas(ocupaciones.length);
        
        String salida="";
        
        if(output!= null) {
            Ticket result_ticket = ticketRepository.save(ticket);

            for(int i = 0; i < ocupaciones.length; i++)
            {
                ocupacionesList.get(i).setTicket(result_ticket);
                ocupacionRepository.save(ocupacionesList.get(i));
            }
            salida = "Operacion Completa";
        }
        else {
            ocupacionRepository.deleteAll(ocupacionesList);
            salida ="Saldo Insuficiente";
        }
       
        return salida ;

    }
    
}
