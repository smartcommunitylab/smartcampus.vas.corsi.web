package eu.trentorise.smartcampus.corsi.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "Studente.findStudenteByUserId", query = "select c from Studente c where c.userSCId = ?1") })
public class Studente extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8681710690984309605L;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "COGNOME")
	private String cognome;

	@Column(name = "CORSO_LAUREA")
	private String corso_laurea;

	@Column(name = "DIPARTIMENTO")
	private String dipartimento;

	@Column(name = "ANNO_CORSO")
	private String anno_corso;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "USER_SC_ID")
	private long userSCId;

	@ManyToMany
	@JoinTable(name = "Frequenze", joinColumns = @JoinColumn(name = "STUDENTE_ID"), inverseJoinColumns = @JoinColumn(name = "CORSO_ID"))
	private Collection<Corso> corsi;

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

	public String getDipartimento() {
		return dipartimento;
	}

	public void setDipartimento(String dipartimento) {
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

}
