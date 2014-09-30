package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

import eu.trentorise.smartcampus.communicator.CommunicatorConnector;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.communicator.model.NotificationAuthor;
import eu.trentorise.smartcampus.corsi.model.AttivitaDiStudio;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.EventoId;
import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.GruppoDiStudioRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.corsi.util.EasyTokenManger;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("attivitaStudioController")
public class AttivitaStudioController {

	private enum TypeNotification {
		AVVISO
	}

	private static final String CLIENT_ID = "b8fcb94d-b4cf-438f-802a-c0a560734c88";
	private static final String CLIENT_SECRET = "186b10c3-1f14-4833-9728-14eaa6c27891";

	private static final Logger logger = Logger
			.getLogger(AttivitaStudioController.class);
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
	@Value("${communicator.address}")
	private String communicatoraddress;

	@Autowired
	private GruppoDiStudioRepository gruppstudioRepository;

	@Autowired
	private EventoRepository eventoRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	// /////////////////////////////////////////////////////////////////////////
	// METODI GET /////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_gruppodistudio
	 * @return le attività di studio dato l'id di un gds
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/attivitadistudio/{id_gruppodistudio}")
	public @ResponseBody
	List<Evento> getAttivitadistudioByID(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_gruppodistudio") Long id_gruppodistudio)

	throws IOException {
		try {
			logger.info("/rest/attivitastudio/{id_gruppidistudio}");

			if (id_gruppodistudio == null)
				return null;

			GruppoDiStudio gdsRefer = gruppstudioRepository
					.findOne(id_gruppodistudio);
			
			List<Evento> attivitaStudioList = new ArrayList<Evento>();

			for (Evento evento : eventoRepository.findAttByIdGds(gdsRefer)) {

				// today
				Long date = System.currentTimeMillis();
				

				if (evento.getEventoId().getDate().getTime() >= date-TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)) {
					attivitaStudioList.add(evento);
				}
			}

		// sort per data
		Collections.sort(attivitaStudioList, new Comparator<Evento>() {
			public int compare(Evento e1, Evento e2) {
				if (e1.getEventoId().getDate() == null
						|| e2.getEventoId().getDate() == null)
					return 0;

				Long millisecondE1 = e1.getEventoId().getDate().getTime();
				Long millisecondE2 = e2.getEventoId().getDate().getTime();

				return millisecondE1.compareTo(millisecondE2);
			}
		});

		
		return attivitaStudioList;
			

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// METODI POST ////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param atDiStudio
	 * @return Aggiunta di un'attività di studio evento_id = -2
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/rest/attivitadistudio/add")
	public @ResponseBody
	boolean saveAttivitaStudio(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@RequestBody Evento atDiStudio)

	throws IOException {
		try {

			logger.info("/rest/attivitadistudio/add");

			// controllo se i campi sono validi
			if (atDiStudio != null && atDiStudio.getTitle() != "") {
				String token = getToken(request);
				BasicProfileService service = new BasicProfileService(
						profileaddress);
				BasicProfile profile = service.getBasicProfile(token);
				Long userId = Long.valueOf(profile.getUserId());

				atDiStudio.getEventoId().setIdStudente(userId);
				atDiStudio.getEventoId().setIdEventAd(-2);

				// ottengo i membri che fanno parte del gruppo di studio
				// relativo all'attività di studio
				GruppoDiStudio gruppoRefersAttivita = gruppstudioRepository
						.findOne(atDiStudio.getGruppo().getId());

				// controllo se lo studente che manda la richiesta fa parte del
				// gruppo associato all'attività e se il gruppo esiste
				if (gruppoRefersAttivita == null
						|| !gruppoRefersAttivita.isContainsStudente(userId))
					return false;

				List<String> users = new ArrayList<String>();
				List<String> idsInvited = gruppoRefersAttivita
						.getListInvited(userId);

				// aggiungo gli user a cui deve essere inviata l'avviso (tranne
				// chi ha effettuato la richiesta)
				for (String id : idsInvited) {
					users.add(id);
				}
				
				// salvo l'attività di studio
				Evento attivitaSaved = eventoRepository.save(atDiStudio);

				if (attivitaSaved == null)
					return false;
				else
					return true;
			} else
				return false;

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param date
	 * @param from
	 * @param to
	 * @param attivitadistudio
	 * @return modifica di un'attività di studio (soltanto chi fa parte del
	 *         gruppo può modificarla) evento_id = -2
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/rest/attivitadistudio/change/date/{date}/from/{from}/to/{to}")
	public @ResponseBody
	boolean changeAttivitaDiStudio(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("date") long date, @PathVariable("from") long from,
			@PathVariable("to") long to, @RequestBody Evento attivitadistudio)

	throws IOException {
		try {
			logger.info("/rest/attivitadistudio/change");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			attivitadistudio.getEventoId().setIdEventAd(-2);
			attivitadistudio.getEventoId().setIdStudente(userId);

			// Studente studente = studenteRepository.findOne(userId);

			// ottengo i membri che fanno parte del gruppo di studio relativo
			// all'attività di studio
			GruppoDiStudio gruppoRefersAttivita = gruppstudioRepository
					.findOne(attivitadistudio.getGruppo().getId());

			// controllo se lo studente che manda la richiesta fa parte del
			// gruppo associato all'attività e se il gruppo esiste
			if (gruppoRefersAttivita == null
					|| !gruppoRefersAttivita.isContainsStudente(userId))
				return false;

			List<String> users = new ArrayList<String>();
			List<String> idsInvited = gruppoRefersAttivita
					.getListInvited(userId);

			// controllo se lo studente che manda la richiesta fa parte del
			// gruppo associato all'attività e se il gruppo esiste
			for (String id : idsInvited) {
				users.add(id);
			}

			// controllo se campi validi
			if (attivitadistudio != null && attivitadistudio.getTitle() != ""
					&& attivitadistudio.getEventoId().getIdStudente() != -1) {

				EventoId eventoToChange = new EventoId();
				eventoToChange.setIdEventAd(-2);
				eventoToChange.setDate(new Date(date));
				eventoToChange.setStart(new Time(from));
				eventoToChange.setStop(new Time(to));
				eventoToChange.setIdStudente(userId);

				List<Evento> eventoToDelete = eventoRepository.selectEventsGdsOfStudent(
						gruppoRefersAttivita, userId);

				for (Evento evento : eventoToDelete) {
					long roundDate = 10000 * (evento.getEventoId().getDate()
							.getTime() / 10000);
					date = 10000 * (date / 10000);
					if (evento.getEventoId().getIdEventAd() == -2
							&& roundDate == date
							&& evento.getEventoId().getStart().getTime() == from
							&& evento.getEventoId().getStop().getTime() == to) {

						eventoRepository.delete(evento);

					}
				}

				// salvo l'evento nel db
				Evento attivitadistudioDB = eventoRepository.save(attivitadistudio);

				if (attivitadistudioDB != null)
					return true;
				else
					return false;

			} else
				return false;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

	// /////////////////////////////////////////////////////////////////////////
	// METODI DELETE //////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param gruppodistudio
	 * @return true se l'operazione è andata a buon fine
	 * @throws IOExceptionù
	 * 
	 *             Elimina un'attività di studio
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/rest/attivitadistudio/delete")
	public @ResponseBody
	boolean deleteAttivita(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@RequestBody Evento attivitadistudio)

	throws IOException {
		try {
			logger.info("/rest/attivitadistudio/delete");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			attivitadistudio.getEventoId().setIdEventAd(-2);
			attivitadistudio.getEventoId().setIdStudente(userId);

			// Studente studente = studenteRepository.findOne(userId);

			if (userId == null)
				return false;

			// ottengo i membri che fanno parte del gruppo di studio relativo
			// all'attività di studio
			GruppoDiStudio gruppoRefersAttivita = gruppstudioRepository
					.findOne(attivitadistudio.getGruppo().getId());

			// controllo se lo studente che manda la richiesta fa parte del
			// gruppo associato all'attività e se il gruppo esiste
			if (gruppoRefersAttivita == null
					|| !gruppoRefersAttivita.isContainsStudente(userId))
				return false;

			List<String> users = new ArrayList<String>();
			List<String> idsInvited = gruppoRefersAttivita
					.getListInvited(userId);

			// controllo se lo studente che manda la richiesta fa parte del
			// gruppo associato all'attività e se il gruppo esiste
			for (String id : idsInvited) {
				users.add(id);
			}

			// se ci sono notifiche da mandare
			if (users.size() > 0) {

				CommunicatorConnector communicatorConnector = new CommunicatorConnector(
						communicatoraddress, appName);

				Notification n = new Notification();
				n.setTitle(attivitadistudio.getTitle());
				NotificationAuthor nAuthor = new NotificationAuthor();
				nAuthor.setAppId(appName);
				nAuthor.setUserId(userId.toString());
				n.setAuthor(nAuthor);
				n.setUser(userId.toString());
				n.setTimestamp(System.currentTimeMillis());
				n.setDescription("L'attività " + attivitadistudio.getTitle()
						+ " è stata eliminata da " + profile.getName() + " "
						+ profile.getSurname());
				Map<String, Object> mapAttivita = new HashMap<String, Object>();
				mapAttivita.put("AttivitaDiStudio", attivitadistudio); // passo
																		// come
																		// contenuto
																		// della
																		// notifica
																		// l'hashmap
																		// con
																		// l'attivita
				n.setContent(mapAttivita);

				// ottengo il client token
				EasyTokenManger tManager = new EasyTokenManger(CLIENT_ID,
						CLIENT_SECRET, profileaddress);

				// communicatorConnector.sendAppNotification(n, appName, users,
				// tManager.getClientSmartCampusToken());
			}

			Evento AttivitaFromDB = eventoRepository.findOne(attivitadistudio
					.getEventoId());

			if (AttivitaFromDB == null)
				return false;

			eventoRepository.delete(AttivitaFromDB);

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
	 * @return String
	 * 
	 *         Ottiene il token
	 * 
	 */
	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

}
