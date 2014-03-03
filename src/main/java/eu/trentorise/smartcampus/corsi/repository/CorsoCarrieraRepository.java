package eu.trentorise.smartcampus.corsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;

@Repository
public interface CorsoCarrieraRepository extends
		JpaRepository<CorsoCarriera, Long> {

}