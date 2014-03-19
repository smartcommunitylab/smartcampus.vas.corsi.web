package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({
	@NamedQuery(name = "CorsoInteresse.findCorsoInteresseByStudenteId", query = "select ci from CorsoInteresse ci where ci.studenteId = ?1"),
	@NamedQuery(name = "CorsoInteresse.findCorsoInteresseByAttivitaIdAndStudenteId", query = "select ci from CorsoInteresse ci where ci.studenteId = ?1 and ci.attivitaDidattica = ?2 ")})
public class CorsoInteresse extends BasicEntity{
	private static final long serialVersionUID = 1306548062859361763L;

	@Id
	@Column(name = "ID")
	private long id;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ATTIVITA_DIDATTICA")
	private AttivitaDidattica attivitaDidattica; 
	
	@Column(name = "STUDENTE_ID")
	private long studenteId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AttivitaDidattica getAttivitaDidattica() {
		return attivitaDidattica;
	}

	public void setAttivitaDidattica(AttivitaDidattica attivitaDidattica) {
		this.attivitaDidattica = attivitaDidattica;
	}

	public long getStudenteId() {
		return studenteId;
	}

	public void setStudenteId(long studenteId) {
		this.studenteId = studenteId;
	}
	
	
	
}
