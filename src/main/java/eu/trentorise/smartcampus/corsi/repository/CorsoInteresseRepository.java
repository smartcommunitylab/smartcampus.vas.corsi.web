package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.CorsoInteresse;

@Repository
public interface CorsoInteresseRepository extends
		JpaRepository<CorsoInteresse, Long> {

	List<CorsoInteresse> findCorsoInteresseByStudenteId(long idStudente);

	CorsoInteresse findCorsoInteresseByAttivitaIdAndStudenteId(long idStudente,
			long ad_id);

	void deleteCorsiInteresseOfStudenteOfCareer(long idStudente);

}
