package com.wakefern.shoppingLists;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;

import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.ShoppingList.prefix)

public class CreateDuplicateList extends BaseService {
	private static final String THIS = "com.wakefern.shoppingLists.CreateDuplicateList";
	
	private final static Logger logger = Logger.getLogger(CreateDuplicateList.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public CreateDuplicateList() {}
    
	@POST
    @Consumes(MWGApplicationConstants.Headers.ShoppingList.list)
    @Produces(MWGApplicationConstants.Headers.ShoppingList.list)
    @Path(MWGApplicationConstants.Requests.ShoppingList.copy)
    
	public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.listID) String listID,
    		
    		@DefaultValue(MWGApplicationConstants.Requests.ShoppingList.strDupeCap)
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,
    		    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		
    		String jsonData
	
	) {
				
		// This is a custom endpoint.  There is no way via the MWG API to directly duplicate a list.
		// The ListID supplied in the path, represents the ID of the List that the UI wishes to duplicate.
		// The only property required in the incoming JSON is "Name".  Which is the name of the new list to be created.
		// The new name *must* be unique, or MWG will kick back an HTTP 409 error.
		//
		// The optional query parameter is used to determine how many records from the original list, should be copied to the new list.
		// The default value is 20, but the UI can override the default by including the "take" query parameter in the request.
		//
		// Steps:
		// * Retrieve the contents of the list to be duplicated
		// * Create a new list
		// * Update the new list with the items from the original list
		
		try {			
			JSONObject respObj = getListItems(chainID, userID, listID, sessionToken);
			JSONArray  respArr = getNewList(chainID, userID, sessionToken, jsonData);
			
			String newListID = respArr.getJSONObject(0).getString("Id");
			
			HashMap<String, Integer> results = populateNewList(newListID, chainID, userID, take, sessionToken, respObj);
			
			if ((results.get("errors") < results.get("items")) || (results.get("items") == 0)) {
				// Build Success Response
				String copiedCount = results.get("successes").toString();
				
				JSONObject jo = new JSONObject();
				
				jo.put("itemsCopied", copiedCount);
				jo.put("origListId", listID);
				jo.put("newListId", newListID);
				jo.put("newList", respArr);

				if(LogUtil.isUserTrackOn) {
					if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
			        	String trackData = LogUtil.getRequestData("chainId", chainID, "userID", userID, 
			        			"listID", listID, "take", take, "sessionToken", sessionToken, "httpBody", jsonData );
						logger.info("Tracking data for " + userID + ": " + trackData);
					}
				}
				
				return this.createValidResponse(jo.toString());
		
			} else {
				// Build Error Response	
				throw new Exception("404, [{ \"Text\" : \"Failed to copy any items from the original list to the new Shopping List.\" }]");
			}
				
		} catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.SHOPPING_LISTS_CREATE_DUPLICATE_LIST);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "chainID", chainID, 
        		 "listID", listID, "take", take, "userID", userID, 
        		 "sessionToken", sessionToken, "accept", accept, "contentType", contentType, "httpBody", jsonData );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
		}
    }
	
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------
	
	/**
	 * Get List Items.
	 * 
	 * @param chainID
	 * @param userID
	 * @param listID
	 * @param sessionToken
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject getListItems(String chainID, String userID, String listID, String token) throws Exception {
		this.requestPath   = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.items;
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.ShoppingList.items, MWGApplicationConstants.Headers.generic, token);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// GetListItems path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.listID, listID);
		
		// GetListItems query parameters.
//		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, "0");
//		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, "9999");
		
		String     strResp = this.mwgRequest(BaseService.ReqType.GET, null, THIS);
		JSONObject objResp = new JSONObject(strResp);
		
		return objResp;
	}
	
	/**
	 * Create a new list using the Name provided in the POST data.
	 * 
	 * @param chainID
	 * @param userID
	 * @param sessionToken
	 * @param data
	 * @return JSONArray
	 * @throws Exception
	 */
	private JSONArray getNewList(String chainID, String userID, String token, String data) throws Exception {
        this.requestPath   = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.lists;
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.ShoppingList.list, token);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = null;

		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		
		String    strResp = this.mwgRequest(BaseService.ReqType.POST, data, THIS);
		JSONArray objResp = new JSONArray(strResp);

		return objResp;
	}
	
	/**
	 * 
	 * @param listID
	 * @param chainID
	 * @param userID
	 * @param strLimit
	 * @param token
	 * @param objListItems
	 * @return HashMap
	 */
	private HashMap<String, Integer> populateNewList(String listID, String chainID, String userID, String strLimit, String token, JSONObject objListItems) {
		this.requestPath   = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.items;
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.ShoppingList.item, token);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = null;
		
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.listID, listID);
		
		// Extract the Array of Items from the JSON Object
		JSONArray arrListItems = objListItems.getJSONArray("Items");

		// Set the Limit value.  
		// This is the number of List Items that will get copied from the old Shopping List to the new one.
		int intLimit;
		
		try {
			intLimit = Integer.parseInt(strLimit);
			
		} catch (Exception e) {
			intLimit = MWGApplicationConstants.Requests.ShoppingList.intDupeCap;
			strLimit = MWGApplicationConstants.Requests.ShoppingList.strDupeCap;
		}
		
		if (arrListItems.length() < intLimit) {
			intLimit = arrListItems.length();
		}
				
		// Add each List Item (up to the Limit) to the newly created Shopping List
		// Yes, the only way to do this through the MWG API is one at a time...
		int errCount = 0;
		
		for (int i = 0; i < intLimit; i++) {
			String reqData = arrListItems.getJSONObject(i).toString();
			
			try {
				this.mwgRequest(BaseService.ReqType.POST, "[" + reqData + "]", THIS);
			
			} catch (Exception e) {
				errCount++;
			}
		}
		
		// Return Data
		HashMap<String, Integer> results = new HashMap<>();
		
		results.put("items", intLimit);
		results.put("errors", errCount);
		results.put("successes", (intLimit - errCount));
		
		return results;
	}
}

