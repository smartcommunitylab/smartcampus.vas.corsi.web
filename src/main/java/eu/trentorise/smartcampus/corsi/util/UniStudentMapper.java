package eu.trentorise.smartcampus.corsi.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.AccountProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.profileservice.model.ExtendedProfile;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoData;

public class UniStudentMapper {

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;
	
	@Autowired
	private StudenteRepository studenteRepository;
	
	@Autowired
	private CorsoRepository corsoRepository;

	private BasicProfile basicProfile;
	private AccountProfile accountProfile;

	private String token;

	private Studente studente;
	private StudentInfoData fromStudent;

	public UniStudentMapper() {
		// TODO Auto-generated constructor stub
	}

	protected Studente _convert(StudentInfoData fromStudent, String token)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		BasicProfileService service = new BasicProfileService(profileaddress);

		// recupero i dati del profilo dell'utente
		basicProfile = service.getBasicProfile(token);
		service.getAccountProfile(token);
		accountProfile = service.getAccountProfile(token);

		// recupero i dati principali dal BasicProfile e AccountProfile
		Long userId = Long.valueOf(basicProfile.getUserId());
		String name = basicProfile.getName();
		String surname = basicProfile.getSurname();
		Long socialId = Long.valueOf(basicProfile.getSocialId());
		Set<String> accountNames = accountProfile.getAccountNames();
		Iterator<String> iter = accountNames.iterator();
		Map<String, String> attributesAccount = null;
		if (iter.hasNext()) {
			attributesAccount = accountProfile
					.getAccountAttributes(iter.next());
		}
		String email = attributesAccount.get("openid.ext1.value.email");

		studente = new Studente();

		studente.setId(userId);
		studente.setNome(name);
		studente.setCognome(surname);
		studente.setUserSocialId(socialId);
		studente.setEmail(email);

		studente.setAnno_corso(fromStudent.getAcademicYear());
		studente.setCorso_laurea(fromStudent.getCds());

		// studente.setDipartimento()

		

		// studente = studenteRepository.save(studente);

		// TODO caricare corsi da esse3
		// Creare associazione su frequenze

		// TEST
		List<Corso> corsiEsse3 = corsoRepository.findAll();

		String supera = null;
		String interesse = null;
		int z = 0;
		supera = new String();
		interesse = new String();

		for (Corso cors : corsiEsse3) {

			if (z % 2 == 0) {
				supera = supera
						.concat(String.valueOf(cors.getId()).concat(","));
			}

			if (z % 4 == 0) {
				interesse = interesse.concat(String.valueOf(cors.getId())
						.concat(","));
			}

			z++;
		}

		// Set corso follwed by studente
		studente.setCorsi(corsiEsse3);
		studente = studenteRepository.save(studente);

		// Set corsi superati
		studente.setIdsCorsiSuperati(supera);
		studente.setIdsCorsiInteresse(interesse);

		return studente = studenteRepository.save(studente);


	}

}
