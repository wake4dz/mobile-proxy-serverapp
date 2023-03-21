package com.wakefern.api.proxy.wakefern.coupon.v3;

import com.wakefern.global.EnvManager;
import com.wakefern.wakefern.WakefernApplicationConstants;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class CouponUtils {

    // Base Coupon URL
    public static final String BaseCouponURL = EnvManager.getTargetCouponV3ServiceEndpoint();

    /**
     * Construct an upstream coupons url given path and parameters
     * @param path
     * @return
     */
    public static String constructCouponV3Url(String path) {
        return constructCouponV3Url(path, null, null);
    }

    /**
     * Construct an upstream coupons url given path and parameters
     * @param path
     * @param params
     * @return
     */
    public static String constructCouponV3Url(String path, Map<String, String> pathParams, Map<String, String> queryParams) {
        if (pathParams == null) {
           pathParams = new HashMap<>();
        }
        // Always append banner information as a path param
        pathParams.put(WakefernApplicationConstants.CouponsV3.PathInfo.banner,
                Requests.Params.banner);
        String url = BaseCouponURL + path;
        UriBuilder builder = UriBuilder.fromPath(url);
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        URI output = builder.buildFromMap(pathParams);
        return output.toASCIIString();
    }

    public static class Requests {
        public static class Routes {
            public static final String UserLogin = "/coupons/v3/login";
            public static final String CouponList = "/coupons/v3/list";
            public static final String AvailableCoupons = "/coupons/v3/available";
            public static final String ExpiredCoupons = "/coupons/v3/expired";
            public static final String ClippedCoupons = "/coupons/v3/clipped";
            public static final String RedeemedCoupons = "/coupons/v3/redeemed";
            public static final String GetCouponByPromoCode = "/coupons/v3/promocode/{promoCode}";
            public static final String AddCouponToPPC = "/coupons/v3/clip";
            public static final String RemoveCouponFromPPC = "/coupons/v3/unclip";
            public static final String GetCouponByUPC = "/coupons/v3/upc/{upc}";
            public static final String GetUPCsByCouponId = "/coupons/v3/upcList/{couponId}";
            public static final String GetCouponByExternalCouponIds = "/coupons/v3/couponIds/{externalCouponIds}";
            public static final String GetCouponsByStoreId = "/coupons/v3/storeId/{storeId}";
            public static final String GetSocialOffersByQuery = "/coupons/v3/socialoffers/{query}";
        }

        public static class Params {
            public static final String banner = "shoprite";
            public static final String couponId = WakefernApplicationConstants.CouponsV3.PathInfo.couponId;
            public static final String couponIds = WakefernApplicationConstants.CouponsV3.PathInfo.couponIds;
            public static final String externalCouponIds = "externalCouponIds";
            public static final String upc = WakefernApplicationConstants.CouponsV3.PathInfo.upc;
            public static final String storeId = "storeId";
        }
    }
}
