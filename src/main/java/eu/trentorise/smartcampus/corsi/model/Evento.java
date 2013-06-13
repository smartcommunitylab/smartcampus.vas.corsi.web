package eu.trentorise.smartcampus.corsi.model;

import java.sql.Time;

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
// @NamedQuery(name = "Evento.findEventoByUser", query =
// "select c from Evento c where c.corso = ?1"),
@NamedQuery(name = "Evento.findEventoByCorso", query = "select c from Evento c where c.corso = ?1") })
public class Evento {
	// id dell'evento
	@Id
	@GeneratedValue
	private long id;

	// corso di riferimento
	// @Column(name = "CORSOLITE")
	@ManyToOne
	@JoinColumn(name = "ID_CORSO")
	private Corso corso;

	// // mail of the owner
	// @Column(name = "ORGANIZER")
	// private String organizer;

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
	// ora inizio
	@Column(name = "START")
	private Time start;

	// ora fine
	@Column(name = "STOP")
	private Time stop;

	// true if occupies the entire day
	@Column(name = "ALL_DAY")
	private boolean all_day;

	// If this event counts as busy time or is free time that can be scheduled
	// over.
	@Column(name = "AVAILABILITY")
	private boolean availability;

	// // Whether guests can modify the event.
	// @Column(name = "CANMODIFY")
	// private boolean guests_can_modify;

	// // Whether guests can invite other guests
	// @Column(name = "CANINVITE")
	// private boolean guests_can_invite;

	// // Whether guests can see the list of attendees
	// @Column(name = "CANSEE")
	// private boolean guests_can_see;

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

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}

	public Time getStop() {
		return stop;
	}

	public void setStop(Time stop) {
		this.stop = stop;
	}

	public boolean isAll_day() {
		return all_day;
	}

	public void setAll_day(boolean all_day) {
		this.all_day = all_day;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

}