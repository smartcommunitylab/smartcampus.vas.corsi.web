package smartcampus.webtemplate.controllers;

import java.io.IOException;
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
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.Frequenze;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.FrequenzeRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("frequenzeController")
public class FrequenzeController extends SCController {

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
	private FrequenzeRepository frequenzeRepository;

	@Autowired
	private CorsoRepository corsoRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private EventoRepository eventoRepository;

	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/frequenze")
	public @ResponseBody
	List<Frequenze> getAllFrequences(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
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
			}

			Corso corso = new Corso();
			corso.setNome("Fisica organica");
			
			
			Frequenze frequenze = new Frequenze();
		
			frequenze.setStudente(studente);
			frequenze.setCorso(corso);
				frequenze = frequenzeRepository
						.save(frequenze);
			
		
		

			

				frequenze = frequenzeRepository.save(frequenze);
			// test

			return frequenzeRepository.findAll();

		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	
	
	
	/*
	 * Ritorna tutti i calendari di un determinato utente (cioè tutti tutti i riferimenti agli eventi di quello studente)
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id_studente}/frequenze")
	public @ResponseBody
	List<Frequenze> getFrequencesByIdStudente(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("id_studente") long id_studente)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			User utente = retrieveUser(request);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			// test

			

		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	
	
}
