
package com.wakefern.dao.user;

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
    "CampaignName",
    "OptIn"
})
public class Preference {

    @JsonProperty("CampaignName")
    private String campaignName;
    @JsonProperty("OptIn")
    private Boolean optIn;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("CampaignName")
    public String getCampaignName() {
        return campaignName;
    }

    @JsonProperty("CampaignName")
    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    @JsonProperty("OptIn")
    public Boolean getOptIn() {
        return optIn;
    }

    @JsonProperty("OptIn")
    public void setOptIn(Boolean optIn) {
        this.optIn = optIn;
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
