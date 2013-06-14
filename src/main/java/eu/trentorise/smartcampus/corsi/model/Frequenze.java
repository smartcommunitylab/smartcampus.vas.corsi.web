package eu.trentorise.smartcampus.corsi.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Frequenze {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	@JoinColumn(name = "STUDENTE_ID")
	private Studente studente;
	
	// testo del commento
	@ManyToOne
	@JoinColumn(name = "CORSO_ID")
	private Corso corso;

	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Studente getStudente() {
		return studente;
	}

	public void setStudente(Studente studente) {
		this.studente = studente;
	}

	public Corso getCorso() {
		return corso;
	}

	public void setCorso(Corso corso) {
		this.corso = corso;
	}

	
	
}
