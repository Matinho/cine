package ar.edu.um.programacion2.cine.repository;

import ar.edu.um.programacion2.cine.domain.Entrada;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Entrada entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {

}
