package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "Commento.getCommentoByCorsoId",
query = "select c from Commento c where c.corso = ?1")
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
	@ManyToOne
	@JoinColumn(name = "corso_id")
	private Corso corso;

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

	

	public Date getData_inserimento() {
		return data_inserimento;
	}

	public long getId_studente() {
		return id_studente;
	}

	public void setData_inserimento(Date data_inserimento) {
		this.data_inserimento = data_inserimento;
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

	public Corso getCorso() {
		return corso;
	}

	public void setCorso(Corso corso) {
		this.corso = corso;
	}

}
