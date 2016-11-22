package com.wakefern.request.models;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by brandyn.brosemer on 8/3/16.
 */
public class Header {
	private Map<String, String> map = new HashMap<String, String>();

	public void put(Map<String,String> aMap){
		map.putAll(aMap);
	}

}
