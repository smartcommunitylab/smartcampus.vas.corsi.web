package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.Notification;
import eu.trentorise.smartcampus.corsi.model.SyncData;
import eu.trentorise.smartcampus.corsi.repository.CorsoCarrieraRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.corsi.util.EasyTokenManger;
import eu.trentorise.smartcampus.network.JsonUtils;
import eu.trentorise.smartcampus.network.RemoteConnector;

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
	 * @param type
	 * @param date_from
	 * @return restituisce una lista di notifiche ottenuta dal communicator filtrata da type e dalla data di inizio
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/notifiche/type/{type}/date/{date_from}")
	public @ResponseBody
	List<Notification> getInfoStudentFromId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("type") String type,
			@PathVariable("date_from") long date_from)

	throws IOException {
		try {

			logger.info("/notifiche/type/{type}/date/{date_from}");

			EasyTokenManger clientTokenManager = new EasyTokenManger(
					profileaddress, client_id, client_secret);
			String client_auth_token = clientTokenManager
					.getClientSmartCampusToken();

			String json = RemoteConnector.getJSON(communicatoraddress,
					"synctype?since=0&type=" + type, client_auth_token);

			SyncData notificheSync = JsonUtils.toObject(json, SyncData.class);

			Set<String> setKeys = notificheSync.getUpdated().keySet();
			Iterator<String> iteratorKeys = setKeys.iterator();

			List<Notification> notificationsList = null;
			List<Notification> notificationsListAll = new ArrayList<Notification>();

			while (iteratorKeys.hasNext()) {
				String key = iteratorKeys.next();

				notificationsList = notificheSync.getUpdated().get(key);

				Iterator<Notification> iteratorNotification = notificationsList
						.iterator();
				Notification notification = null;
				while (iteratorNotification.hasNext()) {
					notification = iteratorNotification.next();
					if (notification.getTimestamp() < date_from) {
						iteratorNotification.remove();
					}
				}

				notificationsListAll.addAll(notificationsList);

			}

			Collections.sort(notificationsListAll,
					new Comparator<Notification>() {
						public int compare(Notification e1, Notification e2) {

							Long millisecondE1 = e1.getTimestamp();
							Long millisecondE2 = e2.getTimestamp();

							return millisecondE2.compareTo(millisecondE1);
						}
					});

			return notificationsListAll;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
