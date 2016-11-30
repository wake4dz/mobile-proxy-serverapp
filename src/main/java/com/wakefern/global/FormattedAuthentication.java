package com.wakefern.global;

import com.wakefern.Planning.StoreDetails;
import com.wakefern.Planning.StoreLocator;
import com.wakefern.Recipes.GetProfile;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zacpuste on 10/5/16.
 */
public class FormattedAuthentication {
    private String pseudoStore;
    private String storeName;
    private String externalId;
    private Boolean fromHome;
    private String sisterId;
    private String sisterPseudo;
    private String sisterName;

    public JSONObject formatAuth(String json, String email, String chainId, String planningAuth, String v5){
        JSONObject retval = new JSONObject();
        JSONObject jsonObject = new JSONObject(json);
        String token = jsonObject.getString(ApplicationConstants.FormattedAuthentication.Token);
        String userId = jsonObject.getString(ApplicationConstants.FormattedAuthentication.UserId);

        //Add /v5/authorization/authenticate
        retval.put(ApplicationConstants.FormattedAuthentication.UserToken, token);
        retval.put(ApplicationConstants.FormattedAuthentication.PlanningToken, planningAuth);
        retval.put(ApplicationConstants.FormattedAuthentication.UserId, userId);

        GetProfile getProfile = new GetProfile();
        try {
            //Get user info
            String xml = getProfile.getInfo(userId, chainId, email, planningAuth);
            XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
            JSONObject xmlJson = new JSONObject(xmLtoJSONConverter.convert(xml)).getJSONObject(ApplicationConstants.FormattedAuthentication.User);

            String storeId = (xmlJson.getJSONObject(ApplicationConstants.FormattedAuthentication.PreferredStore)
                    .get(ApplicationConstants.FormattedAuthentication.Id)).toString();
            String ppc = xmlJson.get(ApplicationConstants.FormattedAuthentication.FSN).toString();

            //Save user info
            retval.put(ApplicationConstants.FormattedAuthentication.FirstName, xmlJson.getString(ApplicationConstants.FormattedAuthentication.FirstName));
            retval.put(ApplicationConstants.FormattedAuthentication.LastName, xmlJson.getString(ApplicationConstants.FormattedAuthentication.LastName));
            retval.put(ApplicationConstants.FormattedAuthentication.PPC, ppc);

            //Get /v5/authorization and find AdSessionGuid
            JSONObject jsonObject1 = new JSONObject(v5);
            for(Object v5Obj: jsonObject1.keySet()){
                String keyStr = (String)v5Obj;
                if(keyStr.equals(ApplicationConstants.FormattedAuthentication.AdSessionGuid)) {
                    Object keyvalue = jsonObject1.get(keyStr);
                    retval.put(keyStr, keyvalue);
                }
            }

            //Get store info
            StoreDetails storeDetails = new StoreDetails();
            String storesJson = storeDetails.getInfo(chainId, storeId, planningAuth);
            searchStores(storesJson, chainId, planningAuth);

            //Save store info
            retval.put(ApplicationConstants.FormattedAuthentication.StoreId, storeId);
            retval.put(ApplicationConstants.FormattedAuthentication.PseudoStoreId, getPseudoStore());
            retval.put(ApplicationConstants.FormattedAuthentication.StoreName, getStoreName());
            retval.put(ApplicationConstants.FormattedAuthentication.ExternalStoreId, getExternalId());
            retval.put(ApplicationConstants.FormattedAuthentication.ShopriteFromHome, getFromHome());

            if(getSisterId() != null) {
                retval.put(ApplicationConstants.FormattedAuthentication.SisterStoreId, getSisterId());
                retval.put(ApplicationConstants.FormattedAuthentication.SisterStoreName, getSisterName());
                retval.put(ApplicationConstants.FormattedAuthentication.SisterStorePseudoStoreId, getSisterPseudo());
            }
        } catch (Exception e){
            ExceptionHandler exceptionHandler = new ExceptionHandler();
            //System.out.print(exceptionHandler.exceptionMessageJson(e));
        }
        return retval;
    }
    //

    private void searchStores(String stores, String chainId, String planningAuth){
        JSONObject jsonObject = new JSONObject(stores).getJSONObject(ApplicationConstants.FormattedAuthentication.Store);
        try{
            setExternalId(jsonObject.get(ApplicationConstants.FormattedAuthentication.ExternalStoreId).toString());
            setPseudoStore(jsonObject.getString(ApplicationConstants.FormattedAuthentication.PseudoStoreId));
        }catch(Exception e){
        	//System.out.println("Into int ::" + e.getMessage());
        	Long aLong =  jsonObject.getLong(ApplicationConstants.FormattedAuthentication.PseudoStoreId);
        	//System.out.println("Int value :: " + aLong.toString());
            setPseudoStore(aLong.toString());
        }
        Object store = jsonObject.getJSONObject(ApplicationConstants.FormattedAuthentication.Sections)
                .getJSONArray(ApplicationConstants.FormattedAuthentication.Section).get(0);
        JSONObject currentStore = (JSONObject) store;
        setStoreName(currentStore.getString(ApplicationConstants.FormattedAuthentication.Name));

        Object services = jsonObject.getJSONObject(ApplicationConstants.FormattedAuthentication.MwgServices)
                .get(ApplicationConstants.FormattedAuthentication.MyWebGrocerService);
        JSONArray jsonArray = (JSONArray) services;

        //Initialize from home to be false
        setFromHome(false);
        setSisterId(null);
        
        for(Object service: jsonArray){
            JSONObject currentService = (JSONObject) service;
            String serviceName = currentService.get(ApplicationConstants.FormattedAuthentication.ServiceName).toString();
            if(serviceName.equals(ApplicationConstants.FormattedAuthentication.Shop2GroPickup)
                    || serviceName.equals(ApplicationConstants.FormattedAuthentication.Shop2GroDelivery)){
                setFromHome(true);
            }
            else if(serviceName.equals(ApplicationConstants.FormattedAuthentication.S2GSisterStore)){
                String uri = currentService.get(ApplicationConstants.FormattedAuthentication.Uri).toString();
                String[] query = uri.split("strid=");
                String id = query[1].substring(4, query[1].length());
                try {
                    StoreDetails storeDetails = new StoreDetails();
                    String storesJson = storeDetails.getInfo(chainId, id, planningAuth);

                    JSONObject jsonObject1 = new JSONObject(storesJson).getJSONObject(ApplicationConstants.FormattedAuthentication.Store);
                    setSisterPseudo(jsonObject1.get(ApplicationConstants.FormattedAuthentication.PseudoStoreId).toString());
                    setSisterId(jsonObject1.get(ApplicationConstants.FormattedAuthentication.StoreId).toString());

                    Object store1 = jsonObject1.getJSONObject(ApplicationConstants.FormattedAuthentication.Sections)
                            .getJSONArray(ApplicationConstants.FormattedAuthentication.Section).get(0);
                    JSONObject currentStore1 = (JSONObject) store1;
                    setSisterName(currentStore1.getString(ApplicationConstants.FormattedAuthentication.Name));
                } catch (Exception e) {
                    //System.out.print("Error:: " + e.getMessage());
                }
            }
        }
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

    public String getExternalId() {
        return externalId;
    }
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Boolean getFromHome() {
        return fromHome;
    }
    public void setFromHome(Boolean fromHome) {
        this.fromHome = fromHome;
    }

    public String getSisterId() {
        return sisterId;
    }
    public void setSisterId(String sisterId) {
        this.sisterId = sisterId;
    }

    public String getSisterPseudo() {
        return sisterPseudo;
    }
    public void setSisterPseudo(String sisterPseudo) {
        this.sisterPseudo = sisterPseudo;
    }

    public String getSisterName() {
        return sisterName;
    }
    public void setSisterName(String sisterName) {
        this.sisterName = sisterName;
    }
}
