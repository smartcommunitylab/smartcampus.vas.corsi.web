package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Corso;

@Repository
public interface CorsoRepository extends JpaRepository<Corso, Long> {
	List<Corso> findCorsoByDipartimentoId(Long id_dipartimento);

}