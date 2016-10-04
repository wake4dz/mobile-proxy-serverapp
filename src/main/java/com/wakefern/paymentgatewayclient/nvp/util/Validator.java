/*
 * Copyright 2015 - Wakefern Food Corp.
 */
package com.wakefern.paymentgatewayclient.nvp.util;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates common Payment Gateway fields. The class is used directly by the Payment Gateway
 * so you can use it for validation before setting certain values in Payment Gateway
 * requests or fields.
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/06/2015
 */
public final class Validator implements Serializable {

	private static final long serialVersionUID = 2718416134916755643L;
	
	private static Pattern pattern;
	private static Matcher matcher;
	
  
	/**
	 * 	A numeric value will have following format:
	 * ^[-+]?[0-9]*\\.?[0-9]+$
	 * 
	 *  ^			: start of line
	 *  [-+]? 		: Starts with an optional '+' or '-' sign
	 *  [0-9]* 		: May have one or more digits
	 *  \\.?   		: May contain an optional '.' (decimal point) character
	 *  [0-9]+		: ends with numeric digit
	 *  $			: end of line
	 */
	private static final String NUMERIC_PATTERN = "^[-+]?[0-9]*\\.?[0-9]+$";
	
	/**
	 * Amount has to be exactly two decimal places and decimal point is ".", or just 0.
	 */
	private static final String AMOUNT_PATTERN  = "^(\\d*\\.\\d{2}|0{1})$";
	
	
	/**
 	 * Phone Number: NANP phone standard for 10 digits has form as NXX NXX XXXX
	 * NPA (Numbering Plan Area Code) - Allowed ranges: [2-9] for the first digit (N), and [0-9] for the second 
	 * and third digits. When the second and third digits of an area code are the same, that code is called 
	 * an easily recognizable code (ERC). ERCs designate special services; e.g., 888 for toll-free service. 
	 * The NANP is not assigning area codes with 9 as the second digit.
	 * NXX (Central Office) - Allowed ranges: [2-9] for the first digit, and [0-9] for both the second and 
	 * third digits (however, the third digit cannot be '1' if the second digit is also '1').
	 * XXXX (Subscriber Number) -[0-9] for each of the four digits.
	 * Matches: (658) 554-1122  or  6581541122  or  658-254-1122
	 * Not matches: 123-311-5555  or  732-021-0544  or  800-311-0566
 	 * 
     * ^			: start of line
     * \\(?   		: May start with an option '(' 
     * (\\d{3})		: Followed by 3 digits
     * \\)?    		: May have an optional ')'
     * [- ]?   		: May have an optional '-' after the first 3 digits or after  the optional ')' character
     * (\\d{3})		: Followed by 3 digits
     * [- ]?   		: May have another optional '-' after numeric digits
     * (\\d{4}) 	: ends with four digits
     * $			: end of line
     * 
     * Examples: Matches following phone numbers:
     *       (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
	 */
	private static final String PHONE_PATTERN = "^\\(?([2-9]{1}[0-9]{2})\\)?[-. ]?([2-9](?!11)[0-9]{2})[-. ]?([0-9]{4})$";
	// private static final String PHONE_PATTERN = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";   // Edited as of 11/21/2014
	
	/**
	 * SSN Pattern - Good SSN formats: xxx-xx-xxxx, xxxxxxxxx, xxx-xxxxxx; xxxxx-xxxx
	 * ^\\d{3}[- ]?\\d{2}[- ]?\\d{4}$
	 * 
	 * ^			: start of line
	 * \\d{3} 		: Starts with three numeric digits
	 * [- ]?   		: Followed by an optional '-'
	 * \\d{2}  		: Two numeric digits after the optional '-'
	 * [- ]?   		: May contain an optional second '-' character
	 * \\d{4}  		: ends with four numeric digits
	 * $			: end of line
	 * 
	 * Examples: 879-89-8989, 869878789
	 * 
	 */
	private static final String SSN_PATTERN = "^\\d{3}[- ]?\\d{2}[- ]?\\d{4}$";
	
	/**
	 * Date format pattern MM/dd/yyyy
	 * 
	 * ^					: start of the line to ensure of no leading space or other chars 
	 * (					: start of group #1 for month MM
	 * 0?[1-9]				: 01-09 or 1-9  The leading "0" is optional
	 * |					: or
	 * 1[012]				: 10, 11, 12
	 * )					: end of group #1
	 * /					: followed by a forward slash  /
	 * (					: start of group #2 for day dd
	 * 0?[1-9]				: 01-09 or 1-9  Again, the leading "0" is optional
	 * |        			: or
	 * [12][0-9]			: 10-19 or 20-29
	 * |					: or
	 * 3[01]				: 30, 31
	 * ) 					: end of group #2
	 * /					: followed by a slash  /
	 * (					: start of group #3 for year yyyy
	 * (19|20|21)\\d\\d		: 19[0-9][0-9] or 20[0-9][0-9] or 21[0-9][0-9]
	 * )					: end of group #3
	 * $					: end of the line to ensure no trailing space or other chars 
	 */
	private static final String DATE_PATTERN = "^(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)$"; 
	
	/**
	 * Email: With a note that the following pattern is NOT strictly adhered to the official standard 
	 * is known as RFC 2822 which allows really strange email addresses and domain names  
	 * 
	 * ^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$
	 * 
	 * ^					: start of the line
	 * [_A-Za-z0-9-\\+]+	: must start with string in the bracket [ ], and must contains one or more (+)
	 * (					: start of group #1
	 * \\.[_A-Za-z0-9-]+	: followed by a dot "." and string in the bracket [ ], and must contains one or more (+)
	 * )*					: end of group #1, this group is optional (*)
	 * @					: must contains a "@" symbol
	 * [A-Za-z0-9-]+      	: followed by string in the bracket [ ], and must contains one or more (+)
	 * (					: start of group #2 - first level TLD checking
	 * \\.[A-Za-z0-9]+  	: followed by a dot "." and string in the bracket [ ], and must contains one or more (+)
	 * )*					: end of group #2, this group is optional (*)
	 * (					: start of group #3 - second level TLD checking
	 * \\.[A-Za-z]{2,}  	: followed by a dot "." and string in the bracket [ ], with minimum length of 2
	 * )					: end of group #3
	 * $					: end of the line
	 */
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]+)$";
	                                             
	
	/**
	 * Time in 24 hours format
	 * ^([01]?[0-9]|2[0-3]):[0-5][0-9]$
	 * 
	 * ^					: beginning of line
	 * (					: start of group #1
	 * [01]?[0-9]			: start with 0-9, 1-9, 00-09, or 10-19
	 * |					: or
	 * 2[0-3]				: start with 20-23
	 * )					: end of group #1
	 * :					: follow by a semicolon (:)
	 * [0-5][0-9]			: follow by 0..5 and 0..9, which means 00 to 59
	 * $
	 */
	private static final String TIME24H_PATTERN = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
	
	/**
	 * Time in 12 hours format
	 * ^(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)$
	 * 
	 * ^					: beginning of line
	 * (					: start of group #1
	 * 1[012]				: start with 10, 11, 12
	 * |					: or
	 * [1-9]				: start with 1,2,...9
	 * )					: end of group #1
	 * :					: follow by a semicolon (:)
	 * [0-5][0-9]			: follow by 0..5 and 0..9, which means 00 to 59
	 * (\\s)?				: follow by a white space (optional)
	 * (?i)					: ignore the case sensitive checking for the following characters
	 * (am|pm)				: follow by am or pm
	 * $					: end of line
	 */
	private static final String TIME12H_PATTERN = "^(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)$";
	
	/**
	 * Image format that only supports JPG, PNG, GIF, and BMP for now
	 * ^([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)$
	 * 
	 * ^					: beginning of line
	 * (					: Start of the group #1
	 * [^\s]+				: must contains one or more anything (except white space)
	 * (					: start of the group #2
	 * \.					: follow by a dot "."
	 * (?i)					: ignore the case sensitive checking for the following characters
	 * (					: start of the group #3
	 * jpg					: contains characters "jpg"
	 * |					: or
	 * png					: contains characters "png"
	 * |					: or
	 * gif					: contains characters "gif"
	 * |					: or
	 * bmp					: contains characters "bmp"
	 * )					: end of the group #3
	 * )					: end of the group #2
	 * $					: end of the string
	 * )					: end of the group #1
	 * $					: end of line
	 */
	private static final String IMAGEEXT_PATTERN = "^([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)$";
	
	/**
	 * EXPIRY DATE with format MM/YY
	 * ^(?:0[1-9]|1[0-2])/[0-9]{2}$
	 * 
	 * ^  				: start of line
	 * (				: start of group
	 * ?:				: optional of
	 * 0[1-9]			: leading zero then followed by 1 digit from 1 thru 9 (JAN thru SEP)
	 * |				: or
	 * 1[0-2]			: leading 1 and followed by either 0, 1, or 2 for OCT, NOV, DEC
	 * )				: end of group
	 * /				: the slash
	 * [0-9]{2}			: any 2 digits after
	 * $				: end of line
	 */
	private static final String CC_EXPIRY_DATE_PATTERN = "^(?:0[1-9]|1[0-2])/[0-9]{2}$";
	
	/**
	 * PPC (Price Plus Card): starts with either 41, 43, 44, 47, or 48. All numbers and 
	 * total length of the PPC is 11
	 * 
	 * ^4[13478][0-9]{9}$
	 * ^				: start of line
	 * 4				: start with number 4 
	 * [13478]			: follow by either 1, 3, 4, 7, or 8 
	 * [0-9]{9}			: then 9 digits followed
	 * $				: end of line
	 */
	private static final String PPC_PATTERN = "^4[13478][0-9]{9}$"; 

	/**
	 * Gift card: starts with number 7 and total length is 17
	 * ^7[0-9]{16}$
	 * 
	 * ^					: start of line
	 * 7					: start with number 7
	 * [0-9]{16}			: followed by 16 digits
	 * $					: end of line
	 */
	private static final String GIFTCARD_PATTERN = "^7[0-9]{16}$";
	
	/**
	 * EBT Card: Families First Card Electronic Benefits Transfer
	 * ^6[0-9]{15}$
	 * 
	 * ^					: start of line
	 * 6					: start with number 6
	 * [0-9]{15}			: followed by 15 digits
	 * $					: end of line
	 */
	private static final String EBT_PATTERN = "^6[0-9]{15}$";
	
	/**
	 * Store Number: valid from 100 to 9999.
	 * Examples of valid store numbers: 105, 0585, 9989
	 * Examples of invalid store numbers: 85, 085, 0055, 29999
	 * ^(0?[1-9][0-9]{2})|([1-9][0-9]{3})$
	 * 
	 * ^				: start of the line
	 * (				: start of alternate group #1
	 * 0?				: 0 is optional
	 * [1-9]			: any digit from 1 thru 9
	 * [0-9]{2}			: any 2 digits
	 * )				: end of alternate group #1
	 * |				: OR
	 * (				: start of alternate group #2
	 * [1-9]			: either digit from 1 thru 9 (0 is not allowed)
	 * [0-9]{3}			: repeat the digits 3 times
	 * )				: end of alternate group #1
	 * $				: end of the line
	 */
	private static final String STORENUM_PATTERN = "^(0?[1-9][0-9]{2})|([1-9][0-9]{3})$";
	
	/**
	 * US States including PR and DC
	 * 
	 */
	private static final String US_STATES_PATTERN = "^(?:(A[KLRZ]|C[AOT]|D[CE]|FL|GA|HI|I[ADLN]|K[SY]|LA|M[ADEINOST]|N[CDEHJMVY]|O[HKR]|P[AR]|RI|S[CD]|T[NX]|UT|V[AIT]|W[AIVY]))$";
	
	/**
	 * US Zip code with 5 digit number
	 * 
	 */
	private static final String ZIPCODE_PATTERN = "^\\d{5}(?:[-\\s]\\d{4})?$";    // "^[0-9]{5}";
	
	/**
	 * First Name
	 * ^[a-zA-z]+([ '-][a-zA-Z]+)*$
	 * 
	 * ^				: start of line
	 * [a-zA-z]+		: start with at least one letter and repeats
	 * (				: start of group
	 * [ '-]			: may be a space, the ', the ., or the -
	 * [a-zA-Z]+		: followed by at least one letter
	 * )				: end of group
	 * *				: the group repeats 0 to more times
	 * $				: end of line
	 */
	private static final String FIRSTNAME_PATTERN = "^[a-zA-z]+([ \\'\\-\\.][a-zA-Z\\.]+)*$";

	/**
	 * Last Name
	 * ^[a-zA-z]+([ '-][a-zA-Z]+)*$
	 * 
	 * ^				: start of line
	 * [a-zA-z]+		: start with at least one letter and repeats
	 * (				: start of group
	 * [ '-.]			: may be a space, the ', the ., or the -
	 * [a-zA-Z]+		: followed by at least one letter
	 * )				: end of group
	 * *				: the group repeats 0 to more times
	 * $				: end of line
	 */
	private static final String LASTNAME_PATTERN = "^[a-zA-z]+([ \\'\\-\\.][a-zA-Z\\.]+)*$";
	
	/**
	 * Digits only and single 0 is accepted
	 * 
	 */
	private static final String DIGITS_ONLY_PATTERN = "^(0|[1-9][0-9]*)$";

	/**
	 * User Name
	 *  ^				: start
	 *  [a-zA-Z]		: starts with letter either lower or upper case
	 *  [a-zA-Z0-9_-]	: followed by letters, numbers, _, or -
	 *  {4,7}			: repeats from 4 to 7 (total length from 5 to 8) 
	 *  $				: end
	 */
	private static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9_-]{4,8}$";
	
	/**
	 * Password pattern:
	 *   Must be between 8 and 50 characters long
	 *   Must contain at least one digit.
	 *   Must contain at least one lower case character.
	 *   Must contain at least one upper case character.
	 *   Must contain at least on special character from [ @ # $ % ! . ].
	 */
	private static final String PASSWORD_PATTERN = "^((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,50})$";
	
	/**
	 * Money pattern
	 * ^             # Start of string.
	 * [0-9]+        # Must have one or more numbers. (.25) does NOT work
	 * (             # Begin optional group.
	 *   \\.         # The decimal point ".",
	 *   [0-9][0-9]  # One or two numbers.
	 * )?            # End group, signify it's optional with ?
	 * $  
	 */ 
	private static final String MONEY_PATTERN = "^[0-9]+(\\.[0-9][0-9]?)?$";
	
	/**
	 * Positive integer pattern
	 */
	private static final String POSITIVE_INTEGER_PATTERN = "^[0-9]+$";
	
	/**
	 * Alpha numeric pattern and up to 256 in length
	 */
	private static final String ALPHA_NUMERIC_PATTERN = "^[a-zA-Z0-9]{1,256}$";
	
	/**
	 * Pattern for matching the URL or URI
	 */
	private static final String URL_URI_PATTERN = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]$";
	
	
	/**
	 * Street Address Pattern
	 */
	private static final String STREET_ADDR_PATTERN = "^[a-zA-Z\\d\\s\\-\\,\\'\\#\\.\\+]+$";

	/**
	 * City Pattern 
	 */
	private static final String CITY_PATTERN = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
	
	
	/**
	 * Valid URL Pattern
	 */
	private static final String URL_PATTERN = "^(http(?:s)?\\:\\/\\/[a-zA-Z0-9]+(?:(?:\\.|\\-)[a-zA-Z0-9]+)+(?:\\:\\d+)?(?:\\/[\\w\\-]+)*(?:\\/?|\\/\\w+\\.[a-zA-Z]{2,4}(?:\\?[\\w]+\\=[\\w\\-]+)?)?(?:\\&[\\w]+\\=[\\w\\-]+)*)$";
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Validate date format with regular expression
	 * 
	 * @param date date address for validation
	 * @return true for valid date format, false for invalid date format
	 **/
	public static boolean isValidDate(final String date){
		
		if (date == null) {
			return false;
		}
		
		pattern = Pattern.compile(DATE_PATTERN);
		
		CharSequence inputDate = date;
		// Trying to find the match
		matcher = pattern.matcher(inputDate);
		
		if (matcher.matches()) {  // if EXACT matched found - IMPORTANT
			matcher.reset();  // Reset to the beginning of the pattern matched
			
			// Even though the pattern was matched. We still have to check for the valid date
			if (matcher.find()) {  // Moving to the 1st occurrence
				String month = matcher.group(1);
				String day   = matcher.group(2);
				int year     = Integer.parseInt(matcher.group(3));
 
				if (day.equals("31") && (month.equals("4") || month .equals("6") || month.equals("9") ||
						month.equals("11") || month.equals("04") || month .equals("06") ||
						month.equals("09"))) {
					return false; // only 1, 3, 5, 7, 8, 10, 12 has 31 days
				} else if (month.equals("2") || month.equals("02")) {
					if (year % 4 == 0) {  // Leap year then February has 29 days
						if (day.equals("30") || day.equals("31")) {
							return false;
						} else {
							return true;
						}
					} else { // If NOT a leap year, February only has 28 days
						if (day.equals("29") || day.equals("30") || day.equals("31")) {
							return false;
						} else {
							return true;
						}
					}
				} else {				 
					return true;				 
				}
			} else {  
				return false;
			}		  
		} else {  // match NOT found
			return false;
		}			    
	}

	/**
	 * Validate email format with Regular Expression
	 * 
	 * @param email
	 * @return true if matched, false otherwise
	 */
	public static boolean isValidEmail(final String email) {
		
		if (email == null) {
			return false;
		}
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		
		CharSequence inputEmail = email;
		// Trying to find the match
		matcher = pattern.matcher(inputEmail);
		
		return matcher.matches();  // return the result of if EXACT matched found or not - IMPORTANT
	}
	
	
	/**
	 * Validate time in 24 hours format with Regular Expression
	 * 
	 * @param time24h
	 * @return @return true if matched, false otherwise
	 */
	public static boolean isValidTime24h(final String time24h) {
		
		if (time24h == null) {
			return false;
		}

		pattern = Pattern.compile(TIME24H_PATTERN);
		
		CharSequence inputTime = time24h;
		// Trying to find the match
		matcher = pattern.matcher(inputTime);
		
		return matcher.matches();  // return the result of if EXACT matched found or not - IMPORTANT
	}

	
	/**
	 * Validate time in 12 hours format with Regular Expression
	 *  
	 * @param time12h
	 * @return @return true if matched, false otherwise
	 */
	public static boolean isValidTime12h(final String time12h) {

		if (time12h == null) {
			return false;
		}

		pattern = Pattern.compile(TIME12H_PATTERN);
		
		CharSequence inputTime = time12h;
		// Trying to find the match
		matcher = pattern.matcher(inputTime);
		
		return matcher.matches();  // return the result of if EXACT matched found or not - IMPORTANT
	}
	
	/**
	 * Validate Image file name format with Regular Expression
	 * 
	 * @param imageFileName
	 * @return @return true if matched, false otherwise
	 */
	public static boolean isValidImageFileName(final String fileName) {

		if (fileName == null) {
			return false;
		}

		pattern = Pattern.compile(IMAGEEXT_PATTERN);
		
		CharSequence inputFileName = fileName;
		// Trying to find the match
		matcher = pattern.matcher(inputFileName);
		
		return matcher.matches();  // return the result of if EXACT matched found or not - IMPORTANT
	}
	

	/**
	 * 
	 * @param expiryDate
	 * @return true if the exp date format is valid; false otherwise
	 */
	public static boolean isValidCardExpiryDate(final String expiryDate) {
		
		if (expiryDate == null) {
			return false;
		}
		
		CharSequence inputExpDate = expiryDate;
		pattern = Pattern.compile(CC_EXPIRY_DATE_PATTERN);
		matcher = pattern.matcher(inputExpDate);
		
	    return matcher.matches();
	}
	
	/**
	 * Function to validate a number using Java regular expression.
	 * This method checks if the input string contains all numeric characters.
	 * 
	 * @param String: Number to validate
	 * @return boolean: true if the input is all numeric, false otherwise.
	 */
	public static boolean isNumeric(final String number){
		
		if (number == null) {
			return false;
		}

		CharSequence inputNumber = number;
		pattern = Pattern.compile(NUMERIC_PATTERN);
		matcher = pattern.matcher(inputNumber);
		
		return matcher.matches();
	}
	
	
    /**
     * Validates string if it is amount supported. Amount has to be exactly two decimal 
     * places, decimal point is ".", or 0. 
     * 
	 * You can set 0 for for example Payment when you set up recurring payment.
     *
     * @param   amount string representing the amount
     * @return  true if amount is valid, false otherwise
     */
    public static boolean isValidAmount(final String amount) {

		if (amount == null) {
			return false;
		}

		CharSequence inputNumber = amount;
		pattern = Pattern.compile(AMOUNT_PATTERN);
		matcher = pattern.matcher(inputNumber);
		
		return matcher.matches();
    }
	
	/** 
	 * This function checks if the input phone number string to see if it is 
	 * a valid phone number. 
	 *
	 * @param String Phone number
	 * @return boolean: true if phone number is valid, false otherwise.
	 */
	public static boolean isValidPhone (final String phoneNumber){
		
		if (phoneNumber == null) {
			return false;
		}
		
		if (phoneNumber.equals("0000000000")) {
			return true;
		}
		CharSequence inputPhone = phoneNumber;
		pattern = Pattern.compile(PHONE_PATTERN);
		matcher = pattern.matcher(inputPhone);
		
		return matcher.matches();
	}
	
	/**
	 * Function to validate the store number
	 * Valid range from 100 to 9999
	 * 
	 * @param storeNum
	 * @return boolean
	 */
	public static boolean isValidStoreNumber(final String storeNum) {
	
		if (storeNum == null) {
			return false;
		}
		
		CharSequence inputStore = storeNum;
		pattern = Pattern.compile(STORENUM_PATTERN);
		matcher = pattern.matcher(inputStore);
		
		return matcher.matches();
	}
	
	/** 
	 * Function to validate Social Security number (SSN) using Java regular 
	 * expression. 
	 * 
	 * @param String Social Security number to validate
	 * @return boolean: true if social security number is valid, false otherwise.
	 */
	public static boolean isValidSSN(final String ssn){
		
		if (ssn == null) {
			return false;
		}
		
		pattern = Pattern.compile(SSN_PATTERN);
		CharSequence inputSSN = ssn;
		matcher = pattern.matcher(inputSSN);
		
		return matcher.matches(); 
	}
	
	/**
	 * Function to validate PPC number
	 * 
	 * @param ppcNum
	 * @return true if PPC valid; or false otherwise
	 */
	public static boolean isValidPPC(final String ppcNum) {
		
		if (ppcNum == null) {
			return false;
		}
		
		pattern = Pattern.compile(PPC_PATTERN);
		CharSequence inputPPC = ppcNum;
		matcher = pattern.matcher(inputPPC);
		
		return matcher.matches(); 		
	}
	
	/**
	 * Function to validate Gift card number
	 * 
	 * @param gcNum
	 * @return true if gift card valid; or false otherwise
	 */
	public static boolean isValidGiftCard(final String gcNum) {
		
		if (gcNum == null) {
			return false;
		}
		
		pattern = Pattern.compile(GIFTCARD_PATTERN);
		CharSequence inputGC = gcNum;
		matcher = pattern.matcher(inputGC);
		
		return matcher.matches(); 		
	}
	
	
	/**
	 * Function to validate EBT number
	 * 
	 * @param ebtNum
	 * @return true if EBT card number is valid; or false otherwise
	 */
	public static boolean isValidEBTCard(final String ebtNum) {
		
		if (ebtNum == null) {
			return false;
		}
		
		pattern = Pattern.compile(EBT_PATTERN);
		CharSequence inputEBT = ebtNum;
		matcher = pattern.matcher(inputEBT);
		
		return matcher.matches(); 		
	}
	
	/**
	 * Function to validate US State
	 * 
	 * @param State abbreviation
	 * @return true if State valid; or false otherwise
	 */
	public static boolean isValidState(final String state) {
		
		if (state == null) {
			return false;
		}
		
		pattern = Pattern.compile(US_STATES_PATTERN);
		CharSequence inputState = state;
		matcher = pattern.matcher(inputState);
		
		return matcher.matches(); 		
	}
	
	/**
	 * Function to validate US Zip code
	 * 
	 * @param zipcode
	 * @return true if zipcode valid; or false otherwise
	 */
	public static boolean isValidZipcode(final String zipcode) {
		
		if (zipcode == null) {
			return false;
		}
		
		pattern = Pattern.compile(ZIPCODE_PATTERN);
		CharSequence inputZipcode = zipcode;
		matcher = pattern.matcher(inputZipcode);
		
		return matcher.matches(); 		
	}
	
	/**
	 * Function to validate the input Fist name
	 * 
	 * @param firstName
	 * @param length
	 * @return true or false
	 */
	public static boolean isValidFirstName(final String firstName, final int length) {
		
		if (firstName == null || firstName.length() > length) {
			return false;
		} 
		
		CharSequence inputFirstName = firstName;
		pattern = Pattern.compile(FIRSTNAME_PATTERN);
		matcher = pattern.matcher(inputFirstName);
		
		return matcher.matches();
	}

	/**
	 * Function to validate the input Last name
	 * 
	 * @param lastName
	 * @param length
	 * @return true or false
	 */
	public static boolean isValidLastName(final String lastName, final int length) {
	
		if (lastName == null || lastName.length() > length) {
			return false;
		}
		
		CharSequence inputLastName = lastName;
		
		pattern = Pattern.compile(LASTNAME_PATTERN);
		matcher = pattern.matcher(inputLastName);
		
		return matcher.matches();
	}
	   
	
	/**
	 * Function to validate the SessionId
	 * 
	 * @param sessionId
	 * @return true or false
	 */
	public static boolean isValidSessionId(final String sessionId) {
		if (sessionId != null && sessionId.trim().length() == 16) {  // for 128 bit sessionId
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Function to validate order number
	 * 
	 * @param orderNumber
	 * @return true or false
	 */
	public static boolean isValidOrderNumber(final String orderNumber) {
		
		if (orderNumber == null || orderNumber.length() > 10) {
			return false;
		}
		
		CharSequence inputOrderNum = orderNumber;
		
		pattern = Pattern.compile(DIGITS_ONLY_PATTERN);
		matcher = pattern.matcher(inputOrderNum);
		
		return matcher.matches();
	}
	
	/**
	 * Function to validate user Id
	 * 
	 * @param userId
	 * @return true or false
	 */
	public static boolean isValidUserId(final String userId) {
		
		if (userId == null) {
			return false;
		}
		
		CharSequence inputUserId = userId;
		
		pattern = Pattern.compile(USERNAME_PATTERN);
		matcher = pattern.matcher(inputUserId);
		
		return matcher.matches();
	}
	
	/**
	 * Function to validate user password
	 * 
	 * @param userPassword
	 * @return true or false
	 */
	public static boolean isValidUserPassword(final String userPassword) {
		
		if (userPassword == null) {
			return false;
		}
		
		CharSequence inputUserPassword = userPassword;
		
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(inputUserPassword);
		
		return matcher.matches();
	}
	
	/**
	 * Function to validate money values
	 * 
	 * @param amount
	 * @return true or false
	 */
	public static boolean isValidMoneyAmount(final String amount) {
		
		if (amount == null) {
			return false;
		}
		
		CharSequence inputAmount = amount;
		
		pattern = Pattern.compile(MONEY_PATTERN);
		matcher = pattern.matcher(inputAmount);
		
		return matcher.matches();
	}
	
	/**
	 * Function to validate positive integer number
	 * 
	 * @param number
	 * @return true or false
	 */
	public static boolean isValidPositiveInt(final String number) {
		
		if (number == null) {
			return false;
		}
		
		CharSequence inputNumber = number;
		
		pattern = Pattern.compile(POSITIVE_INTEGER_PATTERN);
		matcher = pattern.matcher(inputNumber);
		
		return matcher.matches();
	}
	
	/**
	 * Function to validate Alpha Numeric pattern
	 * 
	 * @param someString
	 * @return true or false
	 */
	public static boolean isValidAlphaNumeric(final String someString) {
		
		if (someString == null) {
			return false;
		}
		
		CharSequence inputString = someString;
		
		pattern = Pattern.compile(ALPHA_NUMERIC_PATTERN);
		matcher = pattern.matcher(inputString);
		
		return matcher.matches();
	}
	
	/**
	 * Function to validate URL or URI
	 * 
	 * @param someURI
	 * @return true or false
	 */
	public static boolean isValidURI(final String someURI) {
		
		if (someURI == null) {
			return false;
		}
		
		CharSequence inputString = someURI;
		
		pattern = Pattern.compile(URL_URI_PATTERN);
		matcher = pattern.matcher(inputString);
		
		return matcher.matches();
	}
	
	/**
	 * 
	 * @param freeFormText
	 * @return true or false
	 */
	public static boolean isSafeFromXSS(final String freeFormText) {
		
		return true;
	}
	
	
	/**
	 * Function to validate Street Address
	 * 
	 * @param streetAddress
	 * @return true or false
	 */
	public static boolean isValidStreetAddress(final String streetAddress) {
		
		if (streetAddress == null) {
			return false;
		}
		
		CharSequence inputString = streetAddress;
		
		pattern = Pattern.compile(STREET_ADDR_PATTERN);
		matcher = pattern.matcher(inputString);
		
		return matcher.matches();
	}
	
	/**
	 * Function to validate City name
	 * 
	 * @param cityName
	 * @return true or false
	 */
	public static boolean isValidCity(final String cityName) {
		
		if (cityName == null) {
			return false;
		}
		
		CharSequence inputString = cityName;
		
		pattern = Pattern.compile(CITY_PATTERN);
		matcher = pattern.matcher(inputString);
		
		return matcher.matches();
	}	
	
	/**
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean isInteger(final String s) {
		boolean isValidInteger = false;
		
		try {
			Integer.parseInt(s);
			// s is a valid integer
			isValidInteger = true;
		} catch (NumberFormatException ex) {
			// s is not an integer
		}
		return isValidInteger;
	}

    /**
     * Uses Luhn algorithm to validate numbers. Luhn algorithm is also known as
     * modulus 10 or mod 10 algorithm.
     *
     * In this case, this is used to validate credit card numbers.
     *
     * @param number    number to be validated (credit card number)
     * @return
     */
    public static boolean isValidLuhn(String number) {
    	int sum = 0;
		boolean alternate = false;
		for (int i = number.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(number.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		return (sum % 10 == 0);
    }
    
	/**
	 * A simple but powerful URL validating regex. Accepts multiple sub-domains and sub-directories. Even accept query strings. 
	 * Now accept ports! Accepts HTTP or HTTPS. Also accepts optional "/" on end of address.
	 * Matches:
	 *     http://website.com | http://subdomain.web-site.com/bin/test.cgi?key1=value1&key2=value2
	 * Non-Matches:
	 *     http://website.com/test.cgi?key= | http://web-site.com/bin/test.cgi?key1=value1&key2
	 *     
	 * @param urlString
	 * @return true or false
	 */
	public static boolean isValidURL(final String urlString) {
		
		if (urlString == null) {
			return false;
		}
		
		pattern = Pattern.compile(URL_PATTERN);	
		
		CharSequence inputURL = urlString;
		// Trying to find the match
		matcher = pattern.matcher(inputURL);
		
		return matcher.matches();  
	}
    
}
