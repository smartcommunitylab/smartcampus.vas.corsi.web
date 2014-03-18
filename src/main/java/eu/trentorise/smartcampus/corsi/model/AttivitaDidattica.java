package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "AttivitaDidattica.findAttivitaDidatticaByCdsId", query = "select ad from AttivitaDidattica ad where ad.cds_id = ?1")})
public class AttivitaDidattica extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8529028714305523748L;

	@Id
	@Column(name = "AD_ID")
	private long adId;

	@Column(name = "AD_COD")
	private String adCod;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ID_CDS")
	private long cds_id;

	@Column(name = "ORD_YEAR")
	private String ordYear;

	@Column(name = "OFF_YEAR")
	private String offYear;

	// valutazione media di tutti gli UtenteCorsi
	@Column(name = "VALUTAZIONE_MEDIA")
	private float valutazione_media;

	// valutazione contenuto
	@Column(name = "RATING_CONTENUTO_MEDIO")
	private Float rating_contenuto;

	// valutazione carico studio
	@Column(name = "RATING_CARICO_STUDIO_MEDIO")
	private Float rating_carico_studio;

	// valutazione lezioni
	@Column(name = "RATING_LEZIONI_MEDIO")
	private Float rating_lezioni;

	// valutazione materiali
	@Column(name = "RATING_MATERIALI_MEDIO")
	private Float rating_materiali;

	// valutazione esame
	@Column(name = "RATING_ESAME_MEDIO")
	private Float rating_esame;

	// @Column(name = "FAT_PART")
	// private List<String> fatPart;
	//
	// @Column(name = "DOM_PART")
	// private List<String> domPart;

	public long getAdId() {
		return adId;
	}

	public void setAdId(long adId) {
		this.adId = adId;
	}

	public String getAdCod() {
		return adCod;
	}

	public void setAdCod(String adCod) {
		this.adCod = adCod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCds_id() {
		return cds_id;
	}

	public void setCds_id(long cds_id) {
		this.cds_id = cds_id;
	}

	public String getOrdYear() {
		return ordYear;
	}

	public void setOrdYear(String ordYear) {
		this.ordYear = ordYear;
	}

	public String getOffYear() {
		return offYear;
	}

	public void setOffYear(String offYear) {
		this.offYear = offYear;
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

	
	
	// public List<String> getFatPart() {
	// return fatPart;
	// }
	//
	// public void setFatPart(List<String> fatPart) {
	// this.fatPart = fatPart;
	// }
	//
	// public List<String> getDomPart() {
	// return domPart;
	// }
	//
	// public void setDomPart(List<String> domPart) {
	// this.domPart = domPart;
	// }
	//

}
