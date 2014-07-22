package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;
import eu.trentorise.smartcampus.corsi.model.RegistrationId;

@Repository
public interface RegistrationIdRepository extends JpaRepository<RegistrationId, Long> {
	List<RegistrationId> findRegIdsByStudent(Long student_id);
}
