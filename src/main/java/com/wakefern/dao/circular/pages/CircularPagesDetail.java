
package com.wakefern.dao.circular.pages;

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

@JsonFilter("filterByCircularPagesValue")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Paging",
    "Links",
    "Pages"
})
public class CircularPagesDetail {

    @JsonProperty("Paging")
    private Paging paging;
    @JsonProperty("Links")
    private List<Link> links = null;
    @JsonProperty("Pages")
    private List<Page> pages = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Paging")
    public Paging getPaging() {
        return paging;
    }

    @JsonProperty("Paging")
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    @JsonProperty("Links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("Links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @JsonProperty("Pages")
    public List<Page> getPages() {
        return pages;
    }

    @JsonProperty("Pages")
    public void setPages(List<Page> pages) {
        this.pages = pages;
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
