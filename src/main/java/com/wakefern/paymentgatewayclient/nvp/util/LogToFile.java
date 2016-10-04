package com.wakefern.paymentgatewayclient.nvp.util;

import java.io.*;
import java.util.*;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import com.wakefern.paymentgatewayclient.nvp.domain.Constants;

/**
 * Copied from com.wakefern.utils.LogToFile RMZ 2
 *
 * This class allows the exception and messages from within applications and servlets 
 * to be stored, with a time stamp showing the messages with the current time in 
 * milliseconds posted to the log.
 *
 * For this class to work a file must be created in the specified directory /logs/
 * 
 *  @author Wakefern Food Corp.
 *  @date 10/07/2015
 *  @version 1.0.0
 */
public class LogToFile {
	private static final String RESOURCE_BUNDLE_NAME = Constants.RESOURCE_BUNDLE_NAME;
	private static java.util.ResourceBundle resourceBundle = null;
	private static String logDir = null;
	
	/**
	 * Class method that appends log messages to a file specified in the parameters
	 * Will show the date and the message if requested
	 * returns null if no problems encountered writing the message
	 */
	public static synchronized String dateLog(final String message)	{
		return logToFile(message, Constants.APP_NAME, true);
	}  
	 
	/**
	 * Class method that appends log messages to a file specified in the parameters
	 * Will show the date 
	 */
	public static synchronized String logToFile(final String message, final String filename) {
		return logToFile( message , filename , true ); 
	}    
	 
	/**
	 * lass method that appends log messages to a file specified in the parameters
	 * Will show the date and the message if requested
	 * returns null if no problems encountered writing the message
	 */
	private static synchronized String logToFile(final String message, final String aFileName, 
												final boolean showDate) {
		String return_msg = "LogToFile.log():" ;

		try {
			resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
			
		} catch (Exception ex) {
			File resFile = new File("/wake/ofc/resources/shared");
			URI uri = resFile.toURI();
			URL url = null;
			try {
				url = uri.toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			URL[] urls = { url };
			ClassLoader loader = new URLClassLoader(urls);
			resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, Locale.getDefault(), loader);
		}
		logDir = resourceBundle.getString("LogToFilePath").trim();
		String filename = logDir.trim() + aFileName.trim();		

		try {
			// today's date
			final java.text.DateFormat today = new java.text.SimpleDateFormat("MMddyyyy");
		
			// filename contains today's date
			filename = logDir.trim() + today.format(new Date()) + aFileName + ".log";
					 
			// new log file
			final File f = new File(filename);
			final FileOutputStream fos = new FileOutputStream (f, true );
			final PrintWriter pw = new PrintWriter ( fos , true );
		
			final java.text.DateFormat timestamp = new java.text.SimpleDateFormat("HH:mm:ss:SSS");
			pw.println ( timestamp.format(new Date()) + ":" + message );
				pw.close();
			return_msg  = null ;
			
		} catch ( final Exception e ) {
			System.out.println("Error on LogToFile: " + e.getMessage() );
			return_msg += e.getMessage();
		}
		
		return return_msg;
	}                                      

}
