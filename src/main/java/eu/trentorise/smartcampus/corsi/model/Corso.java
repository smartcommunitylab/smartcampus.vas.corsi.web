package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;


@Entity
public class Corso extends CorsoLite
{
		
	//data di inizio del corso
	@Column(name = "data_inizio")
	private Date data_inizio;
	
	//data di fine del corso
	@Column(name = "data_fine")
	private Date data_fine;
	
	//descrizione del corso
	@Lob
	@Column(name = "descrizione", length = 100000)
	private String descrizione;
	
	//lista dei commenti del corso
	@OneToMany(fetch=FetchType.EAGER, mappedBy="corso", cascade = CascadeType.ALL)
	private List<Commento> commenti;
	
	//valutazione media di tutti gli UtenteCorsi
	@Column(name = "valutazione_media")
	private float valutazione_media;
	
	//seguo o non seguo il corso
	@Column(name = "seguito")
	private boolean seguito;
	
	//aula del corso
	@Column(name = "aula")
	private String aula;
	
	
	public Corso()
	{
		
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

	public void setCommenti(List<Commento> commenti2)
	{
		this.commenti = commenti2;
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
