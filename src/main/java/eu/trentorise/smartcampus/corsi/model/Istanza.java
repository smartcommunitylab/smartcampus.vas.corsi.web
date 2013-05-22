package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

public class Istanza
{
	// The _ID of the event for this instance
	private int event_id;
	
	// The beginning time of the instance, in UTC milliseconds
	private Date begin;
	
	// The ending time of the instance, in UTC milliseconds
	private Date end;
	
	// The Julian start day of the instance, relative to the Calendar's time zone
	private String start_day;
	
	// The start minute of the instance measured from midnight, relative to the Calendar's time zone.
	private String start_minute;
	
	// The Julian end day of the instance, relative to the Calendar's time zone
	private String end_day;
	
	// The end minute of the instance measured from midnight in the Calendar's time zone
	private String end_minute;
	
	
	public Istanza()
	{
	}


	public int getEvent_id()
	{
		return event_id;
	}


	public void setEvent_id(int event_id)
	{
		this.event_id = event_id;
	}


	public Date getBegin()
	{
		return begin;
	}


	public void setBegin(Date begin)
	{
		this.begin = begin;
	}


	public Date getEnd()
	{
		return end;
	}


	public void setEnd(Date end)
	{
		this.end = end;
	}


	public String getStart_day()
	{
		return start_day;
	}


	public void setStart_day(String start_day)
	{
		this.start_day = start_day;
	}


	public String getStart_minute()
	{
		return start_minute;
	}


	public void setStart_minute(String start_minute)
	{
		this.start_minute = start_minute;
	}


	public String getEnd_day()
	{
		return end_day;
	}


	public void setEnd_day(String end_day)
	{
		this.end_day = end_day;
	}


	public String getEnd_minute()
	{
		return end_minute;
	}


	public void setEnd_minute(String end_minute)
	{
		this.end_minute = end_minute;
	}
	
}
