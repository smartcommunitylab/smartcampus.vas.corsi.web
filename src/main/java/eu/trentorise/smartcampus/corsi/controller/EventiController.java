package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.communicator.CommunicatorConnector;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CorsoCarrieraRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.corsi.util.EasyTokenManger;
import eu.trentorise.smartcampus.corsi.util.EventoMapper;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.UniversityPlannerService;
import eu.trentorise.smartcampus.unidataservice.model.CalendarCdsData;

@Controller("eventiController")
public class EventiController {
	private static final Logger logger = Logger
			.getLogger(EventiController.class);

	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${communicator.address}")
	private String communicatoraddress;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private EventoRepository eventoRepository;
	
	@Autowired
	private CorsoCarrieraRepository corsoCarrieraRepository;

	@Autowired
	@Value("${url.studente.service}")
	private String unidataaddress;

	// client id studymate
	@Autowired
	@Value("${studymate.client.id}")
	private String client_id;

	// client secret studymate
	@Autowired
	@Value("${studymate.client.secret}")
	private String client_secret;

	String client_auth_token;

	
		
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param idcorso
	 * @return List<Evento>
	 * @throws IOException
	 * 
	 *             Restituisce tutti gli eventi riferiti ad un corso dato
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/evento/corsolaurea/{id_cds}")
	public @ResponseBody
	List<Evento> getEventoByCorsoLaurea(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_cds") Long id_cds)

	throws IOException {
		try {
			logger.info("/eventi/{id_cds}");

			if (id_cds == null)
				return null;

			CorsoLaurea corso = corsoLaureaRepository.findOne(Long
					.valueOf(id_cds));

			return eventoRepository.findEventoByCds(corso.getCdsId());

		} catch (Exception e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Riceve evento e lo salva nel db
	 */

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param evento
	 * @return Evento
	 * @throws IOException
	 * 
	 *             Salva nel DB l'evento passato dal client e restituisce
	 *             l'evento se l'operazione va a buon fine, altrimenti false
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/evento")
	public @ResponseBody
	Evento saveEvento(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestBody Evento evento)

	throws IOException {
		try {

			// TODO controlli se campi validi
			if (evento != null && evento.getTitle() != "") {

				String token = getToken(request);
				BasicProfileService service = new BasicProfileService(
						profileaddress);
				BasicProfile profile = service.getBasicProfile(token);
				Long userId = Long.valueOf(profile.getUserId());

				CommunicatorConnector communicatorConnector = new CommunicatorConnector(
						communicatoraddress, appName);

				List<String> users = new ArrayList<String>();
				users.add(userId.toString());

				Notification n = new Notification();
				n.setTitle(evento.getTitle());
				n.setUser(userId.toString());
				n.setTimestamp(System.currentTimeMillis());
				n.setDescription("Creazione Evento");

				// communicatorConnector.sendAppNotification(n, appName, users,
				// token);

				return eventoRepository.save(evento);
			} else
				return null;

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return List<Evento>
	 * @throws IOException
	 * 
	 *             Restituisce tutti gli eventi di tutti i corsi personali
	 *             riferiti allo studente
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/evento/me")
	public @ResponseBody
	List<Evento> getEventoByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/evento/me");

			String token = getToken(request);
			
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studente = studenteRepository.findStudenteByUserId(userId);
			
			List<CorsoCarriera> corsiCarrieraList = corsoCarrieraRepository.findCorsoCarrieraByStudenteId(studente.getId());
			logger.info("/evento/me --> corsiCarrieraList from db: size = "+corsiCarrieraList.size());
			List<Evento> listEventi = new ArrayList<Evento>();
			
			// filtro gli eventi che interessano allo studente
			for (CorsoCarriera corsoCarriera : corsiCarrieraList) {
//				if(corsoCarriera.getResult().equals("0")){
				List<Evento> eventiAd = new ArrayList<Evento>();
					eventiAd = eventoRepository.findEventoByAd(corsoCarriera.getName());
					
					for (Evento evento : eventiAd) {
						
//						if(!(evento.getYearCds() > Integer.parseInt(studente.getAcademicYear()))){
//							eventiAd.remove(evento);
//						}
						
						listEventi.add(evento);
					}
					
//				}
			}
			
			Collections.sort(listEventi, new Comparator<Evento>() {
				  public int compare(Evento e1, Evento e2) {
				      if (e1.getEventoId().getDate() == null || e2.getEventoId().getDate() == null)
				        return 0;
				      
				      Long millisecondE1 = e1.getEventoId().getDate().getTime();
				      Long millisecondE2 = e2.getEventoId().getDate().getTime();
				      
				      return millisecondE1.compareTo(millisecondE2);
				  }
				});
	
			


			// for (CorsoCarriera index : studente.getCorsi()) {
			//
			// eventiListByCorso.addAll(eventoRepository
			// .findEventoByCorso(index));
			//
			// }

			return listEventi;
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
	 * @return List<Evento>
	 * @throws IOException
	 * 
	 *             Sincronizza gli eventi di un cds di un determinato anno
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sync/evento/{cds}/{year}")
	public @ResponseBody
	List<Evento> getSyncEventoByCdsAndYear(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("cds_id") CorsoLaurea cds_id,
			@PathVariable("year") String year)

	throws IOException {
		try {
			logger.info("sync/evento/{cds}/{year}");

			EasyTokenManger clientTokenManager = new EasyTokenManger(
					profileaddress, client_id, client_secret);
			 client_auth_token =
			 clientTokenManager.getClientSmartCampusToken();
			//client_auth_token = "6a7e5dfc-af50-4c2c-a632-dfd7e8210c59";
			System.out.println("Client auth token: " + client_auth_token);

			UniversityPlannerService uniplanner = new UniversityPlannerService(
					unidataaddress);

			List<CalendarCdsData> dataCalendarOfWeek = uniplanner
					.getCdsCalendar(client_auth_token, String.valueOf(cds_id),
							year);

			List<Evento> eventsMapped = null;

			if (dataCalendarOfWeek != null) {
				EventoMapper mapperEvento = new EventoMapper();
				eventsMapped = mapperEvento.convert(dataCalendarOfWeek, cds_id,
						Integer.parseInt(year));
			}

			eventsMapped = eventoRepository.save(eventsMapped);

			return eventsMapped;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return List<Evento>
	 * @throws IOException
	 * 
	 *             Sincronizza gli eventi di un cds
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sync/evento/{cds}/all")
	public @ResponseBody
	List<Evento> getSyncEventoByCdsAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("cds") long cds)

	throws IOException {
		try {
			logger.info("/sync/evento/{cds}/all");

			UniversityPlannerService uniConnector = new UniversityPlannerService(
					unidataaddress);

			EasyTokenManger clientTokenManager = new EasyTokenManger(
					profileaddress, client_id, client_secret);
			 client_auth_token =
			 clientTokenManager.getClientSmartCampusToken();
			//client_auth_token = "6a7e5dfc-af50-4c2c-a632-dfd7e8210c59";
			System.out.println("Client auth token: " + client_auth_token);


			List<Evento> eventsListTotal = new ArrayList<Evento>();
			
			CorsoLaurea cdLaurea = corsoLaureaRepository.findOne(cds);
			logger.info("/sync/evento/{cds}/all: corso laurea = "+cdLaurea.getDescripion());
			
			for (int y = 1; y <= Integer.parseInt(cdLaurea.getDurata()); y++) { 
				logger.info("/sync/evento/{cds}/all: corso laurea = "+cdLaurea.getDescripion()+" year = "+y);
				List<CalendarCdsData> dataCalendarOfWeek = uniConnector
						.getCdsCalendar(client_auth_token,
								String.valueOf(cdLaurea.getCdsId()),
								String.valueOf(y));

				EventoMapper mapperEvento = new EventoMapper();
				List<Evento> eventsMapped = mapperEvento.convert(dataCalendarOfWeek, cdLaurea, y);
				
				eventoRepository.save(eventsMapped);
				
				for (Evento evento : eventsMapped) {
					eventsListTotal.add(evento);
				}
				
				
			}

			
			
			return eventsListTotal;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return List<Evento>
	 * @throws IOException
	 * 
	 *             Sincronizza gli eventi di di tutti i gds
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sync/evento/all")
	public @ResponseBody
	boolean getSyncEventoByAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/sync/evento/all");

			UniversityPlannerService uniConnector = new UniversityPlannerService(
					unidataaddress);

			EasyTokenManger clientTokenManager = new EasyTokenManger(
					profileaddress, client_id, client_secret);
			 client_auth_token =
			 clientTokenManager.getClientSmartCampusToken();
			client_auth_token = "c39fce2d-177a-4489-898b-c0a6924191f5";
			System.out.println("Client auth token: " + client_auth_token);

			List<Dipartimento> dipartimenti = dipartimentoRepository.findAll();

			logger.info("/sync/evento/all: list dipartimento size = "+dipartimenti.size());
			if (dipartimenti.size() == 0)
				return false;
			
			List<Evento> eventsMapped = null;
			List<CorsoLaurea> corsiDiLaurea = null;

			for (Dipartimento dip : dipartimenti) {

				corsiDiLaurea = new ArrayList<CorsoLaurea>();

				corsiDiLaurea = corsoLaureaRepository.getCorsiLaureaByDipartimento(dip);
				logger.info("/sync/evento/all: list corsi laurea size = "+corsiDiLaurea.size());

				for (CorsoLaurea cl : corsiDiLaurea) { // per tutti i corsi di
														// laurea
					for (int year = 1; year <= Integer.parseInt(cl.getDurata()); year++) { // per tutti gli anni
						logger.info("/sync/evento/all: corsi laurea year = "+year);
						List<CalendarCdsData> dataCalendarOfWeek = uniConnector
								.getCdsCalendar(client_auth_token,
										String.valueOf(cl.getCdsId()),
										String.valueOf(year));

						EventoMapper mapperEvento = new EventoMapper();
						eventsMapped = mapperEvento.convert(dataCalendarOfWeek, cl, year);
						
						eventoRepository.save(eventsMapped);
					}
				}

			}
			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return false;
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

}
