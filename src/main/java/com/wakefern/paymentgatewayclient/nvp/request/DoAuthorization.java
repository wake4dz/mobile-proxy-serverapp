/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.request;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.wakefern.paymentgatewayclient.nvp.domain.Constants;
import com.wakefern.paymentgatewayclient.nvp.domain.Currency;
import com.wakefern.paymentgatewayclient.nvp.util.Validator;

/**
 * Authorize a payment Request message.
 * 
 * DoAuthorization message format:
 * -------------------------------
 *    USER=client_9999  (part of Profile)
 *    PWD=*********************  (part of Profile)
 *    SIGNATURE=******************************************************  (part of Profile)
 *    VERSION=95.0&
 *    METHOD=DoAuthorization& 
 *    AMT=140.00&
 *    TRANSACTIONID=O-1KJ77171PT877551C&       --- OR ---   EC-1KJ77171PT877551C&
 *    CURRENCYCODE=USD
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/06/2015
 */
public final class DoAuthorization implements Request {

	private static final long serialVersionUID = -3070086222544053100L;

	// Method value of this request 
    private static final String METHOD_NAME = Constants.NVP_METHOD_DOAUTHORIZATION;

    // Map that holds name-value pair request values 
    private final Map<String, String> nvpRequest;

    // Map that holds name-value pair response values 
    private Map<String, String> nvpResponse;

	/**
	 * @param transactionId	The value of the order's transaction identification 
	 * 						number returned by Payment Gateway in the SetExpressCheckout step
	 * 						Character length and limits: 20 single-byte characters maximum.
	 * 						i.e. EC-AWG56124KR207956Y
	 * @param amount		Amount to authorize for payment.
	 * 						Limitations: Value is a positive number which cannot exceed $10,000 USD 
	 * 						in any currency. No	currency symbol. Must have two decimal places, 
	 * 						decimal separator must be a period (.).
	 * @throws IllegalArgumentException
	 */
    public DoAuthorization(String transactionId, String amount) 
											throws IllegalArgumentException {

        // validation...
		if (transactionId == null || transactionId.length() > 20) {
            throw new IllegalArgumentException("Transaction ID can be maximum 20 characters long.");
		}
		
        if (!Validator.isValidAmount(amount)) {
            throw new IllegalArgumentException("Amount is not valid");
        }

        // instance variables...
        nvpResponse = new HashMap<String, String>();
        nvpRequest  = new HashMap<String, String>();
        
        nvpRequest.put("VERSION", Constants.VERSION);
        nvpRequest.put("METHOD", METHOD_NAME);
        nvpRequest.put("TRANSACTIONID", transactionId);
        nvpRequest.put("AMT", amount);
        nvpRequest.put("CURRENCYCODE", Currency.USD.toString());
    }

	/**
	 * Sets currency to other than USD which is by default. 
	 *
	 * @param currency
	 */
	public void setCurrency(Currency currency) {
		// Remove the existing "CURRENCYCODE" key if exists
		Iterator<Map.Entry<String,String>> iter = nvpRequest.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<String,String> entry = iter.next();
		    if ("CURRENCYCODE".equalsIgnoreCase(entry.getKey())) {
		        iter.remove();
		    }
		}
		// Assign the new "CURRENCYCODE" key-value 
		if (currency == null) {
			nvpRequest.put("CURRENCYCODE", Currency.USD.toString());
		} else {
			nvpRequest.put("CURRENCYCODE", currency.toString());
		}
	}

    public Map<String, String> getNVPRequest() {
        return new HashMap<String, String>(nvpRequest);
    }

    public void setNVPResponse(Map<String, String> nvpResponse) {
        this.nvpResponse = new HashMap<String, String>(nvpResponse);
    }

    public Map<String, String> getNVPResponse() {
        return new HashMap<String, String>(nvpResponse);
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("Instance of DoAuthorization");
        str.append("class with the values as nvpRequest: " + nvpRequest.toString());
		str.append("; nvpResponse: " + nvpResponse.toString());
		return str.toString();
    }
}
