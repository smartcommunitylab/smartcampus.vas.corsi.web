package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;

@Controller("dipartmentoController")
public class DipartimentoController extends SCController {

	
	private static final Logger logger = Logger
			.getLogger(CorsiController.class);
	

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	

	/*
	 * Ritorna tutti i dipartimenti
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/dipartimento/all")
	public @ResponseBody
	List<Dipartimento> getDipartimentoAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			
			List<Dipartimento> getDip=dipartimentoRepository.findAll();

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
	@RequestMapping(method = RequestMethod.GET, value = "/dipartimento/{id_dipartimento}")
	public @ResponseBody
	Dipartimento getDipartimentoAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable("id_dipartimento") Long id_dipartimento )

	throws IOException {
		try {
			
			Dipartimento getDip=dipartimentoRepository.findOne(id_dipartimento);

			return getDip;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
//	@PostConstruct
	private void initCorsi() {
		
		
		///////// dipartimento 1
		Dipartimento d = new Dipartimento();

		d.setNome("Ingegneria e scienza dell informazione");
		
		dipartimentoRepository.save(d);
		
		
		
		///////// dipartimento 2
		
		d = new Dipartimento();

		d.setNome("Psicologia e scienze cognitive");
		
		dipartimentoRepository.save(d);
		
		
		///////// dipartimento 3
		
		d = new Dipartimento();

		d.setNome("Fisica");
		
		dipartimentoRepository.save(d);
		
		
		

		// TEST

	}
	
}
