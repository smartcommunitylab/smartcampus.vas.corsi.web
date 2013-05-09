package eu.trentorise.smartcampus.corsi.model;

import java.util.ArrayList;
import java.util.Date;

public class Evento
{
	//id dell'evento
	private int id;
	
	//nome dell'evento
	private String nome;
	
	//luogo dell'evento
	private String luogo;
	
	//data di inizio dell'evento
	private Date data_inizio;
	
	//data di fine dell'evento
	private Date data_fine;
	
	//ricorrenza dell'evento
	private boolean ricorrente;
	
	//corso a cui è relativo l'evento
	private CorsiLite corso;
	
	//elenco delle note sul corso
	private ArrayList<String> note;

	
	public Evento()
	{
		setNote(new ArrayList<String>());
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getLuogo() {
		return luogo;
	}


	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}


	public Date getData_inizio() {
		return data_inizio;
	}


	public void setData_inizio(Date data_inizio) {
		this.data_inizio = data_inizio;
	}


	public Date getData_fine() {
		return data_fine;
	}


	public void setData_fine(Date data_fine) {
		this.data_fine = data_fine;
	}


	public boolean isRicorrente() {
		return ricorrente;
	}


	public void setRicorrente(boolean ricorrente) {
		this.ricorrente = ricorrente;
	}


	public CorsiLite getCorso() {
		return corso;
	}


	public void setCorso(CorsiLite corso) {
		this.corso = corso;
	}


	public ArrayList<String> getNote() {
		return note;
	}


	public void setNote(ArrayList<String> note) {
		this.note = note;
	}
}
