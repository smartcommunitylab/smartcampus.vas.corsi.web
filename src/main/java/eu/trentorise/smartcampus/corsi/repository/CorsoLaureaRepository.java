package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;

@Repository
public interface CorsoLaureaRepository extends JpaRepository<CorsoLaurea, Long>{
	
	List<CorsoLaurea> getCorsiLaureaByDipartimento(Dipartimento dipartimento);
}
