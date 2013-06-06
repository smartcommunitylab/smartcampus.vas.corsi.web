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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.ac.provider.model.User;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.CorsiLite;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("corsiController")
public class CorsiController extends SCController {
	private static final String EVENT_OBJECT = "eu.trentorise.smartcampus.dt.model.EventObject";
	private static final Logger logger = Logger
			.getLogger(CorsiController.class);
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
	 * Ritorna tutti i corsi in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsi/all")
	public @ResponseBody
	List<CorsiLite> getCorsiAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);

			ArrayList<CorsiLite> list = new ArrayList<CorsiLite>();

			CorsiLite c = new CorsiLite();
			c.setId(1);
			c.setName("Fisica dei materiali");
			list.add(c);

			c = new CorsiLite();
			c.setId(1);
			c.setName("Analisi matematica 2");
			list.add(c);

			c = new CorsiLite();
			c.setId(1);
			c.setName("Lettere 1");
			list.add(c);

			return list;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna tutti i corsi della facolt� in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsi/facolta")
	public @ResponseBody
	List<CorsiLite> getCorsiFacolta(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);

			ArrayList<CorsiLite> list = new ArrayList<CorsiLite>();

			CorsiLite c = new CorsiLite();
			c.setId(1);
			c.setName("Analisi matematica 3");
			list.add(c);

			c = new CorsiLite();
			c.setId(1);
			c.setName("Chimica 2");
			list.add(c);

			c = new CorsiLite();
			c.setId(1);
			c.setName("Fisica 2");
			list.add(c);

			return list;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna tutti i corsi dal libretto dell'utente in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsi/frequentati")
	public @ResponseBody
	List<CorsiLite> getCorsiLibrettoUtente(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);

			User us = retrieveUser(request);

			ArrayList<CorsiLite> list = new ArrayList<CorsiLite>();

			CorsiLite c = new CorsiLite();
			c.setId(1);
			c.setName("Analisi matematica 1");
			list.add(c);

			c = new CorsiLite();
			c.setId(1);
			c.setName("Matematica Discreta 1");
			list.add(c);

			c = new CorsiLite();
			c.setId(1);
			c.setName("Programmazione 2");
			list.add(c);

			return list;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna il corso dato l'id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsi/{id}")
	public @ResponseBody
	List<CorsiLite> getCorsoByID(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id") String id)

	throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);

			ArrayList<CorsiLite> list = new ArrayList<CorsiLite>();

			String id_corso = request.getParameter("id");

			CorsiLite c = new CorsiLite();
			c.setId(1);
			c.setName("id vale " + id);
			list.add(c);

			return list;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
