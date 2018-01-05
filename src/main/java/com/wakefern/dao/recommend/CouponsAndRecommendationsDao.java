
package com.wakefern.dao.recommend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.wakefern.dao.coupon.CouponDAO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Recommendations",
    "CouponMetadata"
})
public class CouponsAndRecommendationsDao {

    @JsonProperty("Recommendations")
    private RecommendProduct recommendProducts = null;
    
    @JsonProperty("CouponMetadata")
//    private List<CouponDAO> couponDaoList = null;
    private CouponDAO[] couponDaoArr = null;


    @JsonProperty("Recommendations")
    public RecommendProduct getActiveFilters() {
        return recommendProducts;
    }

    @JsonProperty("Recommendations")
    public void setRecommendProducts(RecommendProduct recommendProducts) {
        this.recommendProducts = recommendProducts;
    }

    @JsonProperty("CouponMetadata")
    public CouponDAO[] getRecentFilters() {
        return couponDaoArr;
    }

    @JsonProperty("CouponMetadata")
    public void setCouponDaoArr(CouponDAO[] couponDaoArr) {
        this.couponDaoArr = couponDaoArr;
    }
}
