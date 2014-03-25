package eu.trentorise.smartcampus.corsi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.corsi.model.AttivitaDidattica;

@Repository
public interface AttivitaDidatticaRepository extends
		JpaRepository<AttivitaDidattica, Long> {

	List<AttivitaDidattica> findAttivitaDidatticaByCdsId(long cds_id);

	List<AttivitaDidattica> findAttivitaDidatticaByAdCod(String ad_cod);

}
