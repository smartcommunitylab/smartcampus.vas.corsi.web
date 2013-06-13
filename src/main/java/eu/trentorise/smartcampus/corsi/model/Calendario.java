package eu.trentorise.smartcampus.corsi.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class Calendario {

	
	@Id
	@GeneratedValue
	private int id;

	@OneToOne
	@JoinColumn(name = "STUDENTE_ID")
	private Studente studente;

	@OneToOne
	@JoinColumn(name = "EVENTO_ID")
	private Evento evento;

	// testo del commento
	@Column(name = "CANCELLATO")
	private boolean cancellato;
	

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "calendario", cascade = CascadeType.ALL)
	private List<Evento> eventi_list;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Studente getStudente() {
		return studente;
	}

	public void setStudente(Studente studente) {
		this.studente = studente;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public boolean isCancellato() {
		return cancellato;
	}

	public void setCancellato(boolean cancellato) {
		this.cancellato = cancellato;
	}

	public List<Evento> getEventi_list() {
		return eventi_list;
	}

	public void setEventi_list(List<Evento> eventi_list) {
		this.eventi_list = eventi_list;
	}

//	public List<Evento> getListaEventi() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
	
	
	
	
}
