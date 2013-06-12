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
import eu.trentorise.smartcampus.communicator.model.Notification;

@Controller("notificationController")
public class NotificationController
{
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
	
	List<Notification> getNotifications(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
	
		
		
		//	CommunicatorConnector communicatorConnector = new CommunicatorConnector(
		//			serverAddress);// , appName);

			//communicatorConnector.sendAppNotification(n, appId, users, token);
			
			
			ArrayList<Notification> list = new ArrayList<Notification>();
			
			Notification n = new Notification();
			n.setTitle("Lezione sospesa");
			n.setUser("10");
			n.setTimestamp(System.currentTimeMillis());
			n.setDescription("Le lezione del corso di Logica (Serafini, Ghidini, Maltese) previste Martedi' 30 Aprile sono sospese");
			list.add(n);	
			
			n = new Notification();
			n.setTitle("Lezione spostata");
			n.setUser("20");
			n.setTimestamp(System.currentTimeMillis());
			n.setDescription("MATEMATICA DISCRETA 2 (prof. S. Baratella) : la lezione di Venerdi' 3 Maggio, ore 10.30-12.30, si terr� in aula A102 anzich� A105");
			list.add(n);
			
			n = new Notification();
			n.setTitle("Vogliamo piu lavoro sul client");
			n.setUser("30");
			n.setTimestamp(System.currentTimeMillis());
			n.setDescription("Ma che bella è l'UniTN?");
			list.add(n);
			
			
		
			
			
			return list;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
}
