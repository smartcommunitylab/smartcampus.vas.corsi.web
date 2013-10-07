package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Studente;

@Repository
public interface CommentiRepository extends JpaRepository<Commento, Long> {

	List<Commento> getCommentoByCorsoAll(Corso corso);

	Commento getCommentoByStudenteAll(Studente studente, Corso corso);
	
	List<Commento> getCommentoByCorsoApproved(Corso corso);

	Commento getCommentoByStudenteApproved(Studente studente, Corso corso);
}
