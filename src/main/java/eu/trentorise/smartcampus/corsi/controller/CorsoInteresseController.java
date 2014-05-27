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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.AttivitaDidattica;
import eu.trentorise.smartcampus.corsi.model.CorsoInteresse;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.AttivitaDidatticaRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoInteresseRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

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
	 * @return lista dei corsi di interesse settati manualmente
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/corsointeresse/me")
	public @ResponseBody
	List<CorsoInteresse> getCorsiInteresseStudente(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/rest/corsoInteresse/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);

			if (studenteDB == null) {
				return null;
			}

			List<CorsoInteresse> ci = new ArrayList<CorsoInteresse>();

			ci = corsoInteresseRepository
					.findCorsoInteresseByStudenteId(studenteDB.getId());

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
	 * @param adId
	 * @return true se il corso di interesse è seguito
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/corsointeresse/{adId}/seguito")
	public @ResponseBody
	CorsoInteresse isCorsoInteresseSeguito(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("adId") long adId)

	throws IOException {
		try {
			logger.info("/rest/corsointeresse/{adId}/seguito");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);

			if (studenteDB == null)
				return null;

			AttivitaDidattica aDidattica = attivitaDidatticaRepository
					.findOne(adId);

			CorsoInteresse ci = new CorsoInteresse();

			ci = corsoInteresseRepository
					.findCorsoInteresseByAttivitaIdAndStudenteId(
							studenteDB.getId(), aDidattica.getAdId());

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
	 * @param idAttivitaDidattica
	 * @return cambia lo stato follow/unfollow di un'attività didattica
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/rest/corsointeresse/{adId}/seguo")
	//
	public @ResponseBody
	boolean setCorsoAsFollowUnflollow(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("adId") Long idAttivitaDidattica)

	throws IOException {
		try {

			logger.info("/rest/corsointeresse/{adId}/seguo");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studente = studenteRepository.findStudenteByUserId(userId);

			AttivitaDidattica aDidattica = attivitaDidatticaRepository
					.findOne(idAttivitaDidattica);

			if (aDidattica == null)
				return false;

			CorsoInteresse cInteresse = corsoInteresseRepository
					.findCorsoInteresseByAttivitaIdAndStudenteId(
							studente.getId(), aDidattica.getAdId());

			if (cInteresse == null) {

				cInteresse = new CorsoInteresse();
				cInteresse.setId(aDidattica.getAdId());
				cInteresse.setAttivitaDidattica(aDidattica);
				cInteresse.setStudenteId(studente.getId());
				corsoInteresseRepository.save(cInteresse);

			} else {

				if (!cInteresse.isCorsoCarriera()) {
					corsoInteresseRepository.delete(cInteresse);
				} else {
					return false;
				}
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
	 * @param response
	 * @param session
	 * @param attivitaDidatticaCod
	 * @return true se un corso di interesse viene eliminato con successo,
	 *         altrimenti false
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/rest/corsointeresse/{adCod}/delete")
	public @ResponseBody
	boolean setCorsoAsUnflollow(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("adCod") String attivitaDidatticaCod)

	throws IOException {
		try {

			logger.info("/rest/corsointeresse/{adCod}/delete");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// test
			Studente studente = studenteRepository.findStudenteByUserId(userId);

			List<AttivitaDidattica> aDidattica = attivitaDidatticaRepository
					.findAttivitaDidatticaByAdCod(attivitaDidatticaCod);

			if (aDidattica.size() == 0)
				return false;

			CorsoInteresse cInteresse = corsoInteresseRepository
					.findCorsoInteresseByAttivitaCodAndStudenteId(
							studente.getId(), aDidattica.get(0).getAdCod());

			if (!cInteresse.isCorsoCarriera()) {
				corsoInteresseRepository.delete(cInteresse);
			} else {
				return false;
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
	 *         Ottiene il token riferito alla request
	 * 
	 */
	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}
}
