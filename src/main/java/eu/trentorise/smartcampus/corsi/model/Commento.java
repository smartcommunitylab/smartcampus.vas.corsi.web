package eu.trentorise.smartcampus.corsi.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({@NamedQuery(name = "Commento.getCommentoByCorso", query = "select c from Commento c where c.corso = ?1"),
@NamedQuery(name = "Commento.getCommentoByStudente", query = "select c from Commento c where c.id_studente = ?1 and c.corso = ?2")})
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
	private String data_inserimento;

	// valutazione contenuto
	@Column(name = "RATING_CONTENUTO")
	private Integer rating_contenuto;

	// valutazione carico studio
	@Column(name = "RATING_CARICO_STUDIO")
	private Integer rating_carico_studio;

	// valutazione lezioni
	@Column(name = "RATING_LEZIONI")
	private Integer rating_lezioni;

	// valutazione materiali
	@Column(name = "RATING_MATERIALI")
	private Integer rating_materiali;

	// valutazione esame
	@Column(name = "RATING_ESAME")
	private Integer rating_esame;

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

	public String getData_inserimento() {
		return data_inserimento;
	}

	public void setData_inserimento(String data_inserimento) {
		this.data_inserimento = data_inserimento;
	}

	public Integer getRating_contenuto() {
		return rating_contenuto;
	}

	public void setRating_contenuto(Integer rating_contenuto) {
		this.rating_contenuto = rating_contenuto;
	}

	public Integer getRating_carico_studio() {
		return rating_carico_studio;
	}

	public void setRating_carico_studio(int rating_carico_studio) {
		this.rating_carico_studio = rating_carico_studio;
	}

	public Integer getRating_lezioni() {
		return rating_lezioni;
	}

	public void setRating_lezioni(Integer rating_lezioni) {
		this.rating_lezioni = rating_lezioni;
	}

	public Integer getRating_materiali() {
		return rating_materiali;
	}

	public void setRating_materiali(int rating_materiali) {
		this.rating_materiali = rating_materiali;
	}

	public Integer getRating_esame() {
		return rating_esame;
	}

	public void setRating_esame(Integer rating_esame) {
		this.rating_esame = rating_esame;
	}

}
