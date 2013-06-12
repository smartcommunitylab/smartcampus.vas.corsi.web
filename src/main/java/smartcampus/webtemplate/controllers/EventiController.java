package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.CorsoLite;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.repository.CorsoLiteRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;



@Controller("eventiController")
public class EventiController extends SCController
{
	private static final Logger logger = Logger
			.getLogger(EventiController.class);
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
	private EventoRepository eventoRepository;
	
	@Autowired
	private CorsoLiteRepository corsoLiteRepository;
	
	/*
	 *   Ritorna tutti gli eventi
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/eventi/{idcorso}")
	public @ResponseBody
	
	List<Evento> getEventi(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("idcorso") String idcorso)
	
	throws IOException
	{
		try
		{
		
			CorsoLite corso=corsoLiteRepository.findOne(Long.valueOf(idcorso));
			
			
			Evento e = new Evento();
			
			e.setAll_day(true);
			//e.setId(1);
			e.setTitolo("Analisi 1");
			e.setDescrizione("Descrizione di prova");
			e.setCorsoLite(corso);
			eventoRepository.save(e);
						
			return eventoRepository.findAll();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
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
