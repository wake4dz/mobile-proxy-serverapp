package com.wakefern.global;

import com.wakefern.mywebgrocer.models.*;

import java.util.Map;

public class ServiceMappings {

	private String path;
	private String genericBody;
	private Map<String, String> genericHeader;

	public Map<String, String> getgenericHeader() {
		return genericHeader;
	}
	public void setgenericHeader(Map<String, String> genericHeader) {
		this.genericHeader = genericHeader;
	}

	public String getGenericBody() {
		return genericBody;
	}
	public void setGenericBody(String genericBody) {
		this.genericBody = genericBody;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public void setMapping(Object serviceObject, String jsonBody) {
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendRequest((BaseService) serviceObject, mwgHeader, mwgBody, jsonBody);
		}
//		} else {
//			Header header = new Header();
//			Body body = new Body();
//			sendRequest((BaseService) serviceObject, header, body);
//		}
	}

	private void sendRequest(BaseService serviceObject,MWGHeader header,MWGBody body, String jsonBody){
		header.authenticate();
		setgenericHeader(header.getMap());
//		setPath(ApplicationConstants.Requests.baseURLV5 + serviceObject.path);
		setPath(ApplicationConstants.Requests.baseURLV1 + serviceObject.path);
		setGenericBody(body.Body(jsonBody));
	}

//	private void auth(MWGHeader mwgHeader, MWGBody mwgBody){
//		mwgHeader.authenticate();
//		setgenericHeader(mwgHeader.getMap());
//		setPath(ApplicationConstants.Requests.baseURLV5 + ApplicationConstants.Requests.Authentication.Authenticate);
//	}
//
//	private void auth(Header header, Body body){
//		setPath(ApplicationConstants.Requests.baseURLV5 + ApplicationConstants.Requests.Authentication.Authenticate);
//	}

	
}
