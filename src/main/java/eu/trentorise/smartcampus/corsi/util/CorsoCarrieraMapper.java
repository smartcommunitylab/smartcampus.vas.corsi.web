package eu.trentorise.smartcampus.corsi.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.trentorise.smartcampus.corsi.model.AttivitaDidattica;
import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.model.AdData;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExam;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExams;

public class CorsoCarrieraMapper {
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	private BasicProfile basicProfile;

	private String token;

	private List<CorsoCarriera> ccList;
	
	public CorsoCarrieraMapper() {
		// TODO Auto-generated constructor stub
	}

	public List<CorsoCarriera> convert(long idStudent, StudentInfoExams examsCareer, String token)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		ccList = new ArrayList<CorsoCarriera>();
		
		for (StudentInfoExam exam : examsCareer.getExams()) {
			CorsoCarriera cc = new CorsoCarriera();
			
			 cc.setId(Long.parseLong(exam.getId()));
			 cc.setCod(exam.getCod());
			 cc.setDate(new Date(exam.getDate()));
			 cc.setName(exam.getName());
			 cc.setWeight(exam.getWeight());
			 cc.setResult(exam.getResult());
			 cc.setLode(exam.isLode());
			 cc.setStudente(idStudent);
			 
			 ccList.add(cc);
		}
		
		return ccList;

	}
	
}
