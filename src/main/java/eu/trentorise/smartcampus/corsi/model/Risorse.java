package eu.trentorise.smartcampus.corsi.model;

import java.util.ArrayList;
import org.codehaus.jackson.map.ObjectMapper;


public class Risorse extends ArrayList<RisorsaPhl> 
{
	private static final long serialVersionUID = 1L;
	
	public Risorse convert(String json) 
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json , Risorse.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String toJson() throws Exception 
	{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}
}