package smartcampus.webtemplate.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.controllers.SCController;
import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;

@Controller("commentiController")
public class CommentiController extends SCController {

	private static final Logger logger = Logger
			.getLogger(CommentiController.class);

	@Autowired
	private AcService acService;

	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${services.server}")
	private String serverAddress;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	private CommentiRepository commentiRepository;

	@Autowired
	private CorsoRepository corsoRepository;

	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/commento/{id_corso}")
	public @ResponseBody
	List<Commento> getCommentoByCorsoId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {
			logger.info("/commenti/{id_corso}");
			if (id_corso == null)
				return null;

			return commentiRepository.getCommentoByCorso(corsoRepository
					.findOne(id_corso));

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/commento")
	public @ResponseBody
	boolean saveCommento(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @RequestBody Commento commento)

	throws IOException {
		try {
			logger.info("/commento");
			if (commento == null)
				return false;

			return commentiRepository.save(commento)!=null;

		} catch (Exception e) {
			
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return false;
		}
		
	}

	@PostConstruct
	private void initCommenti() {
		
		List<Corso> esse3 = corsoRepository.findAll();
		
		for(Corso c : esse3){
			Commento commento = new Commento();
			commento.setCorso(c);
			commento.setRating_carico_studio(4);
			commento.setRating_contenuto(3);
			commento.setRating_esame(5);
			commento.setRating_lezioni(4);
			commento.setRating_materiali(3);
			commento.setTesto("Corso inutile.");
			
			commentiRepository.save(commento);
		}
		
		
	}

}
