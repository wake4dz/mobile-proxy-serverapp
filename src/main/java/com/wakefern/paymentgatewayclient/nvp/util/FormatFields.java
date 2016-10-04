/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Converts fields to the Payment Gateway required format.
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/08/2015
 */
public final class FormatFields implements Serializable {

	private static final long serialVersionUID = 8401243744451162445L;

	// Payment Gateway's date/time format 
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // Credit card date format - MMYYYY 
    private static final SimpleDateFormat CARD_DATE_FORMAT = new SimpleDateFormat("MMyyyy");

    /**
     * This method is used inside main classes, if any classes needs date
     * argument you can use Java Date. In short - you will not need to use this
     * method.
     *
     * Payment Gateway needs  Coordinated Universal Time (UTC/GMT), using ISO 8601
     * format, and of type ns:dateTime for Date/Time formats An example 
     * date/time stamp is 2006-08-24T05:38:48Z
     * 
     * @param dateTime
     * @return  Coordinated Universal Time (UTC/GMT), using ISO 8601 format, 
     *          and of type ns:dateTime
     */
    public static String getDateTimeField(Date dateTime) {
        return DATETIME_FORMAT.format(dateTime);
    }

    /**
     * This method is used inside main classes, if any classes needs date
     * argument you can use Java Date. In short - you will not need to use this
     * method.
     *
     * Payment Gateway needs date in MMYYYY format
     *
     * @param date
     * @return  String in MMYYYY format
     */
    public static String getCardDateField(Date date) {
        return CARD_DATE_FORMAT.format(date);
    }

	/**
	 * Returns formated amount. For example 24.7 will become "24.70".
	 * Returned amount can be used for setting amounts in Payment Gateway requests.
	 * 
	 * @param amount
	 * @return
	 */
	public static String getAmountField(float amount) {
		if (amount < 0) {
			return "0.00";
		}
		return String.format("%.2f", amount);
	}

	/**
	 * Returns formated amount. For example 24.7 will become "24.70".
	 * Returned amount can be used for setting amounts in Payment Gateway requests.
	 * 
	 * @param amount
	 * @return
	 */
	public static String getAmountField(int amount) {
		if (amount < 0) {
			return "0.00";
		}
		return String.format("%d.00", amount);
	}
	
}
