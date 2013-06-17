package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.ac.provider.model.User;
import eu.trentorise.smartcampus.communicator.CommunicatorConnector;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("eventiController")
public class EventiController extends SCController {
	private static final Logger logger = Logger
			.getLogger(EventiController.class);
	@Autowired
	private AcService acService;

	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${services.server}")
	private String serverAddress;

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

	/*
	 * Ritorna tutti gli eventi per corso
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
	@RequestMapping(method = RequestMethod.POST, value = "/evento")
	public @ResponseBody
	Evento saveEvento(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestBody Evento evento)

	throws IOException {
		try {
			// TODO controlli se campi validi

			// User Request create event
			// creati a notification and send to Communicator

			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			User user = retrieveUser(request);

			CommunicatorConnector communicatorConnector = new CommunicatorConnector(
					serverAddress);// , appName);

			List<String> users = new ArrayList<String>();
			users.add(user.getId().toString());

			Notification n = new Notification();
			n.setTitle(evento.getTitolo());
			n.setUser(user.getId().toString());
			n.setTimestamp(System.currentTimeMillis());
			n.setDescription("Creazione Evento");

			communicatorConnector.sendAppNotification(n, appName, users, token);

			return eventoRepository.save(evento);

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna tutti gli eventi per corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/evento/me")
	public @ResponseBody
	List<Evento> getEventoByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/eventi/me");

			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			User utente = retrieveUser(request);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			// test

			Studente studente = studenteRepository.findStudenteByUserId(utente
					.getId());
			if (studente == null) {
				studente = new Studente();
				studente.setNome(profile.getName());
				studente = studenteRepository.save(studente);

				// TODO caricare corsi da esse3
				// Creare associazione su frequenze

				// TEST
				List<Corso> corsiEsse3 = corsoRepository.findAll();

				// TEST

				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);

			}

			studente = studenteRepository.save(studente);

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
	
	@PostConstruct
	private void initEvento(){
		
		List<Corso> esse3 = corsoRepository.findAll();
		for(Corso index:esse3){
			for(int i=0;i<2;i++){
				Evento x=new Evento();
				x.setCorso(index);
				x.setTitolo("Lezione "+i);
				eventoRepository.save(x);
			}
		}
		
	}

}
