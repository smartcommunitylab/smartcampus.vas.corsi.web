package eu.trentorise.smartcampus.corsi.controllers;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.AttivitaDiStudio;
import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("commentiController")
public class CommentiController {

	private static final Logger logger = Logger
			.getLogger(CommentiController.class);

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	private CommentiRepository commentiRepository;

	@Autowired
	private CorsoRepository corsoRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;

	@Autowired
	private EventoRepository eventoRepository;

	private Corso calcRating(Corso findOne) {

		if (findOne == null)
			return findOne;

		// cerco la lista dei commenti
		List<Commento> listCom = commentiRepository.getCommentoByCorsoAll(findOne);
		float Rating_carico_studio = 0;
		float Rating_contenuto = 0;
		float Rating_esame = 0;
		float Rating_lezioni = 0;
		float Rating_materiali = 0;
		int len = listCom.size();

		// sommo le valutazioni per ogni ambito
		for (Commento index : listCom) {
			Rating_carico_studio += index.getRating_carico_studio();
			Rating_contenuto += index.getRating_contenuto();
			Rating_esame += index.getRating_esame();
			Rating_lezioni += index.getRating_lezioni();
			Rating_materiali += index.getRating_materiali();
		}

		// calcolo la media per ogni ambito
		findOne.setRating_carico_studio(Rating_carico_studio / len);
		findOne.setRating_contenuto(Rating_contenuto / len);
		findOne.setRating_esame(Rating_esame / len);
		findOne.setRating_lezioni(Rating_lezioni / len);
		findOne.setRating_materiali(Rating_materiali / len);

		// calcolo la valutazione media generale del corso
		float sommaValutazioni = findOne.getRating_carico_studio()
				+ findOne.getRating_contenuto() + findOne.getRating_esame()
				+ findOne.getRating_lezioni() + findOne.getRating_materiali();

		// setto la media delle valutazioni
		findOne.setValutazione_media(sommaValutazioni / 5);

		return findOne;
	}

	// aggiorno le
	private Corso UpdateRatingCorso(Corso corsoDaAggiornare) {

		if (corsoDaAggiornare == null)
			return null;

		// cerco la lista dei commenti
		List<Commento> listCom = commentiRepository
				.getCommentoByCorsoAll(corsoDaAggiornare);
		float Rating_carico_studio = 0;
		float Rating_contenuto = 0;
		float Rating_esame = 0;
		float Rating_lezioni = 0;
		float Rating_materiali = 0;
		int len = listCom.size();

		// sommo le valutazioni per ogni ambito
		for (Commento index : listCom) {
			Rating_carico_studio += index.getRating_carico_studio();
			Rating_contenuto += index.getRating_contenuto();
			Rating_esame += index.getRating_esame();
			Rating_lezioni += index.getRating_lezioni();
			Rating_materiali += index.getRating_materiali();
		}

		// calcolo la media per ogni ambito

		float sommaValutazioni = 0;

		if (len != 0) {
			corsoDaAggiornare.setRating_carico_studio(Rating_carico_studio
					/ len);
			corsoDaAggiornare.setRating_contenuto(Rating_contenuto / len);
			corsoDaAggiornare.setRating_esame(Rating_esame / len);
			corsoDaAggiornare.setRating_lezioni(Rating_lezioni / len);
			corsoDaAggiornare.setRating_materiali(Rating_materiali / len);

			// calcolo la valutazione media generale del corso
			sommaValutazioni = corsoDaAggiornare.getRating_carico_studio()
					+ corsoDaAggiornare.getRating_contenuto()
					+ corsoDaAggiornare.getRating_esame()
					+ corsoDaAggiornare.getRating_lezioni()
					+ corsoDaAggiornare.getRating_materiali();
		}
		// setto la media delle valutazioni
		corsoDaAggiornare.setValutazione_media(sommaValutazioni / 5);

		Corso corsoAggiornato = corsoRepository.saveAndFlush(corsoDaAggiornare);

		if (corsoAggiornato == null)
			return null;

		if (corsoAggiornato.getId() != corsoDaAggiornare.getId())
			return null;

		return corsoAggiornato;
	}

	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/{id_corso}/commento/all")
	public @ResponseBody
	List<Commento> getCommentoByCorsoId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {

			List<Commento> commenti;

			// Aggiorno le valutazioni del corso
			UpdateRatingCorso(corsoRepository.findOne(id_corso));

			logger.info("/corso/{id_corso}/commento/all");
			if (id_corso == null)
				return null;

			commenti = commentiRepository.getCommentoByCorsoApproved(corsoRepository
					.findOne(id_corso));
			if (commenti.size() != 0) {
				return commenti;
			} else {
				Corso corso = corsoRepository.findOne(id_corso);

				String token = getToken(request);
				BasicProfileService service = new BasicProfileService(
						profileaddress);
				BasicProfile profile = service.getBasicProfile(token);
				Long userId = Long.valueOf(profile.getUserId());
				Studente studente = studenteRepository.findOne(userId);
				Commento commento = new Commento();
				commento.setId_studente(studente);
				commento.setCorso(corso);
				commento.setId_studente(new Studente());
				commento.setRating_carico_studio((float) -1);
				commento.setRating_contenuto((float) -1);
				commento.setRating_esame((float) -1);
				commento.setRating_lezioni((float) -1);
				commento.setRating_materiali((float) -1);
				commento.setTesto(new String(""));
				commento.setData_inserimento(null);

				commenti.add(commento);

				return commenti;
				// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/commento/{id_corso}/{id_studente}")
	public @ResponseBody
	Commento getCommentoByStudenteId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") Long id_corso,
			@PathVariable("id_studente") Long id_studente)

	throws IOException {
		try {
			logger.info("/commenti/{id_corso}/{id_studente}");
			if (id_studente == null)
				return null;

			return commentiRepository.getCommentoByStudenteApproved(
					studenteRepository.findOne(id_studente),
					corsoRepository.findOne(id_corso));

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/commento")
	//
	public @ResponseBody
	boolean saveCommento(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@RequestBody Commento commento)

	throws IOException {
		try {
			logger.info("/commento");
			// TODO control valid field
			if (commento == null)
				return false;

			// cerco nel db se il commento dello studente per questo corso � gi�
			// presente
			Commento commentoDaModificare = commentiRepository
					.getCommentoByStudenteApproved(studenteRepository.findOne(commento
							.getId_studente().getId()), corsoRepository
							.findOne(commento.getCorso().getId()));

			if (commentoDaModificare == null) {
				return commentiRepository.save(commento) != null;
			} else {

				commentiRepository.delete(commentoDaModificare);
				commento.setApproved(true);
				return commentiRepository.save(commento) != null;

			}

		} catch (Exception e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return false;
		}

	}

	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initCommenti() {

		Dipartimento d = new Dipartimento();

		d.setNome("Ingegneria e Scienza Dell'Informazione");
		d.setId(1);

		dipartimentoRepository.save(d);

		// ///// dipartimento 2

		d = new Dipartimento();

		d.setNome("Psicologia e scienze cognitive");

		dipartimentoRepository.save(d);

		// ///// dipartimento 3

		d = new Dipartimento();

		d.setNome("Fisica");

		dipartimentoRepository.save(d);

		List<Dipartimento> dip = new ArrayList();

		dip = dipartimentoRepository.findAll();

		for (Dipartimento dipart : dip) {

			CorsoLaurea corsoL = new CorsoLaurea();
			corsoL.setDipartimento(dipart);
			corsoL.setNome("Informatica");
			corsoL.setId(1);
			corsoLaureaRepository.save(corsoL);

			corsoL = new CorsoLaurea();
			corsoL.setDipartimento(dipart);
			corsoL.setId(2);
			corsoL.setNome("Ingegneria Dell'Informazione");
			corsoLaureaRepository.save(corsoL);

			corsoL = new CorsoLaurea();
			corsoL.setDipartimento(dipart);
			corsoL.setId(3);
			corsoL.setNome("Ingegneria Elettronica e Delle Telecomunicazioni");

			corsoLaureaRepository.save(corsoL);

		}

		Corso c = new Corso();

		c.setNome("Algoritmi Avanzati");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(1);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Algoritmi e Strutture Dati");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(2);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Analisi 1");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(3);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Analisi Matematica 2");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(4);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Architettura Degli Elaboratori");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(5);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Architettura Degli Elaboratori");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(5);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Basi Di Dati");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(10);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Basi Di Dati");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(10);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Calcolo Delle Probabilità e Statistica");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(26);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Communication Systems");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(6);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Comunicazioni Elettriche");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(8);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Controlli Automatici");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(9);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Economia e Organizzazione Aziendale");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(11);
		corsoRepository.save(c);


		c = new Corso();

		c.setNome("Elettronica");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(12);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Elettronica Per L'Automazione Aziendale");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(13);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Elettronica Per L'Innovazione Del Prodotto");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(14);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Elettronica Robotica");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(35);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("English A2");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(15);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("English A2");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(15);
		corsoRepository.save(c);


		c = new Corso();

		c.setNome("English C1");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(16);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("English C1");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(16);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Fisica");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(17);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Fisica Ing.");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(18);
		corsoRepository.save(c);


		c = new Corso();

		c.setNome("Geometria e Algebra Lineare");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(19);
		corsoRepository.save(c);


		c = new Corso();

		c.setNome("Gestione Dei Progetti Software");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(20);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Human Computer Interaction");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(21);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Ingegneria Del Software");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(22);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Linguaggi Formali e Compilatori");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(7);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Matematica Discreta 1");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(23);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Matematica Discreta 2");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(24);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Programmazione 1");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(27);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Programmazione 2");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(28);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Programmazione Ad Oggetti");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(29);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Programmazione Funzionale");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(30);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Programmazione Per Il Web");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(31);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Programmazione Sistemi Elettrici");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(32);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Programmazione Sistemi Mobili e Tablet");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(40);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Propagazione Reti Sistemi Comunicativi");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(33);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Reti Di Calcolatori");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(48);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Semantica");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(36);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Sicurezza Dati");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(2);
		c.setId(37);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Sistemi Di Telerilevamento");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(38);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Sistemi Informativi");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(39);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Sistemi Operativi 1");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(41);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Sistemi Operativi 2");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(1);
		c.setId(42);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Strumentazione Di Misura");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(43);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Tecniche Di Diagnostica Biomedicale");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(44);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Tecniche Propagazione Reti e Wireless");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(45);
		corsoRepository.save(c);

		c = new Corso();

		c.setNome("Wired Systems And Devices");
		c.setDescrizione("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut "
				+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut "
				+ "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "
				+ "dolore eu fugiat nulla pariatur.");
		c.setValutazione_media(0);
		c.setId_dipartimento(1);
		c.setId_corsoLaurea(3);
		c.setId(46);
		corsoRepository.save(c);

		List<Corso> esse3 = corsoRepository.findCorsoByCorsoLaureaId((long) 1);
		Dipartimento dipar = dipartimentoRepository.findOne((long) 1);

		for (int i1 = 0; i1 < 3; i1++) {
			Studente studente = new Studente();
			studente.setId((long) i1);
			studente.setNome("NomeStudente" + i1);
			studente.setCognome("CognomeStudente" + i1);
			studente.setCorsi(esse3);
			studente.setDipartimento(dipar);

			String supera = null;
			int z = 0;
			supera = new String();

			for (Corso cors : esse3) {

				GruppoDiStudio gruppoDiSt = new GruppoDiStudio();
				gruppoDiSt.setCorso(cors);
				gruppoDiSt.setNome("Mio gruppo" + i1 + " di " + cors.getNome());

				if (z % 2 == 0) {
					supera = supera.concat(String.valueOf(cors.getId()).concat(
							","));
				}
				z++;
			}

			String gruppiDiStIds = new String();

			z = 0;
			for (Corso cors : esse3) {
				if (z % 2 == 0) {
					gruppiDiStIds = gruppiDiStIds.concat(String.valueOf(
							cors.getId()).concat(","));
				}
				z++;
			}

			studente.setIdsCorsiSuperati(supera);
			studente.setIdsCorsiInteresse("");
			studente.setIdsGruppiDiStudio(gruppiDiStIds);

			studenteRepository.save(studente);

		}

		esse3 = corsoRepository.findAll();
		Studente stud = studenteRepository.findOne((long) 1);

		for (Corso co : esse3) {

//			if (co.getId() != 1) {
				Commento commento = new Commento();
				commento.setCorso(co);
				commento.setRating_carico_studio((float) 4);
				commento.setRating_contenuto((float) 3);
				commento.setRating_esame((float) 5);
				commento.setRating_lezioni((float) 4);
				commento.setRating_materiali((float) 3);
				commento.setId_studente(stud);
				commento.setTesto("Corso molto utile e soprattutto il professore coinvolge nelle lezioni.");
				commentiRepository.save(commento);
//			}

		}

		esse3 = corsoRepository.findAll();
		for (Corso index : esse3) {
			for (int i1 = 0; i1 < 2; i1++) {
				Evento x = new Evento();
				x.setCorso(index);
				x.setTitolo(index.getNome());
				x.setDescrizione("Lezione teorica di " + index.getNome());
				x.setRoom("A20" + i1);
				x.setEvent_location("Polo Tecnologico Ferrari, Povo");
				x.setData(new Date("2013/06/2" + String.valueOf(i1 + 1)));
				x.setStart(new Time(8, 30, 00));
				x.setStop(new Time(10, 30, 00));
				eventoRepository.save(x);

				x = new Evento();
				x.setCorso(index);
				x.setTitolo(index.getNome());
				x.setDescrizione("Appello d'esame di " + index.getNome());
				x.setRoom("A10" + i1);
				x.setEvent_location("Polo Tecnologico Ferrari, Povo");
				x.setData(new Date(("2013/07/0" + String.valueOf(i1 + 1))
						.toString()));
				x.setStart(new Time(10, 30, 0));
				x.setStop(new Time(12, 30, 0));
				eventoRepository.save(x);

				x = new Evento();
				x.setCorso(index);
				x.setTitolo(index.getNome());
				x.setDescrizione("Lezione di laboratorio di " + index.getNome());
				x.setRoom("A10" + i1);
				x.setEvent_location("Polo Tecnologico Ferrari, Povo");
				x.setData(new Date(("2013/07/0" + String.valueOf(i1 + 1))
						.toString()));
				x.setStart(new Time(14, 0, 0));
				x.setStop(new Time(16, 0, 0));
				eventoRepository.save(x);

				AttivitaDiStudio attivita = new AttivitaDiStudio();
				attivita.setCorso(index);
				attivita.setTitolo(index.getNome());
				attivita.setDescrizione("Meeting del progetto di "
						+ index.getNome());
				attivita.setRoom("A21" + i1);
				attivita.setEvent_location("Polo Tecnologico Ferrari, Povo");
				attivita.setData(new Date(
						("2013/09/0" + String.valueOf(i1 + 1)).toString()));
				attivita.setStart(new Time(15, 0, 0));
				attivita.setStop(new Time(17, 0, 0));
			}
		}

	}

}
