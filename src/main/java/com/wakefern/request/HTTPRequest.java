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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.VcapProcessor;
import com.wakefern.global.errorHandling.ResponseHandler;
import com.wakefern.logging.LogUtil;

public class HTTPRequest {

	private final static Logger logger = LogManager.getLogger(HTTPRequest.class);

	/**
	 * A default read timeout for outgoing HTTP requests.
	 */
	private static final int DEFAULT_READ_TIMEOUT_MS = 30000;

	/**
	 * A default connect timeout for outgoing HTTP requests.
	 */
	private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 5000;

	/**
	 * Set connection timeout and read timeout.
	 */
	private static final int sConnectionTimeoutMs, sReadTimeoutMs;


	static {
		logger.info("Reading default connection and read timeout values from env");

		int connTimeout = 0, readTimeout = 0;
		// Read environment values
		try {
			connTimeout = VcapProcessor.getHttpDefaultConnectTimeoutMs();
		} catch (Exception e) {
			logger.error("Error reading default connection timeout from env");
		}

		try {
			readTimeout = VcapProcessor.getHttpDefaultReadTimeoutMs();
		} catch (Exception e) {
			logger.error("Error reading default connection timeout from env");
		}

		sReadTimeoutMs = readTimeout > 0 ? readTimeout : DEFAULT_READ_TIMEOUT_MS;
		sConnectionTimeoutMs = connTimeout > 0 ? connTimeout : DEFAULT_CONNECTION_TIMEOUT_MS;
		logger.info("HTTPRequest default connection timeout: " + sConnectionTimeoutMs);
		logger.info("HTTPRequest default read timeout: " + sReadTimeoutMs);
	}

	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Trigger HTTP GET request.
	 *
	 * @param requestURL
	 * @param requestHeaders
	 * @param readTimeoutMs
	 * @return
	 * @throws Exception
	 */
	public static String executeGet(String requestURL, Map<String, String> requestHeaders, int readTimeoutMs) throws Exception {
		return executeRequest(requestURL, requestHeaders, null, "GET", readTimeoutMs);
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
		return executePut(requestURL, requestBody, requestHeaders, sReadTimeoutMs);
	}

	/**
	 * Trigger HTTP PUT request with a specified timeout.
	 *
	 * @param requestURL
	 * @param requestBody
	 * @param requestHeaders
	 * @param readTimeoutMs
	 * @return
	 * @throws Exception
	 */
	public static String executePut(String requestURL,
									String requestBody,
									Map<String, String> requestHeaders,
									int readTimeoutMs) throws Exception {
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

				if (readTimeoutMs == 0) {
					readTimeoutMs = sReadTimeoutMs;
				}
				connection.setConnectTimeout(sConnectionTimeoutMs);
				connection.setReadTimeout(readTimeoutMs);

				for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
					connection.addRequestProperty(entry.getKey(), entry.getValue());
				}

				// Set JSON as body of request
				OutputStream oStream = null;
				try {
					oStream = connection.getOutputStream();
					oStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
				} finally {
					if (oStream != null) {
						try {
							oStream.close();
						} catch (Exception ex) {
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
			String response = ResponseHandler.getResponse(connection);

			if (responseCode == 200 || responseCode == 201 || responseCode == 202 || responseCode == 204 || responseCode == 205 || responseCode == 206) {
				return response;

			} else {
				String msg;

				if (response.length() > 0) {
					msg = responseCode + "," + LogUtil.transformHtmlResponse(response);
					if (responseCode == 403) { // could be blocked by Cloudflare
						logCloudflareId(requestURL, connection);
					}
				} else {
					msg = responseCode + "," + connection.getResponseMessage();
				}

				throw new Exception(msg);
			}

		} catch (Exception ex) {
			throw ex;

		} finally {
			if (connection != null) {
				try {
					connection.disconnect();
				} catch (Exception ex) {
					logger.error("[executePut]::Exception closing connection, URL: " + requestURL);
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
		return executePost(requestURL, requestBody, requestHeaders, sReadTimeoutMs);
	}


	/**
	 * Trigger HTTP POST request, with timeout as input, currently being used by create order api.
	 * The create order api, in some cases, requires more than 30 seconds to return success response from MWG;
	 * therefore setting timeout = 40 seconds for create order api
	 *
	 * @param requestURL
	 * @param requestBody
	 * @param requestHeaders
	 * @param readTimeoutMs
	 * @return
	 * @throws Exception
	 */
	public static String executePost(String requestURL,
									 String requestBody,
									 Map<String, String> requestHeaders,
									 int readTimeoutMs) throws Exception {

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

				if (readTimeoutMs == 0) {
					readTimeoutMs = sReadTimeoutMs;
				}
				connection.setConnectTimeout(sConnectionTimeoutMs);
				connection.setReadTimeout(readTimeoutMs);

				for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
					connection.addRequestProperty(entry.getKey(), entry.getValue());
				}

				// Set JSON as body of request
				OutputStream oStream = null;
				try {
					oStream = connection.getOutputStream();
					oStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
				} finally {
					if (oStream != null) {
						try {
							oStream.close();
						} catch (Exception ex) {
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
			String response = ResponseHandler.getResponse(connection);

			if (responseCode == 200 || responseCode == 201 || responseCode == 202 || responseCode == 204 || responseCode == 205 || responseCode == 206) {
				return response;

			} else {
				String msg;

				if (response.length() > 0) {
					msg = responseCode + "," + LogUtil.transformHtmlResponse(response);
					if (responseCode == 403) { // could be blocked by Cloudflare
						logCloudflareId(requestURL, connection);
					}
				} else {
					msg = responseCode + "," + connection.getResponseMessage();
				}

				throw new Exception(msg);
			}

		} catch (MalformedURLException ex) {
			throw ex;

		} catch (IOException ex) {
			throw ex;

		} catch (Exception ex) {
			throw ex;

		} finally {
			if (connection != null) {
				try {
					connection.disconnect();
				} catch (Exception ex) {
					logger.error("[executePost]::Exception closing connection, path: " + requestURL);
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
	 * @param readTimeoutMs
	 * @return
	 * @throws Exception
	 */
	public static String executePostJSON(String requestURL,
										 String requestBody,
										 Map<String, String> requestHeaders,
										 int readTimeoutMs) throws Exception {
		HttpURLConnection connection = null;
		long startTime, endTime;

		BufferedReader br = null;

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

				if (readTimeoutMs == 0) {
					readTimeoutMs = sReadTimeoutMs;
				}
				connection.setConnectTimeout(sConnectionTimeoutMs);
				connection.setReadTimeout(readTimeoutMs);

				for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
					connection.addRequestProperty(entry.getKey(), entry.getValue());
				}

				// Set JSON as body of request
				OutputStream oStream = null;
				try {
					oStream = connection.getOutputStream();
					oStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
				} finally {
					if (oStream != null) {
						try {
							oStream.close();
						} catch (Exception ex) {
							logger.error("[HTTPRequest]::executePostJSON::Exception close stream: " + ex.getMessage());
							throw ex;
						}
					}
				}
			}

			// Connect to the server
			connection.connect();

			int status = connection.getResponseCode();
			br = new BufferedReader(new InputStreamReader(status < 400 ? connection.getInputStream() : connection.getErrorStream()));
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
					break;
				default:
					// sb.append(status);
					throw new Exception(connection.getResponseCode() + "," + connection.getResponseMessage());
			}

			endTime = System.currentTimeMillis();

			logger.trace("[executePostJSON]::Total process time: " + (endTime - startTime) + " ms, path: " + requestURL);

			// return body to auth
			return sb.toString();

		} catch (Exception ex) {
			throw ex;

		} finally {
			br.close();

			if (connection != null) {
				try {
					connection.disconnect();
				} catch (Exception ex) {
					logger.error("[executePostJSON]::Exception closing connection: " + ex.getMessage() + ", URL: " + requestURL);
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
	public static String executeRequest(String requestURL,
										Map<String, String> requestHeaders,
										Map<String, String> requestParameters,
										String requestMethod,
										int readTimeoutMs) throws Exception {
		HttpURLConnection connection = null;

		try {
			long startTime, endTime;

			startTime = System.currentTimeMillis();
			connection = createConnection(requestURL, requestHeaders, requestParameters, requestMethod, readTimeoutMs);
			int responseCode = connection.getResponseCode();
			endTime = System.currentTimeMillis();

			logger.trace("[executeRequest]::Total process time for " + requestMethod + ": " + (endTime - startTime) + " ms, URL: " + requestURL);

			String response = ResponseHandler.getResponse(connection);
			
			logger.debug("responseCode: " + responseCode + ", response data: " + response);

			if (responseCode == 200 || responseCode == 201 || responseCode == 204 || responseCode == 205 || responseCode == 206) {
				return response;

			} else {
				String msg;

				if (response.length() > 0) {
					msg = responseCode + "," + LogUtil.transformHtmlResponse(response);
					if (responseCode == 403) { // could be blocked by Cloudflare
						logCloudflareId(requestURL, connection);
					}
				} else {
					msg = responseCode + "," + connection.getResponseMessage();
				}


				throw new Exception(msg);
			}

		} catch (Exception e) {
			throw e;

		} finally {
			if (connection != null) {
				try {
					connection.disconnect();
				} catch (Exception ex) {
					logger.error(getErrorMsg("[executeRequest]::Exception: " + ex.getMessage(), requestURL));
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
	 * @return Response
	 * @throws Exception
	 */
	public static ResponseObj getResponse(String requestURL,
										  Map<String, String> requestHeaders,
										  Map<String, String> requestParameters,
										  String requestMethod,
										  int readTimeoutMs) throws Exception {
		HttpURLConnection connection = null;

		try {
			long startTime, endTime;

			startTime = System.currentTimeMillis();
			connection = createConnection(requestURL, requestHeaders, requestParameters, requestMethod, readTimeoutMs);
			int responseCode = connection.getResponseCode();
			endTime = System.currentTimeMillis();

			logger.trace("[executeRequest]::Total process time for " + requestMethod + ": " + (endTime - startTime) + " ms, URL: " + requestURL);

			ResponseObj response = ResponseObj.fromConnection(connection);

			for (Map.Entry<String, List<String>> entries : response.getResponseHeaders().entrySet()) {
				String values = "";
				for (String value : entries.getValue()) {
					values += value + ",";
				}
				logger.debug("Response header", entries.getKey() + " - " +  values );
			}

			logger.debug("responseCode: " + responseCode + ", response data: " + response.getResponseBody());

			if (responseCode == 200 || responseCode == 201 || responseCode == 204 || responseCode == 205 || responseCode == 206) {
				return response;

			} else {
				String msg;

				String responseBody = response.getResponseBody();;
				if (responseBody.length() > 0) {
					msg = responseCode + "," + LogUtil.transformHtmlResponse(responseBody);
					if (responseCode == 403) { // could be blocked by Cloudflare
						logCloudflareId(requestURL, connection);
					}
				} else {
					msg = responseCode + "," + connection.getResponseMessage();
				}


				throw new Exception(msg);
			}

		} catch (Exception e) {
			throw e;

		} finally {
			if (connection != null) {
				try {
					connection.disconnect();
				} catch (Exception ex) {
					logger.error(getErrorMsg("[executeRequest]::Exception: " + ex.getMessage(), requestURL));
					throw ex;
				}
			}
		}
	}

	/**
	 * Execute a DELETE http request.
	 *
	 * @param requestURL
	 * @param requestHeaders
	 * @param readTimeoutMs
	 * @return
	 * @throws Exception
	 */
	public static String executeDelete(String requestURL,
									   Map<String, String> requestHeaders,
									   int readTimeoutMs) throws Exception {
		HttpURLConnection connection = null;
		long startTime, endTime;

		try {
			startTime = System.currentTimeMillis();

			// Create connection
			URL url = new URL(requestURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");

			if (readTimeoutMs == 0) {
				readTimeoutMs = sReadTimeoutMs;
			}
			connection.setConnectTimeout(sConnectionTimeoutMs);
			connection.setReadTimeout(readTimeoutMs);

			for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
				connection.addRequestProperty(entry.getKey(), entry.getValue());
			}

			// Connect to the server
			connection.connect();

			int status = connection.getResponseCode();
			endTime = System.currentTimeMillis();

			logger.trace("[executeDelete]::Total process time: " + (endTime - startTime) + " ms, URL: " +
					requestURL + ", response code: " + connection.getResponseCode() + ", msg: " + connection.getResponseMessage());

			switch (status) {
				case 200:
				case 201:
				case 202:
				case 204:
					return status + " Success";
				case 205:
					return status + " - " + connection.getResponseMessage();
				default:
					throw new Exception(connection.getResponseCode() + "," + connection.getResponseMessage());
			}

		} catch (MalformedURLException ex) {
			throw ex;

		} catch (IOException ex) {
			throw ex;

		} catch (Exception ex) {
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
	 * @param readTimeoutMs
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private static HttpURLConnection createConnection(
			String requestURL,
			Map<String, String> requestHeaders,
			Map<String, String> requestParameters,
			String requestMethod,
			int readTimeoutMs) throws IOException, URISyntaxException {

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

		if (readTimeoutMs == 0) {
			readTimeoutMs = sReadTimeoutMs;
		}
		connection.setConnectTimeout(sConnectionTimeoutMs);
		connection.setReadTimeout(readTimeoutMs);

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

		return new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(), newQuery, oldUri.getFragment());
	}
	
	/**
	 * Log a Cloudflare ID if an API call is blocked by Cloudflare
	 * @param connection
	 * @return
	 */
	private static void logCloudflareId(String requestURL, HttpURLConnection connection) {
		
		try {
			String cloudFlareId = connection.getHeaderField("CF-RAY");
			
			if ((cloudFlareId != null) && (cloudFlareId.trim().length() > 0)) {
				logger.error("CloudFlare Ray ID of " + cloudFlareId + " has been created for " + requestURL);
			} 
			
		} catch (Exception e) { // if an exception is ever thrown here, no logging needed
		}

	}
}
