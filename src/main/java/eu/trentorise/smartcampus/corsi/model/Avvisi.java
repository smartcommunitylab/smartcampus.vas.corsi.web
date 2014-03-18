package eu.trentorise.smartcampus.corsi.model;
import eu.trentorise.smartcampus.communicator.model.Notification;

public class Avvisi {

	private long version;
	private Notification updated;
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public Notification getUpdated() {
		return updated;
	}
	public void setUpdated(Notification updated) {
		this.updated = updated;
	}
	
	
}
