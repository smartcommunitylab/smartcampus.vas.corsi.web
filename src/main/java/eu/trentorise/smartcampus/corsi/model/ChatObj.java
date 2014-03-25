package eu.trentorise.smartcampus.corsi.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ChatObj {
	// id del gruppo
	@Id
	@GeneratedValue
	private long id;

	// Nome del gruppo
	@Column(name = "AUTHOR")
	private String nome;

	// Nome del gruppo
	@Column(name = "TIME")
	private Date data;

	public ChatObj() {
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
