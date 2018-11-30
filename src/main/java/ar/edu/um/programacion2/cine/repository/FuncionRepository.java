package ar.edu.um.programacion2.cine.repository;

import ar.edu.um.programacion2.cine.domain.Funcion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Funcion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FuncionRepository extends JpaRepository<Funcion, Long> {

}
