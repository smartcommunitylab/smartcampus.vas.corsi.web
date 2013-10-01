package eu.trentorise.smartcampus.corsi.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQueries({
		@NamedQuery(name = "Corso.findCorsoByDipartimentoId", query = "select c from Corso c where c.id_dipartimento = ?1"),
		@NamedQuery(name = "Corso.findCorsoByCorsoLaureaId", query = "select c from Corso c where c.id_corsoLaurea = ?1") })
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

	// data di inizio del corso
	@Column(name = "CorsoLaurea_id")
	private long id_corsoLaurea;

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

	// valutazione contenuto
	@Transient
	private Float rating_contenuto;

	// valutazione carico studio
	@Transient
	private Float rating_carico_studio;

	// valutazione lezioni
	@Transient
	private Float rating_lezioni;

	// valutazione materiali
	@Transient
	private Float rating_materiali;

	// valutazione esame
	@Transient
	private Float rating_esame;

	public Date getData_inizio() {
		return data_inizio;
	}

	public void setData_inizio(Date data_inizio) {
		this.data_inizio = data_inizio;
	}

	public long getId_dipartimento() {
		return id_dipartimento;
	}

	public void setId_dipartimento(long id_dipartimento) {
		this.id_dipartimento = id_dipartimento;
	}

	public long getId_corsoLaurea() {
		return id_corsoLaurea;
	}

	public void setId_corsoLaurea(long id_corsoLaurea) {
		this.id_corsoLaurea = id_corsoLaurea;
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

	public Float getRating_contenuto() {
		return rating_contenuto;
	}

	public void setRating_contenuto(Float rating_contenuto) {
		this.rating_contenuto = rating_contenuto;
	}

	public Float getRating_carico_studio() {
		return rating_carico_studio;
	}

	public void setRating_carico_studio(Float rating_carico_studio) {
		this.rating_carico_studio = rating_carico_studio;
	}

	public Float getRating_lezioni() {
		return rating_lezioni;
	}

	public void setRating_lezioni(Float rating_lezioni) {
		this.rating_lezioni = rating_lezioni;
	}

	public Float getRating_materiali() {
		return rating_materiali;
	}

	public void setRating_materiali(Float rating_materiali) {
		this.rating_materiali = rating_materiali;
	}

	public Float getRating_esame() {
		return rating_esame;
	}

	public void setRating_esame(Float rating_esame) {
		this.rating_esame = rating_esame;
	}




}
