package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "GruppoDiStudio.findGdsBycourseId(id_course)", query = "select gds from GruppoDiStudio gds where gds.getCorso = ?1")
public class GruppoDiStudio {
	// id del gruppo
	@Id
	@GeneratedValue
	private int id;

	// corso di riferimento
	@ManyToOne
	@JoinColumn(name = "CORSO_ID")
	private Corso corso;

	// Nome del gruppo
	@Column(name = "NOME")
	private String nome;


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
