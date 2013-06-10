package eu.trentorise.smartcampus.corsi.model;

import java.util.ArrayList;

public class Calendar {

	private String id_calendar;
	private String id_student;
	private ArrayList<Event> events;
	
    public Calendar(){}
    
    public Calendar(String idCal, String idStud, ArrayList<Event> evs){
    	this.id_calendar = idCal;
    	this.id_student = idStud;
    	this.events = evs;
    }
    
}
