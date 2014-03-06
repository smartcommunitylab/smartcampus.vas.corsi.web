package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.corsi.util.CorsoCarrieraMapper;
import eu.trentorise.smartcampus.corsi.util.UniStudentMapper;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.StudentInfoService;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoData;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExams;

@Controller("corsoCarrieraController")
public class CorsoCarrieraController {
	
	private static final Logger logger = Logger
			.getLogger(CorsoCarrieraController.class);
	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;
	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	private StudenteRepository studenteRepository;

	
	@Autowired
	@Value("${url.studente.service}")
	private String unidataaddress;
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return Studente
	 * @throws IOException
	 * 
	 *             Restituisce lo studente soltanto se c'� bisogno di
	 *             sincronizzare
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sync/corsocarriera/me")
	public @ResponseBody
	List<CorsoCarriera> getStudenteSync(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/sync/corsocarriera/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);
			
			
			if(studenteDB == null){
				logger.info("Student not stored on db... retrieving student informations...");
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				// ottengo da unidata lo studente
				StudentInfoData studentUniData = studentConnector
						.getStudentData(token);

				if (studentUniData == null)
					return null;

				UniStudentMapper studentMapper = new UniStudentMapper(profileaddress);

				// converto e salvo nel db lo studente aggiornato
				studenteDB = studentMapper.convert(studentUniData,  token);

				studenteDB = studenteRepository.save(studenteDB);
			}
			
			// prendo i dati da unidata e li mappo
			StudentInfoService studentConnector = new StudentInfoService(
					unidataaddress);

			StudentInfoExams studentExamsCareer = studentConnector
					.getStudentExams(token);

			if (studentExamsCareer == null)
				return null;
			
			
			CorsoCarrieraMapper cc = new CorsoCarrieraMapper();
			
			
			List<CorsoCarriera> corsoCarrieraList = cc.convert(studenteDB.getId(), studentExamsCareer, token);


			return corsoCarrieraList;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	

	/**
	 * 
	 * @param request
	 * @return String
	 * 
	 * Ottiene il token riferito alla request
	 * 
	 */
	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}
}
