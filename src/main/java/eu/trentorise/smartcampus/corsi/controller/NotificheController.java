package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import eu.trentorise.smartcampus.corsi.model.Notification;
import eu.trentorise.smartcampus.corsi.model.Notifiche;
import eu.trentorise.smartcampus.corsi.repository.CorsoCarrieraRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.corsi.util.EasyTokenManger;
import eu.trentorise.smartcampus.network.JsonUtils;
import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("notificheController")
public class NotificheController {

	private static final Logger logger = Logger
			.getLogger(NotificheController.class);

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;
	
	@Autowired
	private StudenteRepository studenteRepository;
	
	@Autowired
	private CorsoCarrieraRepository corsoCarrieraRepository;
	
	@Autowired
	@Value("${communicator.address}")
	private String communicatoraddress;
	
	// client id studymate
	@Autowired
	@Value("${studymate.client.id}")
	private String client_id;
	
	// client secret studymate
	@Autowired
	@Value("${studymate.client.secret}")
	private String client_secret;
	
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
	@RequestMapping(method = RequestMethod.GET, value = "/notifiche/{date_from}/me")
	public @ResponseBody
	List<Notification> getInfoStudentFromId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("date_from") long date_from)

	throws IOException {
		try {

			logger.info("/notifiche/{date_from}/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());
			
			EasyTokenManger clientTokenManager = new EasyTokenManger(profileaddress, client_id, client_secret);
			//String client_auth_token = clientTokenManager.getClientSmartCampusToken();
			
			String client_auth_token = "6f10bd36-fb44-49d0-8c41-4cb8fdfee732";//test locale
			
			String json = RemoteConnector.postJSON(communicatoraddress,
					"synctype?since=0&type=Cisca", JsonUtils.toJSON("{}"), client_auth_token);
			
			Notifiche notifiche = JsonUtils.toObject(json, Notifiche.class);
			
			List<Notification> notificheFiltrate = new ArrayList<Notification>();
			
			
 			for (Notification notification : notifiche.getUpdated().getNotifications()) {
				if((notification.getTimestamp() >= date_from) || (notification.getUpdateTime() >= date_from)){
					notificheFiltrate.add(notification);
				}
			}
			
			
			// stud.setCorsiSuperati(assignCorsi(stud));
			return notificheFiltrate;

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
