package smartcampus.webtemplate.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.ac.provider.model.User;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.profileservice.model.ExtendedProfile;

@Controller("UtenteController")
public class UtenteController extends SCController {
	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${services.server}")
	private String serverAddress;

	@RequestMapping(method = RequestMethod.GET, value = "/getextendedprofile")
	public @ResponseBody
	ExtendedProfile getProfile(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {

			User user = retrieveUser(request);

			String appId = request.getParameter("appId");
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profileBase = profileConnector.getBasicProfile(token);
			if (profileBase != null) {
				ExtendedProfile profile = profileConnector.getExtendedProfile(
						user.getId().toString(), appId, profileBase.getUserId()
								.toString(), token);
				CorsiController cC = new CorsiController();
				// cC.getCorsiLibrettoUtente(request, response, session);
				// profile.setContent("corsi",
				// cC.getCorsiLibrettoUtente(request, response, session));
				return profile;
			} else {
				profileBase = new BasicProfile();
				ExtendedProfile profile = profileConnector.getExtendedProfile(
						user.getId().toString(), appId, profileBase.getUserId()
								.toString(), token);
				return profile;
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	// TODO utente si logga in app---> richiama metodo per avere profilo --->
	// se profilo non presente lo crei--> nel crearlo ci salvi dentro l'elenco
	// per ora fittizzio dei corsi--->
	// e lo restituisci al client
}
