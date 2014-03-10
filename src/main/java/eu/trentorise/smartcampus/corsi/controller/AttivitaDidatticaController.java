package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.AttivitaDidattica;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.repository.AttivitaDidatticaRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.servicesync.CorsoLaureaServiceSync;


@Controller("attivitaDidatticaController")
public class AttivitaDidatticaController {

	
	private static final Logger logger = Logger
			.getLogger(AttivitaDidatticaController.class);

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
			
			return getAttivitaDidattica;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
