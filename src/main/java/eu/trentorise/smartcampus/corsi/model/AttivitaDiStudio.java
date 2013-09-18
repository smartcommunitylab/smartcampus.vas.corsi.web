package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity

public class AttivitaDiStudio extends Evento{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// id attività
	@Id
	@GeneratedValue
	private int id;

	// corso di riferimento
	@ManyToOne
	@JoinColumn(name = "CORSO_ID")
	private Allegato allegato;

	// topic
	@Column(name = "Topic")
	private String topic;


	public AttivitaDiStudio() {
	}


	public Allegato getAllegato() {
		return allegato;
	}


	public void setAllegato(Allegato allegato) {
		this.allegato = allegato;
	}


	public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}




}
