package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.corsi.servicesync.StudenteServiceSync;
import eu.trentorise.smartcampus.corsi.util.UniStudentMapper;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.StudentInfoService;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoData;

@Controller("studenteController")
public class StudenteController {

	private static final Logger logger = Logger
			.getLogger(StudenteController.class);

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${url.studente.service}")
	private String URLStudenteService;

	@SuppressWarnings("unused")
	private StudentInfoService studentInfoService;

	@PostConstruct
	private void initStudentInfoService() {
		studentInfoService = new StudentInfoService(URLStudenteService);
	}

	@Autowired
	@Value("${url.studente.service}")
	private String unidataaddress;

	@Autowired
	private StudenteServiceSync controllerSyncStudente;

	/*
	 * Ritorna tutti i corsi in versione lite
	 */

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return Studente
	 * @throws IOException
	 * 
	 *             Restituisce i dati dello studente riferiti allo studente che
	 *             effetua la richiesta
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/studente/me")
	public @ResponseBody
	Studente getInfoStudentFromId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {

			logger.info("/studente/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studente = studenteRepository.findStudenteByUserId(userId);
			//Studente studenteAggiornato = null;

			// se lo studente � null vado a prendere i dati da unidata
			if (studente == null) {
				// prendo i dati da unidata e li mappo
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				// ottengo da unidata lo studente
				StudentInfoData studentUniData = studentConnector
						.getStudentData(token);

				if (studentUniData == null)
					return null;

				UniStudentMapper studentMapper = new UniStudentMapper(
						profileaddress);

				// converto e salvo nel db lo studente aggiornato
				Studente convertedStudent = studentMapper.convert(
						studentUniData, token);

				convertedStudent = studenteRepository.save(convertedStudent);

				return convertedStudent;
			}

			// stud.setCorsiSuperati(assignCorsi(stud));
			return studente;

		} catch (Exception e) {
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
	 *             Restituisce i dati dello studente riferiti allo studente che
	 *             effetua la richiesta
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/studente/{id_studente}")
	public @ResponseBody
	Studente getInfoStudent(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("id_studente") Long id_studente)

	throws IOException {
		try {

			logger.info("/studente/{id_studente}");

			Studente studente = studenteRepository.findStudenteByUserId(id_studente);
			//Studente studenteAggiornato = null;

			// stud.setCorsiSuperati(assignCorsi(stud));
			return studente;

		} catch (Exception e) {
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
	 *             Restituisce lo studente soltanto se c'� bisogno di
	 *             sincronizzare
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sync/studente/me")
	public @ResponseBody
	Studente getStudenteSync(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/sync/studente/me");

			String token = getToken(request);

//			BasicProfileService service = new BasicProfileService(
//					profileaddress);

			//BasicProfile profile = service.getBasicProfile(token);
			//Long userId = Long.valueOf(profile.getUserId());

			//Studente studenteDB = studenteRepository.findOne(userId);

			// prendo i dati da unidata e li mappo
			StudentInfoService studentConnector = new StudentInfoService(
					unidataaddress);

			// ottengo da unidata lo studente
			StudentInfoData studentUniData = studentConnector
					.getStudentData(token);

			if (studentUniData == null)
				return null;

			UniStudentMapper studentMapper = new UniStudentMapper(
					profileaddress);

			// converto e salvo nel db lo studente aggiornato
			Studente convertedStudent = studentMapper.convert(studentUniData,
					token);

			convertedStudent = studenteRepository.save(convertedStudent);

			return convertedStudent;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	// /**
	// *
	// * @param request
	// * @param response
	// * @param session
	// * @return Studente
	// * @throws IOException
	// *
	// * Restituisce lo studente soltanto se c'� bisogno di
	// * sincronizzare
	// *
	// */
	// @RequestMapping(method = RequestMethod.GET, value = "/sync/studente/me")
	// public @ResponseBody
	// Studente getStudenteSync(HttpServletRequest request,
	// HttpServletResponse response, HttpSession session)
	//
	// throws IOException {
	// try {
	// logger.info("/sync/studente/me");
	//
	// String token = getToken(request);
	// BasicProfileService service = new BasicProfileService(
	// profileaddress);
	//
	// BasicProfile profile = service.getBasicProfile(token);
	// Long userId = Long.valueOf(profile.getUserId());
	//
	// Studente studenteDB = studenteRepository.findOne(userId);
	//
	// // prendo i dati da unidata e li mappo
	// StudentInfoService studentConnector = new StudentInfoService(
	// unidataaddress);
	//
	// /*
	// * Da rivedere la gestione della sincronizzazione degli esami:
	// * adesso sincronizza sempre
	// */
	// token = getToken(request);
	// //token = "9a554546-dc69-4282-a169-358a3ece5487";
	// StudentInfoExams studentExamsUniData = studentConnector
	// .getStudentExams(token);
	//
	// // ottengo da unidata lo studente
	// StudentInfoData studentUniData = studentConnector
	// .getStudentData(token);
	//
	// if (studentUniData == null)
	// return null;
	//
	// UniStudentMapper studentMapper = new UniStudentMapper(profileaddress);
	//
	// // converto e salvo nel db lo studente aggiornato
	// Studente convertedStudent = studentMapper.convert(studentUniData,
	// studentExamsUniData, token);
	//
	// convertedStudent = studenteRepository.save(convertedStudent);
	//
	// return convertedStudent;
	//
	// } catch (Exception e) {
	// logger.error(e.getMessage());
	// e.printStackTrace();
	// response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	// }
	// return null;
	// }
	//

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
