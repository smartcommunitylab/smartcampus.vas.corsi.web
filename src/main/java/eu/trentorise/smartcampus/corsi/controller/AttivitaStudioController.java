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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.communicator.CommunicatorConnector;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.corsi.model.AttivitaDiStudio;
import eu.trentorise.smartcampus.corsi.repository.AttivitaStudioRepository;
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
	private StudenteRepository studenteRepository;



//	@RequestMapping(method = RequestMethod.GET, value = "/attivitastudio/{id_gruppidistudio}")
//	public @ResponseBody
//	List<AttivitaDiStudio> getgruppidistudioByID(HttpServletRequest request,
//			HttpServletResponse response, HttpSession session,
//			@PathVariable("id_gruppidistudio") Long id_gruppidistudio)
//
//	throws IOException {
//		try {
//			logger.info("/attivitastudio/{id_gruppidistudio}");
//
//			if (id_gruppidistudio == null)
//				return null;
//
//			return  attivitastudioRepository.findAttByIdGds(id_gruppidistudio);
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//		}
//		return null;
//	}
//
//
//	private String getToken(HttpServletRequest request) {
//		return (String) SecurityContextHolder.getContext().getAuthentication()
//				.getPrincipal();
//	}
//	
//	
//	@RequestMapping(method = RequestMethod.POST, value = "/addgruppodistudio")
//	public @ResponseBody
//	AttivitaDiStudio saveAttivitaStudio(HttpServletRequest request, HttpServletResponse response,
//			HttpSession session, @RequestBody AttivitaDiStudio atDiStudio)
//
//	throws IOException {
//		try {
//
//			// User Request create event
//			// creati a notification and send to Communicator
//
//			// TODO controlli se campi validi
//			if (atDiStudio != null && atDiStudio.getTitolo() != "") {
//
//				String token = getToken(request);
//				BasicProfileService service = new BasicProfileService(
//						profileaddress);
//				BasicProfile profile = service.getBasicProfile(token);
//				Long userId = Long.valueOf(profile.getUserId());
//
//				CommunicatorConnector communicatorConnector = new CommunicatorConnector(
//						communicatoraddress, appName);
//
//				List<String> users = new ArrayList<String>();
//				users.add(userId.toString());
//
//				Notification n = new Notification();
//				n.setTitle(atDiStudio.getTitolo());
//				n.setUser(userId.toString());
//				n.setTimestamp(System.currentTimeMillis());
//				n.setDescription("Creazione Evento");
//
//				communicatorConnector.sendAppNotification(n, appName, users,
//						token);
//
//				return attivitastudioRepository.save(atDiStudio);
//			} else
//				return null;
//
//		} catch (Exception e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//		}
//		return null;
//	}

	
}
	
