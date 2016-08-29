package com.wakefern.global;

import com.wakefern.mywebgrocer.models.*;

import java.util.Map;

public class ServiceMappings {

	private String path;
	private String servicePath;
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

	public String getServicePath() {
		return servicePath;
	}
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public void setMapping(Object serviceObject, String jsonBody) {
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendRequest((BaseService) serviceObject, mwgHeader, mwgBody, jsonBody);
		}
	}

	public void setMapping(Object serviceObject){
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			sendRequest(aService, mwgHeader);
		}
	}

	public void setServiceMapping(Object serviceObject, String jsonBody) {
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendServiceMapping((BaseService) serviceObject, mwgHeader, mwgBody, jsonBody);
		}
	}

	public void setPutMapping(Object serviceObject, String jsonBody){
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendPutRequest(aService, mwgHeader, mwgBody, jsonBody);
		}
	}

	private void sendRequest(BaseService serviceObject,MWGHeader header,MWGBody body, String jsonBody){
		header.authenticate();
		setgenericHeader(header.getMap());
		setPath(ApplicationConstants.Requests.baseURLV5 + serviceObject.path);
		setGenericBody(body.Body(jsonBody));
	}

	private void sendRequest(BaseService serviceObject,MWGHeader header){
		header.authenticate(serviceObject.token);
		setgenericHeader(header.getMap());
		setPath(ApplicationConstants.Requests.baseURLV5 + serviceObject.path);
	}

	private void sendServiceMapping(BaseService serviceObject,MWGHeader header,MWGBody body, String jsonBody){
		header.serviceAuth(serviceObject.token);
		setgenericHeader(header.getMap());
		setServicePath(ApplicationConstants.Requests.serviceURLV1 + serviceObject.path);
		setGenericBody(body.Body(jsonBody));
	}

	private void sendPutRequest(BaseService serviceObject,MWGHeader header, MWGBody body, String jsonBody){
		header.authenticate(serviceObject.token);
		setgenericHeader(header.getMap());
		setPath(ApplicationConstants.Requests.baseURLV5 + serviceObject.path);
		setGenericBody(body.Body(jsonBody));
	}
	
}
