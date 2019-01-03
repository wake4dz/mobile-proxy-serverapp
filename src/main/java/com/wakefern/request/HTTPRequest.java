package com.wakefern.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wakefern.global.errorHandling.ResponseHandler;
import com.wakefern.logging.LogUtil;


public class HTTPRequest {

	private final static Logger logger = Logger.getLogger(HTTPRequest.class);
	
	private static int timeOutInt = 30000; // 30 seconds time out

	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Trigger HTTP GET request.
	 * 
	 * @param requestURL
	 * @param requestHeaders
	 * @param timeOut
	 * @return
	 * @throws Exception
	 */
	public static String executeGet(String requestURL, Map<String, String> requestHeaders, int timeOut) throws Exception {
		return executeRequest(requestURL, requestHeaders, null, "GET", timeOut);
	}
	
	/**
	 * Trigger HTTP PUT request.
	 * 
	 * @param requestURL
	 * @param requestBody
	 * @param requestHeaders
	 * @return
	 * @throws Exception
	 */
	public static String executePut(String requestURL, String requestBody, Map<String, String> requestHeaders) throws Exception {
		HttpURLConnection connection = null;

		try {
			// Create connection
			URL url = new URL(requestURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("PUT");

			if (requestBody != null) {
				// Set Content length
				connection.setRequestProperty("Content-length", requestBody.getBytes().length + "");
				connection.setUseCaches(false);
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setConnectTimeout(timeOutInt);
				connection.setReadTimeout(timeOutInt);

				for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
					connection.addRequestProperty(entry.getKey(), entry.getValue());
				}

				// Set JSON as body of request
				OutputStream oStream = null;
				try {
					oStream = connection.getOutputStream();
					oStream.write(requestBody.getBytes("UTF-8"));
				}finally {
					if(oStream != null) {
						try {
							oStream.close();
						} catch(Exception ex) {
    						logger.error("[HTTPRequest]::executePut::Exception close stream: " + ex.getMessage());
    						throw ex;
						}
					}
				}
			}

			// Connect to the server
			connection.connect();

			// Handle the response
			int responseCode = connection.getResponseCode();
			String response  = ResponseHandler.getResponse(connection);

			if (responseCode == 200 || responseCode == 201 || responseCode == 204 || responseCode == 205 || responseCode == 206) {
				return response;
			
			} else {
				String msg;
				
				if (response.length() > 0) {
					msg = responseCode + "," + LogUtil.transformHtmlResponse(response);
				} else {
					msg = responseCode + "," + connection.getResponseMessage();
				}
				
				throw new Exception(msg);
			}

		} catch (MalformedURLException ex) {
			logger.error(getErrorMsg("[executePut]::Exception: "+ ex.getMessage(), requestURL));
			throw ex;
		
		} catch (IOException ex) {
			logger.error(getErrorMsg("[executePut]::Exception: "+ ex.getMessage(), requestURL));
			throw ex;
		
		} catch (Exception ex) {
			logger.error(getErrorMsg("[executePut]::Exception: "+ ex.getMessage(), requestURL));
			throw ex;
		
		} finally {
			if (connection != null) {
				try {
					connection.disconnect();
				} catch (Exception ex) {
					logger.error("[executePut]::Exception closing connection, URL: "+ requestURL);
					throw ex;
				}
			}
		}
	}

	/**
	 * Trigger HTTP POST request, with timeout=30 seconds. 
	 * 
	 * @param requestURL
	 * @param requestBody
	 * @param requestHeaders
	 * @return
	 * @throws Exception
	 */
	public static String executePost(String requestURL, String requestBody, Map<String, String> requestHeaders) throws Exception {
		return executePost(requestURL, requestBody, requestHeaders, timeOutInt);
	}
	

	/**
	 * Trigger HTTP POST request, with timeout as input, currently being used by create order api.
	 * 	The create order api, in some cases, requires more than 30 seconds to return success response from MWG;
	 * 	therefore setting timeout = 40 seconds for create order api
	 * 
	 * @param requestURL
	 * @param requestBody
	 * @param requestHeaders
	 * @return
	 * @throws Exception
	 */
	public static String executePost(String requestURL, String requestBody, Map<String, String> requestHeaders, int timeout) throws Exception {

		HttpURLConnection connection = null;

		try {
			// Create connection
			URL url = new URL(requestURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			if (requestBody != null) {
				// Set Content length
				connection.setRequestProperty("Content-length", requestBody.getBytes().length + "");
				connection.setUseCaches(false);
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
				
				for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
					connection.addRequestProperty(entry.getKey(), entry.getValue());
				}

				// Set JSON as body of request
				OutputStream oStream = null;
				try {
					oStream = connection.getOutputStream();
					oStream.write(requestBody.getBytes("UTF-8"));
				} finally {
					if(oStream != null) {
						try {
							oStream.close();
						} catch(Exception ex) {
							logger.error("[HTTPRequest]::executePost::Exception close stream: " + ex.getMessage());
							throw ex;
						}
					}
				}
			}

			// Connect to the server
			connection.connect();

			// Handle the response
			int responseCode = connection.getResponseCode();
			String response  = ResponseHandler.getResponse(connection);

			if (responseCode == 200 || responseCode == 201 || responseCode == 204 || responseCode == 205 || responseCode == 206) {
				return response;
			
			} else {
				String msg;
				
				if (response.length() > 0) {
					msg = responseCode + "," + LogUtil.transformHtmlResponse(response);
				} else {
					msg = responseCode + "," + connection.getResponseMessage();
				}
				
				throw new Exception(msg);
			}
		
		} catch (MalformedURLException ex) {
			logger.error(getErrorMsg("[executePost]::Exception: "+ ex.getMessage(), requestURL));
			throw ex;
		
		} catch (IOException ex) {
			logger.error(getErrorMsg("[executePost]::Exception: "+ ex.getMessage(), requestURL));
			throw ex;
		
		} catch (Exception ex) {
			logger.error(getErrorMsg("[executePost]::Exception: "+ ex.getMessage(), requestURL));
			throw ex;
		
		} finally {
			if (connection != null) {
				try {
					connection.disconnect();
				} catch (Exception ex) {
					logger.error("[executePost]::Exception closing connection, path: "+ requestURL);
					throw ex;
				}
			}
		}
	}

	/**
	 * Used by com.wakefern.coupons
	 * 
	 * @param requestURL
	 * @param requestBody
	 * @param requestHeaders
	 * @param timeOut
	 * @return
	 * @throws Exception
	 */
	public static String executePostJSON(String requestURL, String requestBody, Map<String, String> requestHeaders, int timeOut) throws Exception {
		HttpURLConnection connection = null;
		long startTime, endTime;
		
		try {
			startTime = System.currentTimeMillis();
			
			// Create connection
			URL url = new URL(requestURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			if (requestBody != null) {
				// Set Content length
				connection.setRequestProperty("Content-length", requestBody.getBytes().length + "");
				connection.setUseCaches(false);
				connection.setDoOutput(true);
				connection.setDoInput(true);
				timeOut = (timeOut == 0) ? timeOutInt : timeOut;

				logger.trace("[executePostJSON]::Timeout: " +  timeOut);
				
				connection.setConnectTimeout(timeOut);
				connection.setReadTimeout(timeOut);

				for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
					connection.addRequestProperty(entry.getKey(), entry.getValue());
				}

				// Set JSON as body of request
				OutputStream oStream = null;
				try{
					oStream = connection.getOutputStream();
					oStream.write(requestBody.getBytes("UTF-8"));
				} finally {
					if(oStream != null) {
						try {
							oStream.close();
						} catch(Exception ex) {
							logger.error("[HTTPRequest]::executePostJSON::Exception close stream: " + ex.getMessage());
							throw ex;
						}
					}
				}
			}

			// Connect to the server
			connection.connect();

			int status = connection.getResponseCode();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();

			switch (status) {
			case 200:
			case 201:
			case 204:
				// sb.append(status);
				int read;
				char[] chars = new char[1024];
				while ((read = br.read(chars)) != -1) {
					sb.append(chars, 0, read);
				}
				br.close();
				break;
			default:
				// sb.append(status);
				throw new Exception(connection.getResponseCode() + "," + connection.getResponseMessage());
			}
			
			endTime = System.currentTimeMillis();

			logger.trace("[executePostJSON]::Total process time: " + (endTime - startTime) + " ms, path: "+requestURL);
			
			// return body to auth
			return sb.toString();

		} catch (MalformedURLException ex) {
			logger.error(getErrorMsg("[executePostJSON]::Exception: "+ ex.getMessage(), requestURL));
			throw ex;
		
		} catch (IOException ex) {
			logger.error(getErrorMsg("[executePostJSON]::Exception: "+ ex.getMessage(), requestURL));
			throw ex;
		
		} catch (Exception ex) {
			logger.error(getErrorMsg("[executePostJSON]::Exception: "+ ex.getMessage(), requestURL));
			throw ex;
		
		} finally {
			if (connection != null) {
				try {
					connection.disconnect();
				} catch (Exception ex) {
					logger.error("[executePostJSON]::Exception closing connection: "+ex.getMessage()+", URL: "+requestURL);
					throw ex;
				}
			}
		}
	}

	/**
	 * Execute HTTP Request
	 * 
	 * @param requestURL
	 * @param requestHeaders
	 * @param requestParameters
	 * @param requestMethod
	 * @param timeOut
	 * @return
	 * @throws Exception
	 */
	public static String executeRequest(
		String requestURL, 
		Map<String, String> requestHeaders,
		Map<String, String> requestParameters, 
		String requestMethod, 
		int timeOut
	) throws Exception {
		
		HttpURLConnection connection = null;

		try {
			long startTime, endTime;
			
			startTime = System.currentTimeMillis();
			connection = createConnection(requestURL, requestHeaders, requestParameters, requestMethod, timeOut);
			int responseCode = connection.getResponseCode();
			endTime = System.currentTimeMillis();

			logger.trace("[executeRequest]::Total process time for "+requestMethod+": "+(endTime - startTime)+" ms, URL: "+requestURL);
			
			//Note: Since only about 75% of APIs has the userId query parameter, this log message may not print
			//      even if isUserTrackOn=on. Just be aware of this fact.
			//      This block of code is more useful in the future when every API call has the userId parameter.
    		if(LogUtil.isUserTrackOn) {
    			if ((requestURL != null) && (LogUtil.getUserId(requestURL) != null ) ) {
    				if (LogUtil.trackedUserIdsMap.containsKey(LogUtil.getUserId(requestURL).trim())) {
						logger.info("Tracking data for " + LogUtil.getUserId(requestURL).trim() + ": " 
								+ "[executeRequest]::Total process time for " +requestMethod+ ": "+ (endTime - startTime) + " ms, URL: " + requestURL);
    				}
    			}
    		}
			
			String response = ResponseHandler.getResponse(connection);

			if (responseCode == 200 || responseCode == 201 || responseCode == 204 || responseCode == 205 || responseCode == 206) {
				return response;
			
			} else {
				// 2018-07-30 DZ thinks it is a redundant error log
				// logger.error("[executeRequest]::response code: " + connection.getResponseCode() + " , msg: " + connection.getResponseMessage() + 
				// 		" , URL:" + requestURL);
				String msg;
				if(responseCode == 403) {
					msg = "401,"+(response.length() > 0 ? response : connection.getResponseMessage());
					logger.info("Convert 403 error into 401.. URL: " + requestURL + " resp: " + response);
				} else {
					if (response.length() > 0) {
						msg = responseCode + "," + LogUtil.transformHtmlResponse(response);
					} else {
						msg = responseCode + "," + connection.getResponseMessage();
					}
				}
				
				throw new Exception(msg);
			}
		
		} catch (IOException e) {
			logger.error(getErrorMsg("[executeRequest]::Exception: "+ e.getMessage(), requestURL));
			throw e;
		
		} catch (URISyntaxException e) {
			logger.error(getErrorMsg("[executeRequest]::Exception: "+ e.getMessage(), requestURL));
			throw e;
		
		} catch (Exception e) {
			logger.error(getErrorMsg("[executeRequest]::Exception: "+ e.getMessage(), requestURL));
			throw e;
			
		} finally {
			if (connection != null) {
				try {
					connection.disconnect();
				} catch (Exception ex) {
					logger.error(getErrorMsg("[executeRequest]::Exception: "+ ex.getMessage(), requestURL));
					throw ex;
				}
			}
		}
	}

	public static String executeDelete(String requestURL, Map<String, String> requestHeaders, int timeOut) throws Exception {
		HttpURLConnection connection = null;
		long startTime, endTime;

		try {			
			startTime = System.currentTimeMillis();

			// Create connection
			URL url = new URL(requestURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			timeOut = timeOutInt;
			connection.setConnectTimeout(timeOut);
			connection.setReadTimeout(timeOut);

			for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
				connection.addRequestProperty(entry.getKey(), entry.getValue());
			}

			// Connect to the server
			connection.connect();

			int status = connection.getResponseCode();
			endTime = System.currentTimeMillis();

			logger.trace("[executeDelete]::Total process time: " + (endTime - startTime) + " ms, URL: " +
					requestURL + ", response code: " + connection.getResponseCode() + ", msg: " +  connection.getResponseMessage());

			switch (status) {
				case 200:
				case 201:
				case 204:
					return status + " Success";
				case 205:
					return status + " - "+connection.getResponseMessage();
				default:
					throw new Exception(connection.getResponseCode() + "," + connection.getResponseMessage());
			}
		
		} catch (MalformedURLException ex) {
			logger.error("[executeDelete]::MalformedURLException: " + ex.getMessage() + ", URL: " + requestURL);
			throw ex;
		
		} catch (IOException ex) {
			logger.error("[executeDelete]::IOException: " + ex.getMessage() + ", URL: " + requestURL);
			throw ex;
		
		} catch (Exception ex) {
			logger.error("[executeDelete]::Exception: " + ex.getMessage() + ", URL: " + requestURL);
			throw ex;
		
		} finally {
			if (connection != null) {
				try {
					connection.disconnect();
				
				} catch (Exception ex) {
					logger.error("[executeDelete]::Exception: " + ex.getMessage() + ", URL: " + requestURL +
							", response code: " + connection.getResponseCode() + ", msg: " + connection.getResponseMessage());
					
					throw ex;
				}
			}
		}
	}
	
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------


	/**
	 * Return a formatted error message
	 * 
	 * @param msg
	 * @param url
	 * @param respCode
	 * @param respMsg
	 * @return
	 */
	private static String getErrorMsg(String msg, String url) {
		if (msg != null) {
			// remove the new line marker
			return msg.trim() + ", url: " + url;
		} else {
			return "a null" + ", url: " + url; 
		}
	}
	   
	/**
	 * Create HTTP Connection.
	 * 
	 * @param requestURL
	 * @param requestHeaders
	 * @param requestParameters
	 * @param requestMethod
	 * @param timeOut
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private static HttpURLConnection createConnection(
			String requestURL, 
			Map<String, String> requestHeaders,
			Map<String, String> requestParameters, 
			String requestMethod, 
			int timeOut) throws IOException, URISyntaxException {
		
		HttpURLConnection connection = null;
		URI uri = new URI(requestURL);
		
		if (requestParameters != null) {
			for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
				uri = appendUri(uri.toString(), entry.getKey() + "=" + entry.getValue());
			}
		}

		URL url = uri.toURL();
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(requestMethod);
		
		if (timeOut == 0) {
			timeOut = timeOutInt;
		}
		
		connection.setConnectTimeout(timeOut);
		connection.setReadTimeout(timeOut);

		if (requestHeaders != null) {
			for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
				connection.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		// Connect to the server
		connection.connect();

		return connection;
	}

	/**
	 * Append query string to a URL.
	 * 
	 * @param uri
	 * @param appendQuery
	 * @return
	 * @throws URISyntaxException
	 */
	private static URI appendUri(String uri, String appendQuery) throws URISyntaxException {
		URI oldUri = new URI(uri);

		String newQuery = oldUri.getQuery();
		
		if (newQuery == null) {
			newQuery = appendQuery;
		} else {
			newQuery += "&" + appendQuery;
		}

		URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(), newQuery, oldUri.getFragment());

		return newUri;
	}
}
