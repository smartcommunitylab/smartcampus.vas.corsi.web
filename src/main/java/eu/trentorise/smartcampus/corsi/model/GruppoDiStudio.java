package eu.trentorise.smartcampus.corsi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

@Entity
@NamedQuery(name = "GruppoDiStudio.findGdsBycourseId", query = "select gds from GruppoDiStudio gds where gds.corso = ?1")
public class GruppoDiStudio {
	// id del gruppo
	@Id
	@GeneratedValue
	private long id;
	
	// Nome del gruppo
	@Column(name = "NOME")
	private String nome;

	// corso di riferimento
	@JoinColumn(name = "CORSO_ID")
	private long corso;
	
	@Column(name = "STUDENTI_GRUPPO")
	private String idsStudenti;
	
	@Transient
	private List<Studente> studentiGruppo;

//	@ManyToMany(cascade=CascadeType.ALL)
//	@JoinTable(name = "GruppiStudioAttivita", joinColumns = @JoinColumn(name = "GRUPPODISTUDIO_ID"), inverseJoinColumns = @JoinColumn(name = "ATTIVITADISTUDIO_ID"))
//	private Collection<AttivitaDiStudio> attivitaStudio;

//	@ManyToMany(cascade=CascadeType.ALL)
//	@JoinTable(name = "GruppiStudioStudenti", joinColumns = @JoinColumn(name = "GRUPPODISTUDIO_ID"), inverseJoinColumns = @JoinColumn(name = "STUDENTE_ID"))
//	private Collection<Studente> studentiGruppo;
	
	public GruppoDiStudio() {
		this.idsStudenti = "";
	}


	public void setId(long id){
		this.id = id;
	}
	
	public long getCorso() {
		return corso;
	}


	public void setCorso(long corso) {
		this.corso = corso;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public long getId(){
		return id;
	}
	
	public String getIdsStudenti() {
		return idsStudenti;
	}


	public void setIdsStudenti(String idsStudenti) {
		this.idsStudenti = idsStudenti;
	}


	public List<Studente> getStudentiGruppo() {
		return studentiGruppo;
	}


	public void setStudentiGruppo(List<Studente> studentiGruppo) {
		this.studentiGruppo = studentiGruppo;
	}


	public void addStudenteGruppo(GruppoDiStudio gds, long idStudenteDaAggiungere) {
		// TODO Auto-generated method stub
		gds.setIdsStudenti(gds.getIdsStudenti()+String.valueOf(idStudenteDaAggiungere)+",");		
	}

	// chiamata soltanto alla creazione di un nuovo gruppo
	public void initStudenteGruppo(GruppoDiStudio gds, long idStudenteDaAggiungere) {
		// TODO Auto-generated method stub
		gds.setIdsStudenti(String.valueOf(idStudenteDaAggiungere)+",");		
	}

	
	public void removeStudenteGruppo(GruppoDiStudio gds, long id2) {
		// TODO Auto-generated method stub
		String studentiGruppoIds = null;
		
		if(gds.getIdsStudenti() == null)
			return;
		
		studentiGruppoIds = gds.getIdsStudenti();
		
		String[] listS = studentiGruppoIds.split(",");
		
		String studentiGruppoAggiornato = "";
		
		for (String s : listS) {
			if(!s.equals(String.valueOf(id2))){
				studentiGruppoAggiornato = studentiGruppoAggiornato.concat(s.toString()+",");
			}
		}
		gds.setIdsStudenti(studentiGruppoAggiornato);
	}

	
	public boolean isContainsStudente(GruppoDiStudio gds, long idStudente) {
		// TODO Auto-generated method stub
		String studentiGruppoIds = null;
		studentiGruppoIds = gds.getIdsStudenti();
		
		String[] listS = studentiGruppoIds.split(",");
		
		
		for (String s : listS) {
			if(s.equals(String.valueOf(idStudente))){
				return true;
			}
		}
		
		return false;
	}
	
	public List<Long> getListInvited(GruppoDiStudio gds, long idStudente){
		return convertIdsInvitedToList(gds.getIdsStudenti(),idStudente);
	}
	
	
	public List<Long> convertIdsInvitedToList(String ids, long idStudente){
		String[] listIds = null;
		List<Long> listIdsInvited = new ArrayList<Long>();
		
		listIds = ids.split(",");
		
		for (String id : listIds) {
			
			if(!id.equals(String.valueOf(idStudente))){
				listIdsInvited.add(Long.parseLong(id));
			}
			
		}
		
		return listIdsInvited;
	}

}
