package eu.trentorise.smartcampus.corsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.RisorsaPhl;


@Repository
public interface RisorsaPhlRepository extends JpaRepository<RisorsaPhl, Long>{
	
	RisorsaPhl getRisorsaPhlByCorsoId(Corso corso);
	
}


