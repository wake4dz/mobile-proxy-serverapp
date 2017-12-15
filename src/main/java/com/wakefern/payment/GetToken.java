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
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

@Path("payment/token/user/{userId}/store/{storeId}")
public class GetToken extends BaseService {
	
	private final static Logger logger = Logger.getLogger("GetToken");

	private Map<String, String> paymentMap() {
		Map returnMap = new LinkedHashMap<>();
		returnMap.put(ApplicationConstants.Payment.Id, ApplicationConstants.Payment.HardCodedId);
		returnMap.put(ApplicationConstants.Payment.Name, "");
		returnMap.put(ApplicationConstants.Payment.PaymentMethodMessage, "");
		returnMap.put(ApplicationConstants.Payment.PayMethodTooltipUri, "");
		returnMap.put(ApplicationConstants.Payment.PrimaryOption, true);
		returnMap.put(ApplicationConstants.Payment.AllowsMultiple, false);
		returnMap.put(ApplicationConstants.Payment.RequiresCardNumber, false);
		returnMap.put(ApplicationConstants.Payment.RequiredNumeric, false);
		returnMap.put(ApplicationConstants.Payment.MinimumLength, 0);
		returnMap.put(ApplicationConstants.Payment.MaximumLength, 0);
		returnMap.put(ApplicationConstants.Payment.RequiresAmount, false);
		returnMap.put(ApplicationConstants.Payment.CardNumber, null);
		returnMap.put(ApplicationConstants.Payment.Amount, null);
		returnMap.put(ApplicationConstants.Payment.Image, null);
		returnMap.put(ApplicationConstants.Payment.IsVendor, true);
		returnMap.put(ApplicationConstants.Payment.CardNumberLabel, null);
		returnMap.put(ApplicationConstants.Payment.AmountLabel, null);
		returnMap.put(ApplicationConstants.Payment.FulfillmentType, null);

		return returnMap;
	}

	@GET
	@Produces("application/*")
	/**expected body
	 *
	 * {"PaymentMethods":[{"Id":"59","Name":"","PaymentMethodMessage":"","PayMethodTooltipUri":"","PrimaryOption":true,"AllowsMultiple":false,"RequiresCardNumber":false,"RequiredNumeric":false,"MinimumLength":0,"MaximumLength":0,"RequiresAmount":false,"CardNumber":null,"Amount":null,"Image":null,"IsVendor":true,"SuccessCallbackUri":"https://shop.shoprite.com/store/DA87780/checkout/ProcessPayment?authorized=True","CancelCallbackUri":"https://shop.shoprite.com/store/DA87780/checkout/ProcessPayment?authorized=False","CardNumberLabel":null,"AmountLabel":null,"Items":[],"FulfillmentType":""}]}
	 *
	 */
	public Response getInfo(@PathParam("storeId") String storeId, @PathParam("userId") String userId,
							@DefaultValue("")@QueryParam("isMember") String isMember, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
		ArrayList<String> Items = new ArrayList<>();
		ArrayList<Map> PaymentMethods = new ArrayList<>();
		JSONObject retval = new JSONObject();

		this.requestToken = authToken;
		String successCallbackURL = "https://shop.shoprite.com/store/" + storeId + ApplicationConstants.Payment.SuccessCallbackURL;
		String cancelCallbackURL = "https://shop.shoprite.com/store/" + storeId + ApplicationConstants.Payment.CancelCallbackURL;

		Map returnMap = this.paymentMap();
		returnMap.put(ApplicationConstants.Payment.SuccessCallbackUri, successCallbackURL);
		returnMap.put(ApplicationConstants.Payment.CancelCallbackUri, cancelCallbackURL);
		returnMap.put(ApplicationConstants.Payment.Items, Items);
		PaymentMethods.add(returnMap);

		String path = "https://api.shoprite.com/api" + ApplicationConstants.Requests.Checkout.UserCheckout
				+ ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
				+ ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.payment;

		if(!isMember.isEmpty()){
			path += ApplicationConstants.StringConstants.isMember;
		}
		logger.log(Level.INFO, "[getInfo][GetToken]::Path: ", path);

		retval.put(ApplicationConstants.Payment.PaymentMethods, PaymentMethods);

		ServiceMappings secondMapping = new ServiceMappings();
		secondMapping.setPutMapping(this, retval.toString(), null);
		logger.log(Level.INFO, "[getInfo][GetToken]::req: ", retval.toString());

		try {
			String resp = HTTPRequest.executePut("", path, "", secondMapping.getGenericBody(), secondMapping.getgenericHeader(), 0);
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