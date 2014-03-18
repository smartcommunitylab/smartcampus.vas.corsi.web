package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PianoStudi extends BasicEntity {

	@Id
	private long pdsId;
	
	@Column(name = "PDS_COD")
	private String pdsCod;

//	@ManyToOne(fetch=FetchType.LAZY)
//	@PrimaryKeyJoinColumn(name = "pds")
//	private CorsoLaurea cdsId;

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

//	public CorsoLaurea getCdsId() {
//		return cdsId;
//	}
//
//	public void setCdsId(CorsoLaurea cdsId) {
//		this.cdsId = cdsId;
//	}


	
	
	
}
