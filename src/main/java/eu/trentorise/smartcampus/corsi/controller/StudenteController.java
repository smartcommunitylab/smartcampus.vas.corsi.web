package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.CorsoLite;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.StudentInfoService;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoData;

@Controller("studenteController")
public class StudenteController {

	private static final Logger logger = Logger
			.getLogger(StudenteController.class);

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private CorsoRepository corsoRepository;

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${url.studente.service}")
	private String URLStudenteService;

	private StudentInfoService studentInfoService;

	@PostConstruct
	private void initStudentInfoService() {
		studentInfoService = new StudentInfoService(URLStudenteService);
	}

	

	/*
	 * Ritorna tutti i corsi in versione lite
	 */
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return Studente
	 * @throws IOException
	 * 
	 * Restituisce i dati dello studente riferiti allo studente che effetua la richiesta
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/studente/me")
	public @ResponseBody
	Studente getInfoStudentFromId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {

			logger.info("/studente/me");

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
						interesse = interesse.concat(String.valueOf(
								cors.getId()).concat(","));
					}

					z++;
				}

				// TEST

				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);
				studente = studenteRepository.save(studente);

				// Set corsi superati
				studente.setIdsCorsiSuperati(supera);
				studente.setIdsCorsiInteresse(interesse);
			}

			Studente stud = studenteRepository.save(studente);
			// stud.setCorsiSuperati(assignCorsi(stud));
			return stud;

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
