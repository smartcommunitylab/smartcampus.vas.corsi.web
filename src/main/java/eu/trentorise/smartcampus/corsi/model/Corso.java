package eu.trentorise.smartcampus.corsi.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "Corso.findCorsoByDipartimentoId", query = "select c from Corso c where c.id_dipartimento = ?1") })
public class Corso extends CorsoLite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// data di inizio del corso
	@Column(name = "DATA_INIZIO")
	private Date data_inizio;

	// data di inizio del corso
	@Column(name = "Dipartimento_id")
	private long id_dipartimento;

	// data di fine del corso
	@Column(name = "DATA_FINE")
	private Date data_fine;

	// descrizione del corso
	@Lob
	@Column(name = "DESCRIZIONE", length = 100000)
	private String descrizione;

	// valutazione media di tutti gli UtenteCorsi
	@Column(name = "VALUTAZIONE_MEDIA")
	private float valutazione_media;

	// seguo o non seguo il corso
	@Column(name = "SEGUITO")
	private boolean seguito;

	// lista di note
	// private List<Note> note;

	@ManyToMany(mappedBy = "corsi")
	private Collection<Studente> studentiFrequentanti;

	public Corso() {

	}

	public Date getData_inizio() {
		return data_inizio;
	}

	public void setData_inizio(Date data_inizio) {
		this.data_inizio = data_inizio;
	}

	public Date getData_fine() {
		return data_fine;
	}

	public void setData_fine(Date data_fine) {
		this.data_fine = data_fine;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public float getValutazione_media() {
		return valutazione_media;
	}

	public void setValutazione_media(float valutazione_media) {
		this.valutazione_media = valutazione_media;
	}

	public boolean isSeguito() {
		return seguito;
	}

	public void setSeguito(boolean seguito) {
		this.seguito = seguito;
	}

	public long getId_dipartimento() {
		return id_dipartimento;
	}

	public void setId_dipartimento(long id_dipartimento) {
		this.id_dipartimento = id_dipartimento;
	}

	public Collection<Studente> getStudentiFrequentanti() {
		return studentiFrequentanti;
	}

	public void setStudentiFrequentanti(
			Collection<Studente> studentiFrequentanti) {
		this.studentiFrequentanti = studentiFrequentanti;
	}

}
