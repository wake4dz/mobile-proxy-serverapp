
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

//@JsonFilter("filterByCirValue")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Id",
    "Name",
    "StartDate",
    "EndDate",
    "PageCount",
    "ImageLinks",
    "Pages",
    "CircularType",
    "CircularValidText",
    "Links"
})
public class CircularsAllDAO {

    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("StartDate")
    private String startDate;
    @JsonProperty("EndDate")
    private String endDate;
    @JsonProperty("PageCount")
    private Integer pageCount;
    @JsonProperty("ImageLinks")
    private List<ImageLink> imageLinks = null;
    @JsonProperty("Pages")
    private List<Page> pages = null;
    @JsonProperty("CircularType")
    private String circularType;
    @JsonProperty("CircularValidText")
    private String circularValidText;
    @JsonProperty("Links")
    private List<Link> links = null;
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
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("StartDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("StartDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("EndDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("EndDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("PageCount")
    public Integer getPageCount() {
        return pageCount;
    }

    @JsonProperty("PageCount")
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    @JsonProperty("ImageLinks")
    public List<ImageLink> getImageLinks() {
        return imageLinks;
    }

    @JsonProperty("ImageLinks")
    public void setImageLinks(List<ImageLink> imageLinks) {
        this.imageLinks = imageLinks;
    }

    @JsonProperty("Pages")
    public List<Page> getPages() {
        return pages;
    }

    @JsonProperty("Pages")
    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    @JsonProperty("CircularType")
    public String getCircularType() {
        return circularType;
    }

    @JsonProperty("CircularType")
    public void setCircularType(String circularType) {
        this.circularType = circularType;
    }

    @JsonProperty("CircularValidText")
    public String getCircularValidText() {
        return circularValidText;
    }

    @JsonProperty("CircularValidText")
    public void setCircularValidText(String circularValidText) {
        this.circularValidText = circularValidText;
    }

    @JsonProperty("Links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("Links")
    public void setLinks(List<Link> links) {
        this.links = links;
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
