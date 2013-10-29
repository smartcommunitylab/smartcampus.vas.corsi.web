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

import eu.trentorise.smartcampus.communicator.CommunicatorConnector;
import eu.trentorise.smartcampus.communicator.model.EntityObject;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.communicator.model.NotificationAuthor;
import eu.trentorise.smartcampus.corsi.model.AttivitaDiStudio;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.AttivitaStudioRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.GruppoDiStudioRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("attivitaStudioController")
public class AttivitaStudioController {

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
	private AttivitaStudioRepository attivitastudioRepository;

	@Autowired
	private GruppoDiStudioRepository gruppstudioRepository;
	
	@Autowired
	private CorsoRepository corsoRepository;
	
	@Autowired
	private StudenteRepository studenteRepository;



	@RequestMapping(method = RequestMethod.POST, value = "/attivitadistudio/add")
	public @ResponseBody
	boolean saveAttivitaStudio(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestBody AttivitaDiStudio atDiStudio)

	throws IOException {
		try {

			logger.info("/attivitadistudio/add");

			// controllo se i campi sono validi	
			if (atDiStudio != null && atDiStudio.getTitolo() != "") {
				String token = getToken(request);
				BasicProfileService service = new BasicProfileService(
						profileaddress);
				BasicProfile profile = service.getBasicProfile(token);
				Long userId = Long.valueOf(profile.getUserId());
				
				
				// ottengo i membri che fanno parte del gruppo di studio relativo all'attività di studio
				GruppoDiStudio gruppoRefersAttivita = gruppstudioRepository.findOne(atDiStudio.getGruppo());
			
				
				// controllo se lo studente che manda la richiesta fa parte del gruppo associato all'attività e se il gruppo esiste
				if(gruppoRefersAttivita == null || gruppoRefersAttivita.isContainsStudente(userId))
					return false;
				

				List<String> users = new ArrayList<String>();
				List<String> idsInvited = gruppoRefersAttivita.getListInvited(userId);
				
				for(String id : idsInvited){
					users.add(id);
				}
				
//				CommunicatorConnector communicatorConnector = new CommunicatorConnector(
//						communicatoraddress, appName);
//
//				Notification n = new Notification();
//				n.setTitle(atDiStudio.getTitolo());
//				NotificationAuthor nAuthor = new NotificationAuthor();
//				nAuthor.setAppId(appName);
//				nAuthor.setUserId(userId.toString());
//				n.setAuthor(nAuthor);
//				n.setUser(userId.toString());
//				n.setTimestamp(System.currentTimeMillis());
//				n.setDescription("Nuova attività "+atDiStudio.getTitolo()+" creata da "+profile+" nel gruppo "+gruppoRefersAttivita.getNome());
//				Map<String, Object> mapAttivita = new HashMap<String, Object>();
//				mapAttivita.put("AttivitaDiStudio", atDiStudio); //passo come contenuto della notifica l'hashmap con l'attivita
//				n.setContent(mapAttivita);
//				
//				
//				communicatorConnector.sendAppNotification(n, appName, users,
//						getToken(request));
				
				
				AttivitaDiStudio attivitaSaved = attivitastudioRepository.save(atDiStudio);

				if(attivitaSaved == null)
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

	
	
	@RequestMapping(method = RequestMethod.GET, value = "/attivitadistudio/{id_gruppodistudio}")
	public @ResponseBody
	List<AttivitaDiStudio> getAttivitadistudioByID(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_attivitadistudio") Long id_attivitadistudio)

	throws IOException {
		try {
			logger.info("/attivitastudio/{id_gruppidistudio}");

			if (id_attivitadistudio == null)
				return null;

			return  attivitastudioRepository.findAttByIdGds(id_attivitadistudio);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/attivitadistudio/change")
	public @ResponseBody
	boolean changeAttivitaDiStudio(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_attivitadistudio") AttivitaDiStudio attivitadistudio)

	throws IOException {
		try {
			logger.info("/attivitadistudio/change");
			
			
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
						interesse = interesse.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					z++;
				}
				
				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);
				studente = studenteRepository.save(studente);

				// Set corsi superati
				studente.setIdsCorsiSuperati(supera);
				studente.setIdsCorsiInteresse(interesse);
				
				studente = studenteRepository.save(studente);
			}
			

			// ottengo i membri che fanno parte del gruppo di studio relativo all'attività di studio
			GruppoDiStudio gruppoRefersAttivita = gruppstudioRepository.findOne(attivitadistudio.getGruppo());
		
			
			// controllo se lo studente che manda la richiesta fa parte del gruppo associato all'attività e se il gruppo esiste
			if(gruppoRefersAttivita == null || gruppoRefersAttivita.isContainsStudente(userId))
				return false;			
			
			
			List<String> users = new ArrayList<String>();
			List<String> idsInvited = gruppoRefersAttivita.getListInvited(userId);
			
			for(String id : idsInvited){
				users.add(id);
			}
			
//			CommunicatorConnector communicatorConnector = new CommunicatorConnector(
//					communicatoraddress, appName);
//
//			Notification n = new Notification();
//			n.setTitle(attivitadistudio.getTitolo());
//			NotificationAuthor nAuthor = new NotificationAuthor();
//			nAuthor.setAppId(appName);
//			nAuthor.setUserId(userId.toString());
//			n.setAuthor(nAuthor);
//			n.setUser(userId.toString());
//			n.setTimestamp(System.currentTimeMillis());
//			n.setDescription("Nuova attività "+attivitadistudio.getTitolo()+" creata da "+profile+" nel gruppo "+gruppoRefersAttivita.getNome());
//			Map<String, Object> mapAttivita = new HashMap<String, Object>();
//			mapAttivita.put("AttivitaDiStudio", attivitadistudio); //passo come contenuto della notifica l'hashmap con l'attivita
//			n.setContent(mapAttivita);
//			
//			
//			communicatorConnector.sendAppNotification(n, appName, users,
//					getToken(request));
			
			
			
			AttivitaDiStudio attivitadistudioAggiornato = attivitastudioRepository.save(attivitadistudio);
			
			if (attivitadistudioAggiornato == null) {
				return false;
			} else {
				// controllo che il l'attivita aggiornata abbia lo stesso id di prima
				if(attivitadistudioAggiornato.getId() == attivitadistudio.getId())
					return true;
				else
					return false;
			}
			
			
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
	 * @return true se l'operazione è andata a buon fine
	 * @throws IOExceptionù
	 * 
	 * Elimina un'attività di studio
	 * 
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/attivitadistudio/delete")
	public @ResponseBody
	boolean deleteAttivita(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @RequestBody AttivitaDiStudio attivitadistudio)

	throws IOException {
		try {
			logger.info("/attivitadistudio/delete");
			
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
						interesse = interesse.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					z++;
				}
				
				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);
				studente = studenteRepository.save(studente);

				// Set corsi superati
				studente.setIdsCorsiSuperati(supera);
				studente.setIdsCorsiInteresse(interesse);
				
				studente = studenteRepository.save(studente);
			}
			
			
			if (userId == null)
				return false;
			
			
			// ottengo i membri che fanno parte del gruppo di studio relativo all'attività di studio
			GruppoDiStudio gruppoRefersAttivita = gruppstudioRepository.findOne(attivitadistudio.getGruppo());
		
			
			// controllo se lo studente che manda la richiesta fa parte del gruppo associato all'attività e se il gruppo esiste
			if(gruppoRefersAttivita == null || gruppoRefersAttivita.isContainsStudente(userId))
				return false;
			
			
			AttivitaDiStudio AttivitaFromDB = attivitastudioRepository.findOne(attivitadistudio.getId());

			attivitastudioRepository.delete(AttivitaFromDB);
			
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
	 * Ottiene il token
	 * 
	 */
	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}
	
	
	
}
	
