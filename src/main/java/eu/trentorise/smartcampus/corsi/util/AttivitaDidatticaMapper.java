package eu.trentorise.smartcampus.corsi.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.trentorise.smartcampus.corsi.model.AttivitaDidattica;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.unidataservice.model.AdData;

public class AttivitaDidatticaMapper {
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	private List<AttivitaDidattica> adList;

	public AttivitaDidatticaMapper() {
		// TODO Auto-generated constructor stub
	}

	public List<AttivitaDidattica> convert(List<AdData> attDidattiche,
			long idCds, String aaOrd, String offYear, String token)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		adList = new ArrayList<AttivitaDidattica>();

		for (AdData ad : attDidattiche) {
			// wrappo i dati dei dipartimenti
			AttivitaDidattica attivitaDidattica = new AttivitaDidattica();

			attivitaDidattica.setAdId(Long.valueOf(ad.getAdId()));
			attivitaDidattica.setAdCod(ad.getAdcod());
			attivitaDidattica.setDescription(ad.getDescription());
			attivitaDidattica.setCds_id(idCds);
			attivitaDidattica.setOffYear(offYear);
			attivitaDidattica.setOrdYear(aaOrd);
			adList.add(attivitaDidattica);
		}

		return adList;

	}

}
