package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Commento {
	// id del commento
	@Id
	@GeneratedValue
	private int id;

	// testo del commento
	@Column(name = "testo")
	private String testo;

	// data in cui e' stato scritto commento
	@Column(name = "data_inserimento")
	private Date data_inserimento;

	// utente che ha scritto commento
	@Column(name = "id_studente")
	private long id_studente;

	// valutazione commento
	@Column(name = "valutazione")
	private float valutazione;

	// valutazione commento
	@Column(name = "id_corso")
	private long id_corso;

	public Commento() {
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public Date getData() {
		return data_inserimento;
	}

	public void setData(Date data_inserimento) {
		this.data_inserimento = data_inserimento;
	}

	public long getId_corso() {
		return id_corso;
	}

	public Date getData_inserimento() {
		return data_inserimento;
	}

	public long getId_studente() {
		return id_studente;
	}

	public void setData_inserimento(Date data_inserimento) {
		this.data_inserimento = data_inserimento;
	}

	public void setId_corso(long id_corso) {
		this.id_corso = id_corso;
	}

	public void setId_studente(long id_studente) {
		this.id_studente = id_studente;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getValutazione() {
		return valutazione;
	}

	public void setValutazione(float valutazione) {
		this.valutazione = valutazione;
	}

}
