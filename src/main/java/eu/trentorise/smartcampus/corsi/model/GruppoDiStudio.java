package eu.trentorise.smartcampus.corsi.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "GruppoDiStudio.findGdsBycourseId(id_course)", query = "select gds from GruppoDiStudio gds where gds.corso = ?1")
public class GruppoDiStudio {
	// id del gruppo
	@Id
	@GeneratedValue
	private long id;
	
	// Nome del gruppo
	@Column(name = "NOME")
	private String nome;

	// corso di riferimento
	@ManyToOne
	@JoinColumn(name = "CORSO_ID")
	private Corso corso;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "GruppiStudioAttivita", joinColumns = @JoinColumn(name = "GRUPPODISTUDIO_ID"), inverseJoinColumns = @JoinColumn(name = "ATTIVITADISTUDIO_ID"))
	private Collection<AttivitaDiStudio> attivitaStudio;
	
	public GruppoDiStudio() {
	}


	public Corso getCorso() {
		return corso;
	}


	public void setCorso(Corso corso) {
		this.corso = corso;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


}
