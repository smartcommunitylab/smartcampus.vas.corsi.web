package eu.trentorise.smartcampus.corsi.servicesync;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.repository.PianoStudiRepository;
import eu.trentorise.smartcampus.corsi.util.EasyTokenManger;
import eu.trentorise.smartcampus.corsi.util.UniCourseDegreeMapper;
import eu.trentorise.smartcampus.unidataservice.UniversityPlannerService;
import eu.trentorise.smartcampus.unidataservice.model.CdsData;

@Service("corsoLaureaServiceSync")
@Configuration
@ComponentScan("eu.trentorise.smartcampus.corsi.servicesync")
public class CorsoLaureaServiceSync {
	private static final Logger logger = Logger
			.getLogger(CorsoLaureaServiceSync.class);
	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${url.studente.service}")
	private String unidataaddress;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;
	
	@Autowired
	private DipartimentoRepository dipartimentoRepository;
	
	@Autowired
	private PianoStudiRepository pianoStudiRepository;
	

	private Dipartimento dipartimento;
	private List<CorsoLaurea> corsiDiLaurea;
	
	// client id studymate
	@Autowired
	@Value("${studymate.client.id}")
	private String client_id;
	
	// client secret studymate
	@Autowired
	@Value("${studymate.client.secret}")
	private String client_secret;
	
	String client_auth_token;
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return List<Dipartimento>
	 * @throws IOException
	 * 
	 *             Restituisce la lista dei corsi di laurea soltanto se sono da sincronizzare
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sync/corsolaurea/{id_dipartimento}/all")
	public @ResponseBody
	List<CorsoLaurea> getCdsSync(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_dipartimento") Long id_dipartimento)

	throws IOException {
		try {
			logger.info("/sync/corsolaurea/{id_dipartimento}/all");

			dipartimento = dipartimentoRepository.findOne(id_dipartimento);
			
			// se la lista dei dipartimenti � gi� stata scaricata ritorno null
			if(dipartimento == null){
				return null;
			}

			// prendo i dati da unidata e li mappo
			UniversityPlannerService uniConnector = new UniversityPlannerService(
					unidataaddress);
			
			EasyTokenManger clientTokenManager = new EasyTokenManger(client_id, client_secret, profileaddress);
			client_auth_token = clientTokenManager.getClientSmartCampusToken();
			System.out.println("Client auth token: " + client_auth_token);
			
			List<CdsData> dataCdsUni = uniConnector.getCdsData(client_auth_token, String.valueOf(id_dipartimento));
			
			if (dataCdsUni == null)
				return null;

			UniCourseDegreeMapper cdsMapper = new UniCourseDegreeMapper();
			corsiDiLaurea = cdsMapper.convert(dataCdsUni, client_auth_token, dipartimentoRepository, pianoStudiRepository);
			

			corsiDiLaurea = corsoLaureaRepository.save(corsiDiLaurea);

			return corsiDiLaurea;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
}
