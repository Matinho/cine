package ar.edu.um.programacion2.cine.repository;

import ar.edu.um.programacion2.cine.domain.Butaca;
import ar.edu.um.programacion2.cine.domain.Sala;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Butaca entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ButacaRepository extends JpaRepository<Butaca, Long> {

	List<Butaca> findAllBySala(Sala sala);
	List<Butaca> findAllBySalaAndIdNotIn(Sala sala, List<Long> butacas);
	
}
