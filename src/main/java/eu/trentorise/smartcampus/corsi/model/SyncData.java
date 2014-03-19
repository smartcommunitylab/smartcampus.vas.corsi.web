package eu.trentorise.smartcampus.corsi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SyncData {

	private long version;
	private Map<String,List<Notification>> updated = new HashMap<String, List<Notification>>();
	private Map<String,List<String>> deleted = new HashMap<String, List<String>>();

	private Map<String, Object> exclude;
	private Map<String, Object> include;
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public Map<String, List<Notification>> getUpdated() {
		return updated;
	}
	public void setUpdated(Map<String, List<Notification>> updated) {
		this.updated = updated;
	}
	public Map<String, List<String>> getDeleted() {
		return deleted;
	}
	public void setDeleted(Map<String, List<String>> deleted) {
		this.deleted = deleted;
	}
	public Map<String, Object> getExclude() {
		return exclude;
	}
	public void setExclude(Map<String, Object> exclude) {
		this.exclude = exclude;
	}
	public Map<String, Object> getInclude() {
		return include;
	}
	public void setInclude(Map<String, Object> include) {
		this.include = include;
	}
	
	
}
