package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CorsoLite extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1619282265752237328L;

	// id del corso
	@Id
	@GeneratedValue
	private long id;

	// nome del corso
	@Column(name = "NOME")
	private String nome;

//	// nome del dipartimento
//	@Column(name = "FK_DIPARTIMENTO")
//	private long dipartimento;

	public CorsoLite() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
