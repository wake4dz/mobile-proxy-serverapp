
package com.wakefern.dao.recommend;

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
    "Success",
    "products"
})
public class RecommendVPUPCsDAO {

    @JsonProperty("Success")
    private Boolean success;
    @JsonProperty("products")
    private List<String> products = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Success")
    public Boolean getSuccess() {
        return success;
    }

    @JsonProperty("Success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @JsonProperty("products")
    public List<String> getProducts() {
        return products;
    }

    @JsonProperty("products")
    public void setProducts(List<String> products) {
        this.products = products;
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
