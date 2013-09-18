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

public class Servizio {
	// id del commento
	@Id
	@GeneratedValue
	private int id;

	// testo del commento
	@Column(name = "TIPO")
	private String tipo;

	// data in cui e' stato scritto commento
	@Column(name = "DATA_INSERIMENTO")
	private Boolean isAttivo;


	public Servizio() {
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public Boolean getIsAttivo() {
		return isAttivo;
	}


	public void setIsAttivo(Boolean isAttivo) {
		this.isAttivo = isAttivo;
	}

	
}
