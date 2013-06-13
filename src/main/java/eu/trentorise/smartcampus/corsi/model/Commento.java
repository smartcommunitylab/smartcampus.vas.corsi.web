package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "Commento.getCommentoByCorsoId", query = "select c from Commento c where c.corso = ?1")
public class Commento {
	// id del commento
	@Id
	@GeneratedValue
	private int id;

	// corso di riferimento
	@ManyToOne
	@JoinColumn(name = "CORSO_ID")
	private Corso corso;

	// utente che ha scritto commento
	@ManyToOne
	@JoinColumn(name = "ID_STUDENTE")
	private Studente id_studente;

	// testo del commento
	@Column(name = "TESTO")
	private String testo;

	// data in cui e' stato scritto commento
	@Column(name = "DATA_INSERIMENTO")
	private Date data_inserimento;

	// valutazione contenuto
	@Column(name = "RATING_CONTENUTO")
	private int rating_contenuto;

	// valutazione carico studio
	@Column(name = "RATING_CARICO_STUDIO")
	private int rating_carico_studio;

	// valutazione lezioni
	@Column(name = "RATING_LEZIONI")
	private int rating_lezioni;

	// valutazione materiali
	@Column(name = "RATING_MATERIALI")
	private int rating_materiali;

	// valutazione esame
	@Column(name = "RATING_ESAME")
	private int rating_esame;

	public Commento() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Corso getCorso() {
		return corso;
	}

	public void setCorso(Corso corso) {
		this.corso = corso;
	}

	public Studente getId_studente() {
		return id_studente;
	}

	public void setId_studente(Studente id_studente) {
		this.id_studente = id_studente;
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

	public int getRating_contenuto() {
		return rating_contenuto;
	}

	public void setRating_contenuto(int rating_contenuto) {
		this.rating_contenuto = rating_contenuto;
	}

	public int getRating_carico_studio() {
		return rating_carico_studio;
	}

	public void setRating_carico_studio(int rating_carico_studio) {
		this.rating_carico_studio = rating_carico_studio;
	}

	public int getRating_lezioni() {
		return rating_lezioni;
	}

	public void setRating_lezioni(int rating_lezioni) {
		this.rating_lezioni = rating_lezioni;
	}

	public int getRating_materiali() {
		return rating_materiali;
	}

	public void setRating_materiali(int rating_materiali) {
		this.rating_materiali = rating_materiali;
	}

	public int getRating_esame() {
		return rating_esame;
	}

	public void setRating_esame(int rating_esame) {
		this.rating_esame = rating_esame;
	}

}
