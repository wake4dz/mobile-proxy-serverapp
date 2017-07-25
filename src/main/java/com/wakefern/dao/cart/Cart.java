
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
    "Take",
    "Facets",
    "SortLinks",
    "Pages",
    "RecentFilters",
    "TotalQuantity",
    "ActiveFilters",
    "TotalPrice",
    "Skip",
    "ItemCount",
    "Links",
    "PageLinks",
    "Items"
})
public class Cart {

    @JsonProperty("Take")
    private Long take;
    @JsonProperty("Facets")
    private List<Object> facets = null;
    @JsonProperty("SortLinks")
    private List<Object> sortLinks = null;
    @JsonProperty("Pages")
    private List<Object> pages = null;
    @JsonProperty("RecentFilters")
    private List<Object> recentFilters = null;
    @JsonProperty("TotalQuantity")
    private Long totalQuantity;
    @JsonProperty("ActiveFilters")
    private List<Object> activeFilters = null;
    @JsonProperty("TotalPrice")
    private String totalPrice;
    @JsonProperty("Skip")
    private Long skip;
    @JsonProperty("ItemCount")
    private Long itemCount;
    @JsonProperty("Links")
    private List<CartLink> links = null;
    @JsonProperty("PageLinks")
    private List<Object> pageLinks = null;
    @JsonProperty("Items")
    private List<Item> items = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Take")
    public Long getTake() {
        return take;
    }

    @JsonProperty("Take")
    public void setTake(Long take) {
        this.take = take;
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

    @JsonProperty("RecentFilters")
    public List<Object> getRecentFilters() {
        return recentFilters;
    }

    @JsonProperty("RecentFilters")
    public void setRecentFilters(List<Object> recentFilters) {
        this.recentFilters = recentFilters;
    }

    @JsonProperty("TotalQuantity")
    public Long getTotalQuantity() {
        return totalQuantity;
    }

    @JsonProperty("TotalQuantity")
    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @JsonProperty("ActiveFilters")
    public List<Object> getActiveFilters() {
        return activeFilters;
    }

    @JsonProperty("ActiveFilters")
    public void setActiveFilters(List<Object> activeFilters) {
        this.activeFilters = activeFilters;
    }

    @JsonProperty("TotalPrice")
    public String getTotalPrice() {
        return totalPrice;
    }

    @JsonProperty("TotalPrice")
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonProperty("Skip")
    public Long getSkip() {
        return skip;
    }

    @JsonProperty("Skip")
    public void setSkip(Long skip) {
        this.skip = skip;
    }

    @JsonProperty("ItemCount")
    public Long getItemCount() {
        return itemCount;
    }

    @JsonProperty("ItemCount")
    public void setItemCount(Long itemCount) {
        this.itemCount = itemCount;
    }

    @JsonProperty("Links")
    public List<CartLink> getLinks() {
        return links;
    }

    @JsonProperty("Links")
    public void setLinks(List<CartLink> links) {
        this.links = links;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
