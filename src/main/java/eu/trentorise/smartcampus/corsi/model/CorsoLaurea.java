package eu.trentorise.smartcampus.corsi.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "CorsoLaurea.getCorsiLaureaByDipartimento", query = "select cl from CorsoLaurea cl where cl.dipartimento = ?1")
public class CorsoLaurea extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2348842239289261461L;

	@Id
	@Column(name = "CDS_ID")
	private long cdsId;

	@Column(name = "CDS_COD")
	private String cdsCod;

	@Column(name = "DESCRIPTION")
	private String descripion;

	@Column(name = "DURATA")
	private String durata;

	@Column(name = "AA_ORD")
	private String aaOrd;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(joinColumns = @JoinColumn(name = "CDS_ID"), inverseJoinColumns = @JoinColumn(name = "PDS_ID"))
	private List<PianoStudi> pds;

	@ManyToOne
	@JoinColumn(name = "DIPARTIMENTO_ID")
	private Dipartimento dipartimento;

	public long getId() {
		return cdsId;
	}

	public void setId(long id) {
		this.cdsId = id;
	}

	public Dipartimento getDipartimento() {
		return dipartimento;
	}

	public void setDipartimento(Dipartimento dipartimento) {
		this.dipartimento = dipartimento;
	}

	public long getCdsId() {
		return cdsId;
	}

	public void setCdsId(long cdsId) {
		this.cdsId = cdsId;
	}

	public String getCdsCod() {
		return cdsCod;
	}

	public void setCdsCod(String cdsCod) {
		this.cdsCod = cdsCod;
	}

	public String getDescripion() {
		return descripion;
	}

	public void setDescripion(String descripion) {
		this.descripion = descripion;
	}

	public String getDurata() {
		return durata;
	}

	public void setDurata(String durata) {
		this.durata = durata;
	}

	public String getAaOrd() {
		return aaOrd;
	}

	public void setAaOrd(String aaOrd) {
		this.aaOrd = aaOrd;
	}

	public List<PianoStudi> getPds() {
		return pds;
	}

	public void setPds(List<PianoStudi> pds) {
		this.pds = pds;
	}

}
