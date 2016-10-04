/*
 * Copyright (C) 2015 - Wakefern Food Corporation 
 */
package com.wakefern.paymentgatewayclient.nvp.domain;

/**
 * Constants Class
 * 
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/05/2015 
 */
public class Constants {
	
	// Application name. ie. "Payment Client"
	public static final String APP_NAME = "PaymentClientNVP";
	
	// Resource Bundle Properties File name. i.e. paymentclient.properties
	public static final String RESOURCE_BUNDLE_NAME = "paymentclientnvp";
    // SDK Version 
    public static final String VERSION = "95.0";
    
    // Character encoding for the NVP string
    public static final String ENCODING = "UTF-8";      
    
    // Define all NVP method names
    public static final String NVP_BUTTON_SOURCE                    = "ShopRite_Apps";
    public static final String NVP_METHOD_SETEXPRESSCHECKOUT        = "SetExpressCheckout";
    public static final String NVP_METHOD_GETEXPRESSCHECKOUTDETAILS = "GetExpressCheckoutDetails";
    public static final String NVP_METHOD_DOEXPRESSCHECKOUTPAYMENT  = "DoExpressCheckoutPayment";
    public static final String NVP_METHOD_DOAUTHORIZATION           = "DoAuthorization";
    public static final String NVP_METHOD_DOCAPTURE                 = "DoCapture";
    public static final String NVP_METHOD_REFUNDTRANSACTION         = "RefundTransaction";
    public static final String NVP_METHOD_ADDRESSVERIFY             = "AddressVerify";
    
    // Content / MIME type
    public static final String CONTENT_TYPE = "text/xml";  
    
    // Locale for Express Checkout. 
    public enum LocaleCode { 
        /** Australia */
        AU,

        /** Austria */
        AT,

        /** Belgium */
        BE,

        /** Canada */
        CA,

        /** Switzerland */
        CH,

        /** China */
        CN,

        /** Germany */
        DE,

        /** Spain */
        ES,

        /** United Kingdom */
        GB,

        /** France */
        FR,

        /** Italy */
        IT,

        /** Netherlands */
        NL,

        /** Poland */
        PL,

        /** United States */
        US;
    }
	
}
