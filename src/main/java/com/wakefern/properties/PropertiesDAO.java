package com.wakefern.properties;

import java.util.HashMap;
import java.util.Map;

public class PropertiesDAO {

	private Map<String, String> propertiesMap;
	private String key;
	private String value;
	
	public PropertiesDAO() {
		propertiesMap = new HashMap<String, String>();
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public void addEntry(String key, String value){
		this.propertiesMap.put(key, value);
	}
	
	public void removeEntry(String key) {
		this.propertiesMap.remove(key);
	}
	
	
}
