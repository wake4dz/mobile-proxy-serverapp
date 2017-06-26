package com.wakefern.global;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * Created by zacpuste on 8/30/16.
 */
public class XMLtoJSONConverter {

	private final static Logger logger = Logger.getLogger("XMLtoJSONConverter");
	
    public String convert(String xml){
        try{
            JSONObject converted = XML.toJSONObject(xml);
            return converted.toString(ApplicationConstants.xmlTabAmount);
        } catch (JSONException ex){
            //error
            return xml;
        }
    }

    /**
     * Fix issue of store getting sister store detail in object vs array
     * SR app expected array, but received obj and thus no sister store displayed
     * This fix will check if data is object, then converts to array and returns an array
     * (SR of Kingston (12401) returned Obj vs SR of Edison (08817) returned Array (Section))
     * @param xml
     * @return
     */
    public String convertToJsonStr(String xml){
    	JSONArray sectionArr;
        try{
            JSONObject converted = XML.toJSONObject(xml);
            JSONObject store = (JSONObject) converted.get(ApplicationConstants.StoreInfo.Store);
            JSONObject sections = (JSONObject) store.get(ApplicationConstants.StoreInfo.Sections);
            Object section = sections.get(ApplicationConstants.StoreInfo.Section);
            if(section instanceof JSONObject){
            	logger.log(Level.INFO, "[convertToJsonStr]::Sister store is Object, perform array conversion.");
            	sectionArr = new JSONArray();
            	sectionArr.put(section);
            	sections.remove(ApplicationConstants.StoreInfo.Section);
            	sections.put(ApplicationConstants.StoreInfo.Section, sectionArr);
            }
            return converted.toString(ApplicationConstants.xmlTabAmount);
        } catch (JSONException ex){
        	logger.log(Level.SEVERE, "[convertToJsonStr]::JSONException", ex);
        	return this.convert(xml);
        }
    }
}
