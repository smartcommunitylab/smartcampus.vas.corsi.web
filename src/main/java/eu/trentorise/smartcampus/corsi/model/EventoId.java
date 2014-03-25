package eu.trentorise.smartcampus.corsi.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EventoId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5652357310213535292L;

	@Column(name = "ID_EVENT_AD")
	private long idEventAd;

	@Column(name = "DATE")
	private Date date;

	// ora inizio
	@Column(name = "START")
	private Time start;

	// ora fine
	@Column(name = "STOP")
	private Time stop;

	@Column(name = "STUDENTE")
	private long idStudente;

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getIdEventAd() {
		return idEventAd;
	}

	public void setIdEventAd(long idEventAd) {
		this.idEventAd = idEventAd;
	}

	public long getIdStudente() {
		return idStudente;
	}

	public void setIdStudente(long idStudente) {
		this.idStudente = idStudente;
	}

}
