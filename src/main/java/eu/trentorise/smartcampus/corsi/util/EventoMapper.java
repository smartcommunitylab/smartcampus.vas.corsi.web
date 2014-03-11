package eu.trentorise.smartcampus.corsi.util;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.trentorise.smartcampus.corsi.model.CorsoCarriera;
import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.servicesync.ScheduledServiceSync;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.unidataservice.model.CalendarCdsData;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExam;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExams;

public class EventoMapper {
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	private BasicProfile basicProfile;

	private String token;

	private List<Evento> listEvento;
	
	private static final Logger logger = Logger
			.getLogger(EventoMapper.class);
	
	public EventoMapper() {
		// TODO Auto-generated constructor stub
	}

	public List<Evento> convert(List<CalendarCdsData> listCalendarWeek, CorsoLaurea idCds, int year)
			throws IllegalArgumentException, SecurityException,
			ProfileServiceException {

		listEvento = new ArrayList<Evento>();
		
		for (CalendarCdsData eventCal : listCalendarWeek) {
			Evento evento = new Evento();
			
			evento.setId(Long.parseLong(eventCal.getId()));
			evento.setRoom(eventCal.getRoom());
			evento.setTeacher(eventCal.getTeacher());
			evento.setTitle(eventCal.getTitle());
			evento.setDate(new Date(eventCal.getFrom()));
			logger.info("mapping evento: date = "+evento.getDate());
			evento.setStart(new Time(eventCal.getFrom()));
			evento.setStop(new Time(eventCal.getTo()));
			evento.setType(eventCal.getType());
			evento.setYearCds(year);
			evento.setCds(idCds.getCdsId());
			 
			listEvento.add(evento);
		}
		
		return listEvento;

	}

}
