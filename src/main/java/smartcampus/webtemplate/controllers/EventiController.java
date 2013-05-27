package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.Mongo;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.Evento;

import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;


@Controller("eventiController")
public class EventiController extends SCController
{
	private static final String EVENT_OBJECT = "eu.trentorise.smartcampus.dt.model.EventObject";
	private static final Logger logger = Logger.getLogger(EventiController.class);
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
	 *   Ritorna tutti gli eventi
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/eventi/{id}")
	public @ResponseBody
	
	List<Evento> getEventi(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("id") String id)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			
			String id_corso = request.getParameter("id");

						
			ArrayList<Evento> list = new ArrayList<Evento>();
			
			Evento e1 = new Evento();
			e1.setAll_day(true);
			e1.setTitolo("Analisi 1");
			e1.setDescrizione("Descrizione di prova");
			e1.setGuests_can_invite(false);
			
			list.add(e1);
						
			return list;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	/*
	 *   Riceve evento e lo salva nel db
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/eventi")
	public @ResponseBody
	
	Evento saveEvent(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestBody String json)
	
	throws IOException
	{
		try
		{	
		    ObjectMapper m = new ObjectMapper();
		    Evento evento = m.readValue(json, Evento.class);
		    
		    //MongoOperations mongoOps = new MongoTemplate(new Mongo(), "web-corsi-db");
			//mongoOps.insert(evento);
									
			return evento;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
}
