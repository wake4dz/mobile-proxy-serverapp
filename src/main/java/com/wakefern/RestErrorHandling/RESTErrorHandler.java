package com.wakefern.RestErrorHandling;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class RESTErrorHandler {

    private String validResponse;
    private Integer errorCode;

    public void RESTHandlerResponse(String json){
        if(json.contains("Server returned HTTP response code")){
            Pattern p = Pattern.compile("code: ([0-9]+) for URL");
            Matcher m = p.matcher(json);
            if(m.find()) {
                this.setErrorCode(Integer.parseInt(m.group(1)));
            } else {
                this.setErrorCode(-1);
            }
        } else{
            this.setValidResponse(json);
        }
    }

    public String getErrorMessage(Object errorObj){
        Integer errorCode = (Integer) errorObj;
        errorCode = Math.abs(errorCode);
        String returnMessage;
        switch (errorCode){
            case 401:
                returnMessage = "Bad authorization token";
                break;
            default:
                returnMessage = "Error has occurred";
                break;
        }
        return returnMessage;
    }

    public String createJsonObject(Object errorObj, String errorMessage){
        Integer errorCode = (Integer) errorObj;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Status", Math.abs(errorCode));
        jsonObject.put("Message", errorMessage);
        return jsonObject.toString();
    }

    public String getValidResponse() {
        return validResponse;
    }

    public void setValidResponse(String validResponse) {
        this.validResponse = validResponse;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
