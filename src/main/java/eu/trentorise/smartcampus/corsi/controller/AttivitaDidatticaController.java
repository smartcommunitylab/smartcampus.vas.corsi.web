package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.AttivitaDidatticaRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoCarrieraRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.corsi.servicesync.CorsoLaureaServiceSync;
import eu.trentorise.smartcampus.corsi.util.CorsoCarrieraMapper;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.StudentInfoService;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExams;


@Controller("attivitaDidatticaController")
public class AttivitaDidatticaController {

	
	private static final Logger logger = Logger
			.getLogger(AttivitaDidatticaController.class);
	
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


	@Autowired
	private AttivitaDidatticaRepository attivitaDidatticaRepository;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;
	
	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;

	@Autowired
	private CorsoLaureaServiceSync controllerSyncCorsoLaurea;
	
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return List<CorsoLaurea>
	 * @throws IOException
	 * 
	 *             Restituisce tutti i corsi di laurea
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/attivitadidattica/{id_ad}")
	public @ResponseBody
	AttivitaDidattica getAttivitaDidatticaById(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("id_ad") Long id_ad)

	throws IOException {
		try {
			logger.info("/attivitadidattica/"+id_ad);
			
			AttivitaDidattica getAttivitaDidattica = new AttivitaDidattica();
			getAttivitaDidattica = attivitaDidatticaRepository.findOne(id_ad);
			
			return getAttivitaDidattica;

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
	 * @return List<CorsoLaurea>
	 * @throws IOException
	 * 
	 *             Restituisce tutti i corsi di laurea
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/attivitadidattica/corsolaurea/{id_cds}")
	public @ResponseBody
	List<AttivitaDidattica> getAttivitaDidatticaByCds(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("id_cds") Long id_cds)

	throws IOException {
		try {
			logger.info("/attivitadidattica/corsolaurea/"+id_cds);
			
			List<AttivitaDidattica> getAttivitaDidattica = new ArrayList<AttivitaDidattica>();
			getAttivitaDidattica = attivitaDidatticaRepository.findAttivitaDidatticaByCdsId(id_cds);
			
			Collections.sort(getAttivitaDidattica, new Comparator<AttivitaDidattica>() {
				  public int compare(AttivitaDidattica e1, AttivitaDidattica e2) {
				      if (e1.getDescription() == null || e2.getDescription() == null)
				        return 0;
				      return e1.getDescription().compareTo(e2.getDescription());
				  }
				});
			
			return getAttivitaDidattica;

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
	 * @return List<CorsoLaurea>
	 * @throws IOException
	 * 
	 *             Restituisce tutti i corsi di laurea
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/attivitadidattica/dipartimento/{id_dip}")
	public @ResponseBody
	List<AttivitaDidattica> getAttivitaDidatticaByDipartimento(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("id_dip") Long id_dip)

	throws IOException {
		try {
			logger.info("/attivitadidattica/dipartimento/"+id_dip);
			
			Dipartimento dipartimento = dipartimentoRepository.findOne(id_dip);
			
			List<CorsoLaurea> listCds = corsoLaureaRepository.getCorsiLaureaByDipartimento(dipartimento);
			
			List<AttivitaDidattica> attivitaAllByDip = new ArrayList<AttivitaDidattica>();
			
			for (CorsoLaurea corsoLaurea : listCds) {
				List<AttivitaDidattica> getAttivitaDidattica = new ArrayList<AttivitaDidattica>();
				getAttivitaDidattica = attivitaDidatticaRepository.findAttivitaDidatticaByCdsId(corsoLaurea.getCdsId());
				
				attivitaAllByDip.addAll(getAttivitaDidattica);
			}

			
			Collections.sort(attivitaAllByDip, new Comparator<AttivitaDidattica>() {
				  public int compare(AttivitaDidattica e1, AttivitaDidattica e2) {
				      if (e1.getDescription() == null || e2.getDescription() == null)
				        return 0;
				      return e1.getDescription().compareTo(e2.getDescription());
				  }
				});
			
			return attivitaAllByDip;

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
	 * @return List<CorsoLaurea>
	 * @throws IOException
	 * 
	 *             Restituisce tutti i corsi di laurea
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/attivitadidattica/all")
	public @ResponseBody
	List<AttivitaDidattica> getAttivitaDidatticaAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/attivitadidattica/all");
			
			List<AttivitaDidattica> getAttivitaDidattica = new ArrayList<AttivitaDidattica>();
			getAttivitaDidattica = attivitaDidatticaRepository.findAll();
			
			Collections.sort(getAttivitaDidattica, new Comparator<AttivitaDidattica>() {
				  public int compare(AttivitaDidattica e1, AttivitaDidattica e2) {
				      if (e1.getDescription() == null || e2.getDescription() == null)
				        return 0;
				      
				      CorsoLaurea e1Cds = corsoLaureaRepository.findOne(e1.getCds_id());
				      CorsoLaurea e2Cds = corsoLaureaRepository.findOne(e2.getCds_id());
				      
				      return e1Cds.getDescripion().compareTo(e2Cds.getDescripion());
				  }
				});
			
			return getAttivitaDidattica;

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
	 * @return List<CorsoLaurea>
	 * @throws IOException
	 * 
	 *             Restituisce tutti i corsi di laurea
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/attivitadidattica/{ad_cod}/passed")
	public @ResponseBody
	boolean isAttivitaDidatticaPassed(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("ad_cod") String adCod)

	throws IOException {
		try {
			logger.info("/attivitadidattica/{ad_cod}/passed");
			
			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);

			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studenteDB = studenteRepository.findOne(userId);
			
			
			
			CorsoCarriera corsoCarriera = corsoCarrieraRepository
					.findCorsoCarrieraByAdCodAndStudenteId(adCod, studenteDB.getId());

			if (corsoCarriera == null) {
				// prendo i dati da unidata e li mappo
				return false;
			}
			
			if (corsoCarriera.getResult().equals("0")) {
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
