package eu.trentorise.smartcampus.corsi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity
public class Studente {
	
	@Id
	@GeneratedValue
	private int id;

	@JoinColumn(name = "NOME")
	private String nome;

	@Column(name = "COGNOME")
	private long cognome;

	@Column(name = "corso_laurea")
	private String corso_laurea;
	
	@Column(name = "DIPARTIMENTO")
	private String dipartimento;
	
	@Column(name = "anno_corso")
	private String anno_corso;
	
	@Column(name = "email")
	private String email;
	
	@OneToOne
	@JoinColumn(name = "studente_id")
	private Calendario calendario;
}
