package eu.trentorise.smartcampus.corsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Studente;

@Repository
public interface StudenteRepository extends JpaRepository<Studente, Long> {

	Studente findStudenteByUserId(long id);

}