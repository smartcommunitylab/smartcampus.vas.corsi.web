package eu.trentorise.smartcampus.corsi.util;

import java.util.ArrayList;
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
import eu.trentorise.smartcampus.corsi.model.CorsoLite;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.AccountProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.profileservice.model.ExtendedProfile;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoData;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExam;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExams;

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
	private StudentInfoData fromStudentInfo;
	private StudentInfoExams fromStudentExams;

	private List<StudentInfoExam> fromListExams;

	public UniStudentMapper() {
		// TODO Auto-generated constructor stub
	}

	public Studente convert(StudentInfoData fromStudentInfo,
			StudentInfoExams fromStudentExams, String tokenUser)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		BasicProfileService service = new BasicProfileService(profileaddress);

		// recupero i dati del profilo dell'utente
		basicProfile = service.getBasicProfile(tokenUser);
		accountProfile = service.getAccountProfile(tokenUser);

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

		// wrappo i dati dello studente
		studente = new Studente();

		// informazioni generali
		studente.setId(userId);
		studente.setNome(name);
		studente.setCognome(surname);
		studente.setUserSocialId(socialId);
		studente.setEmail(email);

		studente.setAnno_corso(fromStudentInfo.getAcademicYear());
		studente.setCorso_laurea(fromStudentInfo.getCds());

		// informazioni esami
		ArrayList<Corso> corsi = new ArrayList<Corso>();
		ArrayList<CorsoLite> corsiSuperati = new ArrayList<CorsoLite>();
		fromListExams = fromStudentExams.getExams();
		for (StudentInfoExam exam : fromListExams) {
			Corso corso = new Corso();
			CorsoLite corsoSuperato = new CorsoLite();
			corso.setId(Long.valueOf(exam.getId()));
			corso.setNome(exam.getName());
			corsi.add(corso);
			if (exam.getResult() != null) {
				corsoSuperato.setId(Long.valueOf(exam.getId()));
				corsoSuperato.setNome(exam.getName());
				corsiSuperati.add(corsoSuperato);
			}
		}

		studente.setCorsi(corsi);
		studente.setCorsiSuperati(corsiSuperati);

		return studente;

	}

	/**
	 * 
	 * @param fromStudentExams
	 * @param token
	 * @return Converto gli esami presenti in esse3
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ProfileServiceException
	 */
	public List<Corso> convertCoursesEsse3Student(
			StudentInfoExams fromStudentExams, String token)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		BasicProfileService service = new BasicProfileService(profileaddress);

		// recupero i dati del profilo dell'utente
		basicProfile = service.getBasicProfile(token);
		// informazioni esami
		ArrayList<Corso> corsi = new ArrayList<Corso>();
		ArrayList<CorsoLite> corsiSuperati = new ArrayList<CorsoLite>();

		fromListExams = fromStudentExams.getExams();

		for (StudentInfoExam exam : fromListExams) {
			Corso corso = new Corso();
			corso.setId(Long.valueOf(exam.getId()));
			corso.setNome(exam.getName());
			corsi.add(corso);
		}

		return corsi;

	}

	/**
	 * 
	 * @param fromStudentExams
	 * @param token
	 * @return Converto gli esami superati presenti in esse3
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ProfileServiceException
	 */
	public List<CorsoLite> convertCoursesPassedStudent(
			StudentInfoExams fromStudentExams, String token)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		BasicProfileService service = new BasicProfileService(profileaddress);

		// recupero i dati del profilo dell'utente
		basicProfile = service.getBasicProfile(token);

		// informazioni esami
		ArrayList<CorsoLite> corsiSuperati = new ArrayList<CorsoLite>();
		fromListExams = fromStudentExams.getExams();
		
		for (StudentInfoExam exam : fromListExams) {
			CorsoLite corsoSuperato = new CorsoLite();
			if (exam.getResult() != null) {
				corsoSuperato.setId(Long.valueOf(exam.getId()));
				corsoSuperato.setNome(exam.getName());
				corsiSuperati.add(corsoSuperato);
			}
		}

		return corsiSuperati;

	}
	
	
	
	/**
	 * 
	 * @param fromStudentExams
	 * @param token
	 * @return Converto gli esami superati presenti in esse3
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ProfileServiceException
	 */
	public List<CorsoLite> convertCoursesAllStudent(
			StudentInfoExams fromStudentExams, String token)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		BasicProfileService service = new BasicProfileService(profileaddress);

		// recupero i dati del profilo dell'utente
		basicProfile = service.getBasicProfile(token);

		// informazioni esami
		ArrayList<CorsoLite> corsiSuperati = new ArrayList<CorsoLite>();
		fromListExams = fromStudentExams.getExams();
		
		for (StudentInfoExam exam : fromListExams) {
			CorsoLite corsoSuperato = new CorsoLite();
			if (exam.getResult() != null) {
				corsoSuperato.setId(Long.valueOf(exam.getId()));
				corsoSuperato.setNome(exam.getName());
				corsiSuperati.add(corsoSuperato);
			}
		}

		return corsiSuperati;

	}

}
