package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
		@NamedQuery(name = "Evento.findEventoByCds", query = "select e from Evento e where e.cds = ?1"),
		@NamedQuery(name = "Evento.findEventoByAdAndYear", query = "select e from Evento e, CorsoCarriera cc where e.title = ?1 and e.yearCds <= ?2"),
		@NamedQuery(name = "Evento.findEventoByAd", query = "select e from Evento e where (e.title = ?1) and (e.eventoId.idStudente = ?2 or e.eventoId.idStudente = -1 or e.eventoId.idEventAd = -2)"),
		@NamedQuery(name = "Evento.findEventoByIdClass", query = "select e from Evento e where (e.eventoId.idEventAd = ?1) and (e.eventoId.date = ?2) and (e.eventoId.start = ?3) and (e.eventoId.stop = ?4) and (e.eventoId.stop = ?5)"),
		@NamedQuery(name = "Evento.selectEventsGdsOfStudent", query = "select e from Evento e where (e.eventoId.idEventAd = -2) and (e.gruppo = ?1) and (e.eventoId.idStudente = ?2)"),
		@NamedQuery(name = "Evento.findAttByIdGds", query = "select e from Evento e where e.gruppo = ?1")})
@Table(name = "evento")
public class Evento {
	/**
	 * 
	 */

	@EmbeddedId
	private EventoId eventoId;

	// corso di laurea di riferimento
	@Column(name = "ID_CDS")
	private long cds;

	@Column(name = "AD_COD")
	private long adCod;

	@Column(name = "YEAR")
	private int yearCds;

	// title of the event
	@Column(name = "TITOLO")
	private String title;

	// the room where the place takes places
	@Column(name = "ROOM")
	private String room;

	// the room where the place takes places
	@Column(name = "TEACHER")
	private String teacher;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "PERSONAL_DESCRIPTION")
	private String personalDescription;
	
	@JoinColumn(name = "GRUPPODISTUDIO_ID")
	@ManyToOne(fetch=FetchType.EAGER)
	private GruppoDiStudio gruppo;

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public int getYearCds() {
		return yearCds;
	}

	public void setYearCds(int yearCds) {
		this.yearCds = yearCds;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getCds() {
		return cds;
	}

	public void setCds(long cds) {
		this.cds = cds;
	}

	public EventoId getEventoId() {
		return eventoId;
	}

	public void setEventoId(EventoId eventoId) {
		this.eventoId = eventoId;
	}

	public String getDescription() {
		return personalDescription;
	}

	public void setDescription(String description) {
		this.personalDescription = description;
	}

	public long getAdCod() {
		return adCod;
	}

	public void setAdCod(long adCod) {
		this.adCod = adCod;
	}

	public String getPersonalDescription() {
		return personalDescription;
	}

	public void setPersonalDescription(String personalDescription) {
		this.personalDescription = personalDescription;
	}

	public GruppoDiStudio getGruppo() {
		return gruppo;
	}

	public void setGruppo(GruppoDiStudio gruppo) {
		this.gruppo = gruppo;
	}
	

}