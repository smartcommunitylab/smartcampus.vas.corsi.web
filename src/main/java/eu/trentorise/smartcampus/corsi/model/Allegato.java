package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Allegato extends ChatObj{
	// id del gruppo
	@Id
	@GeneratedValue
	private int id;

	// Nome del gruppo
	@Column(name = "URI")
	private String uri;

	public Allegato() {
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	


	
}
