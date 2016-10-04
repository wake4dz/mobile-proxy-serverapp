/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.request;

import java.util.HashMap;
import java.util.Map;

import com.wakefern.paymentgatewayclient.nvp.domain.Constants;
import com.wakefern.paymentgatewayclient.nvp.util.Validator;

/**
 * RefundTransaction Request Message
 * 
 * RefundTransaction message format:
 * -------------------------------
 *    USER=client_9999  (part of Profile)
 *    PWD=*********************  (part of Profile)
 *    SIGNATURE=******************************************************  (part of Profile)
 *    VERSION=95.0&
 *    METHOD=RefundTransaction& 
 *    TRANSACTIONID=5VL116385K2800811& 
 *    REFUNDTYPE=Partial&
 *    AMT=99.00& 
 *    CURRENCYCODE=USD
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 20/07/2015
 */
public final class RefundTransaction implements Request {

	private static final long serialVersionUID = 5071129209269802779L;

	// Type of refund you are making 
	private enum RefundType { OTHER, FULL, PARTIAL }

    // Method value of this request 
    private static final String METHOD_NAME = Constants.NVP_METHOD_REFUNDTRANSACTION;

    // Map that holds name-value pair request values 
    private final Map<String, String> nvpRequest;

    // Map that holds name-value pair response values 
    private Map<String, String> nvpResponse;

	/**
	 * Transaction id cannot be longer than 17 characters and cannot be null,
	 * if it is, exception is thrown.
	 *
	 * @param transactionId Unique identifier of a transaction.
	 * 						Character length and limitations: 17 single-byte
	 * 						alphanumeric characters.
	 * @param refund		type of refund you are making
	 * @throws IllegalArgumentException
	 */
	private RefundTransaction(String transactionId, RefundType refund)
			throws IllegalArgumentException {

		/* validation */
		if (transactionId == null || transactionId.length() > 17) {
			throw new IllegalArgumentException("Transaction id cannot be "
					+ "longer than 17 characters");
		}

		/* set instance variables */
        nvpResponse = new HashMap<String, String>();
        nvpRequest  = new HashMap<String, String>();

        nvpRequest.put("METHOD", METHOD_NAME);
        nvpRequest.put("TRANSACTIONID", transactionId);
        nvpRequest.put("REFUNDTYPE", refund.toString());
	}

	/**
	 * Transaction id cannot be longer than 17 characters and cannot be null,
	 * if it is, exception is thrown.
	 * 
	 * @param transactionId Unique identifier of a transaction.
	 * 						Character length and limitations: 17 single-byte
	 * 						alphanumeric characters.
	 * @return				RefundTransaction where type is set to Full.
	 * @throws IllegalArgumentException
	 */
	public static RefundTransaction getFullRefund(String transactionId) 
			throws IllegalArgumentException {
		return new RefundTransaction(transactionId, RefundType.FULL);
	}

	/**
	 * Transaction id and amount has to be valid, otherwise exception is thrown.
	 *
	 * @param transactionId Unique identifier of a transaction.
	 * 						Character length and limitations: 17 single-byte
	 * 						alphanumeric characters.
	 * @param amount		Amount to reauthorize.
	 * 						Limitations: Value is a positive number which cannot
	 * 						exceed $10,000 USD in any currency. No currency
	 * 						symbol. Must have two decimal places, decimal
	 * 						separator must be a period (.).
	 * @return				RefundTransaction where type is set to Partial.
	 * @throws IllegalArgumentException
	 */
	public static RefundTransaction getPartialRefund(String transactionId, 
			String amount) throws IllegalArgumentException {

		RefundTransaction refundTransaction =
					new RefundTransaction(transactionId, RefundType.PARTIAL);
		refundTransaction.setAmount(amount);
		return refundTransaction;
	}

	/**
	 * Transaction id cannot be longer than 17 characters and cannot be null,
	 * if it is, exception is thrown.
	 *
	 * @param transactionId Unique identifier of a transaction.
	 * 						Character length and limitations: 17 single-byte
	 * 						alphanumeric characters.
	 * @return				RefundTransaction where type is set to Other.
	 * @throws IllegalArgumentException
	 */
	public static RefundTransaction getOtherRefund(String transactionId)
			throws IllegalArgumentException {
		return new RefundTransaction(transactionId, RefundType.OTHER);
	}

	/**
	 * Transaction id and amount has to be valid, otherwise exception is thrown.
	 *
	 * @param transactionId Unique identifier of a transaction.
	 * 						Character length and limitations: 17 single-byte
	 * 						alphanumeric characters.
	 * @param amount		Amount to reauthorize.
	 * 						Limitations: Value is a positive number which cannot
	 * 						exceed $10,000 USD in any currency. No currency
	 * 						symbol. Must have two decimal places, decimal
	 * 						separator must be a period (.).
	 * @return				RefundTransaction where type is set to Other.
	 * @throws IllegalArgumentException
	 */
	public static RefundTransaction getOtherRefund(String transactionId,
			String amount) throws IllegalArgumentException {

		RefundTransaction rt =
				new RefundTransaction(transactionId, RefundType.OTHER);
		rt.setAmount(amount);
		return rt;
	}

	/**
	 * Refund amount. Amount is required if RefundType is Partial.
	 * NOTE:If RefundType is Full, do not set the Amount.
	 *
	 * @param amount	Amount to reauthorize.
	 * 					Limitations: Value is a positive number which cannot
	 * 					exceed $10,000 USD in any currency. No currency symbol.
	 * 					Must have two decimal places, decimal separator must be
	 * 					a period (.).
	 * @throws IllegalArgumentException
	 */
	private void setAmount(String amount) throws IllegalArgumentException {

        if (!Validator.isValidAmount(amount)) {
            throw new IllegalArgumentException("Amount is not valid. Amount "
					+ "has to have exactly two decimal "
                    + "places seaprated by \".\" - example: \"50.00\"");
        }
        nvpRequest.put("AMT", amount);
	}

	/**
	 * Custom memo about the refund.
	 *
	 * @param note	Character length and limitations: 255 single-byte
	 * 				alphanumeric characters.
	 * @throws IllegalArgumentException
	 */
	public void setNote(String note) throws IllegalArgumentException {

		if (note == null || note.length() > 255) {
			throw new IllegalArgumentException("Note cannot be longer than 255 "
					+ "characters");
		}
        nvpRequest.put("NOTE", note);
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

        StringBuffer str = new StringBuffer("Instance of ");
		str.append("RefundTransaction class with the values as nvpRequest: ");
        str.append(nvpRequest.toString());
		str.append("; nvpResponse: ");
		str.append(nvpResponse.toString());
		return str.toString();
    }
}
