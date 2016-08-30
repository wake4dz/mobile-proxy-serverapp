package com.wakefern.global;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * Created by zacpuste on 8/30/16.
 */
public class XMLtoJSONConverter {

    public String convert(String xml){
        try{
            JSONObject converted = XML.toJSONObject(xml);
            return converted.toString(ApplicationConstants.xmlTabAmount);
        } catch (JSONException ex){
            //error
            return xml;
        }
    }
}
