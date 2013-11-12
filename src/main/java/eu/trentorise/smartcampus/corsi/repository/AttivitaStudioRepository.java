package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.AttivitaDiStudio;

@Repository
public interface AttivitaStudioRepository extends JpaRepository<AttivitaDiStudio, Long> {
	List<AttivitaDiStudio> findAttByIdGds(Long id_gruppodistudio);
}