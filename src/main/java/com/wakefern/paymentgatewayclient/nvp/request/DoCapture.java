/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.request;

import java.util.HashMap;
import java.util.Map;
import com.wakefern.paymentgatewayclient.nvp.domain.Constants;
import com.wakefern.paymentgatewayclient.nvp.domain.Currency;
import com.wakefern.paymentgatewayclient.nvp.util.Validator;

/**
 * Capture an authorized payment to the Payment Gateway.
 * 
 * DoCapture message format:
 * -------------------------
 *    USER=client_9999  (part of Profile)
 *    PWD=*********************  (part of Profile)
 *    SIGNATURE=******************************************************  (part of Profile)
 *    VERSION=95.0&
 *    METHOD=DoCapture&
 *    AUTHORIZATIONID=78E2632833237611M& 
 *    COMPLETETYPE=Complete&
 *    AMT=140.00& 
 *    INVNUM=9804374
 *
 * @author Wakefern Food Corp.
 * @date 10/06/2015
 */
public final class DoCapture implements Request {

	private static final long serialVersionUID = 1682098971973348722L;

	/** Method value of this request */
    private static final String METHOD_NAME = Constants.NVP_METHOD_DOCAPTURE;

    /** map that holds name value pair request values */
    private final Map<String, String> nvpRequest;

    /** map that holds name value pair response values */
    private Map<String, String> nvpResponse;

	/**
	 * Returns DoCapture instance if arguments are valid, otherwise an exception is thrown.
	 *
	 * @param authorizationId	The authorization identification number of the payment you want 
	 * 							to capture. This is the	transaction id returned from DoExpressCheckoutPayment 
	 * 							or DoDirectPayment.	Character length and limits: 20 characters maximum.
	 * @param amount 			Amount to capture.
	 * 							Limitations: Value is a positive number which cannot exceed $9,999.99 USD 
	 * 							in any currency. No	currency symbol. Must have two decimal places,
	 * 							decimal separator must be a period (.).
	 * @param completeType		Complete indicates that this the last capture you intend to make. The value 
	 * 							NotComplete	indicates that you intend to make additional captures.
	 * 							NOTE: If Complete, any remaining amount of the original authorized transaction is
	 * 							automatically voided and all remaining open	authorizations are voided.
	 * @param invoiceNum		Your invoice number or other identification number that is displayed to
	 * 							the merchant and customer in his transaction history. MAX=127 chars
	 * @throws IllegalArgumentException
	 */
	public DoCapture(String authorizationId, String amount, 
					boolean completeType, String invoiceNum) throws IllegalArgumentException {

        /* validation */
		if (authorizationId == null || authorizationId.length() > 20) {
            throw new IllegalArgumentException("Authorization id can be maximum 20 characters long.");
		}
        if (!Validator.isValidAmount(amount)) {
            throw new IllegalArgumentException("Amount is not valid");
        }
		String complete = (completeType)?"Complete":"NotComplete";
		if (invoiceNum == null || invoiceNum.trim().equals("")) {
			invoiceNum = "0";
		}

        /* instance variables */
        nvpResponse = new HashMap<String, String>();
        nvpRequest  = new HashMap<String, String>();
        
        nvpRequest.put("VERSION", Constants.VERSION);
        nvpRequest.put("METHOD", METHOD_NAME);
        nvpRequest.put("AUTHORIZATIONID", authorizationId);
        nvpRequest.put("COMPLETETYPE", complete);
        nvpRequest.put("AMT", amount);
        nvpRequest.put("INVNUM", invoiceNum);
	}

	/**
	 * Sets currency code (optional)
	 * 
	 * @param currency Default is USD.
	 */
	public void setCurrency(Currency currency) {
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
        StringBuffer str = new StringBuffer("Instance of DoCapture");
        str.append("class with the values as nvpRequest: " + nvpRequest.toString());
		str.append("; nvpResponse: " + nvpResponse.toString());
		return str.toString();
    }
}
