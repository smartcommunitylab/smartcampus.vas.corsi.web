package eu.trentorise.smartcampus.corsi.model;

import java.io.Serializable;


public class CInteresseId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3151115701918110148L;

	private long id;
	
	private long studenteId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudenteId() {
		return studenteId;
	}

	public void setStudenteId(long studenteId) {
		this.studenteId = studenteId;
	}
	
	
}
