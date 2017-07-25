
package com.wakefern.dao.cart;

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
    "Sale",
    "Decals",
    "Description",
    "Category",
    "Size",
    "ItemType",
    "Source",
    "Name",
    "LineTotal",
    "Aisle",
    "CurrentUnitPrice",
    "Quantity",
    "Brand",
    "Sizes",
    "AlternateSale",
    "CurrentPrice",
    "InStock",
    "IsAvailableInStore",
    "ItemKey",
    "Note",
    "IsAvailable",
    "RegularPrice",
    "Links",
    "Id",
    "Sku",
    "ImageLinks"
})
public class Item {

    @JsonProperty("Sale")
    private Sale sale;
    @JsonProperty("Decals")
    private List<Decal> decals = null;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Category")
    private String category;
    @JsonProperty("Size")
    private String size;
    @JsonProperty("ItemType")
    private String itemType;
    @JsonProperty("Source")
    private Object source;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("LineTotal")
    private String lineTotal;
    @JsonProperty("Aisle")
    private String aisle;
    @JsonProperty("CurrentUnitPrice")
    private String currentUnitPrice;
    @JsonProperty("Quantity")
    private Long quantity;
    @JsonProperty("Brand")
    private String brand;
    @JsonProperty("Sizes")
    private List<Object> sizes = null;
    @JsonProperty("AlternateSale")
    private Object alternateSale;
    @JsonProperty("CurrentPrice")
    private String currentPrice;
    @JsonProperty("InStock")
    private Boolean inStock;
    @JsonProperty("IsAvailableInStore")
    private Boolean isAvailableInStore;
    @JsonProperty("ItemKey")
    private String itemKey;
    @JsonProperty("Note")
    private Object note;
    @JsonProperty("IsAvailable")
    private Boolean isAvailable;
    @JsonProperty("RegularPrice")
    private String regularPrice;
    @JsonProperty("Links")
    private List<ItemLink> links = null;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Sku")
    private String sku;
    @JsonProperty("ImageLinks")
    private List<ImageLink> imageLinks = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Sale")
    public Sale getSale() {
        return sale;
    }

    @JsonProperty("Sale")
    public void setSale(Sale sale) {
        this.sale = sale;
    }

    @JsonProperty("Decals")
    public List<Decal> getDecals() {
        return decals;
    }

    @JsonProperty("Decals")
    public void setDecals(List<Decal> decals) {
        this.decals = decals;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("Category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("Category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("Size")
    public String getSize() {
        return size;
    }

    @JsonProperty("Size")
    public void setSize(String size) {
        this.size = size;
    }

    @JsonProperty("ItemType")
    public String getItemType() {
        return itemType;
    }

    @JsonProperty("ItemType")
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @JsonProperty("Source")
    public Object getSource() {
        return source;
    }

    @JsonProperty("Source")
    public void setSource(Object source) {
        this.source = source;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("LineTotal")
    public String getLineTotal() {
        return lineTotal;
    }

    @JsonProperty("LineTotal")
    public void setLineTotal(String lineTotal) {
        this.lineTotal = lineTotal;
    }

    @JsonProperty("Aisle")
    public String getAisle() {
        return aisle;
    }

    @JsonProperty("Aisle")
    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    @JsonProperty("CurrentUnitPrice")
    public String getCurrentUnitPrice() {
        return currentUnitPrice;
    }

    @JsonProperty("CurrentUnitPrice")
    public void setCurrentUnitPrice(String currentUnitPrice) {
        this.currentUnitPrice = currentUnitPrice;
    }

    @JsonProperty("Quantity")
    public Long getQuantity() {
        return quantity;
    }

    @JsonProperty("Quantity")
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("Brand")
    public String getBrand() {
        return brand;
    }

    @JsonProperty("Brand")
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @JsonProperty("Sizes")
    public List<Object> getSizes() {
        return sizes;
    }

    @JsonProperty("Sizes")
    public void setSizes(List<Object> sizes) {
        this.sizes = sizes;
    }

    @JsonProperty("AlternateSale")
    public Object getAlternateSale() {
        return alternateSale;
    }

    @JsonProperty("AlternateSale")
    public void setAlternateSale(Object alternateSale) {
        this.alternateSale = alternateSale;
    }

    @JsonProperty("CurrentPrice")
    public String getCurrentPrice() {
        return currentPrice;
    }

    @JsonProperty("CurrentPrice")
    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    @JsonProperty("InStock")
    public Boolean getInStock() {
        return inStock;
    }

    @JsonProperty("InStock")
    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    @JsonProperty("IsAvailableInStore")
    public Boolean getIsAvailableInStore() {
        return isAvailableInStore;
    }

    @JsonProperty("IsAvailableInStore")
    public void setIsAvailableInStore(Boolean isAvailableInStore) {
        this.isAvailableInStore = isAvailableInStore;
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
    public Object getNote() {
        return note;
    }

    @JsonProperty("Note")
    public void setNote(Object note) {
        this.note = note;
    }

    @JsonProperty("IsAvailable")
    public Boolean getIsAvailable() {
        return isAvailable;
    }

    @JsonProperty("IsAvailable")
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @JsonProperty("RegularPrice")
    public String getRegularPrice() {
        return regularPrice;
    }

    @JsonProperty("RegularPrice")
    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    @JsonProperty("Links")
    public List<ItemLink> getLinks() {
        return links;
    }

    @JsonProperty("Links")
    public void setLinks(List<ItemLink> links) {
        this.links = links;
    }

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

    @JsonProperty("ImageLinks")
    public List<ImageLink> getImageLinks() {
        return imageLinks;
    }

    @JsonProperty("ImageLinks")
    public void setImageLinks(List<ImageLink> imageLinks) {
        this.imageLinks = imageLinks;
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
