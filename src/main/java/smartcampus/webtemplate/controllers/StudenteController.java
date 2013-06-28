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

import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.ac.provider.model.User;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("studenteController")
public class StudenteController extends SCController {

	private static final Logger logger = Logger
			.getLogger(StudenteController.class);

	
	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private CorsoRepository corsoRepository;

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;
	
	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/studente/me/{id_studente}")
	public @ResponseBody
	Collection<Corso> getStudenteByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("id_studente") Long id_studente)

	throws IOException {
		try {
			logger.info("/studente/me/id_studente");

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

			}

			studente = studenteRepository.save(studente);

			return studente.getCorsi();

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	
	/*
	 * Ritorna tutti i corsi in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/studente/me")
	public @ResponseBody
	Studente getInfoStudentFromId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {

			logger.info("/studente/me");

			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					profileaddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			User utente = retrieveUser(request);

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
			return studenteRepository.save(studente);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	@PostConstruct
	private void initStudenti() {

		List<Corso> esse3 = corsoRepository.findAll();
		
		for (int i=0; i<200; i++) {
			Studente studente = new Studente();
			studente.setNome("NomeStudente"+i);
			studente.setCognome("CognomeStudente"+i);
			studenteRepository.save(studente);
		}


	}

}
