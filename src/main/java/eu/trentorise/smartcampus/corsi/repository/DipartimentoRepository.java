package eu.trentorise.smartcampus.corsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Dipartimento;

@Repository
public interface DipartimentoRepository extends
		JpaRepository<Dipartimento, Long> {

}