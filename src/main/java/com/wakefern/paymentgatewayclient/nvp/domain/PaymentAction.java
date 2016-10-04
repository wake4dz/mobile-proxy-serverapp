/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.domain;

import java.io.Serializable;

/**
 * How you want to obtain payment
 *
 * You cannot set this value to Sale on SetExpressCheckout request and then change 
 * this value to Authorization on the final Express Checkout API DoExpressCheckoutPayment request.
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/06/2015
 */
public enum PaymentAction implements Serializable {

    /**
     * Indicates that this payment is a basic authorization subject to settlement 
     * with the EPay's Authorization & Capture.
     */
    AUTHORIZATION("Authorization"),

    /**
     * Indicates that this payment is is an order authorization subject to settlement 
     * with the EPay's Authorization & Capture.
     */
    ORDER("Order"),

    /**
     * Indicates that this is a final sale for which you are requesting payment.
     */
    SALE("Sale");

    private String value;

    private PaymentAction(String value) {
        this.value = value;
    }

    /**
     *
     * @return  string value for NVP request
     */
    public String getValue() {
        return value;
    }
}
