package smartcampus.webtemplate.controllers;

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.Position;
import it.sayservice.platform.smartplanner.data.message.RType;
import it.sayservice.platform.smartplanner.data.message.TType;
import it.sayservice.platform.smartplanner.data.message.journey.SingleJourney;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.cxf.jaxrs.client.WebClient;
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
import eu.trentorise.smartcampus.communicator.CommunicatorConnector;
import eu.trentorise.smartcampus.communicator.CommunicatorConnectorException;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.communicator.model.NotificationAuthor;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.CorsoLite;
import eu.trentorise.smartcampus.corsi.model.Risorsa;
import eu.trentorise.smartcampus.corsi.model.RisorsaPhl;
import eu.trentorise.smartcampus.corsi.model.UtenteCorsi;
import eu.trentorise.smartcampus.corsi.repository.CorsoLiteRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.RisorsaPhlRepository;
import eu.trentorise.smartcampus.discovertrento.DiscoverTrentoConnector;
import eu.trentorise.smartcampus.dt.model.EventObject;
import eu.trentorise.smartcampus.dt.model.ObjectFilter;
import eu.trentorise.smartcampus.filestorage.client.Filestorage;
import eu.trentorise.smartcampus.filestorage.client.FilestorageException;
import eu.trentorise.smartcampus.filestorage.client.model.AppAccount;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.UserAccount;
import eu.trentorise.smartcampus.journeyplanner.JourneyPlannerConnector;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.socialservice.SocialService;
import eu.trentorise.smartcampus.socialservice.SocialServiceException;
import eu.trentorise.smartcampus.socialservice.model.Group;


@Controller("risorsaController")
public class RisorseController extends SCController
{
	
	private static final Logger logger = Logger.getLogger(RisorseController.class);
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
	@Value("${phl.url}")
	private String phlUrl;
	
	
	@Autowired
	private RisorsaPhlRepository risorsaPhlRepository;
	
	@Autowired
	private CorsoRepository corsoRepository;
	
	
	/*
	 *   Ritorna tutti i corsi in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/risorsa/all")
	public @ResponseBody
	
	List<Risorsa> getRisorsaAll(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			
		
			
			
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	/*
	 *   Ritorna tutti i corsi in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/risorsa/{idcorso}")
	public @ResponseBody
	
	List<Risorsa> getRisorsaByCorsoId(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("idcorso") String idcorso)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			
			List<Risorsa> risorsaReturn=new ArrayList<Risorsa>();
			
			Corso corsoRicerca=corsoRepository.findOne(Long.valueOf(idcorso));
			
			RisorsaPhl risorsaPhl=risorsaPhlRepository.getRisorsaPhlByCorsoId(corsoRicerca);
			
			WebClient client = WebClient.create(phlUrl);
			List<Risorsa> listRisorsaPhl = (List<Risorsa>) client.path("engine?cmd=open&target="+risorsaPhl.getIdRisorsaElFinder()).accept("text/xml").getCollection(Risorsa.class);
			
			//TODO Aggiungere ricerca su moodle e mettere in unica lista
			
			
			risorsaReturn.addAll(listRisorsaPhl);
			
			return risorsaReturn;
			
			
		
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	
	
	
}
