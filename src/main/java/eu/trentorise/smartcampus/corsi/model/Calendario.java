package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({ @NamedQuery(name = "Calendario.getCalendarioByIdStudente", query = "select c from Studente c where c.userSCId = ?1") })
public class Calendario {

	@Id
	@GeneratedValue
	private long id;

	// testo del commento
	@OneToOne
	@JoinColumn(name = "CALENDAR_ID")
	private long id_calendario;

	// testo del commento
	@OneToOne
	@JoinColumn(name = "STUDENTE_ID")
	private Studente studente;

	// testo del commento
	@OneToOne
	@JoinColumn(name = "EVENTO_ID")
	private Evento evento;

	// testo del commento
	@Column(name = "CANCELLATO")
	private boolean cancellato;

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "calendario", cascade =
	// CascadeType.ALL)
	// private List<Evento> eventi_list;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isCancellato() {
		return cancellato;
	}

	public void setCancellato(boolean cancellato) {
		this.cancellato = cancellato;
	}

	// public List<Evento> getEventi_list() {
	// return eventi_list;
	// }
	//
	// public void setEventi_list(List<Evento> eventi_list) {
	// this.eventi_list = eventi_list;
	// }

	// public List<Evento> getListaEventi() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//

}
