package eu.trentorise.smartcampus.corsi.controllers;

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

import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;

@Controller("dipartmentoController")
public class DipartimentoController {

	private static final Logger logger = Logger
			.getLogger(CorsiController.class);

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return List<Dipartimento>
	 * @throws IOException
	 * 
	 * Restituisce la lista di tutti i dipartimenti
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/dipartimento/all")
	public @ResponseBody
	List<Dipartimento> getDipartimentoAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {

			List<Dipartimento> getDip = dipartimentoRepository.findAll();

			return getDip;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna tutti i corsi di laurea associati al dipartimento
	 */
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_dipartimento
	 * @return Dipartimento
	 * @throws IOException
	 * 
	 * Restituisce le informazioni di un dipartimento dato l'id
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/dipartimento/{id_dipartimento}")
	public @ResponseBody
	Dipartimento getDipartimentoAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_dipartimento") Long id_dipartimento)

	throws IOException {
		try {

			Dipartimento getDip = dipartimentoRepository
					.findOne(id_dipartimento);

			return getDip;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}


}
