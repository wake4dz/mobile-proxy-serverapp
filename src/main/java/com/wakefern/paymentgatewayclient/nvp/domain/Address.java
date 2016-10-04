/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Shipping address Class
 *    PAYMENTREQUEST_0_SHIPTONAME=John Smith& 
 *    PAYMENTREQUEST_0_SHIPTOSTREET=123 MAIN ST& 
 *    PAYMENTREQUEST_0_SHIPTOCITY=EDISON& 
 *    PAYMENTREQUEST_0_SHIPTOSTATE=NJ& 
 *    PAYMENTREQUEST_0_SHIPTOZIP=08837& 
 *    PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE=US& 
 *    PAYMENTREQUEST_0_SHIPTOPHONENUM=8005551234& 
 *
 * @author Wakefern Food Corp.
 * @date 10/05/2015
 * @version 1.0.0
 */
public final class Address implements RequestFields {
	
	private static final long serialVersionUID = -6025968605994760537L;
	
	private final String paddingName = "PAYMENTREQUEST_0_SHIPTO";
	
	// Map that holds name-value pair request values 
    private final Map<String, String> nvpRequest;

    /**
     *
     * @param name      Person's name associated with this shipping address.
     *                  Character length and limitations: 32 single-byte characters.
     * @param street    First street address. Character length and limitations: 100 single-byte characters.
     * @param city      Name of city. Character length and limitations: 40 single-byte characters.
     * @param state     State or province. Character length and limitations: 2 single-byte characters.
     * @param zipcode   5-digit or 10-digit zipcode
     * @param phone		20-digit phone - Alphanumeric
     * @throws IllegalArgumentException
     */
    public Address(String name, String street, String city, String state, 
    					String zipcode, String phone) throws IllegalArgumentException {

        if (street.length() > 100 || city.length() > 40 ||  state.length() > 2
        		|| zipcode.length() > 10 || phone.length() > 20) {
            throw new IllegalArgumentException("Either Name, Street Address, State, zipcode, " +
            					"or phone number is invalid");
        }
        nvpRequest = new HashMap<String, String>();
        
        nvpRequest.put(paddingName + "NAME", name);
        nvpRequest.put(paddingName + "STREET", street);
        nvpRequest.put(paddingName + "CITY", city);
        nvpRequest.put(paddingName + "STATE", state);
        nvpRequest.put(paddingName + "ZIP", zipcode);
        nvpRequest.put(paddingName + "PHONENUM", phone);
        nvpRequest.put(paddingName + "COUNTRYCODE", "US");  // By default
    }

    /**
     * Second street address line. Character length and limitations: 100 characters.
     *
     * @param street
     * @throws IllegalArgumentException
     */
    public void setStreet2(String street) throws IllegalArgumentException {
        if (street.length() > 100) {
            throw new IllegalArgumentException("Street Line 2 can be maximum 100 characters");
        }
        nvpRequest.put(paddingName + "STREET2", street);
    }
    
    /**
     * Setting Country Code
     *
     * @param countryCode
     * @throws IllegalArgumentException
     */
    public void setCountryCode(String code) throws IllegalArgumentException {
        if (code.length() != 2) {
            throw new IllegalArgumentException("Invalid Country Code");
        }
        nvpRequest.put(paddingName + "COUNTRYCODE", code);
    }

    public Map<String, String> getNVPRequest() {
        return new HashMap<String, String>(nvpRequest);
    }

    @Override
    public String toString() {
        return "Instance of Address class with the values: nvpRequest: " + nvpRequest.toString();
    }

}
