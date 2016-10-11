package com.wakefern.global;

import java.io.IOException;

import com.wakefern.Planning.StoreDetails;
import com.wakefern.Recipes.GetProfile;
import com.wakefern.global.ErrorHandling.ExceptionHandler;

import org.json.JSONObject;

/**
 * Created by zacpuste on 10/5/16.
 */
public class FormattedAuthentication {
    private String pseudoStore;
    private String storeName;

    public JSONObject formatAuth(String json, String email, String chainId, String planningAuth, String v5) throws IOException, Exception{
        JSONObject retval = new JSONObject();
        JSONObject jsonObject = new JSONObject(json);
        String token = jsonObject.getString(ApplicationConstants.FormattedAuthentication.Token);
        String userId = jsonObject.getString(ApplicationConstants.FormattedAuthentication.UserId);

        retval.put(ApplicationConstants.FormattedAuthentication.UserToken, token);
        retval.put(ApplicationConstants.FormattedAuthentication.PlanningToken, planningAuth);
        retval.put(ApplicationConstants.FormattedAuthentication.UserId, userId);

        GetProfile getProfile = new GetProfile();
 
            String xml = getProfile.getInfo(userId, chainId, email, planningAuth);

            XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
            JSONObject xmlJson = new JSONObject(xmLtoJSONConverter.convert(xml)).getJSONObject(ApplicationConstants.FormattedAuthentication.User);

            String storeId = Integer.toString(xmlJson.getJSONObject(ApplicationConstants.FormattedAuthentication.PreferredStore)
                    .getInt(ApplicationConstants.FormattedAuthentication.Id));
            String ppc = Integer.toString(xmlJson.getInt(ApplicationConstants.FormattedAuthentication.FSN));

            retval.put(ApplicationConstants.FormattedAuthentication.FirstName, xmlJson.getString(ApplicationConstants.FormattedAuthentication.FirstName));
            retval.put(ApplicationConstants.FormattedAuthentication.LastName, xmlJson.getString(ApplicationConstants.FormattedAuthentication.LastName));
            retval.put(ApplicationConstants.FormattedAuthentication.StoreId, storeId);
            retval.put(ApplicationConstants.FormattedAuthentication.PPC, ppc);

            StoreDetails storeDetails = new StoreDetails();
            String storesJson = storeDetails.getInfo(chainId, storeId, planningAuth);
            searchStores(storesJson);

            retval.put(ApplicationConstants.FormattedAuthentication.PseudoStoreId, getPseudoStore());
            retval.put(ApplicationConstants.FormattedAuthentication.StoreName, getStoreName());
            JSONObject jsonObject1 = new JSONObject(v5);
            for(Object v5Obj: jsonObject1.keySet()){
                String keyStr = (String)v5Obj;
                Object keyvalue = jsonObject1.get(keyStr);
                retval.put(keyStr, keyvalue);
            }
        
        return retval;
    }

    private void searchStores(String stores){
        JSONObject jsonObject = new JSONObject(stores).getJSONObject(ApplicationConstants.FormattedAuthentication.Store);
        setPseudoStore(jsonObject.getString(ApplicationConstants.FormattedAuthentication.PseudoStoreId));
        Object store = jsonObject.getJSONObject(ApplicationConstants.FormattedAuthentication.Sections)
                .getJSONArray(ApplicationConstants.FormattedAuthentication.Section).get(0);
        JSONObject currentStore = (JSONObject) store;
        setStoreName(currentStore.getString(ApplicationConstants.FormattedAuthentication.Name));
    }

    public String getPseudoStore() {
        return pseudoStore;
    }
    public void setPseudoStore(String pseudoStore) {
        this.pseudoStore = pseudoStore;
    }

    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
