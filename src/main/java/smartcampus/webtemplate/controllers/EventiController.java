package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
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
import eu.trentorise.smartcampus.corsi.model.CorsoLite;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.repository.CorsoLiteRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
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
	private EventoRepository eventoRepository;

	@Autowired
	private CorsoRepository corsoRepository;

	/*
	 * Ritorna tutti gli eventi
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/eventi/{idcorso}")
	public @ResponseBody
	List<Evento> getEventi(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("idcorso") String idcorso)

	throws IOException {
		try {

			Corso corso = corsoRepository.findOne(Long.valueOf(idcorso));

			Evento e = new Evento();

			e.setAll_day(true);
			// e.setId(1);
			e.setTitolo("Analisi 1");
			e.setDescrizione("Descrizione di prova");
			e.setCorso(corso);
			eventoRepository.save(e);

			return eventoRepository.findEventoByCorso(corso);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Riceve evento e lo salva nel db
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/eventi")
	public @ResponseBody
	Evento saveEvent(HttpServletRequest request, HttpServletResponse response,
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

}
