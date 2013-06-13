package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.cxf.jaxrs.client.WebClient;
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
import eu.trentorise.smartcampus.corsi.model.Risorsa;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;

@Controller("risorsaController")
public class RisorseController extends SCController {

	private static final Logger logger = Logger
			.getLogger(RisorseController.class);
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
	@Value("${phl.url}")
	private String phlUrl;

	@Autowired
	private CorsoRepository corsoRepository;

	/*
	 * Ritorna tutti i corsi in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/risorsa/all")
	public @ResponseBody
	List<Risorsa> getRisorsaAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Ritorna tutti i corsi in versione lite
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/risorsa/{idcorso}")
	public @ResponseBody
	Risorse getRisorsaByCorsoId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("idcorso") String idcorso)

	throws IOException {
		try {

			Risorse listRisorsaPhl = null;

			Risorse risorsaReturn = new Risorse();

			if (corsoRepository.findOne(Long.valueOf(idcorso)) != null) {

				WebClient client = WebClient.create(phlUrl);

				listRisorsaPhl = client.path("getFiles").accept("text/xml")
						.post("{id_sc:" + idcorso + "}", Risorse.class);

			}
			// TODO Aggiungere ricerca su moodle e mettere in unica lista

			risorsaReturn.addAll(listRisorsaPhl);

			return risorsaReturn;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	class Risorse extends ArrayList<Risorsa> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Risorse() {
			super();
		}

	}

}
