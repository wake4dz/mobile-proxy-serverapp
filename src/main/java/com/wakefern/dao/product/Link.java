
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
    "Rel",
    "Placeholders",
    "Queries",
    "Uri"
})
public class Link {

    @JsonProperty("Rel")
    private String rel;
    @JsonProperty("Placeholders")
    private List<Object> placeholders = null;
    @JsonProperty("Queries")
    private Object queries;
    @JsonProperty("Uri")
    private String uri;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Rel")
    public String getRel() {
        return rel;
    }

    @JsonProperty("Rel")
    public void setRel(String rel) {
        this.rel = rel;
    }

    @JsonProperty("Placeholders")
    public List<Object> getPlaceholders() {
        return placeholders;
    }

    @JsonProperty("Placeholders")
    public void setPlaceholders(List<Object> placeholders) {
        this.placeholders = placeholders;
    }

    @JsonProperty("Queries")
    public Object getQueries() {
        return queries;
    }

    @JsonProperty("Queries")
    public void setQueries(Object queries) {
        this.queries = queries;
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
