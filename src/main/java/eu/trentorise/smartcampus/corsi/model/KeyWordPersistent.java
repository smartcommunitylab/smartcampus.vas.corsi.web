package eu.trentorise.smartcampus.corsi.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class KeyWordPersistent {

	private String id;
	private String key;
	private long timestamp;

	public KeyWordPersistent() {

	}

	public KeyWordPersistent(String id, String key, long timestamp) {
		this.id=id;
		this.key=key;
		this.timestamp=timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public static KeyWordPersistent valueOf(String json) {
		try {
			JSONObject o = new JSONObject(json);
			String id =o.getString("id");
			String key =o.getString("key");
			Long timestamp =o.getLong("timestamp");
			KeyWordPersistent keyWordPersistent=new KeyWordPersistent(id,key,timestamp);
			
		
			return keyWordPersistent;
		} catch (JSONException e) {
			return null;
		}
	
	}
	
	public static List<KeyWordPersistent> valueOfList(String json) {
		try {
			
			List<KeyWordPersistent> returnList=new ArrayList<KeyWordPersistent>();
			JSONArray array=new JSONArray(json);
			for(int i=0 ;i<array.length();i++){
				JSONObject o = array.getJSONObject(i);
				String id =o.getString("id");
				String key =o.getString("key");
				Long timestamp =o.getLong("timestamp");
				KeyWordPersistent keyWordPersistent=new KeyWordPersistent(id,key,timestamp);
				returnList.add(keyWordPersistent);
				
			}
			
			
			
		
			return returnList;
		} catch (JSONException e) {
			return null;
		}
	
	}
	
	public String ToJson() {
		String object=new String();
		object="{\"id\":"+id+",\"key\":"+key+",\"timestamp\":"+timestamp+"\"}";
		
		return object;
	}

	
	

}
