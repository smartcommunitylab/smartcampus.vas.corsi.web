package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import eu.trentorise.smartcampus.ac.provider.model.User;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Frequenze;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.FrequenzeRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("corsiController")
public class CorsiController extends SCController {

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
	private FrequenzeRepository frequenzeRepository;

	@Autowired
	private CorsoRepository corsoRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private EventoRepository eventoRepository;


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
			//BasicProfile profile = profileConnector.getBasicProfile(token);

		//	if (profile != null) {

				// TEST
				Corso c = new Corso();

				c.setNome("Fisica dei materiali");
				c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
						+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
						+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
						+ "dolore eu fugiat nulla pariatur.");
				c.setValutazione_media(4);
				corsoRepository.save(c);

				c = new Corso();

				c.setNome("Analisi matematica 2");
				c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
						+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
						+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
						+ "dolore eu fugiat nulla pariatur.");
				c.setValutazione_media(4);
				corsoRepository.save(c);

				c = new Corso();

				c.setNome("Lettere 1");
				c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
						+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
						+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
						+ "dolore eu fugiat nulla pariatur.");
				c.setValutazione_media(4);

				corsoRepository.save(c);

				// TEST

				return corsoRepository.findAll();
		//	} else {
		//	/	return null;
		//	}
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
	@SuppressWarnings("deprecation")
	@RequestMapping(method = RequestMethod.GET, value = "/corsi/{id_corso}")
	public @ResponseBody
	Corso getCorsoByID(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") long id_corso)

	throws IOException {
		try {

			// long id_corso = Long.valueOf(request.getParameter("id_corso"));

			Corso corso = new Corso();
			corso.setId(id_corso);
			corso.setNome("Analisi 2");
			corso.setData_inizio(new Date("10/01/2013"));
			corso.setData_fine(new Date("01/06/2013"));
			corso.setValutazione_media(7);
			corso.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");

			

			corsoRepository.save(corso);

			return corsoRepository.findOne(id_corso);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/me/corsi")
	public @ResponseBody
	Collection<Corso> getAllFrequences(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			User utente = retrieveUser(request);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
		//	BasicProfile profile = profileConnector.getBasicProfile(token);
			// test

			Studente studente = studenteRepository.findStudenteByUserId(utente
					.getId());
			if (studente == null) {
				studente = new Studente();
		//		studente.setNome(profile.getName());
				studente = studenteRepository.save(studente);
				
				//TODO caricare corsi da esse3
				//Creare associazione su frequenze
				
				//TEST
				List<Corso> corsiEsse3=corsoRepository.findAll();
				
				//TEST
				
				for(Corso index: corsiEsse3){
					Frequenze frequenze = new Frequenze();
					
					frequenze.setStudente(studente);
					frequenze.setCorso(index);
						frequenze = frequenzeRepository
								.save(frequenze);
				}
				
			}

			

			return studenteRepository.findOne(studente.getId()).getCorsi();

		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	

}
