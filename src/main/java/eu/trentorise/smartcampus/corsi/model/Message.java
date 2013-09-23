package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Message extends ChatObj{
	// id del gruppo
	@Id
	@GeneratedValue
	private int id;

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
