
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
    "ActiveFilters",
    "RecentFilters",
    "Facets",
    "SortLinks",
    "Pages",
    "PageLinks",
    "Items",
    "Skip",
    "Take",
    "TotalPrice",
    "TotalQuantity",
    "ItemCount",
    "Links"
})
public class ProductSKUsDAO {

    @JsonProperty("ActiveFilters")
    private List<Object> activeFilters = null;
    @JsonProperty("RecentFilters")
    private List<Object> recentFilters = null;
    @JsonProperty("Facets")
    private List<Object> facets = null;
    @JsonProperty("SortLinks")
    private List<Object> sortLinks = null;
    @JsonProperty("Pages")
    private List<Object> pages = null;
    @JsonProperty("PageLinks")
    private List<Object> pageLinks = null;
    @JsonProperty("Items")
    private List<Item> items = null;
    @JsonProperty("Skip")
    private Integer skip;
    @JsonProperty("Take")
    private Integer take;
    @JsonProperty("TotalPrice")
    private Object totalPrice;
    @JsonProperty("TotalQuantity")
    private Integer totalQuantity;
    @JsonProperty("ItemCount")
    private Integer itemCount;
    @JsonProperty("Links")
    private List<Object> links = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ActiveFilters")
    public List<Object> getActiveFilters() {
        return activeFilters;
    }

    @JsonProperty("ActiveFilters")
    public void setActiveFilters(List<Object> activeFilters) {
        this.activeFilters = activeFilters;
    }

    @JsonProperty("RecentFilters")
    public List<Object> getRecentFilters() {
        return recentFilters;
    }

    @JsonProperty("RecentFilters")
    public void setRecentFilters(List<Object> recentFilters) {
        this.recentFilters = recentFilters;
    }

    @JsonProperty("Facets")
    public List<Object> getFacets() {
        return facets;
    }

    @JsonProperty("Facets")
    public void setFacets(List<Object> facets) {
        this.facets = facets;
    }

    @JsonProperty("SortLinks")
    public List<Object> getSortLinks() {
        return sortLinks;
    }

    @JsonProperty("SortLinks")
    public void setSortLinks(List<Object> sortLinks) {
        this.sortLinks = sortLinks;
    }

    @JsonProperty("Pages")
    public List<Object> getPages() {
        return pages;
    }

    @JsonProperty("Pages")
    public void setPages(List<Object> pages) {
        this.pages = pages;
    }

    @JsonProperty("PageLinks")
    public List<Object> getPageLinks() {
        return pageLinks;
    }

    @JsonProperty("PageLinks")
    public void setPageLinks(List<Object> pageLinks) {
        this.pageLinks = pageLinks;
    }

    @JsonProperty("Items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("Items")
    public void setItems(List<Item> items) {
        this.items = items;
    }

    @JsonProperty("Skip")
    public Integer getSkip() {
        return skip;
    }

    @JsonProperty("Skip")
    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    @JsonProperty("Take")
    public Integer getTake() {
        return take;
    }

    @JsonProperty("Take")
    public void setTake(Integer take) {
        this.take = take;
    }

    @JsonProperty("TotalPrice")
    public Object getTotalPrice() {
        return totalPrice;
    }

    @JsonProperty("TotalPrice")
    public void setTotalPrice(Object totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonProperty("TotalQuantity")
    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    @JsonProperty("TotalQuantity")
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @JsonProperty("ItemCount")
    public Integer getItemCount() {
        return itemCount;
    }

    @JsonProperty("ItemCount")
    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    @JsonProperty("Links")
    public List<Object> getLinks() {
        return links;
    }

    @JsonProperty("Links")
    public void setLinks(List<Object> links) {
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
