package eu.trentorise.smartcampus.corsi.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.CorsoLite;
import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("corsiController")
public class CorsiController {

	private static final Logger logger = Logger
			.getLogger(CorsiController.class);
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

	@Autowired
	private CommentiRepository commentiRepository;

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

			return calcRating(corsoRepository.findOne(id_corso));
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private Corso calcRating(Corso findOne) {

		if (findOne == null)
			return findOne;
		List<Commento> listCom = commentiRepository.getCommentoByCorso(findOne);
		float Rating_carico_studio = 0;
		float Rating_contenuto = 0;
		float Rating_esame = 0;
		float Rating_lezioni = 0;
		float Rating_materiali = 0;
		int len = listCom.size();

		for (Commento index : listCom) {
			Rating_carico_studio += index.getRating_carico_studio();
			Rating_contenuto += index.getRating_contenuto();
			Rating_esame += index.getRating_esame();
			Rating_lezioni += index.getRating_lezioni();
			Rating_materiali += index.getRating_materiali();
		}

		findOne.setRating_carico_studio(Rating_carico_studio / len);
		findOne.setRating_contenuto(Rating_contenuto / len);
		findOne.setRating_esame(Rating_esame / len);
		findOne.setRating_lezioni(Rating_lezioni / len);
		findOne.setRating_materiali(Rating_materiali / len);

		return findOne;
	}

	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
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

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// test
			Studente studente = studenteRepository.findStudenteByUserId(userId);
			if (studente == null) {
				studente = new Studente();
				studente.setId(userId);
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
	 * Ritorna tutti i corsi che lo studente ha superato, quindi che pu� votare
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/superati/me")
	public @ResponseBody
	Collection<CorsoLite> getCorsiSuperatiByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corso/superati/me");
			
			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());
			
			List<CorsoLite> corsiSuperati;
			// test
			Studente studente = studenteRepository.findStudenteByUserId(userId);
			if (studente == null) {
				studente = new Studente();
				studente.setId(userId);
				studente.setNome(profile.getName());
				studente.setCognome(profile.getSurname());
				studente = studenteRepository.save(studente);
				
				//studente = studenteRepository.save(studente);

				// TODO caricare corsi da esse3
				// Creare associazione su frequenze

				// TEST
				List<Corso> corsiEsse3 = corsoRepository.findAll();

				String supera = null;
				int z=0;
				supera = new String();
				
				for(Corso cors : corsiEsse3){

					if(z % 2 == 0){
						supera = supera.concat(String.valueOf(cors.getId()).concat(","));
					}
					z++;
				}
				
				
				// TEST

				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);
				studente = studenteRepository.save(studente);
				
				// Set corsi superati
				studente.setIdsCorsiSuperati(supera);
				
			}
				
			studente = studenteRepository.save(studente);
			
			return assignCorsi(studente);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	private List<CorsoLite> assignCorsi(Studente stud) {
		// TODO Auto-generated method stub
		
		String[] listS = stud.getIdsCorsiSuperati().split(",");
		
		List<CorsoLite> reurList=new ArrayList<CorsoLite>();
		for(String s: listS){
			reurList.add(corsoRepository.findOne(Long.valueOf(s)));
		}
		
		
		return reurList;
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

	// @PostConstruct
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

		c = new Corso();

		c.setNome("Architettura degli elaboratori");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Matematica discreta 2");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Probabilit� e statistica");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Ingegneria del software");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Basi di dati");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Sistemi operativi");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Reti di calcolatori");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Algoritmi e strutture dati");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Programmazione per sistemi mobili e tablet");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(4);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		corsoRepository.save(c);

		int i = 0;
		int j = 0;
		for (i = 2; i < 4; i++) {

			c = new Corso();

			c.setNome("Corso" + j++);
			c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
			c.setValutazione_media(4);
			c.setId_dipartimento(i);
			c.setId_corsoLaurea(i);
			corsoRepository.save(c);

			c = new Corso();

			c.setNome("Corso" + j++);
			c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
			c.setValutazione_media(4);
			c.setId_dipartimento(i);
			c.setId_corsoLaurea(i);
			corsoRepository.save(c);

			c = new Corso();

			c.setNome("Corso" + j++);
			c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
					+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
					+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
					+ "dolore eu fugiat nulla pariatur.");
			c.setValutazione_media(4);
			c.setId_dipartimento(i);
			c.setId_corsoLaurea(i);

			corsoRepository.save(c);

			c = new Corso();

			c.setNome("Corso" + j++);
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

			c.setNome("Corso" + j++);
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
