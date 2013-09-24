package eu.trentorise.smartcampus.corsi.model;

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

	@Column(name = "CORSO_LAUREA")
	private String corso_laurea;

	// @Column(name = "DIPARTIMENTO")
	@ManyToOne
	@JoinColumn(name = "ID_DIPARTIMENTO")
	private Dipartimento dipartimento;

	@Column(name = "ANNO_CORSO")
	private String anno_corso;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "USER_SC_ID")
	private long userSCId;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "Frequenze", joinColumns = @JoinColumn(name = "STUDENTE_ID"), inverseJoinColumns = @JoinColumn(name = "CORSO_ID"))
	private Collection<Corso> corsi;

	@Column(name = "ESAMI_SUPERATI")
	private String idsCorsiSuperati;
	
	@Transient
	private List<CorsoLite> corsiSuperati;
	
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

	public Collection<Corso> getCorsi() {
		return corsi;
	}

	public void setCorsi(Collection<Corso> corsi) {
		this.corsi = corsi;
	}

	public long getUserSCId() {
		return userSCId;
	}

	public void setUserSCId(long userSCId) {
		this.userSCId = userSCId;
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

	public String getCorso_laurea() {
		return corso_laurea;
	}

	public void setCorso_laurea(String corso_laurea) {
		this.corso_laurea = corso_laurea;
	}

	public Dipartimento getDipartimento() {
		return dipartimento;
	}

	public void setDipartimento(Dipartimento dipartimento) {
		this.dipartimento = dipartimento;
	}

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

	public String getIdsCorsiSuperati() {
		return idsCorsiSuperati;
	}

	public void setIdsCorsiSuperati(String idsCorsiSuperati) {
		this.idsCorsiSuperati = idsCorsiSuperati;
	}

	public List<CorsoLite> getCorsiSuperati() {
		return corsiSuperati;
	}

	public void setCorsiSuperati(List<CorsoLite> corsiSuperati) {
		this.corsiSuperati = corsiSuperati;
	}


//	public Collection<GruppoDiStudio> getGds() {
//		return gds;
//	}
//
//	public void setGds(Collection<GruppoDiStudio> gds) {
//		this.gds = gds;
//	}
	
	

}
