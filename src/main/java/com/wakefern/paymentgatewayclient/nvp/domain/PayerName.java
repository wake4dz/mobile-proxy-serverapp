/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.domain;

import java.util.Map;
import java.util.HashMap;

/**
 * Payer Name Fields
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/06/2015
 */
public final class PayerName implements RequestFields {

	private static final long serialVersionUID = 6139962814789137789L;
	
	// Map that holds name-value pair request values 
    private final Map<String, String> nvpRequest;

    public PayerName() {
        this.nvpRequest = new HashMap<String, String>();
    }

    /**
     * Payer's salutation. Argument can be maximum 20 characters long, if not,
     * Exception is thrown
     *
     * @param salutation    Character length and limitations: 20 single-byte characters.
     * @throws IllegalArgumentException
     */
    public void setSalutation(String salutation) throws IllegalArgumentException {

        if (salutation.length() > 20) {
            throw new IllegalArgumentException("salutation can be maximum 20 characters long");
        }
        nvpRequest.put("SALUTATION", salutation);
    }

    /**
     * Payer's first name. Argument can be maximum 25 characters long, if not,
     * Exception is thrown
     *
     * @param firstName Character length and limitations: 25 single-byte characters.
     * @throws IllegalArgumentException
     */
    public void setFirstName(String firstName) throws IllegalArgumentException {

        if (firstName.length() > 25) {
            throw new IllegalArgumentException("firstName can be maximum 25 characters long");
        }
        nvpRequest.put("FIRSTNAME", firstName);
    }

    /**
     * Payer's middle name. Argument can be maximum 25 characters long, if not,
     * Exception is thrown
     *
     * @param middleName    Character length and limitations: 25 single-byte characters.
     * @throws IllegalArgumentException
     */
    public void setMiddleName(String middleName)  throws IllegalArgumentException {

        if (middleName.length() > 25) {
            throw new IllegalArgumentException("MiddleName can be maximum 25 characters long");
        }
        nvpRequest.put("MIDDLENAME", middleName);
    }

    /**
     * Payer's last name. Argument can be maximum 25 characters long, if not,
     * Exception is thrown
     *
     * @param lastName  Character length and limitations: 25 single-byte characters.
     * @throws IllegalArgumentException
     */
    public void setLastName(String lastName)  throws IllegalArgumentException {

        if (lastName.length() > 25) {
            throw new IllegalArgumentException("lastName can be maximum 25 characters long");
        }
        nvpRequest.put("LASTNAME", lastName);
    }

    /**
     * Payer's suffix. Argument can be maximum 12 characters long, if not,
     * Exception is thrown
     *
     * @param suffix    Character length and limitations: 12 single-byte characters.
     * @throws IllegalArgumentException
     */
    public void setSuffix(String suffix) throws IllegalArgumentException {

        if (suffix.length() > 25) {
            throw new IllegalArgumentException("suffix can be maximum 12 characters long");
        }
        nvpRequest.put("SUFFIX", suffix);
    }

    public Map<String, String> getNVPRequest() {
        return new HashMap<String, String>(nvpRequest);
    }

    @Override
    public String toString() {
        return "Instance of PayerName class with the values: nvpRequest: " + nvpRequest.toString();
    }
}
