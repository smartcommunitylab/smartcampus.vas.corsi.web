package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Allegato extends ChatObj {

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
