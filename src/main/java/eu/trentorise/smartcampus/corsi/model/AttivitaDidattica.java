package eu.trentorise.smartcampus.corsi.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AttivitaDidattica extends BasicEntity {

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
	
//	@Column(name = "FAT_PART")
//	private List<String> fatPart;
//
//	@Column(name = "DOM_PART")
//	private List<String> domPart;

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

	
//	public List<String> getFatPart() {
//		return fatPart;
//	}
//
//	public void setFatPart(List<String> fatPart) {
//		this.fatPart = fatPart;
//	}
//
//	public List<String> getDomPart() {
//		return domPart;
//	}
//
//	public void setDomPart(List<String> domPart) {
//		this.domPart = domPart;
//	}
//	
	
	
}
