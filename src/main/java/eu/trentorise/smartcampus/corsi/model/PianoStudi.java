package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumns;

@Entity
public class PianoStudi extends BasicEntity {

	@Id
	private long pdsId;
	
	@Column(name = "PDS_COD")
	private String pdsCod;


	public String getPdsCod() {
		return pdsCod;
	}

	public void setPdsCod(String pdsCod) {
		this.pdsCod = pdsCod;
	}

	public long getPdsId() {
		return pdsId;
	}

	public void setPdsId(long pdsId) {
		this.pdsId = pdsId;
	}


	
	
	
}
