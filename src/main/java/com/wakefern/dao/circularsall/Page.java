
package com.wakefern.dao.circularsall;

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
    "Name",
    "Description",
    "Links",
    "ImageLinks",
    "Items"
})
public class Page {

    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("Name")
    private Object name;
    @JsonProperty("Description")
    private Object description;
    @JsonProperty("Links")
    private List<Link> links = null;
    @JsonProperty("ImageLinks")
    private List<ImageLink> imageLinks = null;
    @JsonProperty("Items")
    private List<Item> items = null;
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

    @JsonProperty("Name")
    public Object getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(Object name) {
        this.name = name;
    }

    @JsonProperty("Description")
    public Object getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(Object description) {
        this.description = description;
    }

    @JsonProperty("Links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("Links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @JsonProperty("ImageLinks")
    public List<ImageLink> getImageLinks() {
        return imageLinks;
    }

    @JsonProperty("ImageLinks")
    public void setImageLinks(List<ImageLink> imageLinks) {
        this.imageLinks = imageLinks;
    }

    @JsonProperty("Items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("Items")
    public void setItems(List<Item> items) {
        this.items = items;
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
