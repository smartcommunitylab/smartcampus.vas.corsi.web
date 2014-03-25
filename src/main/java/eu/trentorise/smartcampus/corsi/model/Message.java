package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Message extends ChatObj {
	// id del gruppo

	// Nome del gruppo
	@Column(name = "TESTO")
	private String testo;

	public Message() {
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

}
