package eu.trentorise.smartcampus.corsi.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.model.PianoStudi;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.model.CdsData;
import eu.trentorise.smartcampus.unidataservice.model.FacoltaData;
import eu.trentorise.smartcampus.unidataservice.model.PdsData;

@Service
@Configuration
@ComponentScan("eu.trentorise.smartcampus.corsi.utils")
public class UniCourseDegreeMapper {

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;

	private BasicProfile basicProfile;

	private String token;

	private List<CorsoLaurea> corsiLaurea;

	public UniCourseDegreeMapper() {
		// TODO Auto-generated constructor stub
	}

	public List<CorsoLaurea> convert(List<CdsData> dataCds, String token,
			DipartimentoRepository dipartimentoRepository)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		corsiLaurea = new ArrayList<CorsoLaurea>();

		try {

			for (CdsData cds : dataCds) {
				// wrappo i dati dei dipartimenti
				CorsoLaurea corsoLaurea = new CorsoLaurea();

				corsoLaurea.setId(Long.valueOf(cds.getCdsId()));
				corsoLaurea.setCdsCod(cds.getCdsCod());
				corsoLaurea.setFacId(cds.getFacId());
				corsoLaurea.setDescripion(cds.getDescription());
				corsoLaurea.setDurata(cds.getDurata());
				corsoLaurea.setAaOrd(cds.getAaOrd());

				List<PianoStudi> pdsList = new ArrayList<PianoStudi>();
				for (PdsData pdsData : cds.getPds()) {
					PianoStudi pds = new PianoStudi();
					pds.setPdsId(corsoLaurea);
					pds.setPdsCod(pdsData.getPdsCod());
					pdsList.add(pds);

				}

				Dipartimento dip = dipartimentoRepository.findOne(Long
						.valueOf(cds.getFacId()));

				// devo aggiornare prima i dipartimenti
				if (dip == null)
					return null;

				corsoLaurea.setDipartimento(dip);

				corsiLaurea.add(corsoLaurea);

			}
		} catch (Exception e) {
			Logger.getLogger(UniCourseDegreeMapper.class).error(e.getMessage());
			return null;
		}
		return corsiLaurea;

	}

	public CorsoLaurea convert(CdsData dataCds, String token,
			DipartimentoRepository dipartimentoRepository)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		BasicProfileService service = new BasicProfileService(profileaddress);

		// recupero i dati del profilo dell'utente
		basicProfile = service.getBasicProfile(token);

		if (basicProfile == null)
			return null;

		// wrappo i dati dei dipartimenti
		CorsoLaurea corsoLaurea = new CorsoLaurea();

		corsoLaurea.setId(Long.valueOf(dataCds.getCdsId()));
		corsoLaurea.setDescripion(dataCds.getDescription());

		Dipartimento dip = dipartimentoRepository.findOne(Long.valueOf(dataCds
				.getFacId()));

		// devo aggiornare prima i dipartimenti
		if (dip == null)
			return null;

		corsoLaurea.setDipartimento(dip);

		return corsoLaurea;

	}
}
