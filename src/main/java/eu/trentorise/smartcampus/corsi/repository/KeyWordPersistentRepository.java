package eu.trentorise.smartcampus.corsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.KeyWordPersistent;

@Repository
public interface KeyWordPersistentRepository extends JpaRepository<KeyWordPersistent, Long> {

}
