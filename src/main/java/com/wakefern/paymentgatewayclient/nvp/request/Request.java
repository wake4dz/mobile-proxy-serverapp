/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */

package com.wakefern.paymentgatewayclient.nvp.request;

import java.io.Serializable;
import java.util.Map;

/**
 * Request interface for the other Request Messages such as 
 * SetExpressCheckout, etc.
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/05/2015
 */
public interface Request extends Serializable {

    /**
     * Creates and returns part of the NVP (name-value pair) request 
     * containing request values
     */
    Map<String, String> getNVPRequest();

    /**
     * Setter for NVP (name-value pair) response
     */
    void setNVPResponse(Map<String, String> nvpResponse);

    /**
     * Return response from the Payment Gateway. If response is not 
     * set/received returns empty Map.
     *
     * @return response as a Map (HashMap)
     */
    Map<String, String> getNVPResponse();
}
