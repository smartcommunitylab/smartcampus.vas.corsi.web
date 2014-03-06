package eu.trentorise.smartcampus.corsi.servicesync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.controller.CorsiController;
import eu.trentorise.smartcampus.corsi.model.AttivitaDidattica;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.repository.AttivitaDidatticaRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoLaureaRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.util.AttivitaDidatticaMapper;
import eu.trentorise.smartcampus.corsi.util.EasyTokenManger;
import eu.trentorise.smartcampus.corsi.util.UniCourseDegreeMapper;
import eu.trentorise.smartcampus.corsi.util.UniDepartmentMapper;
import eu.trentorise.smartcampus.unidataservice.UniversityPlannerService;
import eu.trentorise.smartcampus.unidataservice.model.AdData;
import eu.trentorise.smartcampus.unidataservice.model.CdsData;
import eu.trentorise.smartcampus.unidataservice.model.FacoltaData;

@Service("scheduledServiceSync")
@Configuration
@ComponentScan("eu.trentorise.smartcampus.corsi.servicesync")
public class ScheduledServiceSync {

	private static final Logger logger = Logger
			.getLogger(ScheduledServiceSync.class);
	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${url.studente.service}")
	private String unidataaddress;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	private CorsoLaureaRepository corsoLaureaRepository;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	@Autowired
	private AttivitaDidatticaRepository attivitaDidatticaRepository;

	private Dipartimento dipartimento;
	private List<CorsoLaurea> corsiDiLaurea;

	// client id studymate
	@Autowired
	@Value("${studymate.client.id}")
	private String client_id;

	// client secret studymate
	@Autowired
	@Value("${studymate.client.secret}")
	private String client_secret;

	String client_auth_token;
	List<Dipartimento> dipartimenti;

	private final static int MONTH_NEW_OFF = 8;

	/**
	 * Scheduled che aggiorna da unidata le informazioni sui corsi di laurea e
	 * dipartimenti
	 * 
	 * @throws IOException
	 */
	@Scheduled(fixedDelay = 1296000000)
	public @ResponseBody
	void getDipartimentoAndCdsSync()

	throws IOException {
		try {
			logger.info("sync departments, courses degree, courses from unidata service");

			UniversityPlannerService uniConnector = new UniversityPlannerService(
					unidataaddress);

			EasyTokenManger clientTokenManager = new EasyTokenManger(
					profileaddress, client_id, client_secret);
			client_auth_token = clientTokenManager.getClientSmartCampusToken();

			System.out.println("Client auth token: " + client_auth_token);
			List<FacoltaData> dataDepartmentsUni = uniConnector
					.getFacoltaData(client_auth_token);

			if (dataDepartmentsUni == null)
				return;

			UniDepartmentMapper departmentMapper = new UniDepartmentMapper();
			dipartimenti = departmentMapper.convert(dataDepartmentsUni,
					client_auth_token);

			dipartimenti = dipartimentoRepository.save(dipartimenti);

			// se la lista dei dipartimenti � gi� stata scaricata ritorno null
			if (dipartimenti == null) {
				return;
			}

			for (Dipartimento dip : dipartimenti) {
				
				corsiDiLaurea = new ArrayList<CorsoLaurea>();

				List<CdsData> dataCdsUni = uniConnector.getCdsData(
						client_auth_token, String.valueOf(dip.getId()));

				if (dataCdsUni == null)
					return;

				UniCourseDegreeMapper cdsMapper = new UniCourseDegreeMapper();
				corsiDiLaurea = cdsMapper.convert(dataCdsUni,
						client_auth_token, dipartimentoRepository);

				corsiDiLaurea = corsoLaureaRepository.save(corsiDiLaurea);

			}

			List<CorsoLaurea> cdsListDB = corsoLaureaRepository.findAll();

			if (cdsListDB == null)
				return;

			long dateToday = System.currentTimeMillis();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(dateToday);
			int month = c.get(Calendar.MONTH);
			int year = c.get(Calendar.YEAR);
			if (month < MONTH_NEW_OFF) {
				year--;
			}

			AttivitaDidatticaMapper adMapper = new AttivitaDidatticaMapper();
			for (CorsoLaurea cds : cdsListDB) {
				List<AdData> attDidatticheList = uniConnector.getAdData(
						client_auth_token, String.valueOf(cds.getCdsId()),
						cds.getAaOrd(), String.valueOf(year));

				List<AttivitaDidattica> attivitaDidatticaList = adMapper
						.convert(attDidatticheList, cds.getCdsId(),
								cds.getAaOrd(), String.valueOf(year),
								client_auth_token);

				attivitaDidatticaRepository.save(attivitaDidatticaList);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return;
	}

}
