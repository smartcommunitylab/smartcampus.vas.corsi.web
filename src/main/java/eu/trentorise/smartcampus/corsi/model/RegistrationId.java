package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "RegistrationId.findRegIdsByStudent", query = "select r from RegistrationId r where r.studentId = ?1")})
public class RegistrationId extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 542222903380710913L;

	/**
	 * 
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
}
