package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

public class Commento
{	
	//testo del commento
	private String testo;
	
	//data in cui è stato scritto commento
	private Date data;
	
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
		return data;
	}

	public void setData(Date data)
	{
		this.data = data;
	}

	public UtenteCorsi getAutore()
	{
		return autore;
	}

	public void setAutore(UtenteCorsi autore)
	{
		this.autore = autore;
	}
	
	
}
