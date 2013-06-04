package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

public class Nota
{	
	//id della nota
	private int id;
	
	//testo della nota
	private String testo;
	
	//data in cui e' stata scritta la nota
	private Date data_inserimento;
	
	//utente che ha scritto la nota
	private UtenteCorsi autore;
		
	
	public Nota()
	{
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
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
