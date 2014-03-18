package eu.trentorise.smartcampus.corsi.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.trentorise.smartcampus.corsi.model.Dipartimento;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.model.FacoltaData;

public class UniDepartmentMapper {
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	private BasicProfile basicProfile;

	private String token;

	private List<Dipartimento> dipartimenti;
	
	public UniDepartmentMapper() {
		// TODO Auto-generated constructor stub
	}

	public List<Dipartimento> convert(List<FacoltaData> dataFacolta, String token)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		dipartimenti = new ArrayList<Dipartimento>();
		
		for (FacoltaData facolta : dataFacolta) {
			// wrappo i dati dei dipartimenti
			Dipartimento dipartimento = new Dipartimento();

			dipartimento.setId(Long.valueOf(facolta.getFacId()));
			dipartimento.setDescription(facolta.getDescription());

			dipartimenti.add(dipartimento);
		}
		
		
		return dipartimenti;

	}
	
	protected Dipartimento convert(FacoltaData dataFacolta, String token)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

			// wrappo i dati dei dipartimenti
			Dipartimento dipartimento = new Dipartimento();

			dipartimento.setId(Long.valueOf(dataFacolta.getFacId()));
			dipartimento.setDescription(dataFacolta.getDescription());

		
		return dipartimento;

	}
}
