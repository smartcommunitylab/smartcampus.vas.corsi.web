package eu.trentorise.smartcampus.corsi.model;

import org.codehaus.jackson.annotate.JsonProperty;


public class RisorsaPhl extends Risorsa 
{
	private String name;
	private boolean isDirectory;
	private String url;
		
	@JsonProperty("name")
	public String getName()
	{
		return name;
	}
	
	@JsonProperty("name")
	public void setName(String name)
	{
		this.name = name; 
	}
	
	@JsonProperty("url")
	public String getUrl()
	{
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url)
	{
		this.url = url;
	}

	@JsonProperty("isDirectory")
	public boolean isDirectory()
	{
		return isDirectory;
	}

	@JsonProperty("isDirectory")
	public void setDirectory(boolean isDirectory)
	{
		this.isDirectory = isDirectory;
	}
}
