
package com.wakefern.dao.circular.pages;

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
    "Shape",
    "Coordinates"
})
public class AreaCoordinates {

    @JsonProperty("Shape")
    private String shape;
    @JsonProperty("Coordinates")
    private String coordinates;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Shape")
    public String getShape() {
        return shape;
    }

    @JsonProperty("Shape")
    public void setShape(String shape) {
        this.shape = shape;
    }

    @JsonProperty("Coordinates")
    public String getCoordinates() {
        return coordinates;
    }

    @JsonProperty("Coordinates")
    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
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
