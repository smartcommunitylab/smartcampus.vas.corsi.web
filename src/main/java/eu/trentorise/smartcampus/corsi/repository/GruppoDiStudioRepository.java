package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;

@Repository
public interface GruppoDiStudioRepository extends
		JpaRepository<GruppoDiStudio, Long> {
	List<GruppoDiStudio> findGdsBycourseId(Long corso);
	
	GruppoDiStudio findGdsById(Long id);
}