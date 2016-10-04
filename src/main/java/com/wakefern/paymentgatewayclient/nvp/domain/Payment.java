/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.domain;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import com.wakefern.paymentgatewayclient.nvp.util.Validator;

/**
 * Payment Details Type. For simple payment, please use constructor with amount
 * field. If you want to set tax, or more options, use Constructor that takes PaymentItem array.
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/06/2015
 */
public final class Payment implements RequestFields {

	private static final long serialVersionUID = -5380399703536419173L;

	private static final String paddedFieldName = "PAYMENTREQUEST_0_";
	
	// map that holds name-value pair request values 
    private Map<String, String> nvpRequest;

    // same for all constructors 
    { 
		nvpRequest	= new HashMap<String, String>();
	}

    /**
	 * You are advised to use Payment(PaymentItem[] items) constructor, where 
	 * you can specify all items individually, add individual descriptions, 
	 * recurring payments etc.
     *
     * @param amount    Limitations: Must not exceed $9,999.99 USD in any
     *                  currency. No currency symbol. Must have two decimal
     *                  places, decimal separator must be a period (.), and no
     *                  thousands separator.
     * @throws IllegalArgumentException
     */
    public Payment(String amount) throws IllegalArgumentException {
        // can be "0" as well 
        if (!Validator.isValidAmount(amount)) {
            throw new IllegalArgumentException("Amount has to have exactly two "
					+ "decimal places seaprated by \".\" - example: \"50.00\"");
        }

        // values for this request 
        nvpRequest.put(paddedFieldName + "AMT", amount);            // For PAYMENTREQUEST_0_AMT
        nvpRequest.put("L_" + paddedFieldName + "AMT0", amount);    // For L_PAYMENTREQUEST_0_AMT0
        nvpRequest.put(paddedFieldName + "ITEMAMT", amount);        // For PAYMENTREQUEST_0_ITEMAMT
        nvpRequest.put(paddedFieldName + "PAYMENTACTION", "Order"); // By Default
        nvpRequest.put("L_" + paddedFieldName + "NAME0", "Your Groceries"); // By Default
        nvpRequest.put("L_" + paddedFieldName + "QTY0", "1");       // By Default  
    }
    
    /**
     * Setting the Payment request action such as Order, Sale, Authorization
     * 
     * @param action
     */
    public void setPaymentRequestAction(String action) {
    	if (action == null || action.trim().length() == 0 ||
    			!(action.equals("Order") || action.equals("Sale") || action.equals("Authorization"))) {
            throw new IllegalArgumentException("Action " + action +
                    		" is invalid. Action has to be either Sale, Order, or Authorization");
    	} else {
    		nvpRequest.put(paddedFieldName + "PAYMENTACTION", action);   // Setting to new Action
    	} 
    }
    
    /**
     * Setting the Payment request name such as "Your Groceries"
     * 
     * @param aName
     */
    public void setPaymentRequestName(String aName) { 
    	if (aName == null || aName.trim().length() == 0 || aName.trim().length() > 128) {
            throw new IllegalArgumentException("Request Name " + aName +
                    " is invalid. Request Name has to be Alphanumeric of MAX 128 chars");
    	} else {
    		nvpRequest.put("L_" + paddedFieldName + "NAME0", aName);   // Setting to new Action
    	} 
    }
    
    /**
     * Setting the Request Quantity
     * 
     * @param qty
     */
    public void setPaymentRequestQty(int qty) { 
    	if (qty <= 0) {
            throw new IllegalArgumentException("Request Quantity " + qty +
                    " is invalid. Request qunatity has to be a positive integer");
    	} else {
    		nvpRequest.put("L_" + paddedFieldName + "QTY0", Integer.valueOf(qty).toString());  // Setting the qty field
    	} 
    }
    
     /**
     * A three-character currency code. Default: USD 
     * 
     * @param currency
     */
    public void setCurrency(String currencyCode) {
    	if (currencyCode == null || currencyCode.length() != 3) {
    		nvpRequest.put("CURRENCYCODE", "USD");
    	} else {
    		nvpRequest.put("CURRENCYCODE", currencyCode.toUpperCase());
    	}
    }

    /** 
     * Total shipping costs for this order. Note: Character length and 
     * limitations: Must not exceed $9,999.99 USD in any currency. No currency 
     * symbol. Regardless of currency, decimal separator must be a period (.) 
     * Equivalent to nine characters maximum for USD.
     *
     * @param amount
     * @throws IllegalArgumentException
     */
    public void setShippingAmount(String amount) throws IllegalArgumentException {

        if (!Validator.isValidAmount(amount)) {
            throw new IllegalArgumentException("Amount " + amount +
                    " is invalid. Amount has to have exactly two decimal "
                    + "places seaprated by \".\" - example: \"50.00\"");
        }
        nvpRequest.put("SHIPPINGAMT", amount);
    }

    /**
     * Shipping discount for this order, specified as a negative number. Note:
     * Character length and limitations: Must not exceed $9,999.99 USD in any
     * currency. No currency symbol. Regardless of currency, decimal separator
     * must be a period (.). Equivalent to nine characters maximum for USD.
     * 
     * @param discount
     * @throws IllegalArgumentException
     */
    public void setShippingDiscount(String discount) throws IllegalArgumentException {

        // amount is number with exactly two decimal places 
        if (!Validator.isValidAmount(discount)) {
            throw new IllegalArgumentException("Amount " + discount +
                    " is invalid. Amount has to have exactly two decimal "
                    + "places seaprated by \".\" - example: \"50.00\"");
        }
        nvpRequest.put("SHIPPINGDISCOUNT", discount);
    }

    /**
     * Total handling costs for this order. Note: Character length and
     * limitations: Must not exceed $9,999.99 USD in any currency. No currency
     * symbol. Regardless of currency, decimal separator must be a period (.).
     * Equivalent to nine characters maximum for USD.
     *
     * @param amount
     * @throws IllegalArgumentException
     */
    public void setHandlingAmount(String amount) 
            throws IllegalArgumentException {

        if (!Validator.isValidAmount(amount)) {
            throw new IllegalArgumentException("Amount " + amount +
                    " is invalid. Amount has to have exactly two decimal "
                    + "places seaprated by \".\" - example: \"50.00\"");
        }
        nvpRequest.put("HANDLINGAMT", amount);
    }

    /**
     * Description of items the customer is purchasing. Character length and
     * limitations: 127 single-byte alphanumeric characters
     * 
     * @param description
     * @throws IllegalArgumentException
     */
    public void setDescription(String description) 
            throws IllegalArgumentException {

        if (description.length() > 127) {
            throw new IllegalArgumentException("Description cannot exceed 127 characters");
        }
        nvpRequest.put("DESC", description);
    }

    /**
     * A free-form field for your own use. Character length and limitations:
     * 256 single-byte alphanumeric characters
     *
     * @param field
     * @throws IllegalArgumentException
     */
    public void setCustomField(String field) throws IllegalArgumentException {

        if (field.length() > 256) {
            throw new IllegalArgumentException("Field cannot exceed 256 characters");
        }
        nvpRequest.put("CUSTOM", field);
    }

    /**
     * Your own invoice or tracking number. Character length and limitations:
     * 127 single-byte alphanumeric characters
     *
     * @param invoiceNumber
     * @throws IllegalArgumentException
     */
    public void setInvoiceNumber(String invoiceNumber)
            throws IllegalArgumentException {

        if (invoiceNumber.length() > 127) {
            throw new IllegalArgumentException("Invoice number cannot exceed 127 characters");
        }
        nvpRequest.put("INVNUM", invoiceNumber);
    }

    /**
     * An identification code for use by third-party applications to identify
     * transactions. Character length and limitations: 32 single-byte
     * alphanumeric characters
     *
     * @param source
     * @throws IllegalArgumentException
     */
    public void setButtonSource(String source) throws IllegalArgumentException {
        if (source.length() > 127) {
            throw new IllegalArgumentException("Source cannot exceed 127 characters");
        }
        nvpRequest.put("BUTTONSOURCE", source);
    }

    /**
     * Your URL for receiving Instant Payment Notification (IPN) about this
     * transaction. If you do not specify this value in the request, the
     * notification URL from your Merchant Profile is used, if one exists.
     * Important: The notify URL only applies to 
     * <code>DoExpressCheckoutPayment</code>. This value is ignored when set in
     * <code>SetExpressCheckout</code> or <code>GetExpressCheckoutDetails</code>
     * . Character length and limitations: 2,048 single-byte alphanumeric
     * characters
     *
     * @param url
     * @throws IllegalArgumentException
     */
    public void setNotifyUrl(String url) throws IllegalArgumentException {

        if (url.length() > 2048) {
            throw new IllegalArgumentException("Url cannot exceed 2048 "
                    + "characters");
        }
        nvpRequest.put("NOTIFYURL", url);
    }

    /**
     * Transaction identification number of the transaction that was created.
     *
     * @param transactionId
     */
    public void setTransactionId(String transactionId) {
        nvpRequest.put("TRANSACTIONID", transactionId);
    }

   
    public Map<String, String> getNVPRequest() {
		// hash map holding response 
		HashMap<String, String> nvp = new HashMap<String, String>(nvpRequest);

		// format to two decimal places 
		DecimalFormat currency = new DecimalFormat("#0.00");

		// set AMT if not set */
		if (!nvp.containsKey(paddedFieldName + "AMT")) {

			// calculate total - tax, shipping etc. 
			int total = 0;

			if (nvp.containsKey("HANDLINGAMT")) {
				total += Integer.parseInt(nvp.get("HANDLINGAMT").replace(".", ""));
			}
			if (nvp.containsKey("SHIPPINGAMT")) {
				total += Integer.parseInt(nvp.get("SHIPPINGAMT").replace(".", ""));
			}
			// convert back to two decimals 
			nvp.put("AMT", currency.format(total / 100d));
		}

		// handling or shipping amount is set but item amount is not set 
		if ((nvp.containsKey("HANDLINGAMT") || nvp.containsKey("SHIPPINGAMT")) 
									&& !nvp.containsKey("ITEMAMT")) {
			// set the amount for itemamt - because itemamt is required 
			// when handling amount is set 
			nvp.put("ITEMAMT", nvp.get("AMT"));
		}
		// return nvp request 
		return nvp;
    }

    @Override
    public String toString() {
        return "Instance of PaymentDetails class with the values: nvpRequest: " 
                + nvpRequest.toString(); 
    }
}
