/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.request;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.wakefern.paymentgatewayclient.nvp.domain.Constants;
import com.wakefern.paymentgatewayclient.nvp.domain.Address;
import com.wakefern.paymentgatewayclient.nvp.domain.Payment;
import com.wakefern.paymentgatewayclient.nvp.domain.PaymentAction;
import com.wakefern.paymentgatewayclient.nvp.domain.ShipToAddress;
import com.wakefern.paymentgatewayclient.nvp.domain.ShippingOptions;
import com.wakefern.paymentgatewayclient.nvp.util.Validator;

/**
 * Instance is used for SetExpressCheckout request. This request initiates an
 * Express Checkout transaction.
 * 
 * SetExpressCheckout message minimum format:
 * ------------------------------------------
 *    USER=client_9999  (part of Profile)
 *    PWD=*********************  (part of Profile)
 *    SIGNATURE=******************************************************  (part of Profile)
 *    VERSION=95.0&
 *    METHOD=SetExpressCheckout& 
 *    RETURNURL=https://shop.shoprite.com/Checkout_OrderConfirmation.aspx?&sid=78193290&strid=1E961&ppr=1& 
 *    CANCELURL=https://shop.shoprite.com/Checkout_Payment.aspx?&sid=78193290&strid=1E961&ppr=1& 
 *    PAYMENTREQUEST_0_PAYMENTACTION=Order& 
 *    PAYMENTREQUEST_0_AMT=50.00& 
 *    L_PAYMENTREQUEST_0_NAME0=Your Groceries& 
 *    L_PAYMENTREQUEST_0_AMT0=150.00& 
 *    L_PAYMENTREQUEST_0_QTY0=1& 
 *    PAYMENTREQUEST_0_ITEMAMT=150.00& 
 *    NOSHIPPING=2&
 *    ADDROVERRIDE=0& 
 *    L_BILLINGTYPE0=None& 
 *    SOLUTIONTYPE=Sole& 
 *    LANDINGPAGE=Billing&
 *    HDRIMG=https://epay.wakefern.com/app/content/style/img/StoreLogoLarge.png& 
 *    PAYFLOWCOLOR=cabcab&
 *    PAYMENTREQUEST_0_SHIPTONAME=John Smith& 
 *    PAYMENTREQUEST_0_SHIPTOSTREET=123 MAIN ST& 
 *    PAYMENTREQUEST_0_SHIPTOCITY=EDISON& 
 *    PAYMENTREQUEST_0_SHIPTOSTATE=NJ& 
 *    PAYMENTREQUEST_0_SHIPTOZIP=08837& 
 *    PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE=US& 
 *    PAYMENTREQUEST_0_SHIPTOPHONENUM=8005551234& 
 *    EMAIL=somename@somedomain.com
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/08/2015
 */
public final class SetExpressCheckout implements Request {

	private static final long serialVersionUID = -1789171810968418856L;

	// Method value of this request 
    private static final String NVP_METHOD_NAME = Constants.NVP_METHOD_SETEXPRESSCHECKOUT;

    private static final String paddedFieldName = "PAYMENTREQUEST_0_";
    
    // Type of checkout flow 
    public enum SolutionType {
        // Express Checkout for auction
        SOLE,
        // Normal Express Checkout 
        MARK
    }

    /** 
     * Type of Payment Gateway page to display 
     */
    public enum LandingPage {
        // non-Payment Gateway account 
        BILLING,
        // Payment Gateway account login 
        LOGIN
    }

    // name value pair request 
    private final Map<String, String> nvpRequest;

    // name value pair response 
    private Map<String, String> nvpResponse;

    // shipping options, empty if no options set 
    private List<Map<String, String>> shippingOptions;

    // billing agreement (recurring payment etc.), empty if no agreement set 
    private List<Map<String, String>> billingAgreement;

    /**
     * Payment Gateway recommends that the returnUrl be the final review page on which
     * the customer confirms the order and payment or billing agreement.
     *
     * Payment Gateway recommends that the cancelUrl be the original page on which the
     * customer chose to pay with Payment Gateway or establish a billing agreement.
     * 
     * @param payment
     * @param returnUrl URL to which the customer's browser is returned after
     *                  choosing to pay with Payment Gateway. Maximum 2048 characters.
     * @param cancelUrl URL to which the customer is returned if he does not
     *                  approve the use of Payment Gateway to pay you. Maximum 2048
     *                  characters.
     * @throws IllegalArgumentException
     */
    public SetExpressCheckout(Payment payment, String returnUrl, String cancelUrl) 
    														throws IllegalArgumentException {
        if (payment == null || returnUrl == null || cancelUrl == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    	// Cancel URL and Return URL have to be less or equal to 2048 chars 
        if (returnUrl.length() < 10 || returnUrl.length() > 2048) {
            throw new IllegalArgumentException("RETURNURL is invalid. Its length must be between 10 and 2048 characters long.");
        }
        if (cancelUrl.length() < 10 || cancelUrl.length() > 2048) {
            throw new IllegalArgumentException("CANCELURL is invalid. Its length must be between 10 and 2048 characters long.");
        }

        nvpResponse = new HashMap<String, String>();
        nvpRequest  = new HashMap<String, String>();
        shippingOptions = new LinkedList<Map<String, String>>();
        billingAgreement = new LinkedList<Map<String, String>>();

        // Setting up NVP method name
        nvpRequest.put("METHOD", NVP_METHOD_NAME);

        // Copy NVP from payment 
		HashMap<String, String> nvp = new HashMap<String, String>(payment.getNVPRequest());
        nvpRequest.putAll(nvp);

        nvpRequest.put("RETURNURL", returnUrl);
        nvpRequest.put("CANCELURL", cancelUrl);
        nvpRequest.put("NOSHIPPING", "2");				// By Default but set optional
        nvpRequest.put("ADDROVERRIDE", "0");			// By Default but set optional
        nvpRequest.put("L_BILLINGTYPE0", "None");	    // By Default no set
        nvpRequest.put("SOLUTIONTYPE", SolutionType.SOLE.toString());  // By Default but set optional
        nvpRequest.put("LANDINGPAGE", LandingPage.BILLING.toString()); // By Default but set optional
        nvpRequest.put("HDRIMG", "https://epay.wakefern.com/paymentclient/assets/images/shoprite_gray.jpg");  // By Default but set optional
        nvpRequest.put("PAYFLOWCOLOR", "f1f1f1");       // By Default but set optional - equivalent to rgb(241,241,241)
        nvpRequest.put("EMAIL", "");       				// By Default but set optional
    }

    /**
     * Setting TOKEN, and it must be char(20)
     * 
     * @param token 
     * @throws IllegalArgumentException
     */
    public void setToken(String token) throws IllegalArgumentException {
    	//
        if (token.length() != 20) {
            throw new IllegalArgumentException("Invalid token argument");
        }
        nvpRequest.put("TOKEN", token);
    }

    /**
     * The expected maximum total amount of the complete order, including
     * shipping cost and tax charges.
     * If the transaction does not include a one-time purchase, this field is
     * ignored.
     * Limitations: Must not exceed $9,999.99 USD in any currency. No currency
     * symbol. Must have two decimal places, decimal separator must be a
     * period (.), and no thousands separator.
     *
     * @param maxAmount number with exactly two decimal places
     * @throws IllegalArgumentException
     */
    public void setMaxAmount(String maxAmount) throws IllegalArgumentException {
        // amount is number with exactly two decimal places 
        if (!Validator.isValidAmount(maxAmount)) {
            throw new IllegalArgumentException("Amount " + maxAmount + " is invalid. " 
            							+ "Amount has to have exactly two decimal "
            							+ "places seaprated by \".\" - example: \"150.00\"");
        }
        // values for this request 
        nvpRequest.put("MAXAMT", maxAmount);
    }

    /**
     * URL to which the callback request from Payment Gateway is sent. It must start
     * with HTTPS for production integration. It can start with HTTPS or HTTP
     * for sandbox testing.
     *
     * @param callback max 1024 characters
     */
    public void setCallback(String callback) throws IllegalArgumentException {
        if (callback.length() > 1024) {
            throw new IllegalArgumentException("Callback can be maximum 1024 in length");
        }
        nvpRequest.put("CALLBACK", callback);
    }

    /**
     * An override for you to request more or less time to be able to process
     * the callback request and respond. The acceptable range for the override
     * is 1 to 6 seconds.
     *
     * @param timeout integer has to be between 1 - 6
     */
    public void setCallbackTimeout(int timeout) {
        if (timeout < 1 || timeout > 6) {
            throw new IllegalArgumentException("Timeout has to be between 1 - 6");
        }
        nvpRequest.put("CALLBACKTIMEOUT", Integer.toString(timeout));
    }

    /**
     * Indicates that you require that the customer's shipping address on file
     * with Payment Gateway be a confirmed address.
     * Setting this field overrides the setting you have specified in your
     * Merchant Account Profile.
     *
     * @param required if true than confirmed address is required
     */
    public void setRequireConfirmedShipping(boolean required) {
        String req = (required)?"1":"0";
        nvpRequest.put("REQCONFIRMSHIPPING", req);
    }

    /**
     * Indicates that on the Payment Gateway pages, no shipping address fields should be 
     * displayed whatsoever.
     * For digital goods, this field is required, and you must set it to 1. It is one of the following values:
     * 		0 – Payment Gateway displays the shipping address on the page.
     * 		1 – Payment Gateway does not display shipping address fields whatsoever.
     * 		2 – If you do not pass the shipping address, obtains from the buyer's account profile.
     * 
     * @param noShipping if true, no address fields will be displayed
     */
    public void setNoShipping(int noShipping) {
    	if (noShipping < 0 || noShipping > 2) {
    		throw new IllegalArgumentException("No Shipping Address option is invalid. It must be either 0, 1, or 2.");
    	} else {
    		nvpRequest.put("NOSHIPPING", Integer.valueOf(noShipping).toString());
    	}
    }

    /**
     * Indicates that the Payment Gateway pages should display the shipping address set 
     * by you in this SetExpressCheckout request, not the shipping address on 
     * file with Payment Gateway for this customer.
     * Displaying the Payment Gateway street address on file does not allow the customer 
     * to edit that address.
     *     0 – The page should not display the shipping address.
     *     1 – The page should display the shipping address.
     * 
     * Set address using setAddress(ShipToAddress address) method
     * 
     * @param addrOverride if true set address will be used
     */
    public void setAddressOverride(boolean addrOverride) {
        String overD = (addrOverride)?"1":"0";
        nvpRequest.put("ADDROVERRIDE", overD);
    }

    /**
     * Locale of pages displayed by Payment Gateway during Express Checkout.
     * 
     * @param localCode
     */
    public void setLocaleCode(Constants.LocaleCode localeCode) {
    	if (localeCode == null) {
    		nvpRequest.put("LOCALECODE", "US");  // By default
    	} else {
    		nvpRequest.put("LOCALECODE", localeCode.toString());
    	}
    }

    /**
     * Sets the Custom Payment Page Style for payment pages associated with 
     * this button/link. This value corresponds to the HTML variable page_style 
     * for customizing payment pages. The value is the same as the Page Style 
     * Name you chose when adding or editing the page style from the Profile 
     * subtab of the My Account tab of your Payment Gateway account.
     * 
     * Character length and limitations: 30 single-byte alphabetic characters
     * 
     * @param pageStyle 
     * @throws IllegalArgumentException
     */
    public void setPageStyle(String pageStyle) throws IllegalArgumentException {
        if (pageStyle.length() > 30) {
            throw new IllegalArgumentException("Character length exceeded 30 characters");
        }
        nvpRequest.put("PAGESTYLE", pageStyle);
    }
    
    /**
     * URL for the image you want to appear at the top left of the payment page. 
     * The image has a maximum size of 750 pixels wide by 184 pixels high. 
     * Payment Gateway recommends that you provide an image that is stored on a secure 
     * (https) server. If you do not specify an image, the default ShopRite logo is displayed.
     * Character length and limit: 127 single-byte alphanumeric characters
     * 
     * @param imgUrl
     * @throws IllegalArgumentException
     */
    public void setImage(String imgUrl) throws IllegalArgumentException {

        if (imgUrl.length() > 127) {
            throw new IllegalArgumentException("Character length exceeded 30 characters");
        }
        nvpRequest.put("HDRIMG", imgUrl);
    }

    /**
     * Sets the background color for the payment page. By default, the color 
     * is light shade of gray #f1f1f1 or rgb(241,241,241)
     * Character length and limitation: Six character HTML hexadecimal color code in ASCII
     *
     * @param hexColor
     * @throws IllegalArgumentException
     */
    public void setPayFlowColor(String hexColor) throws IllegalArgumentException {

    	if (hexColor == null || hexColor.trim().length() == 0) {  
    		nvpRequest.put("PAYFLOWCOLOR", "f1f1f1"); // Default light gray color is set
    	} else {
	    	/* allowed characters 0-9 and a-f. Exactly 6 characters */
	        Pattern pattern = Pattern.compile("^[0-9,a-f,A-F]{6}$");
	        Matcher matcher = pattern.matcher(hexColor);
	        if (!matcher.find()) {
	            throw new IllegalArgumentException("Hex color" + hexColor + " is invalid.");
	        }
	        nvpRequest.put("PAYFLOWCOLOR", hexColor);
    	}
    }
	
    /**
     * How you want to obtain payment:
     * <ul>
     *  <li>
     *      Sale indicates that this is a final sale for which you are 
     *      requesting payment. (Default)
     *  </li>
     *  <li>
     *      Authorization indicates that this payment is a basic authorization 
     *      subject to settlement with Payment Gateway Authorization & Capture.
     *  </li>
     *  <li>
     *      Order indicates that this payment is an order authorization subject 
     *      to settlement with Payment Gateway Authorization & Capture.
     *  </li>
     * </ul>
     * If the transaction does not include a one-time purchase, this field is 
     * ignored.
     * Note:
     * You cannot set this value to Sale in SetExpressCheckout request and then 
     * change this value to Authorization or Order on the final API 
     * DoExpressCheckoutPayment request. If the value is set to Authorization 
     * or Order in SetExpressCheckout, the value may be set to Sale or the same 
     * value (either Authorization or Order) in DoExpressCheckoutPayment.
     * 
     * @param paymentAction
     */
    public void setPaymentAction(PaymentAction paymentAction) {
        nvpRequest.put(paddedFieldName + "PAYMENTACTION", paymentAction.getValue());
    }

    /**
     * Email address of the buyer as entered during checkout. Payment Gateway uses this 
     * value to pre-fill the Payment Gateway membership sign-up portion of the Payment Gateway 
     * login page.
     * Character length and limit: 127 single-byte alphanumeric characters
     * 
     * @param email
     * @throws IllegalArgumentException
     */
    public void setEmail(String email) throws IllegalArgumentException {

        if (!Validator.isValidEmail(email)) {
            throw new IllegalArgumentException("Email is invalid");
        }

        if (email.length() > 127) {
            throw new IllegalArgumentException("Email can be maximum 127 characters long.");
        }
        nvpRequest.put("EMAIL", email);
    }

    /**
     * Type of checkout flow:
     * <ul>
     *  <li><b>Sole:</b> Express Checkout for auctions</li>
     *  <li><b>Mark:</b> Normal Express Checkout</li>
     * </ul>
     *
     * @param solutionType
     */
    public void setSolutionType(SolutionType solutionType) {
        nvpRequest.put("SOLUTIONTYPE", solutionType.toString());
    }

    /**
     * Type of Payment Gateway page to display:
     * <ul>
     *    <li><b>Billing:</b> non-Payment Gateway account</li>
     *    <li><b>Login:</b> Payment Gateway account login</li>
     * </ul>
     * 
     * @param landingPage
     */
    public void setLandingPage(LandingPage landingPage) {
        nvpRequest.put("LANDINGPAGE", landingPage.toString());
    }

    /**
     * Type of channel: Merchant for non-auction seller
     *   
     * @param channelType
     */
    public void setChannelType(String channelType) {
        nvpRequest.put("CHANNELTYPE", "MERCHANT");
    }
	

	/**
	 * Sets address fields
	 * 
	 * @param address
	 */
	public void setAddress(Address address) {
		nvpRequest.putAll(address.getNVPRequest());
	}

	/**
	 * Sets shipping options
	 * 
     * @param options
	 */
	public void setShippingOptions(ShippingOptions[] options) {
        // check items 
        if (options == null || options.length == 0) {
            throw new IllegalArgumentException("You did not supply options.");
        }

        // iterate supplied array 
        int x = 0;  // this is only for exception message 
        for (ShippingOptions option : options) {
            // item cannot be null 
            if (option == null) {
                throw new IllegalArgumentException("Option at index " + x + " is not set.");
            }
            this.shippingOptions.add(new HashMap<String, String>(option.getNVPRequest()));
            x++;
        }
	}

    /**
     *
     * @param address shipping address
     */
    public void setShippingAddress(ShipToAddress address) {
        nvpRequest.putAll(new HashMap<String, String>(address.getNVPRequest()));
    }

    public Map<String, String> getNVPRequest() {
		// hash map holding response 
		HashMap<String, String> nvp = new HashMap<String, String>(nvpRequest);

        // shipping options 
		for (int i = 0; i < shippingOptions.size(); i++) {
			for (Map.Entry<String, String> entry : shippingOptions.get(i).entrySet()) {
				// KEY n VALUE 
				nvp.put(entry.getKey() + i, entry.getValue());
			}
		}

        // billing agreement 
		for (int i = 0; i < billingAgreement.size(); i++) {
			for (Map.Entry<String, String> entry : billingAgreement.get(i).entrySet()) {
				// KEY n VALUE 
				nvp.put(entry.getKey() + i, entry.getValue());
			}
		}
        return nvp;
    }

    public void setNVPResponse(Map<String, String> nvpResponse) {
        this.nvpResponse = new HashMap<String, String>(nvpResponse);
    }

    public Map<String, String> getNVPResponse() {
        return new HashMap<String, String>(nvpResponse);
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("Instance of SetExpressCheckout ");
        str.append("class with the values as nvpRequest: ");
        str.append(nvpRequest.toString());
		str.append("; nvpResponse: ");
		str.append(nvpResponse.toString());
		return str.toString();
    }
}
