package com.wakefern.global;

import com.wakefern.Wakefern.Models.WakefernHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.*;

import java.util.Map;

public class ServiceMappings {

	private String path;
	private String servicePath;
	private String genericBody;
	private Map<String, String> genericHeader;
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

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

	/**
	 * Used for the Authorization call to retrieve the MWG Session Token & Guest User ID.
	 * 
	 * @param serviceObject
	 * @param jsonBody
	 */
	public void setMapping(Object serviceObject, String jsonBody) {
		BaseService aService = (BaseService) serviceObject;
		
		if (aService.requestHeader instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendRequest((BaseService) serviceObject, mwgHeader, mwgBody, jsonBody);
		}
	}

	/**
	 * Used for GET & DELETE requests.
	 * 
	 * @param serviceObject
	 * @param reqParams
	 */
	public void setGetMapping(Object serviceObject, Map<String, String> reqParams) {
		BaseService aService = (BaseService) serviceObject;
		
		if (aService.requestHeader instanceof MWGHeader) {
			buildRequest(aService, reqParams);
		}
	}

	/**
	 * Used for PUT & POST requests.
	 * 
	 * @param serviceObject
	 * @param jsonBody
	 * @param reqParams
	 */
	public void setPutMapping(Object serviceObject, String jsonBody, Map<String, String> reqParams) {
		BaseService aService = (BaseService) serviceObject;
		
		if (aService.requestHeader instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			buildPutRequest(aService, mwgHeader, mwgBody, jsonBody, reqParams);
		}
	}
	
	//v1 calls with passed in authToken
	public void setServiceMappingv1(Object serviceObject, String jsonBody) {
		BaseService aService = (BaseService) serviceObject;
		if (aService.requestHeader instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendServiceMappingv1((BaseService) serviceObject, mwgHeader, mwgBody, jsonBody);
		}
	}

	//Used for coupon api
	public void setCouponMapping(Object serviceObject){
		BaseService aService = (BaseService) serviceObject;
		if (aService.requestHeader instanceof WakefernHeader){
			WakefernHeader wakefernHeader = new WakefernHeader();
			sendCouponMapping((BaseService) serviceObject, wakefernHeader);
		}
	}
	
	public void setMappingWithURL(Object serviceObject, String baseURL){
		BaseService aService = (BaseService) serviceObject;
		if (aService.requestHeader instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			sendRequestWithURL(aService, mwgHeader, baseURL);
		}
	}

	public void setAllHeadersPutMapping(Object serviceObject, String jsonBody){
		BaseService aService = (BaseService) serviceObject;
		if (aService.requestHeader instanceof MWGHeader) {
			MWGHeader mwgHeader = new MWGHeader();
			MWGBody mwgBody = new MWGBody("");
			sendAllHeadersPutRequest(aService, mwgHeader, mwgBody, jsonBody);
		}
	}
	
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

	private void sendRequest(BaseService serviceObject,MWGHeader header,MWGBody body, String jsonBody){
		header.authenticate();
		setgenericHeader(header.getMap());
		setPath(MWGApplicationConstants.baseURL + serviceObject.requestPath);
		setGenericBody(body.Body(jsonBody));
	}

	private void sendRequestWithURL(BaseService serviceObject,MWGHeader header, String baseURL){
		header.authenticate(serviceObject.token);
		setgenericHeader(header.getMap());
		setPath(baseURL + serviceObject.requestPath);
	}

	/**
	 * Construct a PUT / POST request.
	 * 
	 * @param serviceObject
	 * @param header
	 * @param body
	 * @param jsonBody
	 * @param reqParams
	 */
	private void buildPutRequest(BaseService serviceObject, MWGHeader header, MWGBody body, String jsonBody, Map<String, String> reqParams){
		header.authenticate(serviceObject.token);
		setgenericHeader(header.getMap());
		
		String reqURL = buildURL(serviceObject, reqParams);
		
		setPath(reqURL);
		setGenericBody(body.Body(jsonBody));
	}
	
	/**
	 * Construct a GET / DELETE request.
	 * 
	 * @param serviceObject
	 * @param reqParams
	 */
	private void buildRequest(BaseService serviceObject, Map<String, String> reqParams) {
		MWGHeader header = (MWGHeader) serviceObject.requestHeader;
		setgenericHeader(header.getMap());
		
		String reqURL = buildURL(serviceObject, reqParams);
		
		setPath(reqURL);
	}
	
	/**
	 * Figure out if the 'path' property of the Service Object contains any placeholder text.
	 * If so, replace with matching request parameters.
	 * <p>
	 * For Example:
	 * <p>
	 * If the 'path' contains a '{chainId}' placeholder, there should be a corresponding 'chainId' request parameter.
	 * 
	 * @param {BaseService} serviceObj
	 * @param {Map} reqParams
	 * @return {String}
	 */
	private String buildURL(BaseService serviceObj, Map<String, String> reqParams) {
		String path = serviceObj.requestPath;
				
		if ((reqParams != null) && !reqParams.isEmpty()) {
			if (reqParams.containsKey(MWGApplicationConstants.chainID)) {
				path = path.replace("{" + MWGApplicationConstants.chainID + "}", reqParams.get(MWGApplicationConstants.chainID));
			}
			
			if (reqParams.containsKey(MWGApplicationConstants.userID)) {
				path = path.replace("{" + MWGApplicationConstants.userID + "}", reqParams.get(MWGApplicationConstants.userID));
			}
			
			if (reqParams.containsKey(MWGApplicationConstants.storeID)) {
				path = path.replace("{" + MWGApplicationConstants.storeID + "}", reqParams.get(MWGApplicationConstants.storeID));
			}
		}
		
		return (MWGApplicationConstants.baseURL + path);
	}

	private void sendAllHeadersPutRequest(BaseService serviceObject,MWGHeader header, MWGBody body, String jsonBody){
		header.authenticate(serviceObject.token, ApplicationConstants.shoppingListItemPost.contentType, ApplicationConstants.shoppingListItemPost.contentAccept);
		setgenericHeader(header.getMap());
		setPath(ApplicationConstants.Requests.baseURLV5 + serviceObject.requestPath);
		setGenericBody(body.Body(jsonBody));
	}

	private void sendServiceMappingv1(BaseService serviceObject,MWGHeader header,MWGBody body, String jsonBody){
		header.v1Authentication(serviceObject.token);
		setgenericHeader(header.getMap());
		setServicePath(ApplicationConstants.Requests.serviceURLV1 + serviceObject.requestPath);
		setGenericBody(body.Body(jsonBody));
	}
	
	private void sendCouponMapping(BaseService serviceObject, WakefernHeader wakefernHeader){
		wakefernHeader.cuponAuth(serviceObject.token);
		setgenericHeader(wakefernHeader.getMap());
		setPath(serviceObject.requestPath);
	}
}
