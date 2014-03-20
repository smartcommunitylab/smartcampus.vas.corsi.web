package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@IdClass(CInteresseId.class)
@NamedQueries({
	@NamedQuery(name = "CorsoInteresse.findCorsoInteresseByStudenteId", query = "select ci from CorsoInteresse ci where ci.studenteId = ?1"),
	@NamedQuery(name = "CorsoInteresse.findCorsoInteresseByAttivitaIdAndStudenteId", query = "select ci from CorsoInteresse ci where ci.studenteId = ?1 and ci.attivitaDidattica.adId = ?2 "),
	@NamedQuery(name = "CorsoInteresse.deleteCorsiInteresseOfStudenteOfCareer", query = "delete from CorsoInteresse ci WHERE ci.studenteId = ?1 and ci.isCorsoCarriera = true")})
public class CorsoInteresse extends BasicEntity{
	private static final long serialVersionUID = 1306548062859361763L;

	@Id
	@Column(name = "ID")
	private long id;
	
	@Id
	@Column(name = "STUDENTE_ID")
	private long studenteId;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ATTIVITA_DIDATTICA")
	private AttivitaDidattica attivitaDidattica; 
	
	
	@Column(name = "IS_CAREER_AD")
	private boolean isCorsoCarriera;

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

	public boolean isCorsoCarriera() {
		return isCorsoCarriera;
	}

	public void setCorsoCarriera(boolean isCorsoCarriera) {
		this.isCorsoCarriera = isCorsoCarriera;
	}
	
	
	
}
