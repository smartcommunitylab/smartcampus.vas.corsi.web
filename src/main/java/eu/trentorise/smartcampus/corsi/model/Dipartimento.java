package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Dipartimento extends BasicEntity {

	private static final long serialVersionUID = 8681710690984301605L;

	@Id
	private long facId;

	@Column(name = "DESCRIPTION")
	private String description;

	public long getId() {
		return facId;
	}

	public void setId(long id) {
		this.facId = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
