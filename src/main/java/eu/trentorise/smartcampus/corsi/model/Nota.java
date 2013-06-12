package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Nota
{	
	//id della nota
	@Id
	@GeneratedValue
	private long id;
	
	//testo della nota
	@Column(name = "TESTO")
	private String testo;
	
	//data in cui e' stata scritta la nota
	@Column(name = "DATA_INS")
	private Date data_inserimento;
	
	//utente che ha scritto la nota
	@Column(name = "AUTORE")
	private UtenteCorsi autore;
		
	
	public Nota()
	{
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getTesto() {
		return testo;
	}


	public void setTesto(String testo) {
		this.testo = testo;
	}


	public Date getData_inserimento() {
		return data_inserimento;
	}


	public void setData_inserimento(Date data_inserimento) {
		this.data_inserimento = data_inserimento;
	}


	public UtenteCorsi getAutore() {
		return autore;
	}


	public void setAutore(UtenteCorsi autore) {
		this.autore = autore;
	}

	
}
