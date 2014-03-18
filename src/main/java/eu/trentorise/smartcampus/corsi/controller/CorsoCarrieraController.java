package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoCarrieraRepository;
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
	private CorsoCarrieraRepository corsoCarrieraRepository;

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
	@RequestMapping(method = RequestMethod.GET, value = "/sync/corsocarriera/me")
	public @ResponseBody
	List<CorsoCarriera> getCorsiCarrieraSync(HttpServletRequest request,
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

			if (studenteDB == null) {
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				// ottengo da unidata lo studente
				StudentInfoData studentUniData = studentConnector
						.getStudentData(token);

				if (studentUniData == null)
					return null;

				UniStudentMapper studentMapper = new UniStudentMapper(
						profileaddress);

				// converto e salvo nel db lo studente aggiornato
				studenteDB = studentMapper.convert(studentUniData, token);

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

			List<CorsoCarriera> corsoCarrieraList = cc.convert(
					studenteDB.getId(), studentExamsCareer, token);

			corsoCarrieraList = corsoCarrieraRepository.save(corsoCarrieraList);

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
	 * @param response
	 * @param session
	 * @return Studente
	 * @throws IOException
	 * 
	 *             Ritorna i corsi in carriera nel db
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsocarriera/me")
	public @ResponseBody
	List<CorsoCarriera> getCorsiCarriera(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corsocarriera/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);

			if (studenteDB == null) {
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				// ottengo da unidata lo studente
				StudentInfoData studentUniData = studentConnector
						.getStudentData(token);

				if (studentUniData == null)
					return null;

				UniStudentMapper studentMapper = new UniStudentMapper(
						profileaddress);

				// converto e salvo nel db lo studente aggiornato
				studenteDB = studentMapper.convert(studentUniData, token);

				studenteDB = studenteRepository.save(studenteDB);
			}

			List<CorsoCarriera> corsoCarrieraList = corsoCarrieraRepository
					.findCorsoCarrieraByStudenteId(studenteDB.getId());

			if (corsoCarrieraList.size() == 0) {
				// prendo i dati da unidata e li mappo
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				StudentInfoExams studentExamsCareer = studentConnector
						.getStudentExams(token);

				if (studentExamsCareer == null)
					return null;

				CorsoCarrieraMapper cc = new CorsoCarrieraMapper();

				corsoCarrieraList = cc.convert(studenteDB.getId(),
						studentExamsCareer, token);

				corsoCarrieraList = corsoCarrieraRepository
						.save(corsoCarrieraList);
			}

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
	 * @param response
	 * @param session
	 * @return Studente
	 * @throws IOException
	 * 
	 *             Ritorna i corsi in carriera nel db
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsocarriera/notpassed/me")
	public @ResponseBody
	List<CorsoCarriera> getCorsiCarrieraNotPassed(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corsocarriera/notpassed/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);

			if (studenteDB == null) {
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				// ottengo da unidata lo studente
				StudentInfoData studentUniData = studentConnector
						.getStudentData(token);

				if (studentUniData == null)
					return null;

				UniStudentMapper studentMapper = new UniStudentMapper(
						profileaddress);

				// converto e salvo nel db lo studente aggiornato
				studenteDB = studentMapper.convert(studentUniData, token);

				studenteDB = studenteRepository.save(studenteDB);
			}

			List<CorsoCarriera> corsoCarrieraList = corsoCarrieraRepository
					.findCorsoCarrieraByStudenteId(studenteDB.getId());

			
			
			
			if (corsoCarrieraList.size() == 0) {
				// prendo i dati da unidata e li mappo
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				StudentInfoExams studentExamsCareer = studentConnector
						.getStudentExams(token);

				if (studentExamsCareer == null)
					return null;

				CorsoCarrieraMapper cc = new CorsoCarrieraMapper();

				corsoCarrieraList = cc.convert(studenteDB.getId(),
						studentExamsCareer, token);

				corsoCarrieraList = corsoCarrieraRepository
						.save(corsoCarrieraList);
			}

			if (corsoCarrieraList.size() == 0) {
				return null;
			} else {
				for (Iterator<CorsoCarriera> iterator = corsoCarrieraList.iterator(); iterator.hasNext();) {
					CorsoCarriera cc = iterator.next();
					if (!cc.getResult().equals("0")) {
						iterator.remove();
					}
				}
			}

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
	 * @param response
	 * @param session
	 * @return Studente
	 * @throws IOException
	 * 
	 *             Ritorna i corsi in carriera nel db
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsocarriera/passed/me")
	public @ResponseBody
	List<CorsoCarriera> getCorsiCarrieraPassed(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corsocarriera/passed/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);

			if (studenteDB == null) {
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				// ottengo da unidata lo studente
				StudentInfoData studentUniData = studentConnector
						.getStudentData(token);

				if (studentUniData == null)
					return null;

				UniStudentMapper studentMapper = new UniStudentMapper(
						profileaddress);

				// converto e salvo nel db lo studente aggiornato
				studenteDB = studentMapper.convert(studentUniData, token);

				studenteDB = studenteRepository.save(studenteDB);
			}

			List<CorsoCarriera> corsoCarrieraList = corsoCarrieraRepository
					.findCorsoCarrieraByStudenteId(studenteDB.getId());

			if (corsoCarrieraList.size() == 0) {
				// prendo i dati da unidata e li mappo
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				StudentInfoExams studentExamsCareer = studentConnector
						.getStudentExams(token);

				if (studentExamsCareer == null)
					return null;

				CorsoCarrieraMapper cc = new CorsoCarrieraMapper();

				corsoCarrieraList = cc.convert(studenteDB.getId(),
						studentExamsCareer, token);

				corsoCarrieraList = corsoCarrieraRepository
						.save(corsoCarrieraList);
			}
			
			logger.info("size corsi carriera: "+corsoCarrieraList.size());

			
			for(Iterator<CorsoCarriera> iterator = corsoCarrieraList.iterator(); iterator.hasNext();){
				CorsoCarriera cc = iterator.next();
				if (cc.getResult().equals("0")) {
					iterator.remove();
				}
				
			}
	

			return corsoCarrieraList;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	
	
	

	@RequestMapping(method = RequestMethod.GET, value = "/corsocarriera/{ad_cod}/superato")
	public @ResponseBody
	boolean isCorsoCarrieraPassed(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("ad_cod") String ad_cod)

	throws IOException {
		try {
			logger.info("/corsocarriera/{ad_cod}/superato");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);

			if (studenteDB == null) {
				StudentInfoService studentConnector = new StudentInfoService(
						unidataaddress);

				// ottengo da unidata lo studente
				StudentInfoData studentUniData = studentConnector
						.getStudentData(token);

				if (studentUniData == null)
					return false;

				UniStudentMapper studentMapper = new UniStudentMapper(
						profileaddress);

				// converto e salvo nel db lo studente aggiornato
				studenteDB = studentMapper.convert(studentUniData, token);

				studenteDB = studenteRepository.save(studenteDB);
			}

			CorsoCarriera corsoCarriera = corsoCarrieraRepository
					.findCorsoCarrieraByAdCodAndStudenteId(ad_cod,
							studenteDB.getId());

			if (corsoCarriera == null) {
				return false;
			} else {
				if (corsoCarriera.getResult().equals("0")) {
					return false;
				} else {
					return true;
				}
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
	 * @return String
	 * 
	 *         Ottiene il token riferito alla request
	 * 
	 */
	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}
}
