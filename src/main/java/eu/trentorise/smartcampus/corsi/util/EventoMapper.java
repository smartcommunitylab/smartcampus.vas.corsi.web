package eu.trentorise.smartcampus.corsi.util;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.trentorise.smartcampus.corsi.model.CorsoLaurea;
import eu.trentorise.smartcampus.corsi.model.Evento;
import eu.trentorise.smartcampus.corsi.model.EventoId;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.unidataservice.model.CalendarCdsData;

public class EventoMapper {
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	private DipartimentoRepository dipartimentoRepository;

	private List<Evento> listEvento;

	public EventoMapper() {
		// TODO Auto-generated constructor stub
	}

	public List<Evento> convert(List<CalendarCdsData> listCalendarWeek,
			CorsoLaurea idCds, int year) throws IllegalArgumentException,
			SecurityException, ProfileServiceException {

		listEvento = new ArrayList<Evento>();

		for (CalendarCdsData eventCal : listCalendarWeek) {
			Evento evento = new Evento();

			EventoId eId = new EventoId();
			eId.setIdEventAd(Long.parseLong(eventCal.getId()));
			eId.setDate(new Date(eventCal.getFrom()));
			eId.setStart(new Time(eventCal.getFrom()));
			eId.setStop(new Time(eventCal.getTo()));
			eId.setIdStudente(-1);
			evento.setEventoId(eId);
			evento.setRoom(eventCal.getRoom());
			evento.setTeacher(eventCal.getTeacher());
			evento.setTitle(eventCal.getTitle());
			evento.setType(eventCal.getType());
			evento.setYearCds(year);
			evento.setCds(idCds.getCdsId());

			listEvento.add(evento);
		}

		return listEvento;

	}

	public List<Evento> convert(List<CalendarCdsData> dataCalendarOf2Week,
			CorsoLaurea cl) {

		listEvento = new ArrayList<Evento>();

		for (CalendarCdsData eventCal : dataCalendarOf2Week) {
			Evento evento = new Evento();

			EventoId eId = new EventoId();
			eId.setIdEventAd(Long.parseLong(eventCal.getId()));
			eId.setDate(new Date(eventCal.getFrom()));
			eId.setStart(new Time(eventCal.getFrom()));
			eId.setStop(new Time(eventCal.getTo()));
			eId.setIdStudente(-1);
			evento.setEventoId(eId);
			evento.setRoom(eventCal.getRoom());
			evento.setTeacher(eventCal.getTeacher());
			evento.setTitle(eventCal.getTitle());
			evento.setType(eventCal.getType());
			evento.setYearCds(0);
			evento.setCds(cl.getCdsId());

			listEvento.add(evento);
		}

		return listEvento;

	}

}