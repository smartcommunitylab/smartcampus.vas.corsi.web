package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

public class Commento
{	
	//id del commento
	private int id;
	
	//testo del commento
	private String testo;
	
	//data in cui e' stato scritto commento
	private Date data_inserimento;
	
	//utente che ha scritto commento
	private UtenteCorsi autore;
	
	
	public Commento()
	{
	}

	public String getTesto()
	{
		return testo;
	}

	public void setTesto(String testo)
	{
		this.testo = testo;
	}

	public Date getData()
	{
		return data_inserimento;
	}

	public void setData(Date data_inserimento)
	{
		this.data_inserimento = data_inserimento;
	}

	public UtenteCorsi getAutore()
	{
		return autore;
	}

	public void setAutore(UtenteCorsi autore)
	{
		this.autore = autore;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
