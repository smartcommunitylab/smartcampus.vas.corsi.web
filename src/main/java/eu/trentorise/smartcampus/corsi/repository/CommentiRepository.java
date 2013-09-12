package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Studente;

@Repository
public interface CommentiRepository extends JpaRepository<Commento, Long> {

	List<Commento> getCommentoByCorso(Corso corso);

	Commento getCommentoByStudente(Studente studente, Corso corso);
}
