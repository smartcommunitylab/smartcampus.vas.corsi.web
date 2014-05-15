package eu.trentorise.smartcampus.corsi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.aac.AACService;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.resourceprovider.controller.SCController;
import eu.trentorise.smartcampus.resourceprovider.model.AuthServices;


@Controller
public class PortalController extends SCController {

	private static final String MODERATOR_SERVICE_ID = "smartcampus.moderation";
	private static final String MODERATOR_RESOURCE_PARAMETER_ID = "app";


	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	@Value("${profile.address}")
	private String aacURL;

	@Autowired
	@Value("${studymate.client.id}")
	private String client_id;

	@Autowired
	@Value("${studymate.client.secret}")
	private String client_secret;

	@Autowired
	private AuthServices services;

	protected AACService aacService;
	protected BasicProfileService profileService;
	
	private static final Logger logger = Logger.getLogger(PortalController.class);

	@PostConstruct
	public void init() {
		aacService = new AACService(aacURL, client_id, client_secret);
		profileService = new BasicProfileService(aacURL);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ModelAndView web(HttpServletRequest request) {

		return new ModelAndView("redirect:/web");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/web")
	public ModelAndView index(HttpServletRequest request) throws SecurityException, ProfileServiceException, AACException {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("token", getToken(request));
		//model.put("appsFromDb", getApps(request));
		model.put("aacExtURL", aacURL);
		BasicProfile user=profileService.getBasicProfile(getToken(request));
		model.put("user", user.getSurname()+","+user.getName());
		return new ModelAndView("index", model);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/check")
	public ModelAndView securePage(HttpServletRequest request,
			@RequestParam(required = false) String code)
			throws SecurityException, AACException {
		String redirectUri = getFullRequestPath(request) + "/check";		
		String userToken = aacService.exchngeCodeForToken(code, redirectUri)
				.getAccess_token();
		List<GrantedAuthority> list = Collections
				.<GrantedAuthority> singletonList(new SimpleGrantedAuthority(
						"ROLE_USER"));
		Authentication auth = new PreAuthenticatedAuthenticationToken(
				userToken, "", list);
		auth = authenticationManager.authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(auth);
		request.getSession()
				.setAttribute(
						HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
						SecurityContextHolder.getContext());
		request.getSession().setAttribute("token", userToken);
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/profiles")
	public @ResponseBody List<BasicProfile> getProfiles(HttpServletRequest request) throws SecurityException, ProfileServiceException {
		String token = getToken(request);
		return profileService.getBasicProfiles(null, token);
	} 

	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public ModelAndView secure(HttpServletRequest request) {
		String redirectUri = getFullRequestPath(request) + "/check";
		return new ModelAndView(
				"redirect:"
						+ aacService
								.generateAuthorizationURIForCodeFlow(
										redirectUri,
										null,
										"smartcampus.profile.basicprofile.me,smartcampus.profile.accountprofile.me,smartcampus.profile.basicprofile.all",
										null));
	}

	@Override
	protected AuthServices getAuthServices() {
		return services;
	}

	protected String getToken(HttpServletRequest request) {

		// String fromCtx = (String)
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String fromCtx = (String) request.getSession().getAttribute("token");

		return fromCtx;
	}

//	private String getApps(HttpServletRequest request) throws SecurityException, ProfileServiceException, AACException{
//		BasicProfile x=profileService.getBasicProfile(getToken(request));
//		List<ResourceParameter> lstResourceParameters=services.loadResourceParameterByUserId(x.getUserId());
//		
//		List<AppAndToken> listAppToWeb=new ArrayList<AppAndToken>();
//		
//		for(ResourceParameter rp : lstResourceParameters){
//			if (!MODERATOR_SERVICE_ID.equals(rp.getServiceId()) && !MODERATOR_RESOURCE_PARAMETER_ID.equals(rp.getResourceId())) continue;
//			
//			
//			Query q = Query.query(Criteria
//					.where("parentUserId").is(x.getUserId())
//					.and("userId").is(x.getUserId())
//					.and("appId").is(rp.getValue()));
//			
//			if(db.find(q, ModeratorForApps.class).isEmpty()){
//				ModeratorForApps moderatorForApps=new ModeratorForApps(x, rp.getValue(), x.getUserId(), rp.getClientId());
//				db.save(moderatorForApps);
//			}
//		}
//		
//		Query findModeratorAndOwner = new Query();
//		findModeratorAndOwner.addCriteria(Criteria.where("userId").is(x.getUserId()));		
//	
//		List<ModeratorForApps> listModeratorForApps= db.find(findModeratorAndOwner, ModeratorForApps.class);
//		
//		for(ModeratorForApps rp : listModeratorForApps){
//			
//			ClientDetails cd = null;
//			
//			try{
//				cd = services.loadClientByClientId(rp.getClientId());
//			}catch (Exception e) {
//				logger.warn("Warning: No client with requested id: "+rp.getClientId());
//				db.remove(Query.query(Criteria
//						.where("userId").is(x.getUserId())
//						.and("clientId").is(rp.getClientId())));
//				continue;
//			}
//			String token=new EasyTokenManger(aacURL, rp.getClientId(), cd.getClientSecret()).getClientSmartCampusToken();
//			AppAndToken appAndToken=new AppAndToken(rp.getAppId(),token);
//			if(rp.getParentUserId().compareTo(rp.getUserId())!=0){
//				appAndToken.setOwner(false);
//			}
//			listAppToWeb.add(appAndToken);
//		}
//		
//		return JsonUtils.toJSON(listAppToWeb);
//	}
	
	private String getFullRequestPath(HttpServletRequest request) {
		return request.getScheme() + "://" +  request.getServerName() +  ":" + request.getServerPort() + request.getContextPath();
	}

}
