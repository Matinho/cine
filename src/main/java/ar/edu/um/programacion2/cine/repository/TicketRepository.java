package ar.edu.um.programacion2.cine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.um.programacion2.cine.domain.Ticket;


/**
 * Spring Data  repository for the Ticket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	
}
