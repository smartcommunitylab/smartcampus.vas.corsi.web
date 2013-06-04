package eu.trentorise.smartcampus.corsi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Corso
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
	private ArrayList<Commento> commenti;
	
	//valutazione media di tutti gli UtenteCorsi
	private float valutazione_media;
	
	//seguo o non seguo il corso
	private boolean seguito;
	
	//aula del corso
	private String aula;
	
	
	public Corso()
	{
		this.commenti = new ArrayList();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id_corso)
	{
		this.id = id_corso;
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

	public List<Commento> getCommenti()
	{
		return commenti;
	}

	public void setCommenti(ArrayList<Commento> commenti)
	{
		this.commenti = commenti;
	}

	public float getValutazione_media()
	{
		return valutazione_media;
	}

	public void setValutazione_media(float valutazione_media)
	{
		this.valutazione_media = valutazione_media;
	}

	public Boolean getSeguito() {
		return seguito;
	}

	public void setSeguito(Boolean seguito) {
		this.seguito = seguito;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}
	
}
