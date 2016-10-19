package com.wakefern.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONObject;

import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.paymentgatewayclient.nvp.core.EPayClientSample;
import com.wakefern.paymentgatewayclient.nvp.core.EPayClientSample.Environment;
import com.wakefern.paymentgatewayclient.nvp.domain.Payment;
import com.wakefern.paymentgatewayclient.nvp.profile.BaseProfile;
import com.wakefern.paymentgatewayclient.nvp.profile.Profile;
import com.wakefern.paymentgatewayclient.nvp.request.SetExpressCheckout;
import com.wakefern.request.HTTPRequest;

import org.json.JSONArray;
import org.json.JSONObject;


@Path("payment/token/user/{userId}/store/{storeId}")
public class GetToken extends BaseService {
    
	private String Id = "59";
	private String Name = "";
	private String PaymentMethodMessage = "";
	private String PayMethodTooltipUri = "";
	private String PrimaryOption = "";
	
	private Map<String,String> paymentMap(){
		Map returnMap = new LinkedHashMap<String,String>();
		returnMap.put("Id", "59");
		returnMap.put("Name", "");
		returnMap.put("PaymentMethodMessage","");
		returnMap.put("PayMethodTooltipUri","");
		returnMap.put("PrimaryOption",true);
		returnMap.put("AllowsMultiple",false);
		returnMap.put("RequiresCardNumber",false);
		returnMap.put("RequiredNumeric",false);
		returnMap.put("MinimumLength",0);
		returnMap.put("MaximumLength",0);
		returnMap.put("RequiresAmount",false);
		returnMap.put("CardNumber",null);
		returnMap.put("Amount",null);
		returnMap.put("Image",null);
		returnMap.put("IsVendor",true);
		returnMap.put("CardNumberLabel",null);
		returnMap.put("AmountLabel",null);
		returnMap.put("FulfillmentType",null);

		return returnMap;
	}
	
	@GET
    @Produces("application/*")
	/*expected body
	 * 
	 * {"PaymentMethods":[{"Id":"59","Name":"","PaymentMethodMessage":"","PayMethodTooltipUri":"","PrimaryOption":true,"AllowsMultiple":false,"RequiresCardNumber":false,"RequiredNumeric":false,"MinimumLength":0,"MaximumLength":0,"RequiresAmount":false,"CardNumber":null,"Amount":null,"Image":null,"IsVendor":true,"SuccessCallbackUri":"https://shop.shoprite.com/store/DA87780/checkout/ProcessPayment?authorized=True","CancelCallbackUri":"https://shop.shoprite.com/store/DA87780/checkout/ProcessPayment?authorized=False","CardNumberLabel":null,"AmountLabel":null,"Items":[],"FulfillmentType":""}]}
	 * 
	 */
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("userId") String userId, @HeaderParam("Authorization") String authToken){
        this.token = authToken;
		String successCallbackURL = "https://shop.shoprite.com/store/" + storeId + "/checkout/ProcessPayment?authorized=True";
		String cancelCallbackURL =  "https://shop.shoprite.com/store/"+ storeId + "/checkout/ProcessPayment?authorized=False";
		Map returnMap = this.paymentMap();
		returnMap.put("SuccessCallbackUri",successCallbackURL);
		returnMap.put("CancelCallbackUri",cancelCallbackURL);
		ArrayList<String> Items = new ArrayList<String>();
		ArrayList<Map> PaymentMethods = new ArrayList<Map>();
		returnMap.put("Items",Items);
		PaymentMethods.add(returnMap);
        JSONObject retval = new JSONObject();
		
        //https://shop.shoprite.com/api/checkout/v5/user/6207ebc1-94df-49d9-9983-0b5280ba02da/store/DA87780/payment
        String path = "https://shop.shoprite.com/api/checkout/v5/user/" + userId + "/store/" + storeId + "/payment";
        retval.put("PaymentMethods", PaymentMethods);
        System.out.println("Payment Methods :: " + retval.toString());
        //return retval.toString();
        
        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, retval.toString());

        try {
			return HTTPRequest.executePut("", path, "", secondMapping.getGenericBody(), secondMapping.getgenericHeader());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "error";//
			//e.printStackTrace();
		}
		
	}
	
	public GetToken(){
        this.serviceType = new MWGHeader();
    }
	
//	 final String USER      = "client_0902";
//	 final String PWD       = "HcOrZ8OFVcO/O8KMw5tZPMK2eMKHwpZJ";
//	 final String SIGNATURE = "w67DicO2dsKXQTMzw7hyIFjDiEjCk8K6V8Osw4pAUz1xJ8OUw6fDlyzDi0sHwq9IDcKqQcKjwpZZw4LDlcOtcxvCgV7CmDg=";
//	 Profile userProf       = (new BaseProfile.Builder(USER, PWD, SIGNATURE)).build(); 
//	 Environment env = EPayClientSample.Environment.PROD;
//	 
//	 EPayClientSample ePayClientSample = new EPayClientSample(userProf, env);
//	 
//	 String returnURL = "http://www.wakefern.com/returnURL/abc";
//	 String cancelURL = "http://www.wakefern.com/cancelURL/xyz";
//	 	
//	 	// Preparing the Payment object
//	 	Payment paymentMsg = new Payment("50.00");
//	 	paymentMsg.setButtonSource("");
//	 	paymentMsg.setCurrency("USD");
//	 	paymentMsg.setCustomField("");
//	 	paymentMsg.setDescription("Test Message");
//	 	paymentMsg.setHandlingAmount("10.00");
//	 	paymentMsg.setInvoiceNumber("123456");
//	 	paymentMsg.setNotifyUrl("http://www.wakefern.com/notifyURL");
//	 	paymentMsg.setTransactionId("EDsf43X23dsd4Od7f");
//	 	
//	 	SetExpressCheckout req = new SetExpressCheckout(paymentMsg, returnURL, cancelURL);
//	 	ePayClientSample.setResponse(req);
//	 	String redirectURL = ePayClientSample.getRedirectUrl(req);
//	
//	JSONObject response = new JSONObject();
//	response.put("token", "xxx-xxxx-xxxx-xxxx");
//	return redirectURL;
	
}