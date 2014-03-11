package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;

@Repository
public interface CorsoCarrieraRepository extends
		JpaRepository<CorsoCarriera, Long> {

	List<CorsoCarriera> findCorsoCarrieraByStudenteId(long idStudente);
	CorsoCarriera findCorsoCarrieraByAdCodAndStudenteId(String adId, long idStudente);
	
}