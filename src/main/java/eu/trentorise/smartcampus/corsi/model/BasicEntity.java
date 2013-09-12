package eu.trentorise.smartcampus.corsi.model;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

public class BasicEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ObjectMapper mapper = new ObjectMapper();

	public static BasicEntity convert(Map<String, Object> map) {
		return mapper.convertValue(map, BasicEntity.class);
	}

	public String toJson() throws Exception {
		return mapper.writeValueAsString(this);
	}

}
