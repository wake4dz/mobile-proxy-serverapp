/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.profile;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents Payment Gateway User 
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/05/2015
 */
public interface Profile extends Serializable {

    /**
     * Creates and returns part of the NVP (name value pair) containing user
     * name, password, signature etc. 
     * 
     * @return user part of the nvp request as a Map
     */
    Map<String, String> getNVPMap();
    
}
