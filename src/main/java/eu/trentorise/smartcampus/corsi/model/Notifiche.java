package eu.trentorise.smartcampus.corsi.model;


public class Notifiche {

	private long version;
	private Notifications updated;
	private Notifications deleted;
	private String exclude;
	private String include;
	
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public Notifications getUpdated() {
		return updated;
	}
	public void setUpdated(Notifications updated) {
		this.updated = updated;
	}
	public Notifications getDeleted() {
		return deleted;
	}
	public void setDeleted(Notifications deleted) {
		this.deleted = deleted;
	}
	public String getExclude() {
		return exclude;
	}
	public void setExclude(String exclude) {
		this.exclude = exclude;
	}
	public String getInclude() {
		return include;
	}
	public void setInclude(String include) {
		this.include = include;
	}
	
}
