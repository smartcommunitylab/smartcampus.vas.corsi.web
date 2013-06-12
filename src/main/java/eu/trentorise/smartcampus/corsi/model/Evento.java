package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
		//@NamedQuery(name = "Evento.findEventoByUser", query = "select c from Evento c where c.corso = ?1"),
		@NamedQuery(name = "Evento.findEventoByCorso", query = "select c from Evento c where c.corso = ?1") })
public class Evento {
	// id dell'evento
	@Id
	@GeneratedValue
	private long id;

	// corso di riferimento
	// @Column(name = "CORSOLITE")
	@ManyToOne
	@JoinColumn(name = "idCORSO")
	private Corso corso;

	// mail of the owner
	@Column(name = "ORGANIZER")
	private String organizer;

	// title of the event
	@Column(name = "TITOLO")
	private String titolo;

	// where the place takes places
	@Column(name = "LOCATION")
	private String event_location;

	// the room where the place takes places
	@Column(name = "ROOM")
	private String room;

	// the description of the event
	@Lob
	@Column(name = "DESCRIZIONE", length = 100000)
	private String descrizione;

	// the description of the event
	/*
	 * @Column(name = "NOTE") private ArrayList<Nota> note;
	 */
	// The time the event starts in UTC milliseconds since the epoch
	@Column(name = "START")
	private Date dt_start;

	// The time the event ends in UTC milliseconds since the epoch
	@Column(name = "END")
	private Date dt_end;

	// The duration of the event in RFC5545 format
	@Column(name = "DURATA")
	private String durata;

	// true if occupies the entire day
	@Column(name = "ALL_DAY")
	private boolean all_day;

	// The recurrence rule for the event format. For
	// example,"FREQ=WEEKLY;COUNT=10;WKST=SU".
	@Column(name = "RRULE")
	private String rrule;

	// The recurrence dates for the event.
	// You typically use RDATE in conjunction with RRULE to define an aggregate
	// set of repeating occurrences.
	@Column(name = "R_DATE")
	private Date r_date;

	// If this event counts as busy time or is free time that can be scheduled
	// over.
	@Column(name = "AVAILABILITY")
	private boolean availability;

	// Whether guests can modify the event.
	@Column(name = "CANMODIFY")
	private boolean guests_can_modify;

	// Whether guests can invite other guests
	@Column(name = "CANINVITE")
	private boolean guests_can_invite;

	// Whether guests can see the list of attendees
	@Column(name = "CANSEE")
	private boolean guests_can_see;

	// List of all istance of the event
	/*
	 * @Column(name = "ISTANZE") private List<Istanza> istanze;
	 */

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Corso getCorso() {
		return corso;
	}

	public void setCorso(Corso corso) {
		this.corso = corso;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getEvent_location() {
		return event_location;
	}

	public void setEvent_location(String event_location) {
		this.event_location = event_location;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Date getDt_start() {
		return dt_start;
	}

	public void setDt_start(Date dt_start) {
		this.dt_start = dt_start;
	}

	public Date getDt_end() {
		return dt_end;
	}

	public void setDt_end(Date dt_end) {
		this.dt_end = dt_end;
	}

	public String getDurata() {
		return durata;
	}

	public void setDurata(String durata) {
		this.durata = durata;
	}

	public boolean isAll_day() {
		return all_day;
	}

	public void setAll_day(boolean all_day) {
		this.all_day = all_day;
	}

	public String getRrule() {
		return rrule;
	}

	public void setRrule(String rrule) {
		this.rrule = rrule;
	}

	public Date getR_date() {
		return r_date;
	}

	public void setR_date(Date r_date) {
		this.r_date = r_date;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public boolean isGuests_can_modify() {
		return guests_can_modify;
	}

	public void setGuests_can_modify(boolean guests_can_modify) {
		this.guests_can_modify = guests_can_modify;
	}

	public boolean isGuests_can_see() {
		return guests_can_see;
	}

	public boolean isGuests_can_invite() {
		return guests_can_invite;
	}

	public void setGuests_can_invite(boolean guests_can_invite) {
		this.guests_can_invite = guests_can_invite;
	}

	public void setGuests_can_see(boolean guests_can_see) {
		this.guests_can_see = guests_can_see;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

}