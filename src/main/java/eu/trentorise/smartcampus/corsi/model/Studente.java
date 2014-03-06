package eu.trentorise.smartcampus.corsi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@NamedQueries({ @NamedQuery(name = "Studente.findStudenteByUserId", query = "select c from Studente c where c.id = ?1") })
public class Studente extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8681710690984309605L;

	@Id
	private long id;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "COGNOME")
	private String cognome;

	//@Column(name = "CORSO_LAUREA")
//	@ManyToOne
//	@JoinColumn(name = "ID_CORSOLAUREA")
//	private CorsoLaurea corso_laurea;

	//@Column(name = "DIPARTIMENTO")
//	@ManyToOne
//	@JoinColumn(name = "ID_DIPARTIMENTO")
	//private Dipartimento dipartimento;

	@Column(name = "ANNO_CORSO")
	private String anno_corso;

	@Column(name = "ENROLLMENT_YEAR")
	private String enrollmentYear;
	
	@Column(name = "NATION")
	private String nation;
	
	@Column(name = "ACADEMIC_YEAR")
	private String academicYear;
	
	@Column(name = "SUPLEMENTARY_YEAR")
	private String suplementaryYear;
	
	@Column(name = "CFU")
	private String cfu;
	
	@Column(name = "CFU_TOTAL")
	private String cfuTotal;
	
	@Column(name = "MARKS_NUMBER")
	private String marksNumber;
	
	@Column(name = "MARKS_AVERAGE")
	private String marksAverage;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "DATE_BIRTH")
	private String dateOfBirth;
	
	@Column(name = "PHONE")
	private String phone;
	
	@Column(name = "MOBILE")
	private String mobile;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "CDS")
	private String cds;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "USER_SOCIAL_ID")
	private long userSocialId;
	
	
	@Column(name = "GRUPPI_DI_STUDIO")
	private String idsGruppiDiStudio;
	
	@Transient
	private List<GruppoDiStudio> gruppiDiStudio;


	public String getIdsGruppiDiStudio() {
		return idsGruppiDiStudio;
	}

	public void setIdsGruppiDiStudio(String idsGruppiDiStudio) {
		this.idsGruppiDiStudio = idsGruppiDiStudio;
	}

	public List<GruppoDiStudio> getGruppiDiStudio() {
		return gruppiDiStudio;
	}

	public void setGruppiDiStudio(List<GruppoDiStudio> gruppiDiStudio) {
		this.gruppiDiStudio = gruppiDiStudio;
	}

	public long getUserSocialId() {
		return userSocialId;
	}

	public void setUserSocialId(long userSocialId) {
		this.userSocialId = userSocialId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

//	public CorsoLaurea getCorso_laurea() {
//		return corso_laurea;
//	}
//
//	public void setCorso_laurea(CorsoLaurea corso_laurea) {
//		this.corso_laurea = corso_laurea;
//	}

	public String getAnno_corso() {
		return anno_corso;
	}

	public void setAnno_corso(String anno_corso) {
		this.anno_corso = anno_corso;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void addGruppoStudente(Studente studente, long idGruppoDaAggiungere) {
		// TODO Auto-generated method stub
		studente.setIdsGruppiDiStudio(studente.getIdsGruppiDiStudio()+String.valueOf(idGruppoDaAggiungere)+",");		
	}

	public void removeGruppoStudente(Studente studente, long id2) {
		// TODO Auto-generated method stub
		String studenteGruppoIds = null;
		studenteGruppoIds = studente.getIdsGruppiDiStudio();
		
		String[] listS = studenteGruppoIds.split(",");
		
		String studenteGruppoAggiornato = "";
		
		for (String s : listS) {
			if(!s.equals(String.valueOf(id2))){
				studenteGruppoAggiornato = studenteGruppoAggiornato.concat(s.toString()+",");
			}
		}
		studente.setIdsGruppiDiStudio(studenteGruppoAggiornato);
	}

	public String getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(String enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public String getSuplementaryYear() {
		return suplementaryYear;
	}

	public void setSuplementaryYear(String suplementaryYear) {
		this.suplementaryYear = suplementaryYear;
	}

	public String getCfu() {
		return cfu;
	}

	public void setCfu(String cfu) {
		this.cfu = cfu;
	}

	public String getCfuTotal() {
		return cfuTotal;
	}

	public void setCfuTotal(String cfuTotal) {
		this.cfuTotal = cfuTotal;
	}

	public String getMarksNumber() {
		return marksNumber;
	}

	public void setMarksNumber(String marksNumber) {
		this.marksNumber = marksNumber;
	}

	public String getMarksAverage() {
		return marksAverage;
	}

	public void setMarksAverage(String marksAverage) {
		this.marksAverage = marksAverage;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCds() {
		return cds;
	}

	public void setCds(String cds) {
		this.cds = cds;
	}

	

}
