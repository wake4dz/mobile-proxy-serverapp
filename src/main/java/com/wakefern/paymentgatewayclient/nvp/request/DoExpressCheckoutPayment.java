/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.request;

import java.util.HashMap;
import java.util.Map;
import com.wakefern.paymentgatewayclient.nvp.domain.Address;
import com.wakefern.paymentgatewayclient.nvp.domain.Constants;
import com.wakefern.paymentgatewayclient.nvp.domain.Payment;
import com.wakefern.paymentgatewayclient.nvp.domain.PaymentAction;

/**
 * DoExpressCheckoutPayment class
 * 
 * DoExpressCheckoutPayment message format:
 * -------------------------------
 *    USER=client_9999  (part of Profile)
 *    PWD=*********************  (part of Profile)
 *    SIGNATURE=******************************************************  (part of Profile)
 *    VERSION=95.0&
 *    METHOD=DoExpressCheckoutPayment& 
 *    TOKEN=EC-6CW56215KR207953C& 
 *    PAYERID=W34R2LC226Y4W& 
 *    PAYMENTREQUEST_0_PAYMENTACTION=Order& 
 *    PAYMENTREQUEST_0_AMT=150.00& 
 *    L_PAYMENTREQUEST_0_NAME0=Your Groceries& 
 *    L_PAYMENTREQUEST_0_AMT0=150.00& 
 *    L_PAYMENTREQUEST_0_QTY0=1& 
 *    PAYMENTREQUEST_0_ITEMAMT=150.00& 
 *    BUTTONSOURCE=Wakefern_Ecom_EC& 
 *    PAYMENTREQUEST_0_SHIPTONAME=John Smith& 
 *    PAYMENTREQUEST_0_SHIPTOSTREET=123 MAIN ST&
 *    PAYMENTREQUEST_0_NOTIFYURL=https://shop.shoprite.com/Endpoint/PaymentNotification.aspx& 
 *    PAYMENTREQUEST_0_SHIPTOCITY=EDISON&
 *    PAYMENTREQUEST_0_SHIPTOSTATE=NJ& 
 *    PAYMENTREQUEST_0_SHIPTOZIP=08837& 
 *    PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE=US& 
 *    PAYMENTREQUEST_0_SHIPTOPHONENUM=8005551234
 * 
 * @author Wakefern Food Corp.
 * @date 10/06/2015
 */
public final class DoExpressCheckoutPayment implements Request {

	private static final long serialVersionUID = 6542681635459330245L;

	// Method value of this request 
    private static final String METHOD_NAME = Constants.NVP_METHOD_DOEXPRESSCHECKOUTPAYMENT;

    private static final String paddedFieldName = "PAYMENTREQUEST_0_";
	
    // Map that holds name-value pair request values /
    private final Map<String, String> nvpRequest;

    // Map that holds name-value pair response values 
    private Map<String, String> nvpResponse;

    /**
     * 
	 * @param payment		Should be the same as for SetExpressCheckout
     * @param token         Payment Gateway returned Token (EC-....)
     * @param paymentAction How you want to obtain payment
     * @param payerId       Unique Payment Gateway customer account identification 
     *                      number as returned by GetExpressCheckoutDetails 
     *                      response
     * @throws IllegalArgumentException
     */
    public DoExpressCheckoutPayment(Payment payment, String token, PaymentAction paymentAction, 
    								String payerId, String buttonSource, String notifyURL) throws IllegalArgumentException {
        if (token.length() != 20) {
            throw new IllegalArgumentException("Invalid token argument");
        }
        if (payerId == null || payerId.trim().length() < 1 || payerId.length() > 17) {
            throw new IllegalArgumentException("Invalid payer ID");
        }

        nvpResponse = new HashMap<String, String>();
        nvpRequest  = new HashMap<String, String>();

        nvpRequest.put("VERSION", Constants.VERSION);
        nvpRequest.put("METHOD", METHOD_NAME);
		// insert payment values 
		HashMap<String, String> paymentPairs = new HashMap<String, String>(payment.getNVPRequest());
        nvpRequest.putAll(paymentPairs);
        nvpRequest.put("TOKEN", token);
        nvpRequest.put(paddedFieldName + "PAYMENTACTION", paymentAction.getValue());
        nvpRequest.put("PAYERID", payerId);
        nvpRequest.put("BUTTONSOURCE", buttonSource);
        nvpRequest.put(paddedFieldName + "NOTIFYURL", notifyURL);
    }

	/**
	 * Flag to indicate whether you want the results returned by Fraud 
	 * Management Filters. By default this is false. 
	 * 
	 * @param fmf	true: receive FMF details
	 *				false: do not receive FMF details (default)
	 */
	public void setReturnFMF(boolean fmf) {

		int x = (fmf) ? 1 : 0;
        nvpRequest.put("RETURNFMFDETAILS", Integer.toString(x));
	}

	/**
	 * Sets address fields
	 *
	 * @param address
	 */
	public void setAddress(Address address) {
		HashMap<String, String> nvp =
				new HashMap<String, String>(address.getNVPRequest());
		nvpRequest.putAll(nvp);
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
        StringBuffer str = new StringBuffer("Instance of DoExpressCheckoutPayment ");
        str.append("class with the values as nvpRequest: " + nvpRequest.toString());
		str.append("; nvpResponse: " + nvpResponse.toString());
		return str.toString();
    }
}
