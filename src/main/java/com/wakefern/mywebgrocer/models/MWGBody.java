package com.wakefern.mywebgrocer.models;

import com.wakefern.request.models.Body;

/**
 * Created by brandyn.brosemer on 8/9/16.
 */
public class MWGBody extends Body {
	String body;

	public String getBody(){
		return body;
	}
	public void setBody( String body){
		this.body = body;
	}
	
	public MWGBody(String jsonBody){
		setBody(jsonBody);
		super.Body(jsonBody);
	}
}
