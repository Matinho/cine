package ar.edu.um.programacion2.cine.web.rest;

import com.codahale.metrics.annotation.Timed;

import ar.edu.um.programacion2.cine.domain.*;
import ar.edu.um.programacion2.cine.repository.*;
import ar.edu.um.programacion2.cine.web.rest.errors.BadRequestAlertException;
import ar.edu.um.programacion2.cine.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
import java.util.Set;

/**
 * REST controller for managing Ticket.
 */
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

    @Autowired
    private FuncionRepository funcionRepository;

    @Autowired
    private ButacaRepository butacaRepository;

    @Autowired
    private EntradaRepository entradaRepository;

    public TicketResource(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * POST  /tickets : Create a new ticket maquetado.
     *
  //   * @param ticket the ticket to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ticket, or with status 400 (Bad Request) if the ticket has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tickets")
    @Timed
    public ResponseEntity<Ticket> createTicket() throws URISyntaxException {

        Ticket ticket = new Ticket();
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
    @PutMapping("/tickets/{ticketId}/{butacaId}/{funcionId}/{entradaId}")
    @Timed
    public ResponseEntity<Ticket> updateTicketOcupaciones(@PathVariable Long ticketId, @PathVariable Long butacaId, @PathVariable Long funcionId, @PathVariable Long entradaId) throws URISyntaxException {

        if (ticketRepository.findById(ticketId).equals(null)){
            throw new BadRequestAlertException("No existe el ticket solicitado", ENTITY_NAME, "idnull");
        }
        if (funcionRepository.findById(funcionId).equals(null)) {
            throw new BadRequestAlertException("No existe la funcion solicitada", ENTITY_NAME, "idnull");
        }
        if (butacaRepository.findById(funcionId).equals(null)) {
            throw new BadRequestAlertException("No existe la butaca solicitada", ENTITY_NAME, "idnull");
        }
        if (entradaRepository.findById(entradaId).equals(null)) {
            throw new BadRequestAlertException("No existe la entrada solicitada", ENTITY_NAME, "idnull");
        }

        log.debug("REST request to update Ticket Ocupaciones : {}");

        Optional<Funcion> funcion = funcionRepository.findById(funcionId);
        Optional<Butaca> butaca = butacaRepository.findById(butacaId);

        List<Ocupacion> ocupaciones = ocupacionRepository.findAllByFuncionAndButacaNotNull(funcion.get());

        for (int i = 0; i < ocupaciones.size(); i++) {
            if (butaca.equals(ocupaciones.get(i).getButaca())){
                throw new BadRequestAlertException("Una butaca solicitada esta ocupada", ENTITY_NAME, "idnull");
            }
        }

        Optional<Ticket> ticket = ticketRepository.findById(ticketId);

        Ocupacion ocupacion = new Ocupacion();
        ocupacion.setCreated(ZonedDateTime.now());
        ocupacion.setEntrada(entradaRepository.findById(entradaId).get());
        ocupacion.setValor(entradaRepository.findById(entradaId).get().getValor());
        ocupacion.setButaca(butaca.get());
        ocupacion.setFuncion(funcion.get());
        ocupacion.setTicket(ticket.get());
        ocupacion.setUpdated(ZonedDateTime.now());
        ocupacionRepository.save(ocupacion);
        
        funcion.get().addOcupacion(ocupacion);
        funcionRepository.save(funcion.get());

        ticket.get().addOcupacion(ocupacion);

        if(ticket.get().getButacas() == null){
            ticket.get().setButacas(1);
        }else{
            ticket.get().setButacas((ticket.get().getButacas()) + 1);
        }

        ticket.get().setUpdated(ZonedDateTime.now());
        Ticket result = ticketRepository.save(ticket.get());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ticket.get().getId().toString()))
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

    @PutMapping("/tickets/{ticketId}/{clienteId}/{tarjetaNum}")
    @Timed
    public Ticket updateTicketPago(@PathVariable Long ticketId, @PathVariable Long clienteId, @PathVariable String tarjetaNum) throws URISyntaxException, IOException, java.io.IOException {
        if (ticketRepository.findById(ticketId).equals(null)){
            throw new BadRequestAlertException("No existe el ticket solicitado", ENTITY_NAME, "idnull");
        }
        if (clienteRepository.findById(clienteId).equals(null)) {
            throw new BadRequestAlertException("No existe el cliente solicitado", ENTITY_NAME, "idnull");
        }

        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        Set<Ocupacion> ocupaciones = ticket.get().getOcupacions();
        List<Ocupacion> ocupacionList = new ArrayList<>(ocupaciones);
        Optional<Cliente> cliente = clienteRepository.findById(clienteId);

        ticket.get().setImporte(BigDecimal.ZERO);
        for (int i = 0; i < ticket.get().getButacas(); i++) {
            ticket.get().setImporte(ocupacionList.get(i).getValor().add(ticket.get().getImporte()));
        }

        ticket.get().setCliente(cliente.get());

        URL url = new URL("http://localhost:8090/api/pagos/"+tarjetaNum+"/"+ ticket.get().getImporte().toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTU0NDEwODY4OH0.-pqOfr5g27yMmqsXAxPWy8R8SP5gp6tuzRNMLfbqsXc-SJKSqCWRYMhW2GB4F-dpu2K_gCSgP9jLDuQWX2Z5Rw");

        if (conn.getResponseCode() != 200) {
            ocupacionRepository.deleteAll(ocupaciones);

            BufferedReader err = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String errorkey = null;
            for (int i = 0; i < 3; i++) {
                if (i==2){
                    errorkey = err.readLine();
                }
                err.readLine();
            }
            if (errorkey.contains("saldo")){
                throw new RuntimeException("Error: Saldo insuficiente en tarjeta : "
                    + conn.getResponseCode());
            }else if (errorkey.contains("num_tarjeta")){
                throw new RuntimeException("Error: Numero de tarjeta inexistente : "
                    + conn.getResponseCode());
            }else {
                throw new RuntimeException("Failed : HTTP Error code en cinepago : "
                    + conn.getResponseCode());
            }
        }

        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);

        ticket.get().setPagoUuid(br.readLine());
        ticket.get().setFechaTransaccion(ZonedDateTime.now());
        ticket.get().setUpdated(ZonedDateTime.now());

        Ticket result = ticketRepository.save(ticket.get());

        return result;
    }

}