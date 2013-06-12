package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.CorsoLite;
import eu.trentorise.smartcampus.corsi.repository.CorsoLiteRepository;


@Controller("corsiLiteController")
public class CorsiLiteController extends SCController
{
	
	//private static final Logger logger = Logger.getLogger(CorsiLiteController.class);
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
	private CorsoLiteRepository corsoLiteRepository;
	
	
	/*
	 *   Ritorna tutti i corsi in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsilite/all")
	public @ResponseBody
	
	List<CorsoLite> getCorsiAll(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
			
			
			//TEST
			CorsoLite c = new CorsoLite();
		
			c.setNome("Fisica dei materiali");
			corsoLiteRepository.save(c);
			
			c = new CorsoLite();
		
			c.setNome("Analisi matematica 2");
			corsoLiteRepository.save(c);
			
			c = new CorsoLite();
	
			c.setNome("Lettere 1");
			corsoLiteRepository.save(c);
			
			//TEST
			
			return corsoLiteRepository.findAll();
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	/*
	 *   Ritorna tutti i corsi del corso di laurea in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsilite/laurea")
	public @ResponseBody
	
	List<CorsoLite> getCorsiLaurea(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
			
						
			ArrayList<CorsoLite> list = new ArrayList<CorsoLite>();
			
			CorsoLite c = new CorsoLite();
			c.setId(1);
			c.setNome("Analisi matematica 3");
			list.add(c);	
			
			c = new CorsoLite();
			c.setId(2);
			c.setNome("Chimica 2");
			list.add(c);	
			
			c = new CorsoLite();
			c.setId(3);
			c.setNome("Fisica 2");
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
	 * Ritorna tutti i corsi del dipartimento in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsilite/dipartimento")
	public @ResponseBody
	
	List<CorsoLite> getCorsiDipartimento(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
		
						
			ArrayList<CorsoLite> list = new ArrayList<CorsoLite>();
			
			CorsoLite c = new CorsoLite();
			c.setId(1);
			c.setNome("Analisi matematica 3");
			list.add(c);	
			
			c = new CorsoLite();
			c.setId(2);
			c.setNome("Analisi 2");
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
	 *   Ritorna tutti i corsi dal libretto dell'utente in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsilite/frequentati")
	public @ResponseBody
	
	List<CorsoLite> getCorsiLibrettoUtente(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
			
			
			ArrayList<CorsoLite> list = new ArrayList<CorsoLite>();
			
			CorsoLite c = new CorsoLite();
			c.setId(1);
			c.setNome("Analisi matematica 1");
			list.add(c);	
			
			c = new CorsoLite();
			c.setId(2);
			c.setNome("Matematica Discreta 1");
			list.add(c);	
			
			c = new CorsoLite();
			c.setId(3);
			c.setNome("Programmazione 2");
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
