package eu.trentorise.smartcampus.corsi.controllers;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("eventiController")
public class EventiController {
	private static final Logger logger = Logger
			.getLogger(EventiController.class);

	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${communicator.address}")
	private String communicatoraddress;

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

	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param idcorso
	 * @return List<Evento>
	 * @throws IOException
	 * 
	 * Restituisce tutti gli eventi riferiti ad un corso dato
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/evento/{idcorso}")
	public @ResponseBody
	List<Evento> getEventoByCorso(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("idcorso") Long idcorso)

	throws IOException {
		try {
			logger.info("/eventi/{idcorso}");

			if (idcorso == null)
				return null;

			Corso corso = corsoRepository.findOne(Long.valueOf(idcorso));

			return eventoRepository.findEventoByCorso(corso);

		} catch (Exception e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Riceve evento e lo salva nel db
	 */
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param evento
	 * @return Evento
	 * @throws IOException
	 * 
	 * Salva nel DB l'evento passato dal client e restituisce l'evento se l'operazione va a buon fine, altrimenti false
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/evento")
	public @ResponseBody
	Evento saveEvento(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestBody Evento evento)

	throws IOException {
		try {

			// TODO controlli se campi validi
			if (evento != null && evento.getTitolo() != "") {

				String token = getToken(request);
				BasicProfileService service = new BasicProfileService(
						profileaddress);
				BasicProfile profile = service.getBasicProfile(token);
				Long userId = Long.valueOf(profile.getUserId());

				CommunicatorConnector communicatorConnector = new CommunicatorConnector(
						communicatoraddress, appName);

				List<String> users = new ArrayList<String>();
				users.add(userId.toString());

				Notification n = new Notification();
				n.setTitle(evento.getTitolo());
				n.setUser(userId.toString());
				n.setTimestamp(System.currentTimeMillis());
				n.setDescription("Creazione Evento");

				communicatorConnector.sendAppNotification(n, appName, users,
						token);

				return eventoRepository.save(evento);
			} else
				return null;

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return List<Evento>
	 * @throws IOException
	 * 
	 * Restituisce tutti gli eventi di tutti i corsi personali riferiti allo studente
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/evento/me")
	public @ResponseBody
	List<Evento> getEventoByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/evento/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studente = studenteRepository.findStudenteByUserId(userId);
			
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
				// Set corsi interesse
				studente.setIdsCorsiInteresse(interesse);
				
				studente = studenteRepository.save(studente);
			}

			List<Evento> eventiListByCorso = new ArrayList<Evento>();

			for (Corso index : studente.getCorsi()) {

				eventiListByCorso.addAll(eventoRepository
						.findEventoByCorso(index));

			}

			return eventiListByCorso;
		} catch (Exception e) {

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
	 * Ottiene il token riferito alla request
	 * 
	 */
	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

}
