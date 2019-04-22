package com.wakefern.global;

import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.*;
import com.wakefern.wakefern.WakefernHeader;

import java.util.Map;
import java.util.Map.Entry;

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
	 * Used for GET & DELETE requests.
	 * 
	 * @param serviceObject
	 * @param reqParams
	 */
	public void setGetMapping(Object serviceObject) {
		BaseService aService = (BaseService) serviceObject;
		
		if (aService.requestHeader instanceof MWGHeader) {
			buildGetRequest(aService);
		}
	}

	/**
	 * Used for PUT & POST requests.
	 * 
	 * @param serviceObject
	 * @param jsonBody
	 * @param reqParams
	 */
	public void setPutMapping(Object serviceObject, String jsonBody) {
		BaseService aService = (BaseService) serviceObject;
		
		if (aService.requestHeader instanceof MWGHeader) {
			MWGBody mwgBody = new MWGBody("");
			buildPostRequest(aService, mwgBody, jsonBody);
		}
	}
	
	// Used for Coupons
	public void setCouponMapping(Object serviceObject){
		BaseService aService = (BaseService) serviceObject;
		if (aService.requestHeader instanceof WakefernHeader){
			WakefernHeader wakefernHeader = new WakefernHeader();
			sendCouponMapping((BaseService) serviceObject, wakefernHeader);
		}
	}
	
	// Used for Recommendations & Rewards
	public void setMappingWithURL(Object serviceObject, String baseURL) {
		BaseService aService = (BaseService) serviceObject;
		MWGHeader mwgHeader  = new MWGHeader();
		sendRequestWithURL(aService, mwgHeader, baseURL);
	}
	
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

	/**
	 * Construct a PUT / POST request.
	 * 
	 * @param serviceObject
	 * @param body
	 * @param jsonBody
	 * @param reqParams
	 */
	private void buildPostRequest(BaseService serviceObject, MWGBody body, String jsonBody) {
		MWGHeader header = (MWGHeader) serviceObject.requestHeader;
		setgenericHeader(header.getMap());
		
		String reqURL = buildURL(serviceObject);
		
		setPath(reqURL);
		setGenericBody(body.Body(jsonBody));
	}
	
	/**
	 * Construct a GET / DELETE request.
	 * 
	 * @param serviceObject
	 * @param reqParams
	 */
	private void buildGetRequest(BaseService serviceObject) {
		MWGHeader header = (MWGHeader) serviceObject.requestHeader;
		setgenericHeader(header.getMap());
		
		String reqURL = buildURL(serviceObject);
		
		setPath(reqURL);
	}
	
	/**
	 * Build the full URL. If the 'requestPath' property of the Service Object contains any placeholder text, replace with matching request parameters.
	 * <p>
	 * For Example:
	 * <p>
	 * If the requestPath contains a '{chainId}' placeholder, there should be a corresponding 'chainId' request parameter.
	 * 
	 * @param serviceObj
	 * @param reqParams
	 * @param queryParams
	 * @return {String}
	 */
	private String buildURL(BaseService serviceObj) {
		String path = serviceObj.requestPath;
		StringBuilder query = new StringBuilder();
				
		// Insert any Request Path parameters
		if ((serviceObj.requestParams != null) && !serviceObj.requestParams.isEmpty()) {
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.chainID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.chainID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.userID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.userID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.storeID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.storeID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.orderID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.orderID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.regionID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.regionID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.zipCode)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.zipCode, path, serviceObj);
			}

			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.parentCatID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.parentCatID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.categoryID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.categoryID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.productSKU)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.productSKU, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.productID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.productID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.itemID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.itemID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.circularID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.circularID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.pageID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.pageID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.recipeID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.recipeID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.listID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.listID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.listItemID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.listItemID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.srcListID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.srcListID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.mwgStoreID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.districtID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.districtID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.token)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.token, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.fulfillType)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.fulfillType, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.circItemID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.circItemID, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.promoCode)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.promoCode, path, serviceObj);
			}
			
			if (serviceObj.requestParams.containsKey(MWGApplicationConstants.Requests.Params.Path.circCategoriesID)) {
				path = replacePathParam(MWGApplicationConstants.Requests.Params.Path.circCategoriesID, path, serviceObj);
			}
		}
		
		// Build the query string, if there are any query parameters
		if ((serviceObj.queryParams != null) && (!serviceObj.queryParams.isEmpty())) {
			boolean isQueryStart = true;
						
			for (Entry<String, String> pair : serviceObj.queryParams.entrySet()) {	            
	            if (pair.getValue() != null) {
	            		if (isQueryStart) {
	            			query.append("?");
	            			isQueryStart = false;
	            		}
	            		
	            		query.append(pair.getKey() + "=" + pair.getValue());
	            		query.append("&");
	            }
	        }
		
		} else {
			query.setLength(0);
		}
		
		// If the length of the query string is > 0, then the last char will be '&'.
		// Which needs to be removed before sending the request.
		if (query.length() > 0) {
			query.deleteCharAt(query.length() - 1);
		}
		
		return (MWGApplicationConstants.getBaseURL() + path + query);
	}
	
	/**
	 * Replace a URL path placeholder with the corresponding value.
	 * 
	 * @param pathParam
	 * @param path
	 * @param serviceObj
	 * @return
	 */
	private String replacePathParam(String pathParam, String path, BaseService serviceObj) {
		return path.replace("{" + pathParam + "}", serviceObj.requestParams.get(pathParam));
	}

	// Used for Coupons
	private void sendCouponMapping(BaseService serviceObject, WakefernHeader wakefernHeader){
		wakefernHeader.cuponAuth(serviceObject.requestToken);
		setgenericHeader(wakefernHeader.getMap());
		setPath(serviceObject.requestPath);
	}
	
	// Used for Recommendations & Rewards
	private void sendRequestWithURL(BaseService serviceObject,MWGHeader header, String baseURL){
		header.authenticate(serviceObject.requestToken);
		setgenericHeader(header.getMap());
		setPath(baseURL + serviceObject.requestPath);
	}
}
