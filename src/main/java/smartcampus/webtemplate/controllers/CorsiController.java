package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.CorsoLite;
import eu.trentorise.smartcampus.corsi.model.UtenteCorsi;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("corsiController")
public class CorsiController extends SCController {

	private static final String EVENT_OBJECT = "eu.trentorise.smartcampus.dt.model.EventObject";
	private static final Logger logger = Logger
			.getLogger(CorsiLiteController.class);
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

	/*
	 * Ritorna tutti i corsi in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsi/all")
	public @ResponseBody
	List<Corso> getCorsiCompletiAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);

			if (profile != null) {

				// TEST
				Corso c = new Corso();

				c.setNome("Fisica dei materiali");
				c.setAula("a101");
				c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
				c.setValutazione_media(4);
				corsoRepository.save(c);

				c = new Corso();

				c.setNome("Analisi matematica 2");
				c.setAula("a101");
				c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
				c.setValutazione_media(4);
				corsoRepository.save(c);

				c = new Corso();

				c.setNome("Lettere 1");
				c.setAula("a101");
				c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
				c.setValutazione_media(4);
				corsoRepository.save(c);

				// TEST

				return corsoRepository.findAll();
			} else {
				return null;
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna i dati completi di un corso dato l'id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsi/{id_corso}")
	public @ResponseBody
	Corso getCorsoByID(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") String id)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);

			String id_corso = request.getParameter("id_corso");

			Corso corso = new Corso();
			corso.setId(10);
			corso.setNome("Analisi 2");
			corso.setData_inizio(new Date("10/01/2013"));
			corso.setData_fine(new Date("01/06/2013"));
			corso.setValutazione_media(7);
			corso.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");

			List<Commento> commenti = new ArrayList<Commento>();

			Date data = new Date("2013/04/23");
			for (int i = 0; i < 10; i++) {
				Commento co = new Commento();
				co.setCorso(corso);
				co.setData_inserimento(data);
				co.setTesto("Commento del corso Commento del corso Commento del corso Commento del corso. ");
				co.setValutazione(4);
				commenti.add(co);
			}

			corso.setCommenti(commenti);

			return corso;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
