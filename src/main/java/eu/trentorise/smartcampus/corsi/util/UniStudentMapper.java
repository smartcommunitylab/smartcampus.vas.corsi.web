package eu.trentorise.smartcampus.corsi.util;

import java.sql.Date;
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
import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
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

	public UniStudentMapper(String profileaddress) {
		this.profileaddress = profileaddress;
	}

	public Studente convert(StudentInfoData fromStudentInfo, String tokenUser)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		BasicProfileService service = new BasicProfileService(profileaddress);
		//tokenUser = "b1931a7e-66a4-4136-907a-907bfbca893d";
		
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
		studente.setAddress(fromStudentInfo.getAddress());
		studente.setCds(fromStudentInfo.getCds());
		studente.setCfu(fromStudentInfo.getCfu());
		studente.setCfuTotal(fromStudentInfo.getCfuTotal());
		studente.setDateOfBirth(fromStudentInfo.getDateOfBirth());
		studente.setEnrollmentYear(fromStudentInfo.getEnrollmentYear());
		studente.setGender(fromStudentInfo.getGender());
		studente.setMobile(fromStudentInfo.getMobile());
		studente.setNation(fromStudentInfo.getNation());
		studente.setPhone(fromStudentInfo.getPhone());
		studente.setSuplementaryYear(fromStudentInfo.getSupplementaryYears());
		studente.setMarksAverage(fromStudentInfo.getMarksAverage());
		studente.setMarksNumber(fromStudentInfo.getMarksNumber());


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
	public List<CorsoCarriera> convertCoursesEsse3Student(
			StudentInfoExams fromStudentExams, String token)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		BasicProfileService service = new BasicProfileService(profileaddress);

		// recupero i dati del profilo dell'utente
		basicProfile = service.getBasicProfile(token);
		// informazioni esami
		ArrayList<CorsoCarriera> corsi = new ArrayList<CorsoCarriera>();
		ArrayList<CorsoCarriera> corsiSuperati = new ArrayList<CorsoCarriera>();

		fromListExams = fromStudentExams.getExams();

		for (StudentInfoExam exam : fromListExams) {
			CorsoCarriera corso = new CorsoCarriera();
			corso.setId(Long.valueOf(exam.getId()));
			corso.setName(exam.getName());
			corso.setCod(exam.getCod());
			corso.setDate(new Date(exam.getDate()));
			corso.setResult(exam.getResult());
			corso.setLode(exam.isLode());
			corso.setWeight(exam.getWeight());
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
