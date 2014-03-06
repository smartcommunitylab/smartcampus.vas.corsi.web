package eu.trentorise.smartcampus.corsi.servicesync;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.derby.iapi.sql.compile.AccessPath;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.controller.CorsiController;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.corsi.util.CorsoCarrieraMapper;
import eu.trentorise.smartcampus.corsi.util.EasyTokenManger;
import eu.trentorise.smartcampus.corsi.util.UniStudentMapper;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.StudentInfoService;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoData;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExams;
import eu.trentorise.smartcampuse.unidataservice.Constants;

@Service("studenteServiceSync")
@Configuration
@ComponentScan("eu.trentorise.smartcampus.corsi.servicesync")
public class StudenteServiceSync {

	private static final Logger logger = Logger
			.getLogger(StudenteServiceSync.class);
	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${communicator.address}")
	private String communicatoraddress;

	@Autowired
	@Value("${url.studente.service}")
	private String unidataaddress;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	private CorsoRepository corsoRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private EventoRepository eventoRepository;

	@Autowired
	private CommentiRepository commentiRepository;

	
	String client_auth_token = "6d6ed274-4db7-4d9c-8c78-0a519ff33625";
	
	private final String client_id = "b8fcb94d-b4cf-438f-802a-c0a560734c88";
	private final String client_secret_mobile = "186b10c3-1f14-4833-9728-14eaa6c27891";
	private final String client_secret = "536560ac-cb74-4e1b-86a1-ef2c06c3313a";
	private final String grant_type = "client_credentials";

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return Studente
	 * @throws IOException
	 * 
	 *             Restituisce lo studente soltanto se c'� bisogno di
	 *             sincronizzare
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sync/studente/{id_studente}")
	public @ResponseBody
	Studente getStudenteByIdSync(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_studente") Long id_studente)

	throws IOException {
		try {
			logger.info("/sync/studente/" + id_studente);
			
			EasyTokenManger tokenManager = new EasyTokenManger(client_id, client_secret, grant_type);
			client_auth_token = tokenManager.getClientSmartCampusToken();

			Studente studenteDB = studenteRepository.findOne(id_studente);

			// se lo studenteDB non � presente nel DB di studymate, sincronizzo
			// i dati. Altrimenti ritorno null
			if (studenteDB != null)
				return null;

			// prendo i dati da unidata e li mappo
			StudentInfoService studentConnector = new StudentInfoService(
					unidataaddress);

			/*
			 * Da rivedere la gestione della sincronizzazione degli esami:
			 * adesso sincronizza sempre
			 */
			StudentInfoExams studentExamsUniData = studentConnector
					.getStudentExams(client_auth_token, String.valueOf(id_studente));

			// ottengo da unidata lo studente
			StudentInfoData studentUniData = studentConnector
					.getStudentData(client_auth_token, String.valueOf(id_studente));

			if (studentUniData == null)
				return null;

			UniStudentMapper studentMapper = new UniStudentMapper(profileaddress);
			
			String token = getToken(request);

			// converto e salvo nel db lo studente aggiornato
			Studente convertedStudent = studentMapper.convert(studentUniData, token);

			convertedStudent = studenteRepository.save(convertedStudent);

			return convertedStudent;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return Studente
	 * @throws IOException
	 * 
	 *             Restituisce lo studente sincronizzando soltanto i corsi da
	 *             libretto
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sync/studente/corsilibretto/me")
	public @ResponseBody
	Studente getCorsoEsse3StudenteSync(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/sync/studente/corsilibretto/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);

			// se lo studenteDB non � presente nel DB di studymate, non posso
			// sincronizzare gli esami.
			if (studenteDB == null)
				return null;

			// prendo i dati da unidata e li mappo
			StudentInfoService studentConnector = new StudentInfoService(
					unidataaddress);

			StudentInfoExams studentExamsUniData = studentConnector
					.getStudentExams(token);

			// ottengo da unidata lo studente
			StudentInfoData studentUniData = studentConnector
					.getStudentData(token);

			if (studentUniData == null)
				return null;

			UniStudentMapper studentMapper = new UniStudentMapper(profileaddress);

			List<CorsoCarriera> convertedEsse3Courses = studentMapper
					.convertCoursesEsse3Student(studentExamsUniData, token);

			studenteRepository.delete(studenteDB);

			// sincronizzo i corsi da libretto dello studente
			studenteDB = studenteRepository.saveAndFlush(studenteDB);

			return studenteDB;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @return String
	 * 
	 *         Ottiene il token riferito alla request
	 * 
	 */
	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}
}
