package eu.trentorise.smartcampus.corsi.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.internal.util.compare.ComparableComparator;

@Entity
@NamedQueries({ @NamedQuery(name = "Evento.findEventoByCds", query = "select e from Evento e where e.cds = ?1"),
	      		@NamedQuery(name = "Evento.findEventoByAdAndYear", query = "select e from Evento e, CorsoCarriera cc where e.title = ?1 and e.yearCds <= ?2"),
	      		@NamedQuery(name = "Evento.findEventoByAd", query = "select e from Evento e, CorsoCarriera cc where e.title = ?1")})
@Table(name="evento")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Evento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4216708970101566299L;

	// id dell'evento
	@Id
	private long id;

	// corso di laurea di riferimento
	@Column(name = "ID_CDS")
	private long cds;

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
	
	@Column(name = "DATE")
	private Date date;

	// ora inizio
	@Column(name = "START")
	private Time start;

	// ora fine
	@Column(name = "STOP")
	private Time stop;

	@Column(name = "TYPE")
	private String type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}