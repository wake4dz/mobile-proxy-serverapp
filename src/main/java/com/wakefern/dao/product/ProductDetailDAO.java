package com.wakefern.dao.product;

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
    "ItemType",
    "ItemKey",
    "Brand",
    "BrandId",
    "Name",
    "Size",
    "Sizes",
    "SpecificCategory",
    "DisplayCategory",
    "Category",
    "CategoryId",
    "UniversalCategoryId",
    "UniversalCategoryName",
    "IsSubStoreCategory",
    "CurrentPrice",
    "RegularPrice",
    "CurrentUnitPrice",
    "Description",
    "Aisle",
    "InStock",
    "IsAvailable",
    "IsAvailableInStore",
    "FulfillmentTypeExclusion",
    "Sale",
    "AlternateSale",
    "ManufacturerInformation",
    "Labels",
    "HasLabels",
    "Source",
    "Decals",
    "ImageLinks",
    "Links",
    "VariationLinks",
    "PointRedemptionInfo",
    "Notifications"
})
public class ProductDetailDAO {

    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("Sku")
    private String sku;
    @JsonProperty("ItemType")
    private String itemType;
    @JsonProperty("ItemKey")
    private String itemKey;
    @JsonProperty("Brand")
    private String brand;
    @JsonProperty("BrandId")
    private Integer brandId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Size")
    private String size;
    @JsonProperty("Sizes")
    private List<Object> sizes = null;
    @JsonProperty("SpecificCategory")
    private SpecificCategory specificCategory;
    @JsonProperty("DisplayCategory")
    private DisplayCategory displayCategory;
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
    @JsonProperty("CurrentPrice")
    private String currentPrice;
    @JsonProperty("RegularPrice")
    private String regularPrice;
    @JsonProperty("CurrentUnitPrice")
    private String currentUnitPrice;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Aisle")
    private String aisle;
    @JsonProperty("InStock")
    private Boolean inStock;
    @JsonProperty("IsAvailable")
    private Boolean isAvailable;
    @JsonProperty("IsAvailableInStore")
    private Boolean isAvailableInStore;
    @JsonProperty("FulfillmentTypeExclusion")
    private List<Object> fulfillmentTypeExclusion = null;
    @JsonProperty("Sale")
    private Sale sale;
    @JsonProperty("AlternateSale")
    private Object alternateSale;
    @JsonProperty("ManufacturerInformation")
    private Object manufacturerInformation;
    @JsonProperty("Labels")
    private List<Label> labels = null;
    @JsonProperty("HasLabels")
    private Boolean hasLabels;
    @JsonProperty("Source")
    private Source source;
    @JsonProperty("Decals")
    private List<Decal> decals = null;
    @JsonProperty("ImageLinks")
    private List<ImageLink> imageLinks = null;
    @JsonProperty("Links")
    private List<Link> links = null;
    @JsonProperty("VariationLinks")
    private List<VariationLink> variationLinks = null;
    @JsonProperty("PointRedemptionInfo")
    private Object pointRedemptionInfo;
    @JsonProperty("Notifications")
    private List<Object> notifications = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(Integer id) {
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

    @JsonProperty("Brand")
    public String getBrand() {
        return brand;
    }

    @JsonProperty("Brand")
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @JsonProperty("BrandId")
    public Integer getBrandId() {
        return brandId;
    }

    @JsonProperty("BrandId")
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
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

    @JsonProperty("CurrentPrice")
    public String getCurrentPrice() {
        return currentPrice;
    }

    @JsonProperty("CurrentPrice")
    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    @JsonProperty("RegularPrice")
    public String getRegularPrice() {
        return regularPrice;
    }

    @JsonProperty("RegularPrice")
    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    @JsonProperty("CurrentUnitPrice")
    public String getCurrentUnitPrice() {
        return currentUnitPrice;
    }

    @JsonProperty("CurrentUnitPrice")
    public void setCurrentUnitPrice(String currentUnitPrice) {
        this.currentUnitPrice = currentUnitPrice;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("Aisle")
    public String getAisle() {
        return aisle;
    }

    @JsonProperty("Aisle")
    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    @JsonProperty("InStock")
    public Boolean getInStock() {
        return inStock;
    }

    @JsonProperty("InStock")
    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
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

    @JsonProperty("FulfillmentTypeExclusion")
    public List<Object> getFulfillmentTypeExclusion() {
        return fulfillmentTypeExclusion;
    }

    @JsonProperty("FulfillmentTypeExclusion")
    public void setFulfillmentTypeExclusion(List<Object> fulfillmentTypeExclusion) {
        this.fulfillmentTypeExclusion = fulfillmentTypeExclusion;
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

    @JsonProperty("ManufacturerInformation")
    public Object getManufacturerInformation() {
        return manufacturerInformation;
    }

    @JsonProperty("ManufacturerInformation")
    public void setManufacturerInformation(Object manufacturerInformation) {
        this.manufacturerInformation = manufacturerInformation;
    }

    @JsonProperty("Labels")
    public List<Label> getLabels() {
        return labels;
    }

    @JsonProperty("Labels")
    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    @JsonProperty("HasLabels")
    public Boolean getHasLabels() {
        return hasLabels;
    }

    @JsonProperty("HasLabels")
    public void setHasLabels(Boolean hasLabels) {
        this.hasLabels = hasLabels;
    }

    @JsonProperty("Source")
    public Source getSource() {
        return source;
    }

    @JsonProperty("Source")
    public void setSource(Source source) {
        this.source = source;
    }

    @JsonProperty("Decals")
    public List<Decal> getDecals() {
        return decals;
    }

    @JsonProperty("Decals")
    public void setDecals(List<Decal> decals) {
        this.decals = decals;
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

    @JsonProperty("VariationLinks")
    public List<VariationLink> getVariationLinks() {
        return variationLinks;
    }

    @JsonProperty("VariationLinks")
    public void setVariationLinks(List<VariationLink> variationLinks) {
        this.variationLinks = variationLinks;
    }

    @JsonProperty("PointRedemptionInfo")
    public Object getPointRedemptionInfo() {
        return pointRedemptionInfo;
    }

    @JsonProperty("PointRedemptionInfo")
    public void setPointRedemptionInfo(Object pointRedemptionInfo) {
        this.pointRedemptionInfo = pointRedemptionInfo;
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
