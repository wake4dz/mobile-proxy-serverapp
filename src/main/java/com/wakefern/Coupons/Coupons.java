package com.wakefern.Coupons;

import com.wakefern.Wakefern.Models.WakefernHeader;
import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */

@Path(ApplicationConstants.Requests.Coupons.GetCoupons)
public class Coupons extends BaseService {

    @GET
    @Produces("application/*")
    public String getInfo(@PathParam(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC) String ppcParam){
        this.path = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.GetCoupons;
        if(!ApplicationUtils.isEmpty(ppcParam)){
            if(ppcParam.equalsIgnoreCase(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC_All)){
                //Get all metadata

            }else{
                //Create query with hash on the necessary tokens to be displayed
            }
        }else{
            //Throw error as the PPC # / parameter should not be empty
        }
        return "";
    }

    public Coupons () {this.serviceType = new WakefernHeader();}
}
