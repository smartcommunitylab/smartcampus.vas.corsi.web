package smartcampus.webtemplate.controllers;

import java.io.IOException;
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
import eu.trentorise.smartcampus.corsi.model.Calendario;
import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CalendarioRepository;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;


@Controller("calendarioController")
public class CalendarioController {

	
	
private static final Logger logger = Logger.getLogger(CommentiController.class);
	
	
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
	
	
	@Autowired
	private CalendarioRepository calendarioRepository;
	
	
	
	
	/*
	 *   Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/calendario/all")
	public @ResponseBody
	
	List<Calendario> getAllCalendar(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(serverAddress);
		
			long userId=Long.valueOf(profileConnector.getBasicProfile(token).getUserId());
			
			
				//TEST
				Calendario c = new Calendario();
				Studente s = new Studente();
				Evento e = new Evento();
				
				s.setId(1);
				s.setNome("Manuel");
				s.setCognome("Visentin");
				s.setCalendario(c);
				
				e.setId(2);
				e.setTitolo("evento prova");
				
				c.setStudente(s);
				c.setEvento(e);
				calendarioRepository.save(c);
				
				//TEST
				
				return calendarioRepository.findAll();
			
			
			
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
}
