package com.wakefern.global;

import com.wakefern.Wakefern.Models.WakefernHeader;
import com.wakefern.mywebgrocer.models.*;

import java.util.HashMap;
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

	//Used for v5 authorization
	public void setMapping(Object serviceObject, String jsonBody) {
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendRequest((BaseService) serviceObject, mwgHeader, mwgBody, jsonBody);
		}
	}

	private void sendRequest(BaseService serviceObject,MWGHeader header,MWGBody body, String jsonBody){
		header.authenticate();
		setgenericHeader(header.getMap());
		setPath(ApplicationConstants.Requests.baseURLV5 + serviceObject.path);
		setGenericBody(body.Body(jsonBody));
	}

	//Used for GET and DELETE
	public void setMapping(Object serviceObject){
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			sendRequest(aService, mwgHeader);
		}
	}

	public void setMappingWithURL(Object serviceObject, String baseURL){
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			sendRequestWithURL(aService, mwgHeader, baseURL);
		}
	}

	private void sendRequest(BaseService serviceObject,MWGHeader header){
		header.authenticate(serviceObject.token);
		setgenericHeader(header.getMap());
		setPath(ApplicationConstants.Requests.baseURLV5 + serviceObject.path);
	}

	private void sendRequestWithURL(BaseService serviceObject,MWGHeader header, String baseURL){
		header.authenticate(serviceObject.token);
		setgenericHeader(header.getMap());
		setPath(baseURL + serviceObject.path);
	}

	//User for all PUT and POST methods
	public void setPutMapping(Object serviceObject, String jsonBody){
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendPutRequest(aService, mwgHeader, mwgBody, jsonBody);
		}
	}

	private void sendPutRequest(BaseService serviceObject,MWGHeader header, MWGBody body, String jsonBody){
		header.authenticate(serviceObject.token);
		setgenericHeader(header.getMap());
		setPath(ApplicationConstants.Requests.baseURLV5 + serviceObject.path);
		setGenericBody(body.Body(jsonBody));
	}

	public void setAllHeadersPutMapping(Object serviceObject, String jsonBody){
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendAllHeadersPutRequest(aService, mwgHeader, mwgBody, jsonBody);
		}
	}

	private void sendAllHeadersPutRequest(BaseService serviceObject,MWGHeader header, MWGBody body, String jsonBody){
		header.authenticate(serviceObject.token, ApplicationConstants.shoppingListItemPost.contentType,
				ApplicationConstants.shoppingListItemPost.contentAccept);
		setgenericHeader(header.getMap());
		setPath(ApplicationConstants.Requests.baseURLV5 + serviceObject.path);
		setGenericBody(body.Body(jsonBody));
	}

	//v1 calls with passed in authToken
	public void setServiceMappingv1(Object serviceObject, String jsonBody) {
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendServiceMappingv1((BaseService) serviceObject, mwgHeader, mwgBody, jsonBody);
		}
	}

	private void sendServiceMappingv1(BaseService serviceObject,MWGHeader header,MWGBody body, String jsonBody){
		header.v1Authentication(serviceObject.token);
		setgenericHeader(header.getMap());
		setServicePath(ApplicationConstants.Requests.serviceURLV1 + serviceObject.path);
		setGenericBody(body.Body(jsonBody));
	}

	//Used for coupon api
	public void setCouponMapping(Object serviceObject){
		BaseService aService = (BaseService) serviceObject;
		if (aService.serviceType instanceof WakefernHeader){
			WakefernHeader wakefernHeader = new WakefernHeader();
			sendCouponMapping((BaseService) serviceObject, wakefernHeader);
		}
	}

	private void sendCouponMapping(BaseService serviceObject, WakefernHeader wakefernHeader){
		wakefernHeader.cuponAuth(serviceObject.token);
		setgenericHeader(wakefernHeader.getMap());
		setPath(serviceObject.path);
	}
}
