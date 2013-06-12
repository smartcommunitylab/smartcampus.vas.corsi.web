package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "RisorsaPhl.getRisorsaPhlByCorsoId",
query = "select c from RisorsaPhl c where c.corso = ?1")
public class RisorsaPhl
{
	@Id
	@GeneratedValue
	private long id;

	@Column(name="ID_RISORSA_ELFINDER")
	private String idRisorsaElFinder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "corso_id")
	private Corso corso;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getIdRisorsaElFinder() {
		return idRisorsaElFinder;
	}

	public void setIdRisorsaElFinder(String idRisorsaElFinder) {
		this.idRisorsaElFinder = idRisorsaElFinder;
	}

	public Corso getCorso() {
		return corso;
	}

	public void setCorso(Corso corso) {
		this.corso = corso;
	}
}
