package eu.trentorise.smartcampus.corsi.model;

import java.util.ArrayList;
import java.util.Date;

public class Event {
	private String id_event;
	private String title;
	private String location;
	private String room;
	private String description;
	private Date start_date;
	private Date end_date;
	private boolean isExam;
	private String idCourse;
	private ArrayList<Instance> instances;

	public Event() {

	}

	public void setIdCourse(String idCourse) {
		this.idCourse = idCourse;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setInstances(ArrayList<Instance> instances) {
		this.instances = instances;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getDescription() {
		return description;
	}

	public boolean isExam() {
		return isExam;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setExam(boolean isExam) {
		this.isExam = isExam;
	}

	public void setId_event(String id_event) {
		this.id_event = id_event;
	}

	public String getId_event() {
		return id_event;
	}

	public String getTitle() {
		return title;
	}

	public String getLocation() {
		return location;
	}

	public String getRoom() {
		return room;
	}

	public Date getStart_date() {
		return start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public String getIdCourse() {
		return idCourse;
	}

	public ArrayList<Instance> getInstances() {
		return instances;
	}

}
