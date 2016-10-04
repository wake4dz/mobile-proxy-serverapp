/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.request;

import java.util.HashMap;
import java.util.Map;

import com.wakefern.paymentgatewayclient.nvp.domain.Constants;

/**
 * Obtain information about an Express Checkout transaction.
 * 
 * GetExpressCheckoutDetails message format:
 * -------------------------------
 *    USER=client_9999  (part of Profile)
 *    PWD=*********************  (part of Profile)
 *    SIGNATURE=******************************************************  (part of Profile)
 *    VERSION=95.0&
 *    METHOD=GetExpressCheckoutDetails& 
 *    TOKEN=EC-6CW56215KR207953C
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/08/2015
 */
public final class GetExpressCheckoutDetails implements Request {

	private static final long serialVersionUID = 8479418949120242736L;

	// Method value of this request 
    private static final String METHOD_NAME = Constants.NVP_METHOD_GETEXPRESSCHECKOUTDETAILS;

    // Map that holds name-value pair request values 
    private final Map<String, String> nvpRequest;

    // Map that holds name-value pair response values 
    private Map<String, String> nvpResponse;

    /**
     * Token is appended to the return or cancel URL set in SetExpressCheckout. 
     * 
     * @param token - Payment Gateway token
     * @throws IllegalArgumentException
     */
    public GetExpressCheckoutDetails(String token)
            throws IllegalArgumentException {

        if (token.length() != 20) {
            throw new IllegalArgumentException("Invalid token argument");
        }

        nvpResponse = new HashMap<String, String>();
        nvpRequest  = new HashMap<String, String>();

        nvpRequest.put("METHOD", METHOD_NAME);
        nvpRequest.put("TOKEN", token);
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

        StringBuffer str = new StringBuffer("Instance of GetExpressCheckoutDetails ");
        str.append("class with the values as nvpRequest: ");
        str.append(nvpRequest.toString());
		str.append("; nvpResponse: ");
		str.append(nvpResponse.toString());
		return str.toString();
    }
}
