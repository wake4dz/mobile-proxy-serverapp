
package com.wakefern.dao.product;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Uri",
    "View"
})
public class Source {

    @JsonProperty("Uri")
    private String uri;
    @JsonProperty("View")
    private Object view;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("Uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    @JsonProperty("View")
    public Object getView() {
        return view;
    }

    @JsonProperty("View")
    public void setView(Object view) {
        this.view = view;
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
