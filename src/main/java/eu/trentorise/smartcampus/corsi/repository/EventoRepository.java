package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.Frequenze;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

	// List<Evento> findEventoByUser(User user);

	List<Evento> findEventoByCorso(Corso corso);

}