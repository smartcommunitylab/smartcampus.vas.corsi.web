package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;
import java.util.List;


public class Corsi
{
	//id del corso
	private int id;
	
	//nome del corso
	private String nome;
	
	//data di inizio del corso
	private Date data_inizio;
	
	//data di fine del corso
	private Date data_fine;
	
	//descrizione del corso
	private String descrizione;
	
	//lista dei commenti del corso
	private List<Commento> commenti;
	
	public Corsi()
	{
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public Date getData_inizio()
	{
		return data_inizio;
	}

	public void setData_inizio(Date data_inizio)
	{
		this.data_inizio = data_inizio;
	}

	public Date getData_fine()
	{
		return data_fine;
	}

	public void setData_fine(Date data_fine)
	{
		this.data_fine = data_fine;
	}

	public String getDescrizione()
	{
		return descrizione;
	}

	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}
	
}
