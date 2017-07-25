
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
    "Description2",
    "Types",
    "LimitText",
    "Description1",
    "DateText"
})
public class Sale {

    @JsonProperty("Description2")
    private String description2;
    @JsonProperty("Types")
    private List<Object> types = null;
    @JsonProperty("LimitText")
    private String limitText;
    @JsonProperty("Description1")
    private String description1;
    @JsonProperty("DateText")
    private String dateText;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Description2")
    public String getDescription2() {
        return description2;
    }

    @JsonProperty("Description2")
    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    @JsonProperty("Types")
    public List<Object> getTypes() {
        return types;
    }

    @JsonProperty("Types")
    public void setTypes(List<Object> types) {
        this.types = types;
    }

    @JsonProperty("LimitText")
    public String getLimitText() {
        return limitText;
    }

    @JsonProperty("LimitText")
    public void setLimitText(String limitText) {
        this.limitText = limitText;
    }

    @JsonProperty("Description1")
    public String getDescription1() {
        return description1;
    }

    @JsonProperty("Description1")
    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    @JsonProperty("DateText")
    public String getDateText() {
        return dateText;
    }

    @JsonProperty("DateText")
    public void setDateText(String dateText) {
        this.dateText = dateText;
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
