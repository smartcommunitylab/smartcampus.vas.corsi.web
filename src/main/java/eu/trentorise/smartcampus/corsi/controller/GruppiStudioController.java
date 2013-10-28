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
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
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
	
	@Autowired
	private CorsoRepository corsoRepository;

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
	@RequestMapping(method = RequestMethod.GET, value = "/gruppidistudio/{id_corso}")
	public @ResponseBody
	List<GruppoDiStudio> getgruppidistudioByIDCourse(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {
			logger.info("/gruppidistudio/{id_corso}");

			if (id_corso == null)
				return null;

			return  gruppidistudioRepository.findGdsBycourseId(id_corso);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	/*
	 * Ritorna i gruppi di uno studente
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/gruppidistudio/me")
	public @ResponseBody
	List<GruppoDiStudio> getgruppidistudioByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/gruppidistudio/me");
			
			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());
			
			Studente studente = studenteRepository.findOne(userId);
			if (studente == null) {
				studente = new Studente();
				studente.setId(userId);
				studente.setNome(profile.getName());
				studente.setCognome(profile.getSurname());
				studente = studenteRepository.save(studente);

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
						supera = supera.concat(String.valueOf(cors.getId())
								.concat(","));
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
				
				studente = studenteRepository.save(studente);
			}
			
			
			if (userId == null)
				return null;

			List<GruppoDiStudio> listaGruppi = gruppidistudioRepository.findAll();
			
			List<GruppoDiStudio> listaGruppiStudente = new ArrayList<GruppoDiStudio>();
			
			for(GruppoDiStudio gruppoDiStudio : listaGruppi){
				if(gruppoDiStudio.isContainsStudente(gruppoDiStudio, userId)){
					listaGruppiStudente.add(gruppoDiStudio);
				}
			}
			
			return listaGruppiStudente;
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	/*
	 * Ritorna i gruppi di uno studente per un determinato corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/gruppidistudio/{id_corso}/me")
	public @ResponseBody
	List<GruppoDiStudio> getgruppidistudioByIDCourseByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {
			logger.info("/gruppidistudio/{id_corso}/me");
			
			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());
			
			Studente studente = studenteRepository.findOne(userId);
			if (studente == null) {
				studente = new Studente();
				studente.setId(userId);
				studente.setNome(profile.getName());
				studente.setCognome(profile.getSurname());
				studente = studenteRepository.save(studente);

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
						supera = supera.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					if (z % 4 == 0) {
						interesse = interesse.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					z++;
				}
			}
			
			if (userId == null)
				return null;

			if (id_corso == null)
				return null;

			List<GruppoDiStudio> listaGruppiCorso = gruppidistudioRepository.findGdsBycourseId(id_corso);
			
			List<GruppoDiStudio> listaGruppiCorsoStudente = new ArrayList<GruppoDiStudio>();
			
			for(GruppoDiStudio gruppoDiStudio : listaGruppiCorso){
				if(gruppoDiStudio.isContainsStudente(gruppoDiStudio, userId)){
					listaGruppiCorsoStudente.add(gruppoDiStudio);
				}
			}
			
			return listaGruppiCorsoStudente;
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
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

	private void checkStudentExists(){
		
	}
}
	
