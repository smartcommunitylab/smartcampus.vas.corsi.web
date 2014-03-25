package eu.trentorise.smartcampus.corsi.controller;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.corsi.model.AttivitaDidattica;
import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.AttivitaDidatticaRepository;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoCarrieraRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.GruppoDiStudioRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.corsi.util.EasyTokenManger;
import eu.trentorise.smartcampus.mediation.engine.MediationParserImpl;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("commentiController")
public class CommentiController {

	private static final Logger logger = Logger
			.getLogger(CommentiController.class);

	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${studymate.client.id}")
	private String clientId;

	@Autowired
	@Value("${studymate.client.secret}")
	private String clientSecret;

	@Autowired
	private CommentiRepository commentiRepository;

	@Autowired
	@Value("${communicator.address}")
	private String communicatoraddress;

	@Autowired
	private CorsoCarrieraRepository corsoCarrieraRepository;

	@Autowired
	private AttivitaDidatticaRepository attivitaDidatticaRepository;

	private MediationParserImpl mediationParserImpl;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;

	@Autowired
	private EventoRepository eventoRepository;

	@Autowired
	private GruppoDiStudioRepository gdsRepository;

	private eu.trentorise.smartcampus.corsi.util.EasyTokenManger tkm;

	@PostConstruct
	private void init() {
		tkm = new EasyTokenManger(profileaddress, clientId, clientSecret);

		Properties prop = new Properties();

		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream streamIn = loader
					.getResourceAsStream("/corsi.properties");
			prop.load(streamIn);
			prop.setProperty("url.file.resource.keywords",
					CommentiController.class.getResource("/bad-words/keywords")
							.toString());

			prop.store(new FileWriter("corsi.properties"), null);

			loader = Thread.currentThread().getContextClassLoader();

			prop.load(streamIn);

			String mediationService = prop
					.getProperty("url.mediation.services");
			String webappName = prop.getProperty("webapp.name");
			String urlResourceKW = prop
					.getProperty("url.file.resource.keywords");

			mediationParserImpl = new MediationParserImpl(mediationService,
					webappName, new URL(urlResourceKW));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param corsoDaAggiornare
	 * @return Il corso le cui valutazioni sono state aggiornate con successo,
	 *         altrimenti ritorna null.
	 * 
	 *         Calcola e aggiorna nel DB le valutazioni medie per ogni ambito
	 *         del corso
	 * 
	 */
	private AttivitaDidattica UpdateRatingCorso(
			AttivitaDidattica corsoDaAggiornare) {

		if (corsoDaAggiornare == null)
			return null;

		// cerco la lista dei commenti
		List<Commento> listCom = commentiRepository
				.getCommentoByCorsoAll(corsoDaAggiornare.getAdId());
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

		AttivitaDidattica corsoAggiornato = attivitaDidatticaRepository
				.saveAndFlush(corsoDaAggiornare);

		if (corsoAggiornato == null)
			return null;

		if (corsoAggiornato.getAdId() != corsoDaAggiornare.getAdId())
			return null;

		return corsoAggiornato;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_corso
	 * @return List<Commento>
	 * @throws IOException
	 * 
	 *             Ritorna tutte le recensioni dato l'id di un corso
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/{id_corso}/commento/all")
	public @ResponseBody
	List<Commento> getCommentoByCorsoId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {

			List<Commento> commentiAggiornati;

			// Aggiorno le valutazioni del corso
			UpdateRatingCorso(attivitaDidatticaRepository.findOne(id_corso));

			logger.info("/corso/{id_corso}/commento/all");
			if (id_corso == null)
				return null;

			commentiAggiornati = commentiRepository
					.getCommentoByCorsoApproved(attivitaDidatticaRepository
							.findOne(id_corso).getAdId());

			if (commentiAggiornati.size() != 0) {
				commentiAggiornati = commentiRepository
						.save(commentiAggiornati);
				return commentiAggiornati;
			} else {
				AttivitaDidattica corso = attivitaDidatticaRepository
						.findOne(id_corso);
				String token = getToken(request);
				BasicProfileService service = new BasicProfileService(
						profileaddress);
				BasicProfile profile = service.getBasicProfile(token);
				Long userId = Long.valueOf(profile.getUserId());
				Studente studente = studenteRepository.findOne(userId);

				Commento commento = new Commento();
				commento.setId_studente(studente.getId());
				commento.setCorso(corso.getAdId());
				commento.setRating_carico_studio((float) -1);
				commento.setRating_contenuto((float) -1);
				commento.setRating_esame((float) -1);
				commento.setRating_lezioni((float) -1);
				commento.setRating_materiali((float) -1);
				commento.setTesto(new String(""));
				commento.setData_inserimento(null);

				commentiAggiornati.add(commento);

				return commentiAggiornati;

			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@Scheduled(fixedDelay = 900000)
	// 15min
	public void updateRemoteComment() throws AACException {
		logger.debug("Update comment in local");
		// aggiorno i commenti
		// MediationParserImpl mediationParserImpl = new MediationParserImpl();
		Map<String, Boolean> updatedCommentList = mediationParserImpl
				.updateComment(0, System.currentTimeMillis(),
						tkm.getClientSmartCampusToken());
		if (updatedCommentList != null && !updatedCommentList.isEmpty()) {
			@SuppressWarnings("rawtypes")
			Iterator iterator = updatedCommentList.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry mapEntry = (Map.Entry) iterator.next();

				Commento c = commentiRepository.findOne(Long.parseLong(mapEntry
						.getKey().toString()));

				if (c != null) {
					c.setApproved((Boolean) mapEntry.getValue());
					commentiRepository.saveAndFlush(c);
					logger.info("Scheduled Synchronization comments... "
							+ updatedCommentList.size() + " comments updated");
				}
			}
		}
	}

	/**
	 * 
	 * @param request
	 * @return String
	 * 
	 *         Ottiene il token riferito alla request
	 * 
	 */
	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_corso
	 * @return Commento
	 * @throws IOException
	 * 
	 *             Restituisce il commento personale per un determinato corso
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/commento/{id_corso}/me")
	public @ResponseBody
	Commento getCommentoByStudenteId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {
			logger.info("/commento/{id_corso}/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			if (userId == null)
				return null;

			return commentiRepository.getCommentoByStudenteApproved(
					studenteRepository.findOne(userId).getId(),
					attivitaDidatticaRepository.findOne(id_corso).getAdId());

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param commento
	 * @return boolean
	 * @throws IOException
	 * 
	 *             Riceve il metodo post dal client e lo salva nel DB. Ritorna
	 *             true se l'operazione va a buon fine, altrimenti false.
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

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// sincronizzo le keyword tra il portale e studymate
			mediationParserImpl.initKeywords(tkm.getClientSmartCampusToken());

			// controllo se lo studente � presente nel db
			Studente studente = studenteRepository.findStudenteByUserId(userId);

			if (studente == null) {
				studente = new Studente();
				studente.setId(userId);
				studente.setNome(profile.getName());
				studente.setCognome(profile.getSurname());
				studente = studenteRepository.save(studente);

				// studente = studenteRepository.save(studente);

				// TODO caricare corsi da esse3
				// Creare associazione su frequenze

				// TEST
				List<CorsoCarriera> corsiEsse3 = corsoCarrieraRepository
						.findAll();

				String supera = null;
				String interesse = null;
				int z = 0;
				supera = new String();
				interesse = new String();

				for (CorsoCarriera cors : corsiEsse3) {

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

				studente = studenteRepository.save(studente);
			}

			// cerco nel db se il commento dello studente per questo corso c'�
			// gi�
			// presente

			if (profile != null) {

				Commento commentoDaModificare = commentiRepository
						.getCommentoByStudenteApproved(
								studenteRepository.findOne(userId).getId(),
								attivitaDidatticaRepository.findOne(
										commento.getCorso()).getAdId());

				// gestisco il commento nel caso sia già presente

				if (commentoDaModificare != null) {
					commentoDaModificare.setId_studente(userId);
					commentoDaModificare.setNome_studente(profile.getName());

					commentoDaModificare.setCorso(commento.getCorso());
					commentoDaModificare.setRating_carico_studio(commento
							.getRating_carico_studio());
					commentoDaModificare.setRating_contenuto(commento
							.getRating_contenuto());
					commentoDaModificare.setRating_esame(commento
							.getRating_esame());
					commentoDaModificare.setRating_lezioni(commento
							.getRating_esame());
					commentoDaModificare.setRating_materiali(commento
							.getRating_materiali());
					commentoDaModificare.setTesto(commento.getTesto());
					commentoDaModificare.setData_inserimento(String
							.valueOf(new Date(System.currentTimeMillis())));
					commentoDaModificare.setNome_studente(profile.getName());
					commentoDaModificare.setApproved(mediationParserImpl
							.localValidationComment(
									commentoDaModificare.getTesto(),
									commentoDaModificare.getId().toString(),
									userId, tkm.getClientSmartCampusToken()));

					if (commentoDaModificare.isApproved()) {
						mediationParserImpl.remoteValidationComment(
								commentoDaModificare.getTesto(),
								commentoDaModificare.getId().toString(),
								userId, tkm.getClientSmartCampusToken());
					}

					if (commentoDaModificare.isApproved()) {
						commentoDaModificare = commentiRepository
								.save(commentoDaModificare);
						if (commentoDaModificare != null) {
							return true;
						} else {
							return false;
						}
					} else {
						commentiRepository.delete(commentoDaModificare);
						return false;
					}

					// se il commento è nuovo
				} else {

					Commento commentoNuovo = new Commento();

					commentoNuovo.setId_studente(userId);
					commentoNuovo.setNome_studente(profile.getName());

					commentoNuovo.setData_inserimento(String.valueOf(new Date(
							System.currentTimeMillis())));
					commentoNuovo.setCorso(commento.getCorso());
					commentoNuovo.setRating_carico_studio(commento
							.getRating_carico_studio());
					commentoNuovo.setRating_contenuto(commento
							.getRating_contenuto());
					commentoNuovo.setRating_esame(commento.getRating_esame());
					commentoNuovo.setRating_lezioni(commento.getRating_esame());
					commentoNuovo.setRating_materiali(commento
							.getRating_materiali());
					commentoNuovo.setTesto(commento.getTesto());

					commentoNuovo.setApproved(true);

					// salvo il commento
					commentoNuovo = commentiRepository.save(commentoNuovo);

					commentoNuovo.setApproved(mediationParserImpl
							.localValidationComment(commentoNuovo.getTesto(),
									commentoNuovo.getId().toString(), userId,
									tkm.getClientSmartCampusToken()));

					// salvo il commento
					commentoNuovo = commentiRepository.save(commentoNuovo);

					if (commentoNuovo.isApproved()) {
						mediationParserImpl.remoteValidationComment(
								commentoNuovo.getTesto(), commentoNuovo.getId()
										.toString(), userId, tkm
										.getClientSmartCampusToken());
					}

					if (commentoNuovo.isApproved()) {
						commentoNuovo = commentiRepository.save(commentoNuovo);
						if (commentoNuovo != null) {
							return true;
						} else {
							return false;
						}
					} else {
						commentiRepository.delete(commentoNuovo);
						return false;
					}

				}
			}

		} catch (Exception e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

}
