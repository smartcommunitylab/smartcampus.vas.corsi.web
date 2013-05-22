package eu.trentorise.smartcampus.corsi.model;

import java.util.ArrayList;
import java.util.Date;

public class Evento
{
	// id dell'evento
	private int id;
	
	//corso di riferimento
	private CorsoLite corso;
	
	// mail of the owner
	private String organizer;
	
	// title of the event
	private String titolo;
	
	// where the place takes places 
	private String event_location;
	
	// the description of the event
	private String descrizione;
	
	// The time the event starts in UTC milliseconds since the epoch
	private Date dt_start;
	
	// The time the event ends in UTC milliseconds since the epoch
	private Date dt_end;
	
	// The duration of the event in RFC5545 format
	private String durata;
	
	// true if occupies the entire day
	private boolean all_day;
	
	// The recurrence rule for the event format. For example,"FREQ=WEEKLY;COUNT=10;WKST=SU". 
	private String rrule;
	
	// The recurrence dates for the event. 
	// You typically use RDATE in conjunction with RRULE to define an aggregate set of repeating occurrences.
	private Date r_date;
	
	// If this event counts as busy time or is free time that can be scheduled over.
	private boolean availability;
	
	// Whether guests can modify the event.
	private boolean guests_can_modify;
	
	// Whether guests can invite other guests
	private boolean guests_can_invite;
		
	// Whether guests can see the list of attendees
	private boolean guests_can_see;
	
	// List of all istance of the event
	private ArrayList<Istanza> istanze;
	
	
	public Evento()
	{
		this.setIstanze(new ArrayList<Istanza>());
	}


	public int getId()
	{
		return id;
	}


	public void setId(int id)
	{
		this.id = id;
	}


	public CorsoLite getCorso()
	{
		return corso;
	}


	public void setCorso(CorsoLite corso)
	{
		this.corso = corso;
	}


	public String getOrganizer()
	{
		return organizer;
	}


	public void setOrganizer(String organizer)
	{
		this.organizer = organizer;
	}


	public String getTitolo()
	{
		return titolo;
	}


	public void setTitolo(String titolo)
	{
		this.titolo = titolo;
	}


	public String getEvent_location()
	{
		return event_location;
	}


	public void setEvent_location(String event_location)
	{
		this.event_location = event_location;
	}


	public String getDescrizione()
	{
		return descrizione;
	}


	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}


	public Date getDt_start()
	{
		return dt_start;
	}


	public void setDt_start(Date dt_start)
	{
		this.dt_start = dt_start;
	}


	public Date getDt_end()
	{
		return dt_end;
	}


	public void setDt_end(Date dt_end)
	{
		this.dt_end = dt_end;
	}


	public String getDurata()
	{
		return durata;
	}


	public void setDurata(String durata)
	{
		this.durata = durata;
	}


	public boolean isAll_day()
	{
		return all_day;
	}


	public void setAll_day(boolean all_day)
	{
		this.all_day = all_day;
	}


	public String getRrule()
	{
		return rrule;
	}


	public void setRrule(String rrule)
	{
		this.rrule = rrule;
	}


	public Date getR_date()
	{
		return r_date;
	}


	public void setR_date(Date r_date)
	{
		this.r_date = r_date;
	}


	public boolean isAvailability()
	{
		return availability;
	}


	public void setAvailability(boolean availability)
	{
		this.availability = availability;
	}


	public boolean isGuests_can_modify()
	{
		return guests_can_modify;
	}


	public void setGuests_can_modify(boolean guests_can_modify)
	{
		this.guests_can_modify = guests_can_modify;
	}


	public boolean isGuests_can_invite()
	{
		return guests_can_invite;
	}


	public void setGuests_can_invite(boolean guests_can_invite)
	{
		this.guests_can_invite = guests_can_invite;
	}


	public boolean isGuests_can_see()
	{
		return guests_can_see;
	}


	public void setGuests_can_see(boolean guests_can_see)
	{
		this.guests_can_see = guests_can_see;
	}


	public ArrayList<Istanza> getIstanze()
	{
		return istanze;
	}


	public void setIstanze(ArrayList<Istanza> istanze)
	{
		this.istanze = istanze;
	}
}
