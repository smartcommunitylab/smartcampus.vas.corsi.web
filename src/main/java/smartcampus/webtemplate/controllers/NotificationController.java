package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("notificationController")
public class NotificationController {
	private static final String EVENT_OBJECT = "eu.trentorise.smartcampus.dt.model.EventObject";
	private static final Logger logger = Logger
			.getLogger(NotificationController.class);
	@Autowired
	private AcService acService;

	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${services.server}")
	private String serverAddress;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	/*
	 * Example to get the profile of the authenticated user.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/notifiche/get")
	public @ResponseBody
	List<Notification> getNotifications(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);

			ArrayList<Notification> list = new ArrayList<Notification>();

			Notification n = new Notification();
			n.setTitle("Lezione sospesa");
			n.setDescription("Le lezioni del corso di Logica (Serafini, Ghidini, Maltese) previste Marted� 30 Aprile sono sospese");
			list.add(n);

			n = new Notification();
			n.setTitle("Lezione spostata");
			n.setDescription("MATEMATICA DISCRETA 2 (prof. S. Baratella) : la lezione di Venerd� 3 Maggio, ore 10.30-12.30, si terr� in aula A102 anzich� A105");
			list.add(n);

			return list;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
}
