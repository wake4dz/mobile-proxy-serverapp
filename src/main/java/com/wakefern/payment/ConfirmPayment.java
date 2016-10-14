package com.wakefern.payment;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONObject;

import com.wakefern.global.BaseService;

@Path("payment/confirm")
public class ConfirmPayment extends BaseService {
	@POST
    @Produces("application/*")
    public String getInfo(String jsonBody){
    	JSONObject response = new JSONObject();
    	response.put("payment-status", true);
    	return response.toString();
    }
}
