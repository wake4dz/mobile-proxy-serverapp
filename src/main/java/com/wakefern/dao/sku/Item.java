
package com.wakefern.dao.sku;

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
    "Id",
    "Sku",
    "Brand",
    "Name",
    "Description",
    "Quantity",
    "Size",
    "Sizes",
    "Category",
    "CategoryId",
    "UniversalCategoryId",
    "UniversalCategoryName",
    "IsSubStoreCategory",
    "SpecificCategory",
    "DisplayCategory",
    "Aisle",
    "AislePrecedence",
    "ItemType",
    "ItemKey",
    "Note",
    "CurrentPrice",
    "CurrentUnitPrice",
    "RegularPrice",
    "LineTotal",
    "InStock",
    "Sale",
    "AlternateSale",
    "ImageLinks",
    "Links",
    "Decals",
    "Source",
    "IsAvailable",
    "IsAvailableInStore",
    "PointRedemptionInfo",
    "FulfillmentTypeExclusion",
    "Notifications"
})
public class Item {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("Sku")
    private String sku;
    @JsonProperty("Brand")
    private String brand;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Quantity")
    private Integer quantity;
    @JsonProperty("Size")
    private String size;
    @JsonProperty("Sizes")
    private List<Object> sizes = null;
    @JsonProperty("Category")
    private String category;
    @JsonProperty("CategoryId")
    private Integer categoryId;
    @JsonProperty("UniversalCategoryId")
    private Object universalCategoryId;
    @JsonProperty("UniversalCategoryName")
    private Object universalCategoryName;
    @JsonProperty("IsSubStoreCategory")
    private Boolean isSubStoreCategory;
    @JsonProperty("SpecificCategory")
    private SpecificCategory specificCategory;
    @JsonProperty("DisplayCategory")
    private DisplayCategory displayCategory;
    @JsonProperty("Aisle")
    private String aisle;
    @JsonProperty("AislePrecedence")
    private Integer aislePrecedence;
    @JsonProperty("ItemType")
    private String itemType;
    @JsonProperty("ItemKey")
    private String itemKey;
    @JsonProperty("Note")
    private String note;
    @JsonProperty("CurrentPrice")
    private String currentPrice;
    @JsonProperty("CurrentUnitPrice")
    private String currentUnitPrice;
    @JsonProperty("RegularPrice")
    private String regularPrice;
    @JsonProperty("LineTotal")
    private Object lineTotal;
    @JsonProperty("InStock")
    private Boolean inStock;
    @JsonProperty("Sale")
    private Sale sale;
    @JsonProperty("AlternateSale")
    private Object alternateSale;
    @JsonProperty("ImageLinks")
    private List<ImageLink> imageLinks = null;
    @JsonProperty("Links")
    private List<Link> links = null;
    @JsonProperty("Decals")
    private List<Decal> decals = null;
    @JsonProperty("Source")
    private Source source;
    @JsonProperty("IsAvailable")
    private Boolean isAvailable;
    @JsonProperty("IsAvailableInStore")
    private Boolean isAvailableInStore;
    @JsonProperty("PointRedemptionInfo")
    private Object pointRedemptionInfo;
    @JsonProperty("FulfillmentTypeExclusion")
    private List<Object> fulfillmentTypeExclusion = null;
    @JsonProperty("Notifications")
    private List<Object> notifications = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Sku")
    public String getSku() {
        return sku;
    }

    @JsonProperty("Sku")
    public void setSku(String sku) {
        this.sku = sku;
    }

    @JsonProperty("Brand")
    public String getBrand() {
        return brand;
    }

    @JsonProperty("Brand")
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("Quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("Quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("Size")
    public String getSize() {
        return size;
    }

    @JsonProperty("Size")
    public void setSize(String size) {
        this.size = size;
    }

    @JsonProperty("Sizes")
    public List<Object> getSizes() {
        return sizes;
    }

    @JsonProperty("Sizes")
    public void setSizes(List<Object> sizes) {
        this.sizes = sizes;
    }

    @JsonProperty("Category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("Category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("CategoryId")
    public Integer getCategoryId() {
        return categoryId;
    }

    @JsonProperty("CategoryId")
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @JsonProperty("UniversalCategoryId")
    public Object getUniversalCategoryId() {
        return universalCategoryId;
    }

    @JsonProperty("UniversalCategoryId")
    public void setUniversalCategoryId(Object universalCategoryId) {
        this.universalCategoryId = universalCategoryId;
    }

    @JsonProperty("UniversalCategoryName")
    public Object getUniversalCategoryName() {
        return universalCategoryName;
    }

    @JsonProperty("UniversalCategoryName")
    public void setUniversalCategoryName(Object universalCategoryName) {
        this.universalCategoryName = universalCategoryName;
    }

    @JsonProperty("IsSubStoreCategory")
    public Boolean getIsSubStoreCategory() {
        return isSubStoreCategory;
    }

    @JsonProperty("IsSubStoreCategory")
    public void setIsSubStoreCategory(Boolean isSubStoreCategory) {
        this.isSubStoreCategory = isSubStoreCategory;
    }

    @JsonProperty("SpecificCategory")
    public SpecificCategory getSpecificCategory() {
        return specificCategory;
    }

    @JsonProperty("SpecificCategory")
    public void setSpecificCategory(SpecificCategory specificCategory) {
        this.specificCategory = specificCategory;
    }

    @JsonProperty("DisplayCategory")
    public DisplayCategory getDisplayCategory() {
        return displayCategory;
    }

    @JsonProperty("DisplayCategory")
    public void setDisplayCategory(DisplayCategory displayCategory) {
        this.displayCategory = displayCategory;
    }

    @JsonProperty("Aisle")
    public String getAisle() {
        return aisle;
    }

    @JsonProperty("Aisle")
    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    @JsonProperty("AislePrecedence")
    public Integer getAislePrecedence() {
        return aislePrecedence;
    }

    @JsonProperty("AislePrecedence")
    public void setAislePrecedence(Integer aislePrecedence) {
        this.aislePrecedence = aislePrecedence;
    }

    @JsonProperty("ItemType")
    public String getItemType() {
        return itemType;
    }

    @JsonProperty("ItemType")
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @JsonProperty("ItemKey")
    public String getItemKey() {
        return itemKey;
    }

    @JsonProperty("ItemKey")
    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    @JsonProperty("Note")
    public String getNote() {
        return note;
    }

    @JsonProperty("Note")
    public void setNote(String note) {
        this.note = note;
    }

    @JsonProperty("CurrentPrice")
    public String getCurrentPrice() {
        return currentPrice;
    }

    @JsonProperty("CurrentPrice")
    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    @JsonProperty("CurrentUnitPrice")
    public String getCurrentUnitPrice() {
        return currentUnitPrice;
    }

    @JsonProperty("CurrentUnitPrice")
    public void setCurrentUnitPrice(String currentUnitPrice) {
        this.currentUnitPrice = currentUnitPrice;
    }

    @JsonProperty("RegularPrice")
    public String getRegularPrice() {
        return regularPrice;
    }

    @JsonProperty("RegularPrice")
    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    @JsonProperty("LineTotal")
    public Object getLineTotal() {
        return lineTotal;
    }

    @JsonProperty("LineTotal")
    public void setLineTotal(Object lineTotal) {
        this.lineTotal = lineTotal;
    }

    @JsonProperty("InStock")
    public Boolean getInStock() {
        return inStock;
    }

    @JsonProperty("InStock")
    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    @JsonProperty("Sale")
    public Sale getSale() {
        return sale;
    }

    @JsonProperty("Sale")
    public void setSale(Sale sale) {
        this.sale = sale;
    }

    @JsonProperty("AlternateSale")
    public Object getAlternateSale() {
        return alternateSale;
    }

    @JsonProperty("AlternateSale")
    public void setAlternateSale(Object alternateSale) {
        this.alternateSale = alternateSale;
    }

    @JsonProperty("ImageLinks")
    public List<ImageLink> getImageLinks() {
        return imageLinks;
    }

    @JsonProperty("ImageLinks")
    public void setImageLinks(List<ImageLink> imageLinks) {
        this.imageLinks = imageLinks;
    }

    @JsonProperty("Links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("Links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @JsonProperty("Decals")
    public List<Decal> getDecals() {
        return decals;
    }

    @JsonProperty("Decals")
    public void setDecals(List<Decal> decals) {
        this.decals = decals;
    }

    @JsonProperty("Source")
    public Source getSource() {
        return source;
    }

    @JsonProperty("Source")
    public void setSource(Source source) {
        this.source = source;
    }

    @JsonProperty("IsAvailable")
    public Boolean getIsAvailable() {
        return isAvailable;
    }

    @JsonProperty("IsAvailable")
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @JsonProperty("IsAvailableInStore")
    public Boolean getIsAvailableInStore() {
        return isAvailableInStore;
    }

    @JsonProperty("IsAvailableInStore")
    public void setIsAvailableInStore(Boolean isAvailableInStore) {
        this.isAvailableInStore = isAvailableInStore;
    }

    @JsonProperty("PointRedemptionInfo")
    public Object getPointRedemptionInfo() {
        return pointRedemptionInfo;
    }

    @JsonProperty("PointRedemptionInfo")
    public void setPointRedemptionInfo(Object pointRedemptionInfo) {
        this.pointRedemptionInfo = pointRedemptionInfo;
    }

    @JsonProperty("FulfillmentTypeExclusion")
    public List<Object> getFulfillmentTypeExclusion() {
        return fulfillmentTypeExclusion;
    }

    @JsonProperty("FulfillmentTypeExclusion")
    public void setFulfillmentTypeExclusion(List<Object> fulfillmentTypeExclusion) {
        this.fulfillmentTypeExclusion = fulfillmentTypeExclusion;
    }

    @JsonProperty("Notifications")
    public List<Object> getNotifications() {
        return notifications;
    }

    @JsonProperty("Notifications")
    public void setNotifications(List<Object> notifications) {
        this.notifications = notifications;
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
