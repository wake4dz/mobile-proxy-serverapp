package com.wakefern.global;

import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by zacpuste on 9/16/16.
 */
public class Search extends BaseService {

    public String search(String partialUrl, String take, String skip, String fq, String sort, String authToken) throws Exception {
        this.token = authToken;

        int intTake = Integer.parseInt(take);
        int initSkip = Integer.parseInt(skip);
        int finalLoop = intTake % 20;
        intTake -= finalLoop;
        intTake /= 20;
        try {
            if(fq != "") {
                fq = URLEncoder.encode(fq, "UTF-8");
            }
            if(sort != ""){
                sort = URLEncoder.encode(sort, "UTF-8");
            }
        } catch (Exception e){

        }

        String[] jsonArray = new String[intTake + 1];
        for(int i = 0; i < intTake; i++){
            this.path = partialUrl + ApplicationConstants.StringConstants.takeAmp
                    + ApplicationConstants.StringConstants.twenty + ApplicationConstants.StringConstants.skip
                    + String.valueOf((20 * i) + initSkip );

            if(sort != ""){
                this.path = this.path + ApplicationConstants.StringConstants.sort + sort;
            }

            if(fq != ""){
                this.path = this.path + ApplicationConstants.StringConstants.fq + fq;
            }

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setMapping(this);

            String result = HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader());
            jsonArray[i] = result;
        }

        if(finalLoop != 0) {
            this.path = partialUrl + ApplicationConstants.StringConstants.takeAmp
                    + Integer.toString(finalLoop) + ApplicationConstants.StringConstants.skip
                    + String.valueOf((20 * intTake) + initSkip);

            if(sort != ""){
                this.path = this.path + ApplicationConstants.StringConstants.sort + sort;
            }

            if(fq != ""){
                this.path = this.path + ApplicationConstants.StringConstants.fq + fq;
            }

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setMapping(this);

            String result = HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader());
            jsonArray[intTake] = result;
        }
        return Arrays.deepToString(jsonArray);
    }

    public Search(){
        this.serviceType = new MWGHeader();
    }
}
