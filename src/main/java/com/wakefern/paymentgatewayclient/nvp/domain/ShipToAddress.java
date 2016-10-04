/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Shipping address
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/07/2015
 */
public final class ShipToAddress implements RequestFields {

	private static final long serialVersionUID = -6708507750537035344L;
	
	private static final String paddedString = "PAYMENTREQUEST_0_";
	
	// Map that holds name-value pair request values 
    private final Map<String, String> nvpRequest;

    /**
     * 
     * @param name      Person's name associated with this shipping address.
     *                  Character length and limitations: 32 single-byte 
     *                  characters.
     * @param street    First street address. Character length and 
     *                  limitations: 100 single-byte characters.
     * @param city      Name of city. Character length and limitations: 40 
     *                  single-byte characters.
     * @param state     State or province. Character length and limitations: 40 
     *                  single-byte character.
     * @param zipcode   US Zipcode. Numeric length limitations: 10. i.e. 08831  -or-  08831-3455
     * 
     * @param phoneNum  Phone number char(10) as 7325551234
     *                  
     * @throws IllegalArgumentException
     */
    public ShipToAddress(String name, String street, String city, 
    					String state, String zipcode, String phoneNum)  throws IllegalArgumentException {

        if (name.length() > 32 || street.length() > 100 || city.length() > 40 || 
                state.length() > 40 || phoneNum.length() != 10) {
            throw new IllegalArgumentException();
        }

        nvpRequest = new HashMap<String, String>();

        nvpRequest.put(paddedString + "SHIPTONAME", name);
        nvpRequest.put(paddedString + "SHIPTOSTREET", street);
        nvpRequest.put(paddedString + "SHIPTOCITY", city);
        nvpRequest.put(paddedString + "SHIPTOSTATE", state);
        nvpRequest.put(paddedString + "SHIPTOZIP", zipcode);
        nvpRequest.put(paddedString + "SHIPTOPHONENUM", phoneNum);
        nvpRequest.put(paddedString + "SHIPTOCOUNTRYCODE", "US");
    }

    /**
     * Second street address. Character length and limitations: 100 single-byte
     * characters.
     *
     * @param street
     * @throws IllegalArgumentException
     */
    public void setStreet2(String street) throws IllegalArgumentException {

        if (street.length() > 100) {
            throw new IllegalArgumentException("Street can be maximum 100 characters");
        }
        nvpRequest.put(paddedString + "SHIPTOSTREET2", street);
    }

    /**
     * Phone number. Character length and limit: 20 single-byte characters.
     *
     * @param phoneNumber
     * @throws IllegalArgumentException
     */
    public void setPhoneNumber(String phoneNumber)
            throws IllegalArgumentException {

        if (phoneNumber.length() > 20) {
            throw new IllegalArgumentException("Phone number can be maximum 20 characters");
        }
        nvpRequest.put(paddedString + "SHIPTOPHONENUM", phoneNumber);
    }

    public Map<String, String> getNVPRequest() {
        return new HashMap<String, String>(nvpRequest);
    }

    @Override
    public String toString() {
        return "Instance of ShipToAddress class with the values: nvpRequest:" + nvpRequest.toString(); 
    }
}
