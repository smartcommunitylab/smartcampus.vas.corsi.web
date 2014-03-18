package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "AttivitaDiStudio.findAttByIdGds", query = "select a from AttivitaDiStudio a where a.gruppo = ?1")
public class AttivitaDiStudio extends Evento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// gds di riferimentoì
	@JoinColumn(name = "GRUPPODISTUDIO_ID")
	private long gruppo;
	
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


	public long getGruppo() {
		return gruppo;
	}


	public void setGruppo(long gruppo) {
		this.gruppo = gruppo;
	}


//	public Collection<Allegato> getAllegato() {
//		return allegato;
//	}
//
//
//	public void setAllegato(Collection<Allegato> allegato) {
//		this.allegato = allegato;
//	}


//	public Collection<Servizio> getServizio() {
//		return servizio;
//	}
//
//
//	public void setServizio(Collection<Servizio> servizio) {
//		this.servizio = servizio;
//	}



}
