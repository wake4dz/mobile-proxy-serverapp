/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.core;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.wakefern.paymentgatewayclient.nvp.request.Request;
import com.wakefern.paymentgatewayclient.nvp.domain.Constants;
import com.wakefern.paymentgatewayclient.nvp.profile.Profile;
import com.wakefern.paymentgatewayclient.nvp.util.LogToFile;
import com.wakefern.statica.StaticaProxyAuthenticator;

/**
 * This EPayClientSample class can be used as the main program which sends requests and 
 * captures responses from the Payment Gateway.
 * 
 * 	////===========  STEPS for making NVP calls to the Payment Gateway  ===========////
 * 
 *  //---------------------------------------------------------------------------------
 *	//--- Step 1: Preparing Profile - Store 902 OAuth credentials in TEST
 *  //---------------------------------------------------------------------------------
 *	final String USER      = "client_0902";
 *	final String PWD       = "HcOrZ8OFVcO/O8KMw5tZPMK2eMKHwpZJ";
 *	final String SIGNATURE = "w67DicO2dsKXQTMzw7hyIFjDiEjCk8K6V8Osw4pAUz1xJ8OUw6fDlyzDi0sHwq9IDcKqQcKjwpZZw4LDlcOtcxvCgV7CmDg=";
 *	Profile userProf       = (new BaseProfile.Builder(USER, PWD, SIGNATURE)).build(); 
 *
 *  //---------------------------------------------------------------------------------
 *	//--- Step 2: Preparing/Set the environment to either DEV, QA, or Prod
 *  //---------------------------------------------------------------------------------
 *	Environment env = EPayClientSample.Environment.DEV;   // For DEV
 *	Environment env = EPayClientSample.Environment.QA;    // For QA
 *  Environment env = EPayClientSample.Environment.PROD;  // For PROD
 *  
 *  //---------------------------------------------------------------------------------
 *	//--- Step 3: Creating the new EPayClientSample
 *  //---------------------------------------------------------------------------------
 *	EPayClientSample ePayClientSample = new EPayClient(userProf, env);
 *
 *  //---------------------------------------------------------------------------------
 *	//--- Step 4: Preparing the Request message
 *  //---------------------------------------------------------------------------------
 *	// Preparing the return and cancel URL's
 *	String returnURL = "http://www.wakefern.com/returnURL/abc";
 *	String cancelURL = "http://www.wakefern.com/cancelURL/xyz";
 *	
 *	// Preparing the Payment object
 *	Payment paymentMsg = new Payment("50.00");
 *	paymentMsg.setButtonSource("");
 *	paymentMsg.setCurrency("USD");
 *	paymentMsg.setCustomField("");
 *	paymentMsg.setDescription("Test Message");
 *	paymentMsg.setHandlingAmount("10.00");
 *	paymentMsg.setInvoiceNumber("123456");
 *	paymentMsg.setNotifyUrl("http://www.wakefern.com/notifyURL");
 *	paymentMsg.setTransactionId("EDsf43X23dsd4Od7f");
 *	paymentMsg.set......
 *	
 *	// Creating the new Request: SetExpressCheckout, GetExpresscheckoutDetails, DoAuthorization, etc...
 *	SetExpressCheckout req = new SetExpressCheckout(paymentMsg, returnURL, cancelURL);
 *	
 *  //---------------------------------------------------------------------------------
 *	//--- Step 5: Executing HTTP call and getting the response from the Payment Gateway
 *  //---------------------------------------------------------------------------------
 *	EPayClientSample.setResponse(req);
 *	
 *  //---------------------------------------------------------------------------------
 *	//--- Step 6 (for SetExpressCheckout only): Based on the result, redirect the user 
 *  //---                                       to the Payment page
 *  //---------------------------------------------------------------------------------
 *	//EPayClientSample.getRedirectUrl(request);
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/05/2015
 */
public final class EPayClientSample implements Serializable {
	
	private static final long serialVersionUID = 2105141766418067086L;

    // Sends request and returns response 
    private final Transport transport;

    // Profile class holds the profile details such as the OAuth's credentials 
    private final Profile profile;

    // Environment - DEV, QA, PROD, etc. 
    private final Environment environment;

    /**
     * Payment Gateway Environments - Dev, Test/QA, or Production
     * This is the part of the URI to access the Payment Gateway
     */
    public enum Environment {
    	// DEV environment 
        DEV("epaydev.wakefern.com/paymentclient"),
        // QA or TEST environment 
        QA("epayqa.wakefern.com/paymentclient"),
        // PROD environment 
        PROD("epay.wakefern.com/paymentclient"),
        // LOCAL MACHINE
        LOCAL("localhost:9443/PaymentClient");
        
        // String representation of the environment as part of the NVP URL 
        private final String environment;

        private Environment(String environment) { this.environment = environment; }

        /**
         * Return URL where you send request, this changes according to the environment set.
         *
         * @return - URL string where to send request
         */
        private String getEnvironmentPartUrl() {
            return environment;
        }
    }

    /** 
     * Common for all constructors 
     */
    { transport = new HttpPost(); }

    /**
     * Returns new instance of EPayClientSample to use with the API signatures.
     * 
     * @param profile
     * @param environment
     */
    public EPayClientSample(Profile profile, Environment environment) {
        this.profile     = profile;
        this.environment = environment;
    }

    /**
     * Sets response from ePay. Call setNVPResponse on supplied request
     * argument and sets response Map from ePay.
     * For Request inputs, please refer to the Individual Request classes for
     * further details on what fields are required and/or included. They are
     * located in: com.wakefern.paymentgatewayclient.nvp.request package
     *
     * @param request
     */
    public void setResponse(Request request) {
    	String methodName = "setResponse(Request request)";
    	
    	LogToFile.dateLog(this.getClass().getName() + "." + methodName + ": was called at " 
    													  + (new Date()).toString());
    	
        StringBuffer nvpString = new StringBuffer();
        String encoding = Constants.ENCODING;    // "UTF-8" character encoding for the NVP string

        // Creating NVP string 
        try {
            // Building the Profile part 
            for (Entry<String, String> e : profile.getNVPMap().entrySet()) {
                nvpString.append(e.getKey() + "=" + URLEncoder.encode(e.getValue(), encoding));
                nvpString.append("&");
            }
            // Creating the request part 
            for (Entry<String, String> e : request.getNVPRequest().entrySet()) {
                nvpString.append(e.getKey() + "=" + URLEncoder.encode(e.getValue(), encoding));
                nvpString.append("&");
            }
            // Building the rest of the field(s) 
            nvpString.append("VERSION=" + URLEncoder.encode(Constants.VERSION, encoding)); // VERSION=95.0
            
        } catch (UnsupportedEncodingException ex) {
            LogToFile.dateLog(this.getClass().getName() + "." + methodName 
            									+ ": UnsupportedEncodingException was thrown - " + ex.getMessage());
        } catch (Exception ex) {
            LogToFile.dateLog(this.getClass().getName() + "." + methodName + ": Exception was thrown - "
            									+ ex.getMessage());
        }

        // Create the end-point URL depending on the environment used
        StringBuffer endpointUrl = new StringBuffer();
       	endpointUrl.append("https://");   // For other environments       	
        endpointUrl.append(environment.getEnvironmentPartUrl());
        endpointUrl.append("/nvp");

        // send the request and capture then save response 
        String response = null;
        try {
        	LogToFile.dateLog(this.getClass().getName() + "." + methodName + ": Calling transport.getResponse(...) with endpoint=" 
        									+ endpointUrl.toString());
        	
            response = transport.getResponse(endpointUrl.toString(), nvpString.toString());
            
        } catch (MalformedURLException ex) {
        	LogToFile.dateLog(this.getClass().getName() + "." + methodName 
        						+ ": MalformedURLException was thrown - " + ex.getMessage());
        }

        //--- If there is response comes back then....
        if (response != null && response.indexOf("&") > 0 && response.indexOf("=") > 0) {
        	LogToFile.dateLog(this.getClass().getName() + "." + methodName 
							+ ": Response with length as " + response.length() 
							+ " was returned from the NVP end-point call.");
        	
            // Declare the HashMap which holds the response
            Map<String, String> responseMap = new HashMap<String, String>();

            // Add response to the Map 
            String[] pairs = null;
            try {
                pairs = response.split("&");  // split the NVP string
                for (String pair : pairs) {
                    String[] nvp = pair.split("=");    // split key/value pair
                    responseMap.put(nvp[0], URLDecoder.decode(nvp[1], encoding));
                }
                
            } catch (UnsupportedEncodingException ex) {
            	LogToFile.dateLog(this.getClass().getName() + "." + methodName 
            						+ ": UnsupportedEncodingException was thrown - " + ex.getMessage());
            }
            LogToFile.dateLog(this.getClass().getName() + "." + methodName + ": Responds back with " 
            								+ ((pairs==null)?"NULL":pairs.length)+ " name-value pairs");
            // Set response 
            request.setNVPResponse(responseMap);
            
        } else {  //--- If no response, then...
        	//String responseMod = ((response==null)?"":response.trim());
        	LogToFile.dateLog(this.getClass().getName() + "." + methodName 
									+ ": Invalid or no response was returned from the NVP end-point call.");
        }
    }
    
//    public String getResponse(StaticaProxyAuthenticator proxy, String urlToRead) {
//        String result = "";
//        try {
//           URL url = new URL(urlToRead);
//           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//           conn.setRequestProperty("Proxy-Authorization", "Basic " + proxy.getEncodedAuth());
//           conn.setRequestProperty("Accept-Encoding", "gzip");
//           Authenticator.setDefault(proxy.getAuth());
//           conn.setRequestMethod("POST");
//           InputStream is = conn.getInputStream();
//           if(conn.getContentEncoding()!=null && conn.getContentEncoding().equalsIgnoreCase("gzip")){
//             is = new GZIPInputStream(is);
//           }
//           byte[] buffer = new byte[1024];
//           int len;
//           ByteArrayOutputStream bos = new ByteArrayOutputStream();
//           while (-1 != (len = is.read(buffer))) {
//             bos.write(buffer, 0, len);
//           }           
//           result = new String(bos.toByteArray());
//           is.close();
//        } catch (IOException e) {
//           e.printStackTrace();
//        } catch (Exception e) {
//           e.printStackTrace();
//        }
//        return result;
//     }    
    

    /**
     * Function to redirect the user to the URL which is the Payment Page. If Request has
     * not been sent, or response has not been successful, null is returned.
     * 
     * @return - URL where to redirect 
     */
    public String getRedirectUrl(Request request) {
    	String methodName = "getRedirectUrl(...)";
    	
        // Getting the response 
        Map<String, String> response = request.getNVPResponse();

        // nvpResponse is not set 
        if (response == null) {
        	LogToFile.dateLog(this.getClass().getName()+ "." + methodName 
        							+ ": There is no response from the NVP call.");
            return null;
        }
        //System.out.println("Response String :: " + response.toString());
        String epaywebURL = "";
        String ack   = response.get("ACK");
        String token = response.get("TOKEN");
        // Validating the ACK and the TOKEN 
        if (ack == null || !ack.equalsIgnoreCase("SUCCESS")) {
        	LogToFile.dateLog(this.getClass().getName()+ "." + methodName 
        						+ ": ACK was NOT satisfied from the NVP response.");
        	
        } else if (token == null || token.trim().length() < 19) {
        	LogToFile.dateLog(this.getClass().getName()+ "." + methodName 
        						+ ": TOKEN was NOT satisfied from the NVP response.");
        	
        } else {
        	LogToFile.dateLog(this.getClass().getName()+ "." + methodName 
        						+ ": ACK and TOKEN (" + token + ") returned successfully from the NVP response.");
        	
       		epaywebURL = "https://" + environment.getEnvironmentPartUrl() + "/epayweb?cmd=_express-checkout&token=" + token;
       		LogToFile.dateLog(this.getClass().getName()+ "." + methodName + ": redirecting to URL=" + epaywebURL);
        }    
        return epaywebURL;
    }

    @Override
    public String toString() {
		return "Instance of EPayClientSample class with values: VERSION: " + Constants.VERSION
                + ", User profile: " + profile.toString() + ", Transport transport: "
                + transport.toString() + ", Environment environment: " + environment.toString();
    }
    
}

