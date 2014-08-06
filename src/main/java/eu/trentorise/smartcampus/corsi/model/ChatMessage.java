package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ChatMessage {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "AUTHOR")
	private String nome_studente;
	
	@Column(name = "STUDENT_ID")
	private long id_studente;

	@Column(name = "TIME")
	private long data;
	
	@Column(name = "TESTO")
	private String testo;
	
	@Column(name = "GDS_ID")
	private long gds;
	
	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public ChatMessage() {
	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public String getNome_studente() {
		return nome_studente;
	}

	public void setNome_studente(String nome_studente) {
		this.nome_studente = nome_studente;
	}

	public long getId_studente() {
		return id_studente;
	}

	public void setId_studente(long id_studente) {
		this.id_studente = id_studente;
	}

	public long getGds() {
		return gds;
	}

	public void setGds(long gds) {
		this.gds = gds;
	}


	
}
