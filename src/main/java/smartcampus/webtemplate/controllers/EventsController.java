package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.Event;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("corsiController")
public class EventsController extends SCController {

	
	private static final String EVENT_OBJECT = "eu.trentorise.smartcampus.dt.model.EventObject";
	private static final Logger logger = Logger.getLogger(CorsiController.class);
	
	
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
	 *   Ritorna tutti gli eventi della settimana dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/events/week/{id_course}")
	public @ResponseBody
	
	List<Event> getEventsWeekByCourse(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("id_course") String id_course)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
						
			ArrayList<Event> list = new ArrayList<Event>();
			
			Event c = new Event();
			c.setTitle("Analisi 1 lezione teorica");
			c.setLocation("Polo Ferrari, Povo");
			c.setRoom("A103");
			c.setId_event("1");
			c.setIdCourse(id_course);
			list.add(c);	
			
			c = new Event();
			c.setTitle("Analisi 1 lezione laboratorio");
			c.setLocation("Polo Ferrari, Povo");
			c.setRoom("A105");
			c.setId_event("1");
			c.setIdCourse(id_course);
			list.add(c);	
				
			
			return list;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	/*
	 *   Ritorna tutti gli eventi del mese dato l'id di un corso un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/events/month/{id_course}")
	public @ResponseBody
	
	List<Event> getEventsMonthByCourse(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("id_course") String id_course)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			ArrayList<Event> list = new ArrayList<Event>();
			
			Event c = new Event();
			c.setTitle("Analisi 1 lezione teorica");
			c.setLocation("Polo Ferrari, Povo");
			c.setRoom("A103");
			c.setId_event("1");
			c.setIdCourse(id_course);
			list.add(c);	
			
			c = new Event();
			c.setTitle("Analisi 1 lezione laboratorio");
			c.setLocation("Polo Ferrari, Povo");
			c.setRoom("A105");
			c.setId_event("1");
			c.setIdCourse(id_course);
			list.add(c);	
				
			
			return list;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	/*
	 *   Ritorna gli eventi di una determinata data, di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/events/{date}/{id_course}")
	public @ResponseBody
	
	List<Event> getEvents(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("date") Date date, @PathVariable("id_course") String id_course)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
						
			ArrayList<Event> list = new ArrayList<Event>();
			
			Event c = new Event();
			c.setTitle("Analisi 1 lezione teorica");
			c.setLocation("Polo Ferrari, Povo");
			c.setRoom("A103");
			c.setId_event("1");
			c.setIdCourse(id_course);
			list.add(c);	
			
			c = new Event();
			c.setTitle("Analisi 1 lezione laboratorio");
			c.setLocation("Polo Ferrari, Povo");
			c.setRoom("A105");
			c.setId_event("1");
			c.setIdCourse(id_course);
			list.add(c);	
				
			
			return list;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	/*
	 *   Ritorna gli eventi settimanali di uno studente
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/events/{id_student}/week")
	public @ResponseBody
	
	List<Event> getEventsWeekByIdStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("id_student") String id_student)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
						
			ArrayList<Event> list = new ArrayList<Event>();
			
			Event c = new Event();
			c.setTitle("Analisi 1 lezione teorica");
			c.setLocation("Polo Ferrari, Povo");
			c.setRoom("A103");
			c.setId_event("1");
			c.setIdCourse("12938");
			list.add(c);	
			
			c = new Event();
			c.setTitle("Analisi 1 lezione laboratorio");
			c.setLocation("Polo Ferrari, Povo");
			c.setRoom("A105");
			c.setId_event("2");
			c.setIdCourse("12938");
			list.add(c);	
				
			
			return list;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	
	/*
	 *   Ritorna gli eventi mensili di uno studente
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/events/{id_student}/month")
	public @ResponseBody
	
	List<Event> getEventsMonthByIdStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("id_student") String id_student)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
						
			ArrayList<Event> list = new ArrayList<Event>();
			
			Event c = new Event();
			c.setTitle("Analisi 1 lezione teorica");
			c.setLocation("Polo Ferrari, Povo");
			c.setRoom("A103");
			c.setId_event("1");
			c.setIdCourse("12938");
			list.add(c);	
			
			c = new Event();
			c.setTitle("Analisi 1 lezione laboratorio");
			c.setLocation("Polo Ferrari, Povo");
			c.setRoom("A105");
			c.setId_event("2");
			c.setIdCourse("12938");
			list.add(c);	
				
			
			return list;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	
}
