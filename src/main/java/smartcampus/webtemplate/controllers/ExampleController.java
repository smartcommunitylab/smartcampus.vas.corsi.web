package smartcampus.webtemplate.controllers;

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.Position;
import it.sayservice.platform.smartplanner.data.message.RType;
import it.sayservice.platform.smartplanner.data.message.TType;
import it.sayservice.platform.smartplanner.data.message.journey.SingleJourney;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import eu.trentorise.smartcampus.communicator.CommunicatorConnector;
import eu.trentorise.smartcampus.communicator.CommunicatorConnectorException;
import eu.trentorise.smartcampus.communicator.model.Notification;
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

@Controller("exampleController")
public class ExampleController
{

	private static final String EVENT_OBJECT = "eu.trentorise.smartcampus.dt.model.EventObject";
	private static final Logger logger = Logger.getLogger(ExampleController.class);
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
	@RequestMapping(method = RequestMethod.GET, value = "/getprofile")
	public @ResponseBody
	
	BasicProfile getProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			
			return profile;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Request all the routes for Trentino Trasporti (agencyId = "12")
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getroutes")
	public @ResponseBody
	
	List<Route> getRoutes(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			JourneyPlannerConnector journeyPlannerConnector = new JourneyPlannerConnector(serverAddress);
			List<Route> routes = journeyPlannerConnector.getRoutes("12", token);
			
			return routes;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Plan a single journey
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/plansinglejourney")
	public @ResponseBody
	
	List<Itinerary> planSingleJourney(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			JourneyPlannerConnector journeyPlannerConnector = new JourneyPlannerConnector(serverAddress);
			
			SingleJourney req = new SingleJourney();
			req.setDate("03/28/2013");
			req.setDepartureTime("10:25");
			
			Position from = new Position();
			from.setLat("46.062005");
			from.setLon("11.129169");
			
			Position to = new Position();
			to.setLat("46.068854");
			to.setLon("11.151184");
			
			req.setFrom(from);
			req.setTo(to);
			
			TType[] tt = new TType[] { TType.TRANSIT };
			req.setTransportTypes(tt);
			req.setResultsNumber(1);
			req.setRouteType(RType.fastest);

			List<Itinerary> itineraries = journeyPlannerConnector.planSingleJourney(req, token);
			return itineraries;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Get all the events whose category is "Concerts"
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getconcerts")
	public @ResponseBody
	
	List<EventObject> getConterts(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			DiscoverTrentoConnector discoverTrentoConnector = new DiscoverTrentoConnector(serverAddress);
			
			ObjectFilter filter = new ObjectFilter();		
			filter.setClassName(EVENT_OBJECT);
			filter.setType("Concerts");
			filter.setFromTime(System.currentTimeMillis());
			
			Map<String, List<?>> result = discoverTrentoConnector.getObjects(filter, token);

			@SuppressWarnings("unchecked")
			List<EventObject> list = (List<EventObject>) result.get(EVENT_OBJECT);
			return list;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Get all notifications
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getnotifications")
	public @ResponseBody
	
	List<Notification> getNotifications(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	
	throws IOException
	{
		try
		{
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			CommunicatorConnector communicatorConnector = null;

			List<Notification> result = communicatorConnector.getNotifications(0L, 0, -1, token);
			
			return (List<Notification>) result;
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Example to get all storage application accounts binded to a specific
	 * application. Example uses tutorial application account named 'hackathon'.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/appaccount")
	public @ResponseBody
	
	List<AppAccount> getAppAccounts(HttpServletRequest request)
	
	throws FilestorageException
	{
		Filestorage filestorage = new Filestorage(serverAddress, "hackathon");
		return filestorage.getAppAccounts();
	}

	/*
	 * Example to get all storage user accounts owned by the authenticated user
	 * binded to an application . Example uses tutorial application account
	 * named 'hackathon'
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/useraccount")
	public @ResponseBody
	
	List<UserAccount> getuserAccounts(HttpServletRequest request)
	
	throws FilestorageException
	{
		String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
		Filestorage filestorage = new Filestorage(serverAddress, "hackathon");

		return filestorage.getUserAccounts(token);
	}

	/*
	 * Example to get the information about a stored resource. Example uses the
	 * tutorial application account named 'hackathon' and a default resource
	 * already stored (resourceId = 513da746975aa4412a383769
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/metadata")
	public @ResponseBody
	
	Metadata getResourceMetadata(HttpServletRequest request)
	
	throws FilestorageException
	{
		String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
		Filestorage filestorage = new Filestorage(serverAddress, "hackathon");
		
		return filestorage.getResourceMetadata(token, "513da746975aa4412a383769");
	}

	/*
	 * Example to get some social information about a user. Example shows how
	 * retrieve the group created by the user
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/group")
	public @ResponseBody
	
	List<Group> getUsergroups(HttpServletRequest request)
	
	throws SecurityException, SocialServiceException
	{
		String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
		SocialService socialsrv = new SocialService(serverAddress);
		
		return socialsrv.getGroups(token);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/gettime")
	public @ResponseBody
	
	String getTime(HttpServletRequest request, HttpServletResponse response,HttpSession session) 
	throws IOException
	{
		return "it works";
	}

}
