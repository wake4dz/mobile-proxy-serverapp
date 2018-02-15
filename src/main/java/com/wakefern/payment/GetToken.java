package com.wakefern.payment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.wakefern.global.ApplicationConstants;
import org.json.JSONObject;

import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path("payment/token/user/{userId}/store/{storeId}")
public class GetToken extends BaseService {
	
	private final static Logger logger = Logger.getLogger("GetToken");

	private Map<String, String> paymentMap() {
		Map returnMap = new LinkedHashMap<>();
		returnMap.put("Id", "59");
		returnMap.put("Name", "");
		returnMap.put("PaymentMethodMessage", "");
		returnMap.put("PayMethodTooltipUri", "");
		returnMap.put("PrimaryOption", true);
		returnMap.put("AllowsMultiple", false);
		returnMap.put("RequiresCardNumber", false);
		returnMap.put("RequiredNumeric", false);
		returnMap.put("MinimumLength", 0);
		returnMap.put("MaximumLength", 0);
		returnMap.put("RequiresAmount", false);
		returnMap.put("CardNumber", null);
		returnMap.put("Amount", null);
		returnMap.put("Image", null);
		returnMap.put("IsVendor", true);
		returnMap.put("CardNumberLabel", null);
		returnMap.put("AmountLabel", null);
		returnMap.put("FulfillmentType", null);

		return returnMap;
	}

	@GET
	@Produces(MWGApplicationConstants.Headers.generic)
	public Response getInfo(
			@PathParam("storeId") String storeId, 
			@PathParam("userId") String userId,
			@DefaultValue("") @QueryParam("isMember") String isMember, 
			@HeaderParam("Authorization") String authToken
	) throws Exception, IOException {
		
		/*
		 * Response body
		 * 
		 * "PaymentMethods":[{
		 * 	"Id":"59",
		 * 	"Name":"",
		 * 	"PaymentMethodMessage":"",
		 * 	"PayMethodTooltipUri":"",
		 * 	"PrimaryOption":true,
		 * 	"AllowsMultiple":false,
		 * 	"RequiresCardNumber":false,
		 * 	"RequiredNumeric":false,
		 * 	"MinimumLength":0,
		 * 	"MaximumLength":0,
		 * 	"RequiresAmount":false,
		 * 	"CardNumber":null,
		 * 	"Amount":null,
		 * 	"Image":null,
		 * 	"IsVendor":true,
		 * 	"SuccessCallbackUri":"https://shop.shoprite.com/store/DA87780/checkout/ProcessPayment?authorized=True",
		 * 	"CancelCallbackUri":"https://shop.shoprite.com/store/DA87780/checkout/ProcessPayment?authorized=False",
		 * 	"CardNumberLabel":null,
		 * 	"AmountLabel":null,
		 * 	"Items":[],
		 * 	"FulfillmentType":""
		 * }]
		 */

		ArrayList<String> Items = new ArrayList<>();
		ArrayList<Map> PaymentMethods = new ArrayList<>();
		JSONObject retval = new JSONObject();

		this.requestToken = authToken;  //WakefernApplicationConstants.authToken;
		String successCallbackURL = "https://shop.shoprite.com/store/" + storeId + "/checkout/ProcessPayment?authorized=True";
		String cancelCallbackURL = "https://shop.shoprite.com/store/" + storeId + "/checkout/ProcessPayment?authorized=False";

		Map returnMap = this.paymentMap();
		returnMap.put("SuccessCallbackUri", successCallbackURL);
		returnMap.put("CancelCallbackUri", cancelCallbackURL);
		returnMap.put("Items", Items);
		PaymentMethods.add(returnMap);

		String path = "https://api.shoprite.com/api" 
				+ MWGApplicationConstants.Requests.Checkout.UserCheckout
				+ "/" + userId + ApplicationConstants.StringConstants.store
				+ "/" + storeId + ApplicationConstants.StringConstants.payment;

		if (!isMember.isEmpty()) {
			path += ApplicationConstants.StringConstants.isMember;
		}
		
		logger.log(Level.INFO, "[getInfo][GetToken]::Path: ", path);

		retval.put("PaymentMethods", PaymentMethods);

		ServiceMappings secondMapping = new ServiceMappings();
		secondMapping.setPutMapping(this, retval.toString());
		
		logger.log(Level.INFO, "[getInfo][GetToken]::req: ", retval.toString());

		try {
			String resp = HTTPRequest.executePut(path, secondMapping.getGenericBody(), secondMapping.getgenericHeader());
			logger.log(Level.INFO, "[getInfo][GetToken]::resp: ", resp);
			return this.createValidResponse(resp);
		
		} catch (Exception e) {
			return this.createErrorResponse(e);
		}
	}

	public GetToken() {
		this.requestHeader = new MWGHeader();
	}
}