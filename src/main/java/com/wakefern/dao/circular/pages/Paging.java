
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
    "Skip",
    "Take",
    "Total"
})
public class Paging {

    @JsonProperty("Skip")
    private Integer skip;
    @JsonProperty("Take")
    private Integer take;
    @JsonProperty("Total")
    private Integer total;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("Total")
    public Integer getTotal() {
        return total;
    }

    @JsonProperty("Total")
    public void setTotal(Integer total) {
        this.total = total;
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
