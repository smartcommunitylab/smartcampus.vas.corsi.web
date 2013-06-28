package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.ac.provider.model.User;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("corsiController")
public class CorsiController extends SCController {

	private static final Logger logger = Logger
			.getLogger(CorsiController.class);
	@Autowired
	private AcService acService;

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
	private CorsoRepository corsoRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private EventoRepository eventoRepository;

	/*
	 * Ritorna tutti i corsi in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/all")
	public @ResponseBody
	List<Corso> getCorsiCompletiAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {

			List<Corso> getCor = corsoRepository.findAll();

			return getCor;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna i dati completi di un corso dato l'id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/{id_corso}")
	public @ResponseBody
	Corso getCorsoByID(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {
			logger.info("/corsi/{id_corso}");

			if (id_corso == null)
				return null;

			return corsoRepository.findOne(id_corso);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/me")
	public @ResponseBody
	Collection<Corso> getCorsoByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corso/me");
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			User utente = retrieveUser(request);
			ProfileConnector profileConnector = new ProfileConnector(
					profileaddress);
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
				studente = studenteRepository.save(studente);
			}

		

			return studente.getCorsi();

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	
	
	
	

	/*
	 * getCorsoByDipartimento
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/dipartimento/{id_dipartimento}")
	public @ResponseBody
	Collection<Corso> getCorsoByDipartimento(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_dipartimento") Long id_dipartimento)

	throws IOException {
		try {
			logger.info("/corsi/dipartimento/{id_dipartimento}");
			if (id_dipartimento == null)
				return null;

			return corsoRepository.findCorsoByDipartimentoId(id_dipartimento);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * getCorsoByCorsoLaurea
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/corsolaurea/{id_corsoLaurea}")
	public @ResponseBody
	Collection<Corso> getCorsoByCorsoLaurea(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corsoLaurea") Long id_corsoLaurea)

	throws IOException {
		try {
			logger.info("/corsi/corsolaurea/{id_corsoLaurea}");
			if (id_corsoLaurea == null)
				return null;

			return corsoRepository.findCorsoByCorsoLaureaId(id_corsoLaurea);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@PostConstruct
	private void initCorsi() {
		Corso c = new Corso();

		c.setNome("Programmazione 1");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Analisi matematica 1");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Matematica discreta 1");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);
		
		
		c = new Corso();

		c.setNome("Programmazione 2");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);
		
		
		c = new Corso();

		c.setNome("Programmazione funzionale");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		int i = 0;
		for (i = 2; i < 4; i++) {

			c = new Corso();

			c.setNome("Fisica dei materiali");
			c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
			c.setValutazione_media(4);
			c.setId_dipartimento(i);
			c.setId_corsoLaurea(i);
			corsoRepository.save(c);

			c = new Corso();

			c.setNome("Analisi matematica 2");
			c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
			c.setValutazione_media(4);
			c.setId_dipartimento(i);
			c.setId_corsoLaurea(i);
			corsoRepository.save(c);

			c = new Corso();

			c.setNome("Lettere 1");
			c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
			c.setValutazione_media(4);
			c.setId_dipartimento(i);
			c.setId_corsoLaurea(i);

			corsoRepository.save(c);

			c = new Corso();

			c.setNome("Fisica dei materiali 5");
			c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
			c.setValutazione_media(4);
			c.setId_dipartimento(i);
			c.setId_corsoLaurea(i);
			corsoRepository.save(c);

			c = new Corso();

			c.setNome("Analisi matematica 1");
			c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
			c.setValutazione_media(4);
			c.setId_dipartimento(i);
			c.setId_corsoLaurea(i);
			corsoRepository.save(c);

			c = new Corso();

			c.setNome("Lettere 2");
			c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
			c.setValutazione_media(4);
			c.setId_dipartimento(i);
			c.setId_corsoLaurea(i);

			corsoRepository.save(c);
		}
		// TEST

	}

}
