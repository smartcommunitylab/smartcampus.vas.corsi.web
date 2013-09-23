package eu.trentorise.smartcampus.corsi.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "AttivitaDiStudio.findAttByIdGds(id_gruppidistudio)", query = "select a from AttivitaDiStudio a where a.gruppo = ?1")
public class AttivitaDiStudio extends Evento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// corso di riferimento
	@ManyToOne
	@JoinColumn(name = "CORSO_ID")
	private Corso corso;

	// gds di riferimento
	@ManyToOne
	@JoinColumn(name = "GDS_ID")
	private GruppoDiStudio gruppo;

	@OneToMany
	@JoinColumn(name = "ALLEGATO_ID")
	private Collection<Allegato> allegato;

	@OneToMany
	@JoinColumn(name = "SERVIZIO_ID")
	private Collection<Servizio> servizio;

	// topic
	@Column(name = "Topic")
	private String topic;

	public AttivitaDiStudio() {
	}


	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Corso getCorso() {
		return corso;
	}

	public void setCorso(Corso corso) {
		this.corso = corso;
	}


	public Collection<Allegato> getAllegato() {
		return allegato;
	}


	public void setAllegato(Collection<Allegato> allegato) {
		this.allegato = allegato;
	}


	public Collection<Servizio> getServizio() {
		return servizio;
	}


	public void setServizio(Collection<Servizio> servizio) {
		this.servizio = servizio;
	}



	public GruppoDiStudio getGruppo() {
		return gruppo;
	}

	public void setGruppo(GruppoDiStudio gruppo) {
		this.gruppo = gruppo;
	}

	
}
