package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "RegistrationId.findRegIdsByStudent", query = "select r from RegistrationId r where r.studentId = ?1")})
public class RegistrationId {

	@Id
	@Column(name = "ID")
	private long id;
	
	@Column(name = "STUDENT")
	private long studentId;
	
	@Column(name = "REG_ID")
	private String regId;
	
	public RegistrationId() {
		// TODO Auto-generated constructor stub
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}
	
	
}
