package eu.trentorise.smartcampus.corsi.model;

import org.codehaus.jackson.annotate.JsonProperty;


public class RisorsaPhl extends Risorsa 
{
	private String name;
	private String url;
	private String mime;
	private String modified;
	private int level;
	private String parent;


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

	@JsonProperty("mime")
	public String getMime()
	{
		return mime;
	}

	@JsonProperty("mime")
	public void setMime(String mime)
	{
		this.mime = mime;
	}

	@JsonProperty("modified")
	public String getModified() {
		return modified;
	}

	@JsonProperty("modified")
	public void setModified(String modified) {
		this.modified = modified;
	}
	
	@JsonProperty("level")
	public int getLevel() {
		return level;
	}

	@JsonProperty("level")
	public void setLevel(int level) {
		this.level = level;
	}

	@JsonProperty("parent")
	public String getParent() {
		return parent;
	}

	@JsonProperty("parent")
	public void setParent(String parent) {
		this.parent = parent;
	}
}
