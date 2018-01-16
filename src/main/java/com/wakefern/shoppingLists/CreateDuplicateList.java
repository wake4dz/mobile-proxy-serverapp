package com.wakefern.shoppingLists;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.ShoppingList.prefix)
public class CreateDuplicateList extends BaseService {
	
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
    		    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		String jsonData
	) throws Exception, IOException {
		
		// This is a custom endpoint.  There is no way via the MWG API to directly duplicate a list.
		// The ListID supplied in the path, represents the ID of the List that the UI wishes to duplicate.
		// The only property required in the incoming JSON is "Name".  Which is the name of the new list to be created.
		// The new name *must* be unique, or MWG will kick back an HTTP 409 error.
		//
		// Steps:
		// * Retrieve the contents of the list to be duplicated
		// * Create a new list
		// * Update the new list with the items from the original list
		
		try {			
			JSONObject respObj = getListItems(chainID, userID, listID, sessionToken);
			JSONArray  respArr = getNewList(chainID, userID, sessionToken, jsonData);
			
			String newListID = respArr.getJSONObject(0).getString("Id");
			String jsonResp  = populateNewList(newListID, chainID, userID, sessionToken, respObj);
		
			return this.createValidResponse(jsonResp);
		
		} catch (Exception e) {
			return this.createErrorResponse(e);
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
	 * @return
	 * @throws Exception
	 */
	private JSONObject getListItems(String chainID, String userID, String listID, String token) throws Exception {
		this.requestPath   = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.items;
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.ShoppingList.items, MWGApplicationConstants.Headers.json, token);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// GetListItems path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.listID, listID);
		
		// GetListItems query parameters.
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, "0");
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, "9999");
		
		String     strResp = this.mwgRequest(BaseService.ReqType.GET, null);
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
	 * @return
	 * @throws Exception
	 */
	private JSONArray getNewList(String chainID, String userID, String token, String data) throws Exception {
        this.requestPath   = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.lists;
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.ShoppingList.list, token);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = null;

		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		
		String    strResp = this.mwgRequest(BaseService.ReqType.POST, data);
		JSONArray objResp = new JSONArray(strResp);

		return objResp;
	}
	
	// TODO: This ain't not no working!
	// TODO: Currently returning an HTTP 404.
	// TODO: I *think* it's an issue on MWG's end.  But not 100% sure about that.
	//
	private String populateNewList(String listID, String chainID, String userID, String token, JSONObject listItems) throws Exception {
		this.requestPath   = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.items;
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.ShoppingList.items, token);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = null;
		
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.listID, listID);
		
		String reqString = listItems.toString();
		String strResp = this.mwgRequest(BaseService.ReqType.POST, reqString);

		return strResp;
	}
}

