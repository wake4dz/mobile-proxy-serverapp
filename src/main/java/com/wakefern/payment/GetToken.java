package com.wakefern.payment;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.json.JSONObject;

import com.wakefern.global.BaseService;

@Path("payment/token")
public class GetToken extends BaseService {
    
	@POST
    @Produces("application/*")
    public String getInfo(String jsonBody){
    	JSONObject response = new JSONObject();
    	response.put("token", "xxx-xxxx-xxxx-xxxx");
    	return response.toString();
    }
	
}