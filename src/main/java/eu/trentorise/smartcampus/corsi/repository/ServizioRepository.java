package eu.trentorise.smartcampus.corsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.Servizio;

@Repository
public interface ServizioRepository extends JpaRepository<Servizio, Long> {

}