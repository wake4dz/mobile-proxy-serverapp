package com.wakefern.global;

import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by zacpuste on 9/16/16.
 */
public class Search extends BaseService {

    public String search(String partialUrl, String take, String skip, String fq, String sort, String isMember, String authToken) throws Exception {
        this.requestToken = authToken;

        int intTake = Integer.parseInt(take);
        int initSkip = Integer.parseInt(skip);
        int finalLoop = intTake % 20;
        intTake -= finalLoop;
        intTake /= 20;
        try {
            if(!fq.isEmpty()){// != "") {
                fq = URLEncoder.encode(fq, "UTF-8");
            }
            if(!sort.isEmpty()){// != ""){
                sort = URLEncoder.encode(sort, "UTF-8");
            }
        } catch (Exception e){

        }

        String[] jsonArray = new String[intTake + 1];
        for(int i = 0; i < intTake; i++){
            this.requestPath = partialUrl + ApplicationConstants.StringConstants.takeAmp
                    + ApplicationConstants.StringConstants.twenty + ApplicationConstants.StringConstants.skip
                    + String.valueOf((20 * i) + initSkip );

            if(!sort.isEmpty()){// != ""){
                this.requestPath = this.requestPath + ApplicationConstants.StringConstants.sort + sort;
            }

            if(!fq.isEmpty()){// != ""){
                this.requestPath = this.requestPath + ApplicationConstants.StringConstants.fq + fq;
            }

            if(!isMember.isEmpty()){
                this.requestPath += ApplicationConstants.StringConstants.isMemberAmp;
            }

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setGetMapping(this, null, null);

            String result = HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
            jsonArray[i] = result;
        }

        if(finalLoop != 0) {
            this.requestPath = partialUrl + ApplicationConstants.StringConstants.takeAmp
                    + Integer.toString(finalLoop) + ApplicationConstants.StringConstants.skip
                    + String.valueOf((20 * intTake) + initSkip);

            if(!sort.isEmpty()){// != ""){
                this.requestPath = this.requestPath + ApplicationConstants.StringConstants.sort + sort;
            }

            if(!fq.isEmpty()){// != ""){
                this.requestPath = this.requestPath + ApplicationConstants.StringConstants.fq + fq;
            }

            if(!isMember.isEmpty()){
                this.requestPath += ApplicationConstants.StringConstants.isMemberAmp;
            }

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setGetMapping(this, null, null);

            String result = HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
            jsonArray[intTake] = result;
        }
        return Arrays.deepToString(jsonArray);
    }

    public Search(){
        this.requestHeader = new MWGHeader();
    }
}
