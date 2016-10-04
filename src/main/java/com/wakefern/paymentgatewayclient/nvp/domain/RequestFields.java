/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * RequestFields interface
 * 
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/06/2015
 */
public interface RequestFields extends Serializable {

    /**
     * Creates and returns part of the nvp (name value pair) request containing
     * request values
     *
     * @return part of the nvp request as a Map
     */
    Map<String, String> getNVPRequest();
}
