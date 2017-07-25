
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
    "Queries",
    "Placeholders",
    "Rel",
    "Uri"
})
public class ImageLink {

    @JsonProperty("Queries")
    private Object queries;
    @JsonProperty("Placeholders")
    private List<Object> placeholders = null;
    @JsonProperty("Rel")
    private String rel;
    @JsonProperty("Uri")
    private String uri;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Queries")
    public Object getQueries() {
        return queries;
    }

    @JsonProperty("Queries")
    public void setQueries(Object queries) {
        this.queries = queries;
    }

    @JsonProperty("Placeholders")
    public List<Object> getPlaceholders() {
        return placeholders;
    }

    @JsonProperty("Placeholders")
    public void setPlaceholders(List<Object> placeholders) {
        this.placeholders = placeholders;
    }

    @JsonProperty("Rel")
    public String getRel() {
        return rel;
    }

    @JsonProperty("Rel")
    public void setRel(String rel) {
        this.rel = rel;
    }

    @JsonProperty("Uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("Uri")
    public void setUri(String uri) {
        this.uri = uri;
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
