package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.EventoId;

@Repository
public interface EventoRepository extends JpaRepository<Evento, EventoId> {

	// List<Evento> findEventoByUser(User user);

	List<Evento> findEventoByCds(long corso);
	List<Evento> findEventoByAdAndYear(String nameAd, int year);
	List<Evento> findEventoByAd(String nameAd, long idStudente);
}