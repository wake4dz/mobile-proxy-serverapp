package com.wakefern.Wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ErrorHandling.ResponseHandler;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.wakefern.request.HTTPRequest.appendUri;

/**
 * Created by zacpuste on 10/18/16.
 */
@Path(ApplicationConstants.Requests.Wakefern.ItemLocatorAuth)
public class WakefernAuth extends BaseService {
    @GET
    @Produces("text/plain")
    public Response getInfoResponse(@HeaderParam("Authorization") String authToken) throws Exception, IOException {
        Map<String, String> wkfn = new HashMap<>();

        String path = "https://api.wakefern.com" + ApplicationConstants.Requests.Wakefern.ItemLocatorAuth;
        wkfn.put(ApplicationConstants.Requests.Header.contentType, "text/plain");
        wkfn.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
            return this.createValidResponse(executeGet(path, wkfn));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }
    
    public String getInfo(@HeaderParam("Authorization") String authToken) throws Exception, IOException {
        Map<String, String> wkfn = new HashMap<>();

        String path = "https://api.wakefern.com" + ApplicationConstants.Requests.Wakefern.ItemLocatorAuth;
        wkfn.put(ApplicationConstants.Requests.Header.contentType, "text/plain");
        wkfn.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
            return executeGet(path, wkfn);
        } catch (Exception e){
            return null;
        }
    }

    public WakefernAuth(){
        this.serviceType = new MWGHeader();
    }

    public static String executeGet(String requestURL, Map<String, String> requestHeaders) throws Exception{
        return executeRequest(requestURL,requestHeaders,null,"GET");
    }

    public static String executeRequest(String requestURL,Map<String,String> requestHeaders,Map<String,String>
            requestParameters,String requestMethod) throws Exception{
        HttpURLConnection connection = null;
        try {
            connection = createConnection(requestURL,requestHeaders,requestParameters,requestMethod);
            int responseCode = connection.getResponseCode();
            if(responseCode == 200 || responseCode == 201 || responseCode == 204 || responseCode == 205 || responseCode == 206){
                return buildResponse(connection);
            } else {
                throw new Exception(responseCode + "," + connection.getResponseMessage());
            }
        } catch (IOException e) {
            throw e;
        } catch (URISyntaxException e) {
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    throw ex;
                }
            }
        }
    }

    private static HttpURLConnection createConnection(String requestURL,Map<String,String> requestHeaders,Map<String,String> requestParameters,String requestMethod) throws IOException, URISyntaxException {
        HttpURLConnection connection = null;
        URI uri = new URI(requestURL);
        if(requestParameters!=null) {
            for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
                uri = appendUri(uri.toString(), entry.getKey() + "=" + entry.getValue());
            }
        }

        URL url = uri.toURL();
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setConnectTimeout(10000);


        if(requestHeaders != null) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        //Connect to the server
        connection.connect();

        return connection;
    }

    private static String buildResponse(HttpURLConnection connection){
        return ResponseHandler.Response(connection);
    }
}
