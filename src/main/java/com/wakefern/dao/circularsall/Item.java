
package com.wakefern.dao.circularsall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonFilter("filterByCirPageItemValue")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Id",
    "AreaCoordinates",
    "Category",
    "DisplayCategory",
    "ItemType",
    "Title",
    "Description",
    "PriceText",
    "ImageName",
    "Options",
    "ImageLinks",
    "Links",
    "ChildItems"
})
public class Item {

    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("AreaCoordinates")
    private AreaCoordinates areaCoordinates;
    @JsonProperty("Category")
    private Category category;
    @JsonProperty("DisplayCategory")
    private DisplayCategory displayCategory;
    @JsonProperty("ItemType")
    private String itemType;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("PriceText")
    private String priceText;
    @JsonProperty("ImageName")
    private String imageName;
    @JsonProperty("Options")
    private List<Object> options = null;
    @JsonProperty("ImageLinks")
    private List<ImageLink> imageLinks = null;
    @JsonProperty("Links")
    private List<Link> links = null;
    @JsonProperty("ChildItems")
    private List<Object> childItems = null;
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

    @JsonProperty("AreaCoordinates")
    public AreaCoordinates getAreaCoordinates() {
        return areaCoordinates;
    }

    @JsonProperty("AreaCoordinates")
    public void setAreaCoordinates(AreaCoordinates areaCoordinates) {
        this.areaCoordinates = areaCoordinates;
    }

    @JsonProperty("Category")
    public Category getCategory() {
        return category;
    }

    @JsonProperty("Category")
    public void setCategory(Category category) {
        this.category = category;
    }

    @JsonProperty("DisplayCategory")
    public DisplayCategory getDisplayCategory() {
        return displayCategory;
    }

    @JsonProperty("DisplayCategory")
    public void setDisplayCategory(DisplayCategory displayCategory) {
        this.displayCategory = displayCategory;
    }

    @JsonProperty("ItemType")
    public String getItemType() {
        return itemType;
    }

    @JsonProperty("ItemType")
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @JsonProperty("Title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("Title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("PriceText")
    public String getPriceText() {
        return priceText;
    }

    @JsonProperty("PriceText")
    public void setPriceText(String priceText) {
        this.priceText = priceText;
    }

    @JsonProperty("ImageName")
    public String getImageName() {
        return imageName;
    }

    @JsonProperty("ImageName")
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @JsonProperty("Options")
    public List<Object> getOptions() {
        return options;
    }

    @JsonProperty("Options")
    public void setOptions(List<Object> options) {
        this.options = options;
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

    @JsonProperty("ChildItems")
    public List<Object> getChildItems() {
        return childItems;
    }

    @JsonProperty("ChildItems")
    public void setChildItems(List<Object> childItems) {
        this.childItems = childItems;
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
