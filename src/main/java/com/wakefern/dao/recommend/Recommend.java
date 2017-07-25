
package com.wakefern.dao.recommend;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Aisle",
    "Brand",
    "Category",
    "Name",
    "Sku",
    "ProductId",
    "RegularPrice",
    "CurrentPrice",
    "Size",
    "InStock",
    "Imagelink",
    "Note",
    "DateText",
    "coupon_id",
    "ItemKey",
    "ItemType"
})
public class Recommend {

    @JsonProperty("Aisle")
    private String aisle;
    @JsonProperty("Brand")
    private String brand;
    @JsonProperty("Category")
    private String category;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Sku")
    private String sku;
    @JsonProperty("ProductId")
    private int productId;
    @JsonProperty("RegularPrice")
    private String regularPrice;
    @JsonProperty("CurrentPrice")
    private String currentPrice;
    @JsonProperty("Size")
    private String size;
    @JsonProperty("InStock")
    private Boolean inStock;
    @JsonProperty("Imagelink")
    private String imagelink;
    @JsonProperty("Note")
    private String note;
    @JsonProperty("DateText")
    private String dateText;
    @JsonProperty("coupon_id")
    private String couponId;
    @JsonProperty("ItemKey")
    private String itemKey;
    @JsonProperty("ItemType")
    private String itemType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Aisle")
    public String getAisle() {
        return aisle;
    }

    @JsonProperty("Aisle")
    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    @JsonProperty("Brand")
    public String getBrand() {
        return brand;
    }

    @JsonProperty("Brand")
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @JsonProperty("Category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("Category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Sku")
    public String getSku() {
        return sku;
    }

    @JsonProperty("Sku")
    public void setSku(String sku) {
        this.sku = sku;
    }

    @JsonProperty("ProductId")
    public int getProductId() {
        return productId;
    }

    @JsonProperty("ProductId")
    public void setProductId(int productId) {
        this.productId = productId;
    }

    @JsonProperty("RegularPrice")
    public String getRegularPrice() {
        return regularPrice;
    }

    @JsonProperty("RegularPrice")
    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    @JsonProperty("CurrentPrice")
    public String getCurrentPrice() {
        return currentPrice;
    }

    @JsonProperty("CurrentPrice")
    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    @JsonProperty("Size")
    public String getSize() {
        return size;
    }

    @JsonProperty("Size")
    public void setSize(String size) {
        this.size = size;
    }

    @JsonProperty("InStock")
    public Boolean getInStock() {
        return inStock;
    }

    @JsonProperty("InStock")
    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    @JsonProperty("Imagelink")
    public String getImagelink() {
        return imagelink;
    }

    @JsonProperty("Imagelink")
    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    @JsonProperty("Note")
    public String getNote() {
        return note;
    }

    @JsonProperty("Note")
    public void setNote(String note) {
        this.note = note;
    }

    @JsonProperty("DateText")
    public String getDateText() {
        return dateText;
    }

    @JsonProperty("DateText")
    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    @JsonProperty("coupon_id")
    public String getCouponId() {
        return couponId;
    }

    @JsonProperty("coupon_id")
    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    @JsonProperty("ItemKey")
    public String getItemKey() {
        return itemKey;
    }

    @JsonProperty("ItemKey")
    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    @JsonProperty("ItemType")
    public String getItemType() {
        return itemType;
    }

    @JsonProperty("ItemType")
    public void setItemType(String itemType) {
        this.itemType = itemType;
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
