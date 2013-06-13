package smartcampus.webtemplate.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;


@Controller("studenteController")
public class StudenteController {

	
	
private static final Logger logger = Logger.getLogger(CommentiController.class);
	
	
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
	private StudenteRepository studenteRepository;
	
}
