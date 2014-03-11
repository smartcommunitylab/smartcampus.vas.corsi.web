package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

	// List<Evento> findEventoByUser(User user);

	List<Evento> findEventoByCds(long corso);
	List<Evento> findEventoByAdAndYear(String nameAd, int year);
	List<Evento> findEventoByAd(String nameAd);
}