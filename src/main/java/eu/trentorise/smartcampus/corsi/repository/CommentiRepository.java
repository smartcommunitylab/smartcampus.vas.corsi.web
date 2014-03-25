package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Commento;

@Repository
public interface CommentiRepository extends JpaRepository<Commento, Long> {

	List<Commento> getCommentoByCorsoAll(Long corso);

	Commento getCommentoByStudenteAll(Long studente, Long corso);

	List<Commento> getCommentoByCorsoApproved(Long corso);

	Commento getCommentoByStudenteApproved(Long studente, Long corso);
}
