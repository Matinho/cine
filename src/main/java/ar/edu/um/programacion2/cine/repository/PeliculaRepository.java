package ar.edu.um.programacion2.cine.repository;

import ar.edu.um.programacion2.cine.domain.Pelicula;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Pelicula entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

}
