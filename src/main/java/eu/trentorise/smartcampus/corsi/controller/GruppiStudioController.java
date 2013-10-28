package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.communicator.CommunicatorConnector;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.GruppoDiStudioRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("gruppiStudioController")
public class GruppiStudioController {

	private static final Logger logger = Logger
			.getLogger(GruppiStudioController.class);
	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	@Value("${communicator.address}")
	private String communicatoraddress;
	
	@Autowired
	private GruppoDiStudioRepository gruppidistudioRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	/*
	 * Ritorna tutti i corsi in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/gruppidistudio/all")
	public @ResponseBody
	List<GruppoDiStudio> getgruppidistudioAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {

			List<GruppoDiStudio> getgruppidistudio = gruppidistudioRepository.findAll();

			return getgruppidistudio;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
//
//	/*
//	 * Ritorna i dati completi di un gruppo dato l'id
//	 */
//	@RequestMapping(method = RequestMethod.GET, value = "/gruppidistudio/{id_gruppidistudio}")
//	public @ResponseBody
//	GruppoDiStudio getgruppidistudioByID(HttpServletRequest request,
//			HttpServletResponse response, HttpSession session,
//			@PathVariable("id_gruppidistudio") Long id_gruppidistudio)
//
//	throws IOException {
//		try {
//			logger.info("/gruppidistudio/{id_gruppidistudio}");
//
//			if (id_gruppidistudio == null)
//				return null;
//
//			return  gruppidistudioRepository.findOne(id_gruppidistudio);
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//		}
//		return null;
//	}


	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}
	/*
	 * Ritorna i gruppi di un corso
	 */
//	@RequestMapping(method = RequestMethod.GET, value = "/gruppidistudio/{id_course}")
//	public @ResponseBody
//	List<GruppoDiStudio> getgruppidistudioByIDCourse(HttpServletRequest request,
//			HttpServletResponse response, HttpSession session,
//			@PathVariable("id_course") Long id_course)
//
//	throws IOException {
//		try {
//			logger.info("/gruppidistudio/{id_course}");
//
//			if (id_course == null)
//				return null;
//
//			return  gruppidistudioRepository.findGdsBycourseId(id_course);
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//		}
//		return null;
//	}

	
//	@RequestMapping(method = RequestMethod.GET, value = "/gruppidistudio/me")
//	public @ResponseBody
//	Collection<GruppoDiStudio> getgruppidistudioByMe(HttpServletRequest request,
//			HttpServletResponse response, HttpSession session)
//
//	throws IOException {
//		try {
//			logger.info("/gruppidistudio/me");
//
//			String token = getToken(request);
//			BasicProfileService service = new BasicProfileService(
//					profileaddress);
//			BasicProfile profile = service.getBasicProfile(token);
//			Long userId = Long.valueOf(profile.getUserId());
//
//			// test
//			Studente studente = studenteRepository.findStudenteByUserId(userId);
//			if (studente == null) {
//				studente = new Studente();
//				studente.setId(userId);
//				studente.setNome(profile.getName());
//				studente = studenteRepository.save(studente);
//
//				// TODO Filtrare
//
//				// TEST
//				List<GruppoDiStudio> mygruppidistudio = gruppidistudioRepository.findAll();
//
//				// TEST
//
//				// Set corso follwed by studente
//				studente.setGds(mygruppidistudio);
//				studente = studenteRepository.save(studente);
//			}
//
//			return studente.getGds();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//		}
//		return null;
//	}
	
//	@RequestMapping(method = RequestMethod.POST, value = "/addgruppodistudio")
//	public @ResponseBody
//	GruppoDiStudio AddGds(HttpServletRequest request, HttpServletResponse response,
//			HttpSession session, @RequestBody GruppoDiStudio gruppodistudio)
//
//	throws IOException {
//		try {
//
//			// User Request create event
//			// creati a notification and send to Communicator
//
//			// TODO controlli se campi validi
//			if (gruppodistudio != null && gruppodistudio.getNome() != "") {
//
//				String token = getToken(request);
//				BasicProfileService service = new BasicProfileService(
//						profileaddress);
//				BasicProfile profile = service.getBasicProfile(token);
//				Long userId = Long.valueOf(profile.getUserId());
//
//				CommunicatorConnector communicatorConnector = new CommunicatorConnector(
//						communicatoraddress, appName);
//
//				List<String> users = new ArrayList<String>();
//				users.add(userId.toString());
//
//				Notification n = new Notification();
//				n.setTitle(gruppodistudio.getNome());
//				n.setUser(userId.toString());
//				n.setTimestamp(System.currentTimeMillis());
//				n.setDescription("Creazione Evento");
//
//				communicatorConnector.sendAppNotification(n, appName, users,
//						token);
//
//				return gruppidistudioRepository.save(gruppodistudio);
//			} else
//				return null;
//
//		} catch (Exception e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//		}
//		return null;
//	}

	
}
	
