package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import eu.trentorise.smartcampus.profileservice.model.BasicProfile;


@Entity
public class CorsoLaurea extends BasicProfile{

	private static final long serialVersionUID = 8681730600984301605L;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "NOME")
	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "DIPARTIMENTO_ID")
	private Dipartimento dipartimento;

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

	public Dipartimento getDipartimento() {
		return dipartimento;
	}

	public void setDipartimento(Dipartimento dipartimento) {
		this.dipartimento = dipartimento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
