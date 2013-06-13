package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;

@Controller("commentiController")
public class CommentiController extends SCController {

	private static final Logger logger = Logger
			.getLogger(CommentiController.class);

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
	private CommentiRepository commentiRepository;

	@Autowired
	private CorsoRepository corsoRepository;

	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/commenti/{id_corso}")
	public @ResponseBody
	List<Commento> getCommentoByCorsoId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") long id_corso)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);

			long userId = Long.valueOf(profileConnector.getBasicProfile(token)
					.getUserId());

			// TEST
			Commento c = new Commento();

			// Corso x= corsoRepository.findOne(id_corso);

			@SuppressWarnings("deprecation")
			Date d = new Date("2013/06/11");
			c.setData_inserimento(d);
			c.setTesto("questa � una valutazione del corso. Una valutazione valutazione valutazione.");
			c.setRating_carico_studio(3);
			c.setRating_contenuto(3);
			c.setRating_esame(3);
			c.setRating_lezioni(3);
			c.setRating_materiali(3);
			// c.setId_studente(userId);
			// ////////////////////////////////////c.setCorso(x);
			c.setData_inserimento(d);
			commentiRepository.save(c);

			c = new Commento();

			c.setData_inserimento(d);
			c.setTesto("questa � una valutazione del corso. Una valutazione valutazione valutazione.");
			c.setRating_carico_studio(3);
			c.setRating_contenuto(3);
			c.setRating_esame(3);
			c.setRating_lezioni(3);
			c.setRating_materiali(3);
			// c.setId_studente(userId);
			// ////////////////////////////////////c.setCorso(x);
			c.setData_inserimento(d);
			commentiRepository.save(c);

			c = new Commento();

			c.setData_inserimento(d);
			c.setTesto("questa � una valutazione del corso. Una valutazione valutazione valutazione.");
			c.setRating_carico_studio(3);
			c.setRating_contenuto(3);
			c.setRating_esame(3);
			c.setRating_lezioni(3);
			c.setRating_materiali(3);
			// c.setId_studente(userId);
			// ////////////////////////////////////c.setCorso(x);
			c.setData_inserimento(d);
			commentiRepository.save(c);

			// TEST

			return commentiRepository.findAll();

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/commenti/all")
	public @ResponseBody
	List<Commento> getCommentoAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			long userId = Long.valueOf(profileConnector.getBasicProfile(token)
					.getUserId());

			// TEST
			Commento c = new Commento();

			@SuppressWarnings("deprecation")
			Date d = new Date("2013/06/11");
			c.setData_inserimento(d);
			c.setTesto("questa � una valutazione del corso. Una valutazione valutazione valutazione.");
			c.setRating_carico_studio(3);
			c.setRating_contenuto(3);
			c.setRating_esame(3);
			c.setRating_lezioni(3);
			c.setRating_materiali(3);
			// c.setId_studente(userId);
			commentiRepository.save(c);

			c = new Commento();

			c.setData_inserimento(d);
			c.setTesto("questa � una valutazione del corso. Una valutazione valutazione valutazione.");
			c.setRating_carico_studio(3);
			c.setRating_contenuto(3);
			c.setRating_esame(3);
			c.setRating_lezioni(3);
			c.setRating_materiali(3);
			// c.setId_studente(userId);
			c.setData_inserimento(d);
			commentiRepository.save(c);

			c = new Commento();

			c.setData_inserimento(d);
			c.setTesto("questa � una valutazione del corso. Una valutazione valutazione valutazione.");
			c.setRating_carico_studio(3);
			c.setRating_contenuto(3);
			c.setRating_esame(3);
			c.setRating_lezioni(3);
			c.setRating_materiali(3);
			// c.setId_studente(userId);
			c.setData_inserimento(d);
			commentiRepository.save(c);

			// TEST

			return commentiRepository.findAll();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
