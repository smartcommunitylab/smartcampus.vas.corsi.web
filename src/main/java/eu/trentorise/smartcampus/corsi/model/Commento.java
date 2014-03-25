package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import eu.trentorise.smartcampus.mediation.model.CommentBaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
		@NamedQuery(name = "Commento.getCommentoByCorsoApproved", query = "select c from Commento c where c.corso = ?1 and c.approved = true"),
		@NamedQuery(name = "Commento.getCommentoByStudenteApproved", query = "select c from Commento c where c.id_studente = ?1 and c.corso = ?2 and c.approved = true"),
		@NamedQuery(name = "Commento.getCommentoByCorsoAll", query = "select c from Commento c where c.corso = ?1"),
		@NamedQuery(name = "Commento.getCommentoByStudenteAll", query = "select c from Commento c where c.id_studente = ?1 and c.corso = ?2") })
public class Commento extends CommentBaseEntity {

	public Commento() {
		super();
	}

	// corso di riferimento
	@JoinColumn(name = "AD_ID")
	private long corso;

	// utente che ha scritto commento
	@JoinColumn(name = "ID_STUDENTE")
	private long id_studente;

	// utente che ha scritto commento
	@Column(name = "NOME_STUDENTE")
	private String nome_studente;

	// data in cui e' stato scritto commento
	@Column(name = "DATA_INSERIMENTO")
	private String data_inserimento;

	// valutazione contenuto
	@Column(name = "RATING_CONTENUTO")
	private Float rating_contenuto;

	// valutazione carico studio
	@Column(name = "RATING_CARICO_STUDIO")
	private Float rating_carico_studio;

	// valutazione lezioni
	@Column(name = "RATING_LEZIONI")
	private Float rating_lezioni;

	// valutazione materiali
	@Column(name = "RATING_MATERIALI")
	private Float rating_materiali;

	// valutazione esame
	@Column(name = "RATING_ESAME")
	private Float rating_esame;

	// @Override
	// @Id
	// public Long getId() {
	// return id;
	// }
	//
	// public void setId(Long id) {
	// this.id = id;
	// }

	public long getId_studente() {
		return id_studente;
	}

	public long getCorso() {
		return corso;
	}

	public void setCorso(long corso) {
		this.corso = corso;
	}

	public void setId_studente(long id_studente) {
		this.id_studente = id_studente;
	}

	// public String getTesto() {
	// return testo;
	// }
	//
	// public void setTesto(String testo) {
	// this.testo = testo;
	// }

	public String getData_inserimento() {
		return data_inserimento;
	}

	public void setData_inserimento(String data_inserimento) {
		this.data_inserimento = data_inserimento;
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

	// @Override
	// public boolean isApproved() {
	// return approved;
	// }
	//
	// @Override
	// public void setApproved(boolean approved) {
	// this.approved = approved;
	// }

	public String getNome_studente() {
		return nome_studente;
	}

	public void setNome_studente(String nome_studente) {
		this.nome_studente = nome_studente;
	}

}