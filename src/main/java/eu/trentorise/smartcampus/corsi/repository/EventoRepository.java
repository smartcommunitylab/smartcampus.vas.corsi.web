package eu.trentorise.smartcampus.corsi.repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.EventoId;
import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;

@Repository
public interface EventoRepository extends JpaRepository<Evento, EventoId> {

	// List<Evento> findEventoByUser(User user);

	List<Evento> findEventoByCds(long corso);

	List<Evento> findEventoByAdAndYear(String nameAd, int year);

	List<Evento> findEventoByAd(String nameAd, long idStudente);

	Evento findEventoByIdClass(long id, Date date, Time from, Time to,
			long idStudente);
	
	List<Evento> findAttByIdGds(GruppoDiStudio id_gruppodistudio);
	
	List<Evento> selectEventsGdsOfStudent(GruppoDiStudio gds, long idStudente);
	
	List<Evento> findEventsBeforeDate(Date date);
}