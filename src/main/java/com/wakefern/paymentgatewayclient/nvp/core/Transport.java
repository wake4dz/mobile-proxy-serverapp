/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.core;

import java.io.Serializable;
import java.net.MalformedURLException;

/**
 * Used for sending request and returning response
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/05/2015
 */
interface Transport extends Serializable {

	/**
	 * Sends request (msg attribute) to the specified URL and returns response as a string
	 *
	 * @param URLString	where to send the request
	 * @param msg		request message to be sent
	 * @return			response message
	 * @throws 			MalformedURLException
	 */
	String getResponse(String URLString, String msg) throws MalformedURLException;
	
}
