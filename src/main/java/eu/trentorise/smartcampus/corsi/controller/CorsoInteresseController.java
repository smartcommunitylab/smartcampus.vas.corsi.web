package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.AttivitaDidattica;
import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;
import eu.trentorise.smartcampus.corsi.model.CorsoInteresse;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.AttivitaDidatticaRepository;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoInteresseRepository;
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

@Controller("corsoInteresseController")
public class CorsoInteresseController {

	private static final Logger logger = Logger
			.getLogger(CorsoInteresseController.class);
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
	private CorsoInteresseRepository corsoInteresseRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private AttivitaDidatticaRepository attivitaDidatticaRepository;
	
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
	 *             Sincronizza con unidata service corsi in carriera
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsointeresse/me")
	public @ResponseBody
	List<CorsoInteresse> getCorsiInteresseStudente(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corsoInteresse/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);
			
			
			if(studenteDB == null){
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				// ottengo da unidata lo studente
				StudentInfoData studentUniData = studentConnector
						.getStudentData(token);

				if (studentUniData == null)
					return null;

				UniStudentMapper studentMapper = new UniStudentMapper(profileaddress);

				// converto e salvo nel db lo studente aggiornato
				studenteDB = studentMapper.convert(studentUniData, token);

				studenteDB = studenteRepository.save(studenteDB);
			}
			
			
			
			
			List<CorsoInteresse> ci = new ArrayList<CorsoInteresse>();
			
			ci = corsoInteresseRepository.findCorsoInteresseByStudenteId(studenteDB.getId());
			
			

			return ci;

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
	 * @param response
	 * @param session
	 * @return Studente
	 * @throws IOException
	 * 
	 *             Sincronizza con unidata service corsi in carriera
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsointeresse/{adId}/seguito")
	public @ResponseBody
	boolean isCorsoInteresseSeguito(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("adId") long adId)

	throws IOException {
		try {
			logger.info("/corsointeresse/{adId}/seguito");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);
			
			
			if(studenteDB == null){
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				// ottengo da unidata lo studente
				StudentInfoData studentUniData = studentConnector
						.getStudentData(token);

				if (studentUniData == null)
					return false;

				UniStudentMapper studentMapper = new UniStudentMapper(profileaddress);

				// converto e salvo nel db lo studente aggiornato
				studenteDB = studentMapper.convert(studentUniData, token);

				studenteDB = studenteRepository.save(studenteDB);
			}
			
			
			AttivitaDidattica aDidattica = attivitaDidatticaRepository.findOne(adId);
			
			CorsoInteresse ci = new CorsoInteresse();
			
			ci = corsoInteresseRepository.findCorsoInteresseByAttivitaIdAndStudenteId(studenteDB.getId(), aDidattica);
			
			if(ci == null){
				return false;
			}else{
				return true;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}	
	
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param corso
	 * @return boolean
	 * @throws IOException
	 * 
	 * Dato un corso restituisce al client true se il corso � di interesse dello studente altrimenti false 
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/corsointeresse/{adId}/seguo")
	//
	public @ResponseBody
	boolean setCorsoAsFollowUnflollow(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("adId") Long idAttivitaDidattica)

	throws IOException {
		try {
			
			logger.info("/corsointeresse/{adId}/seguo");
			
			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// test
			Studente studente = studenteRepository.findStudenteByUserId(userId);
			
			AttivitaDidattica aDidattica = attivitaDidatticaRepository.findOne(idAttivitaDidattica);
			
			
			CorsoInteresse cInteresse = corsoInteresseRepository.findCorsoInteresseByAttivitaIdAndStudenteId(studente.getId(), aDidattica);
			
			if(cInteresse == null){
				
				if(aDidattica == null)
					return false;
				
				cInteresse = new CorsoInteresse();
				
				cInteresse.setAttivitaDidattica(aDidattica);
				cInteresse.setStudenteId(studente.getId());
				corsoInteresseRepository.save(cInteresse);
			}else{
				corsoInteresseRepository.delete(cInteresse);
			}
			
			return true;
			
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
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
