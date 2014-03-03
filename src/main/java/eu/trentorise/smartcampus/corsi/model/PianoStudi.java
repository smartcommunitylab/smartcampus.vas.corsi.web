package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PianoStudi extends BasicEntity {

	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PDS_ID")
	private CorsoLaurea pdsId;
	
	@Column(name = "PDS_COD")
	private String pdsCod;


	public String getPdsCod() {
		return pdsCod;
	}

	public void setPdsCod(String pdsCod) {
		this.pdsCod = pdsCod;
	}

	public CorsoLaurea getPdsId() {
		return pdsId;
	}

	public void setPdsId(CorsoLaurea pdsId) {
		this.pdsId = pdsId;
	}
	
	
	
}
