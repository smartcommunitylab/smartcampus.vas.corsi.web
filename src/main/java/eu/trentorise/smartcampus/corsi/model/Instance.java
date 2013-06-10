package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

public class Instance {

	private String id_istance;
	private Date date;
	private String start_time;
	private String end_time;
	private String id_event;

	public String getId_istance() {
		return id_istance;
	}

	public void setId_istance(String id_istance) {
		this.id_istance = id_istance;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getId_event() {
		return id_event;
	}

	public void setId_event(String id_event) {
		this.id_event = id_event;
	}

}
