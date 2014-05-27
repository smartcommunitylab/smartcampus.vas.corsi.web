package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.communicator.model.NotificationAuthor;
import eu.trentorise.smartcampus.corsi.model.AttivitaDidattica;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.AttivitaDidatticaRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.GruppoDiStudioRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("gruppiStudioController")
public class GruppiStudioController {

	private enum TypeNotification {
		INVITO, AVVISO
	}

	private static final Logger logger = Logger
			.getLogger(GruppiStudioController.class);

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${studymate.client.id}")
	private String clientId;

	@Autowired
	@Value("${studymate.client.secret}")
	private String clientSecret;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	@Value("${communicator.address}")
	private String communicatoraddress;

	@Autowired
	private GruppoDiStudioRepository gruppidistudioRepository;

	@Autowired
	private AttivitaDidatticaRepository attivitaDidatticaRepository;

	@Autowired
	private EventoRepository eventoRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;

	// /////////////////////////////////////////////////////////////////////////
	// METODI GET /////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return ritorna la lista di tutti i gruppi di studio
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/gruppodistudio/all")
	public @ResponseBody
	List<GruppoDiStudio> getgruppidistudioAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/rest/gruppodistudio/all");
			List<GruppoDiStudio> getgruppidistudio = gruppidistudioRepository
					.findAll();

			for (GruppoDiStudio gruppoDiStudio : getgruppidistudio) {

				List<Long> idsStudenti = convertIdsAllStudentsToList(gruppoDiStudio
						.getIdsStudenti());
				List<Studente> studentiGruppo = new ArrayList<Studente>();
				for (Long id : idsStudenti) {
					Studente studente = studenteRepository.findOne(id);
					studentiGruppo.add(studente);

				}
				gruppoDiStudio.setStudentiGruppo(studentiGruppo);
			}
			return getgruppidistudio;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// METODI GET /////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return retituisce tutti i gruppi di studio a cui lo studente non è
	 *         iscritto
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/gruppodistudio/find")
	public @ResponseBody
	List<GruppoDiStudio> getgruppidistudioSearchToSubscribe(
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session)

	throws IOException {
		try {
			logger.info("/rest/gruppodistudio/find");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			List<GruppoDiStudio> getgruppidistudio = gruppidistudioRepository
					.findAll();

			List<GruppoDiStudio> getGds = new ArrayList<GruppoDiStudio>();
			for (GruppoDiStudio gruppoDiStudio : getgruppidistudio) {
				List<Long> listStudIscritti = convertIdsAllStudentsToList(gruppoDiStudio
						.getIdsStudenti());
				List<Studente> studentiGruppo = new ArrayList<Studente>();
				for (Long studenteId : listStudIscritti) {
					int i = 0;
					if (studenteId != userId) {
						getGds.add(gruppoDiStudio);
					}
					Studente studente = studenteRepository.findOne(studenteId);
					studentiGruppo.add(studente);
				}

				gruppoDiStudio.setStudentiGruppo(studentiGruppo);
			}
			return getGds;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// METODI GET /////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param ad_id
	 * @return restituisce la lista di tutti i gds associati ad un'attività
	 *         didattica in cui lo studente non è iscritto
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/gruppodistudio/find/{ad_id}")
	public @ResponseBody
	List<GruppoDiStudio> getgruppidistudioSearchToSubscribeByAd(
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("ad_id") Long ad_id)

	throws IOException {
		try {
			logger.info("/rest/gruppodistudio/find/{ad_id}");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			List<GruppoDiStudio> getgruppidistudio = gruppidistudioRepository
					.findAll();

			List<GruppoDiStudio> getGds = new ArrayList<GruppoDiStudio>();
			for (GruppoDiStudio gruppoDiStudio : getgruppidistudio) {

				if (gruppoDiStudio.getCorso() == ad_id) {
					List<Long> listStudIscritti = convertIdsAllStudentsToList(gruppoDiStudio
							.getIdsStudenti());
					List<Studente> studentiGruppo = new ArrayList<Studente>();
					for (Long studenteId : listStudIscritti) {
						if (studenteId != userId) {
							Studente studente = studenteRepository
									.findOne(studenteId);

							studentiGruppo.add(studente);
						}

					}

					if (!gruppoDiStudio.isContainsStudente(userId)) {
						gruppoDiStudio.setStudentiGruppo(studentiGruppo);
						getGds.add(gruppoDiStudio);

					}
				}
			}
			return getGds;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_corso
	 * @return ritorna tutti i gruppi di studio associati ad un'attività
	 *         didattica
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/gruppodistudio/{id_corso}")
	public @ResponseBody
	List<GruppoDiStudio> getgruppidistudioByIDCourse(
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {
			logger.info("/rest/gruppodistudio/{id_corso}");

			if (id_corso == null)
				return null;

			List<GruppoDiStudio> getgruppidistudio = gruppidistudioRepository
					.findGdsBycourseId(id_corso);

			for (GruppoDiStudio gruppoDiStudio : getgruppidistudio) {

				List<Long> idsStudenti = convertIdsAllStudentsToList(gruppoDiStudio
						.getIdsStudenti());
				List<Studente> studentiGruppo = new ArrayList<Studente>();
				for (Long id : idsStudenti) {
					Studente studente = studenteRepository.findOne(id);
					studentiGruppo.add(studente);

				}
				gruppoDiStudio.setStudentiGruppo(studentiGruppo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return restituisce la lista di tutti i gruppi di studio a cui è iscritto
	 *         lo studente
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/gruppodistudio/me")
	public @ResponseBody
	List<GruppoDiStudio> getgruppidistudioByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/rest/gruppodistudio/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			if (userId == null)
				return null;

			List<GruppoDiStudio> listaGruppi = gruppidistudioRepository
					.findAll();

			List<GruppoDiStudio> listaGruppiStudente = new ArrayList<GruppoDiStudio>();

			for (GruppoDiStudio gruppoDiStudio : listaGruppi) {
				if (gruppoDiStudio.isContainsStudente(userId)) {
					List<Long> idsStudenti = convertIdsAllStudentsToList(gruppoDiStudio
							.getIdsStudenti());
					List<Studente> studentiGruppo = new ArrayList<Studente>();
					for (Long id : idsStudenti) {
						Studente studente = studenteRepository.findOne(id);
						studentiGruppo.add(studente);

					}
					gruppoDiStudio.setStudentiGruppo(studentiGruppo);

					listaGruppiStudente.add(gruppoDiStudio);
				}
			}

			return listaGruppiStudente;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_corso
	 * @return restituisce tutti i gruppi di studio a cui lo studente è
	 *         iscritto, filtrati per una determinata attività didattica
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/gruppodistudio/{id_corso}/me")
	public @ResponseBody
	List<GruppoDiStudio> getgruppidistudioByIDCourseByMe(
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {
			logger.info("/rest/gruppidistudio/{id_corso}/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studente = studenteRepository.findOne(userId);
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
				List<AttivitaDidattica> corsiEsse3 = attivitaDidatticaRepository
						.findAll();

				String supera = null;
				String interesse = null;
				int z = 0;
				supera = new String();
				interesse = new String();

				for (AttivitaDidattica cors : corsiEsse3) {

					if (z % 2 == 0) {
						supera = supera.concat(String.valueOf(cors.getAdId())
								.concat(","));
					}

					if (z % 4 == 0) {
						interesse = interesse.concat(String.valueOf(
								cors.getAdId()).concat(","));
					}

					z++;
				}
			}

			if (userId == null)
				return null;

			if (id_corso == null)
				return null;

			List<GruppoDiStudio> listaGruppiCorso = gruppidistudioRepository
					.findGdsBycourseId(id_corso);

			List<GruppoDiStudio> listaGruppiCorsoStudente = new ArrayList<GruppoDiStudio>();

			for (GruppoDiStudio gruppoDiStudio : listaGruppiCorso) {
				if (gruppoDiStudio.isContainsStudente(userId)) {
					List<Long> idsStudenti = convertIdsAllStudentsToList(gruppoDiStudio
							.getIdsStudenti());
					List<Studente> studentiGruppo = new ArrayList<Studente>();
					for (Long id : idsStudenti) {
						Studente stud = studenteRepository.findOne(id);
						studentiGruppo.add(stud);

					}
					gruppoDiStudio.setStudentiGruppo(studentiGruppo);

					listaGruppiCorsoStudente.add(gruppoDiStudio);
				}
			}

			return listaGruppiCorsoStudente;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// METODI POST /////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param gruppodistudio
	 * @return true se l'operazione va a buon fine, false altrimenti
	 * @throws IOException
	 * 
	 *             Aggiunge un nuovo gruppo di studio nel db. L'unico studente
	 *             appartenente al gruppo è chi ha fatto la POST.
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/rest/gruppodistudio/add")
	public @ResponseBody
	boolean AddGds(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestBody GruppoDiStudio gruppodistudio)

	throws IOException {
		try {
			logger.info("/rest/gruppodistudio/add");
			// TODO control valid field
			if (gruppodistudio == null)
				return false;

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// gruppodistudio.getEventoId().setIdStudente(userId);
			// gruppodistudio.getEventoId().setIdEventAd(-1);

			// mediationParserImpl.updateKeyWord(token);

			List<String> users = new ArrayList<String>();
			List<String> idsInvited = gruppodistudio.getListInvited(userId);

			for (String id : idsInvited) {
				users.add(id);
			}

			Notification n = new Notification();
			n.setTitle(gruppodistudio.getNome());
			NotificationAuthor nAuthor = new NotificationAuthor();
			nAuthor.setAppId(appName);
			nAuthor.setUserId(userId.toString());
			n.setAuthor(nAuthor);
			n.setUser(userId.toString());
			n.setType(TypeNotification.INVITO.toString());
			n.setTimestamp(System.currentTimeMillis());
			n.setDescription("Invito da " + profile.getName() + " "
					+ profile.getSurname() + " al gruppo "
					+ gruppodistudio.getNome());
			Map<String, Object> mapGruppo = new HashMap<String, Object>();
			gruppodistudio.initStudenteGruppo(userId); // inizializzo i membri
														// del gruppo
			gruppodistudio.setVisible(false); // setto a visible = false finchè
												// non ci saranno almeno 2
												// componenti
			mapGruppo.put("GruppoDiStudio", gruppodistudio); // passo come
																// contenuto
																// della
																// notifica
																// l'hashmap con
																// l'attivita
			n.setContent(mapGruppo);

			// ottengo il client token
			// tkm = new EasyTokenManger(profileaddress, clientId,
			// clientSecret);
			// communicatorConnector.sendAppNotification(n, appName, users,
			// tkm.getClientSmartCampusToken());

			// communicatorConnector.sendAppNotification(n, appName,
			// users,tManager.getClientSmartCampusToken());

			gruppodistudio.setId(-1); // setto l'id a -1 per evitare che il
										// commento venga sovrascritto

			GruppoDiStudio gruppodistudioAggiornato = gruppidistudioRepository
					.save(gruppodistudio);

			// Controllo se il gruppodistudio è gia presente
			if (gruppodistudioAggiornato == null) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return false;
		}

	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param gruppodistudio
	 * @return true se l'operazione va a buon fine, false altrimenti
	 * @throws IOException
	 * 
	 *             Aggiunge un nuovo membro al gruppo di studio passato come
	 *             input.
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/rest/gruppodistudio/accept")
	public @ResponseBody
	boolean AcceptGds(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestBody GruppoDiStudio gruppodistudio)

	throws IOException {
		try {
			logger.info("/rest/gruppodistudio/accept");
			// TODO control valid field
			if (gruppodistudio == null)
				return false;

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// mediationParserImpl.updateKeyWord(token);

			GruppoDiStudio gdsFromDB = gruppidistudioRepository
					.findOne(gruppodistudio.getId());

			// se gds non è nel db -> false
			if (gdsFromDB == null)
				return false;

			gdsFromDB.addStudenteGruppo(userId); // aggiungo il membro al gruppo
			// gdsFromDB.setIfVisibleFromNumMembers();

			// mando una notifica se il gruppo diventa visibile (ci sono almeno
			// 2 membri)
			// if (gdsFromDB.isVisible()) {

			// CommunicatorConnector communicatorConnector = new
			// CommunicatorConnector(
			// communicatoraddress, appName);

			List<String> users = new ArrayList<String>();
			List<String> idsInvited = gdsFromDB.getListInvited(userId);

			for (String id : idsInvited) {
				users.add(id);
			}

			// se ci sono notifiche da mandare
			if (users.size() > 0) {

				Notification n = new Notification();
				n.setTitle(gdsFromDB.getNome());
				NotificationAuthor nAuthor = new NotificationAuthor();
				nAuthor.setAppId(appName);
				nAuthor.setUserId(userId.toString());
				n.setAuthor(nAuthor);
				n.setUser(userId.toString());
				n.setType(TypeNotification.AVVISO.toString());
				n.setTimestamp(System.currentTimeMillis());
				n.setDescription("Il gruppo " + gdsFromDB.getNome()
						+ " a cui sei iscritto ora è visibile");
				Map<String, Object> mapGruppo = new HashMap<String, Object>();

				// gdsFromDB.initStudenteGruppo(userId); // inizializzo i
				// membri del gruppo
				gdsFromDB.setVisible(true); // setto a visible = false
											// finchè non ci saranno
											// almeno 2 componenti
				mapGruppo.put("GruppoDiStudio", gdsFromDB); // passo come
															// contenuto
															// della
															// notifica
															// l'hashmap con
															// l'attivita

				n.setContent(mapGruppo);

				// ottengo il client token
				// EasyTokenManger tManager = new EasyTokenManger(CLIENT_ID,
				// CLIENT_SECRET, profileaddress);

				// communicatorConnector.sendAppNotification(n, appName,
				// users, tManager.getClientSmartCampusToken());

			}
			// }

			GruppoDiStudio gruppodistudioAggiornato = gruppidistudioRepository
					.save(gdsFromDB);

			if (gruppodistudioAggiornato == null) {
				return false;
			} else {
				// controllo che il gds aggiornato abbia lo stesso id di prima
				if (gdsFromDB.getId() == gruppodistudioAggiornato.getId())
					return true;
				else
					return false;
			}

		} catch (Exception e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return false;
		}

	}

	// /////////////////////////////////////////////////////////////////////////
	// METODI DELETE
	// /////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param gruppodistudio
	 * @return true se lo studente viene tolto dal gruppo di studio
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/rest/gruppodistudio/delete/me")
	public @ResponseBody
	boolean deleteMeByGds(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@RequestBody long gruppodistudio)

	throws IOException {
		try {
			logger.info("/rest/gruppodistudio/delete/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// Studente studente = studenteRepository.findOne(userId);

			if (userId == null)
				return false;

			GruppoDiStudio gdsFromDB = gruppidistudioRepository
					.findOne(gruppodistudio);

			gdsFromDB.removeStudenteGruppo(userId);
			gdsFromDB.setIfVisibleFromNumMembers();
			// se il gruppo ha 0 membri lo elimino dal db
			if (gdsFromDB.canRemoveGruppoDiStudioIfVoid()) {
				List<Evento> eventsToRemove = eventoRepository
						.selectEventsGdsOfStudent(gdsFromDB, userId);
				for (Evento evento : eventsToRemove) {
					eventoRepository.delete(evento);
				}
				gruppidistudioRepository.delete(gdsFromDB);

			} else {
				gruppidistudioRepository.save(gdsFromDB);
				List<Evento> eventsToRemove = eventoRepository
						.selectEventsGdsOfStudent(gdsFromDB, userId);
				for (Evento evento : eventsToRemove) {
					eventoRepository.delete(evento);
				}

			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param gruppodistudio
	 * @return true se il gds viene modificato correttamente
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/rest/gruppodistudio/change")
	public @ResponseBody
	boolean changeGds(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestBody GruppoDiStudio gruppodistudio)

	throws IOException {
		try {
			logger.info("/rest/gruppodistudio/change");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			if (userId == null)
				return false;

			gruppidistudioRepository.delete(gruppodistudio);

			GruppoDiStudio gdsFromDB = gruppidistudioRepository
					.save(gruppodistudio);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

	public List<Long> convertIdsAllStudentsToList(String ids) {
		String[] listIds = null;
		List<Long> listIdsInvited = new ArrayList<Long>();

		listIds = ids.split(",");

		for (String id : listIds) {

			listIdsInvited.add(Long.parseLong(id));

		}

		return listIdsInvited;
	}

}
