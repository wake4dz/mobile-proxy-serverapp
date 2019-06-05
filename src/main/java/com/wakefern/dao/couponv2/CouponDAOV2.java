
package com.wakefern.dao.couponv2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "short_description",
    "requirement_upcs",
    "featured",
    "pos_live_date",
    "external_id",
    "expiration_date",
    "enabled",
    "total_downloads",
    "publication_id",
    "coupon_id",
    "reward_upcs",
    "display_end_date",
    "savings",
    "value",
    "offer_type",
    "targeted_offer",
    "offer_priority",
    "display_start_date",
    "image_url",
    "brand_name",
    "long_description",
    "requirement_description",
    "tags",
    "long_description_header",
    "targeting_buckets",
    "category",
    "subcategory"
})
public class CouponDAOV2 {

    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("requirement_upcs")
    private List<String> requirementUpcs = null;
    @JsonProperty("featured")
    private String featured;
    @JsonProperty("pos_live_date")
    private String posLiveDate;
    @JsonProperty("external_id")
    private String externalId;
    @JsonProperty("expiration_date")
    private String expirationDate;
    @JsonProperty("enabled")
    private String enabled;
    @JsonProperty("total_downloads")
    private String totalDownloads;
    @JsonProperty("publication_id")
    private String publicationId;
    @JsonProperty("coupon_id")
    private String couponId;
    @JsonProperty("reward_upcs")
    private List<Object> rewardUpcs = null;
    @JsonProperty("display_end_date")
    private String displayEndDate;
    @JsonProperty("savings")
    private String savings;
    @JsonProperty("value")
    private String value;
    @JsonProperty("offer_type")
    private String offerType;
    @JsonProperty("targeted_offer")
    private String targetedOffer;
    @JsonProperty("offer_priority")
    private String offerPriority;
    @JsonProperty("display_start_date")
    private String displayStartDate;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("brand_name")
    private String brandName;
    @JsonProperty("long_description")
    private String longDescription;
    @JsonProperty("requirement_description")
    private String requirementDescription;
    @JsonProperty("tags")
    private List<Tag> tags = null;
    @JsonProperty("long_description_header")
    private String longDescriptionHeader;
    @JsonProperty("targeting_buckets")
    private List<Object> targetingBuckets = null;
    @JsonProperty("category")
    private String category;
    @JsonProperty("subcategory")
    private String subcategory;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("short_description")
    public String getShortDescription() {
        return shortDescription;
    }

    @JsonProperty("short_description")
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @JsonProperty("requirement_upcs")
    public List<String> getRequirementUpcs() {
        return requirementUpcs;
    }

    @JsonProperty("requirement_upcs")
    public void setRequirementUpcs(List<String> requirementUpcs) {
        this.requirementUpcs = requirementUpcs;
    }

    @JsonProperty("featured")
    public String getFeatured() {
        return featured;
    }

    @JsonProperty("featured")
    public void setFeatured(String featured) {
        this.featured = featured;
    }

    @JsonProperty("pos_live_date")
    public String getPosLiveDate() {
        return posLiveDate;
    }

    @JsonProperty("pos_live_date")
    public void setPosLiveDate(String posLiveDate) {
        this.posLiveDate = posLiveDate;
    }

    @JsonProperty("external_id")
    public String getExternalId() {
        return externalId;
    }

    @JsonProperty("external_id")
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @JsonProperty("expiration_date")
    public String getExpirationDate() {
        return expirationDate;
    }

    @JsonProperty("expiration_date")
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @JsonProperty("enabled")
    public String getEnabled() {
        return enabled;
    }

    @JsonProperty("enabled")
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @JsonProperty("total_downloads")
    public String getTotalDownloads() {
        return totalDownloads;
    }

    @JsonProperty("total_downloads")
    public void setTotalDownloads(String totalDownloads) {
        this.totalDownloads = totalDownloads;
    }

    @JsonProperty("publication_id")
    public String getPublicationId() {
        return publicationId;
    }

    @JsonProperty("publication_id")
    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    @JsonProperty("coupon_id")
    public String getCouponId() {
        return couponId;
    }

    @JsonProperty("coupon_id")
    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    @JsonProperty("reward_upcs")
    public List<Object> getRewardUpcs() {
        return rewardUpcs;
    }

    @JsonProperty("reward_upcs")
    public void setRewardUpcs(List<Object> rewardUpcs) {
        this.rewardUpcs = rewardUpcs;
    }

    @JsonProperty("display_end_date")
    public String getDisplayEndDate() {
        return displayEndDate;
    }

    @JsonProperty("display_end_date")
    public void setDisplayEndDate(String displayEndDate) {
        this.displayEndDate = displayEndDate;
    }

    @JsonProperty("savings")
    public String getSavings() {
        return savings;
    }

    @JsonProperty("savings")
    public void setSavings(String savings) {
        this.savings = savings;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("offer_type")
    public String getOfferType() {
        return offerType;
    }

    @JsonProperty("offer_type")
    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    @JsonProperty("targeted_offer")
    public String getTargetedOffer() {
        return targetedOffer;
    }

    @JsonProperty("targeted_offer")
    public void setTargetedOffer(String targetedOffer) {
        this.targetedOffer = targetedOffer;
    }

    @JsonProperty("offer_priority")
    public String getOfferPriority() {
        return offerPriority;
    }

    @JsonProperty("offer_priority")
    public void setOfferPriority(String offerPriority) {
        this.offerPriority = offerPriority;
    }

    @JsonProperty("display_start_date")
    public String getDisplayStartDate() {
        return displayStartDate;
    }

    @JsonProperty("display_start_date")
    public void setDisplayStartDate(String displayStartDate) {
        this.displayStartDate = displayStartDate;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("brand_name")
    public String getBrandName() {
        return brandName;
    }

    @JsonProperty("brand_name")
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @JsonProperty("long_description")
    public String getLongDescription() {
        return longDescription;
    }

    @JsonProperty("long_description")
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @JsonProperty("requirement_description")
    public String getRequirementDescription() {
        return requirementDescription;
    }

    @JsonProperty("requirement_description")
    public void setRequirementDescription(String requirementDescription) {
        this.requirementDescription = requirementDescription;
    }

    @JsonProperty("tags")
    public List<Tag> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @JsonProperty("long_description_header")
    public String getLongDescriptionHeader() {
        return longDescriptionHeader;
    }

    @JsonProperty("long_description_header")
    public void setLongDescriptionHeader(String longDescriptionHeader) {
        this.longDescriptionHeader = longDescriptionHeader;
    }

    @JsonProperty("targeting_buckets")
    public List<Object> getTargetingBuckets() {
        return targetingBuckets;
    }

    @JsonProperty("targeting_buckets")
    public void setTargetingBuckets(List<Object> targetingBuckets) {
        this.targetingBuckets = targetingBuckets;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("subcategory")
    public String getSubcategory() {
        return subcategory;
    }

    @JsonProperty("subcategory")
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
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
