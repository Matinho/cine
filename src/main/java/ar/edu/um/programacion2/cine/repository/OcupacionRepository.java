package ar.edu.um.programacion2.cine.repository;

import ar.edu.um.programacion2.cine.domain.Funcion;
import ar.edu.um.programacion2.cine.domain.Ocupacion;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ocupacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OcupacionRepository extends JpaRepository<Ocupacion, Long> {

	List<Ocupacion> findAllByFuncionAndButacaNotNull(Funcion funcion);
	List<Ocupacion> findAllByFuncion(Funcion funcion);
	
}
