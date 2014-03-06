package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
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

import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.servicesync.CorsoLaureaServiceSync;
import eu.trentorise.smartcampus.corsi.servicesync.DipartimentoServiceSync;

@Controller("corsoLaureaController")
public class CorsoLaureaController {

	private static final Logger logger = Logger
			.getLogger(CorsiController.class);

	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

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
	@RequestMapping(method = RequestMethod.GET, value = "/corsolaurea/all")
	public @ResponseBody
	List<CorsoLaurea> getCorsiLaureaAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corsolaurea/all");
			List<CorsoLaurea> getCorsiLaurea = corsoLaureaRepository.findAll();
			logger.info(corsoLaureaRepository);
			logger.info(getCorsiLaurea);
			return getCorsiLaurea;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna tutti i corsi di laurea di un dipartimento dato
	 */

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_dipartimento
	 * @return List<CorsoLaurea>
	 * @throws IOException
	 * 
	 *             Restituisce tutti i corsi di laurea dato un dipartimento
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsolaurea/{id_dipartimento}")
	public @ResponseBody
	List<CorsoLaurea> getCorsiLaureaAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_dipartimento") Long id_dipartimento)

	throws IOException {
		try {
			logger.info("/corsolaurea/{id_dipartimento}");
			if (id_dipartimento == null)
				return null;

			List<CorsoLaurea> getCds = corsoLaureaRepository
					.getCorsiLaureaByDipartimento(dipartimentoRepository
							.findOne(id_dipartimento));


			return getCds;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;

	}

}
