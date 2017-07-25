
package com.wakefern.dao.coupon;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonFilter("filterByValue")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",  //commented out
    "__createdAt",  //commented out
    "__updatedAt",  //commented out
    "__version",  //commented out
    "__deleted",  //commented out
    "coupon_id",
    "featured",
    "requirement_description",
    "brand_name",
    "reward_upcs",
    "short_description",
    "image_url",
    "value",	//commented out
    "total_downloads",  //commented out
    "targeting_buckets",  //commented out
    "targeted_offer",  //commented out
    "tags",  //commented out
    "enabled",  //commented out
    "external_id",
    "pos_live_date",
    "display_start_date",
    "display_end_date",
    "offer_type", //commented out
    "Category",
    "publication_id",	//commented out
    "long_description",	//commented out
    "requirement_upcs",
    "subcategory",	//commented out
    "offer_priority",	//commented out
    "expiration_date",
    
    "long_description_header", //commented out
    "coupon_value"
})
public class CouponDAO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("__createdAt")
    private String createdAt;
    @JsonProperty("__updatedAt")
    private String updatedAt;
    @JsonProperty("__version")
    private Version version;
    @JsonProperty("__deleted")
    private Boolean deleted;
    @JsonProperty("coupon_id")
    private String couponId;
    @JsonProperty("featured")
    private String featured;
    @JsonProperty("requirement_description")
    private String requirementDescription;
    @JsonProperty("brand_name")
    private String brandName;
    @JsonProperty("reward_upcs")
    private String rewardUpcs;
    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("value")
    private String value;
    @JsonProperty("total_downloads")
    private String totalDownloads;
    @JsonProperty("targeting_buckets")
    private String targetingBuckets;
    @JsonProperty("targeted_offer")
    private String targetedOffer;
    @JsonProperty("tags")
    private String tags;
    @JsonProperty("enabled")
    private String enabled;
    @JsonProperty("external_id")
    private String externalId;
    @JsonProperty("pos_live_date")
    private String posLiveDate;
    @JsonProperty("display_start_date")
    private String displayStartDate;
    @JsonProperty("display_end_date")
    private String displayEndDate;
    @JsonProperty("offer_type")
    private String offerType;
    @JsonProperty("Category")
    private String category;
    @JsonProperty("publication_id")
    private String publicationId;
    @JsonProperty("long_description")
    private String longDescription;
    @JsonProperty("requirement_upcs")
    private String requirementUpcs;
    @JsonProperty("subcategory")
    private String subcategory;
    @JsonProperty("offer_priority")
    private String offerPriority;
    @JsonProperty("expiration_date")
    private String expirationDate;
    @JsonProperty("long_description_header")
    private String longDescriptionHeader;
    @JsonProperty("coupon_value")
    private String couponValue = "";
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("__createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("__createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("__updatedAt")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("__updatedAt")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("__version")
    public Version getVersion() {
        return version;
    }

    @JsonProperty("__version")
    public void setVersion(Version version) {
        this.version = version;
    }

    @JsonProperty("__deleted")
    public Boolean getDeleted() {
        return deleted;
    }

    @JsonProperty("__deleted")
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @JsonProperty("coupon_id")
    public String getCouponId() {
        return couponId;
    }

    @JsonProperty("coupon_id")
    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    @JsonProperty("featured")
    public String getFeatured() {
        return featured;
    }

    @JsonProperty("featured")
    public void setFeatured(String featured) {
        this.featured = featured;
    }

    @JsonProperty("requirement_description")
    public String getRequirementDescription() {
        return requirementDescription;
    }

    @JsonProperty("requirement_description")
    public void setRequirementDescription(String requirementDescription) {
        this.requirementDescription = requirementDescription;
    }

    @JsonProperty("brand_name")
    public String getBrandName() {
        return brandName;
    }

    @JsonProperty("brand_name")
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @JsonProperty("reward_upcs")
    public String getRewardUpcs() {
        return rewardUpcs;
    }

    @JsonProperty("reward_upcs")
    public void setRewardUpcs(String rewardUpcs) {
        this.rewardUpcs = rewardUpcs;
    }

    @JsonProperty("short_description")
    public String getShortDescription() {
        return shortDescription;
    }

    @JsonProperty("short_description")
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("total_downloads")
    public String getTotalDownloads() {
        return totalDownloads;
    }

    @JsonProperty("total_downloads")
    public void setTotalDownloads(String totalDownloads) {
        this.totalDownloads = totalDownloads;
    }

    @JsonProperty("targeting_buckets")
    public String getTargetingBuckets() {
        return targetingBuckets;
    }

    @JsonProperty("targeting_buckets")
    public void setTargetingBuckets(String targetingBuckets) {
        this.targetingBuckets = targetingBuckets;
    }

    @JsonProperty("targeted_offer")
    public String getTargetedOffer() {
        return targetedOffer;
    }

    @JsonProperty("targeted_offer")
    public void setTargetedOffer(String targetedOffer) {
        this.targetedOffer = targetedOffer;
    }

    @JsonProperty("tags")
    public String getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(String tags) {
        this.tags = tags;
    }

    @JsonProperty("enabled")
    public String getEnabled() {
        return enabled;
    }

    @JsonProperty("enabled")
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @JsonProperty("external_id")
    public String getExternalId() {
        return externalId;
    }

    @JsonProperty("external_id")
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @JsonProperty("pos_live_date")
    public String getPosLiveDate() {
        return posLiveDate;
    }

    @JsonProperty("pos_live_date")
    public void setPosLiveDate(String posLiveDate) {
        this.posLiveDate = posLiveDate;
    }

    @JsonProperty("display_start_date")
    public String getDisplayStartDate() {
        return displayStartDate;
    }

    @JsonProperty("display_start_date")
    public void setDisplayStartDate(String displayStartDate) {
        this.displayStartDate = displayStartDate;
    }

    @JsonProperty("display_end_date")
    public String getDisplayEndDate() {
        return displayEndDate;
    }

    @JsonProperty("display_end_date")
    public void setDisplayEndDate(String displayEndDate) {
        this.displayEndDate = displayEndDate;
    }

    @JsonProperty("offer_type")
    public String getOfferType() {
        return offerType;
    }

    @JsonProperty("offer_type")
    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    @JsonProperty("Category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("Category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("publication_id")
    public String getPublicationId() {
        return publicationId;
    }

    @JsonProperty("publication_id")
    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    @JsonProperty("long_description")
    public String getLongDescription() {
        return longDescription;
    }

    @JsonProperty("long_description")
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @JsonProperty("requirement_upcs")
    public String getRequirementUpcs() {
        return requirementUpcs;
    }

    @JsonProperty("requirement_upcs")
    public void setRequirementUpcs(String requirementUpcs) {
        this.requirementUpcs = requirementUpcs;
    }

    @JsonProperty("subcategory")
    public String getSubcategory() {
        return subcategory;
    }

    @JsonProperty("subcategory")
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @JsonProperty("offer_priority")
    public String getOfferPriority() {
        return offerPriority;
    }

    @JsonProperty("offer_priority")
    public void setOfferPriority(String offerPriority) {
        this.offerPriority = offerPriority;
    }

    @JsonProperty("expiration_date")
    public String getExpirationDate() {
        return expirationDate;
    }

    @JsonProperty("expiration_date")
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @JsonProperty("long_description_header")
    public String getLongDescriptionHeader() {
        return longDescriptionHeader;
    }

    @JsonProperty("long_description_header")
    public void setLongDescriptionHeader(String longDescriptionHeader) {
        this.longDescriptionHeader = longDescriptionHeader;
    }

    @JsonProperty("coupon_value")
    public String getCouponValue() {
        return couponValue;
    }

    @JsonProperty("coupon_value")
    public void setCouponValue(String couponValue) {
        this.couponValue = couponValue;
    }
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
