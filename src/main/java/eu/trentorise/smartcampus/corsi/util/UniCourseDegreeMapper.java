package eu.trentorise.smartcampus.corsi.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.model.CdsData;
import eu.trentorise.smartcampus.unidataservice.model.FacoltaData;

@Service
@Configuration
@ComponentScan("eu.trentorise.smartcampus.corsi.utils")
public class UniCourseDegreeMapper {

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	private BasicProfile basicProfile;

	private String token;

	private List<CorsoLaurea> corsiLaurea;

	public UniCourseDegreeMapper() {
		// TODO Auto-generated constructor stub
	}

	public List<CorsoLaurea> convert(List<CdsData> dataCds, String token)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		corsiLaurea = new ArrayList<CorsoLaurea>();

		for (CdsData cds : dataCds) {
			// wrappo i dati dei dipartimenti
			CorsoLaurea corsoLaurea = new CorsoLaurea();

			corsoLaurea.setId(Long.valueOf(cds.getCdsId()));
			corsoLaurea.setNome(cds.getDescription());

			Dipartimento dip = dipartimentoRepository.findOne(Long.valueOf(cds
					.getFacId()));

			// devo aggiornare prima i dipartimenti
			if (dip == null)
				return null;

			corsoLaurea.setDipartimento(dip);

			corsiLaurea.add(corsoLaurea);
		}


		return corsiLaurea;

	}

	public CorsoLaurea convert(CdsData dataCds, String token)
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
		corsoLaurea.setNome(dataCds.getDescription());

		Dipartimento dip = dipartimentoRepository.findOne(Long.valueOf(dataCds
				.getFacId()));

		// devo aggiornare prima i dipartimenti
		if (dip == null)
			return null;

		corsoLaurea.setDipartimento(dip);

		return corsoLaurea;

	}
}
