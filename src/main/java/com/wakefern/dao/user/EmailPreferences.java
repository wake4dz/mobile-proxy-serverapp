
package com.wakefern.dao.user;

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
    "EmailProviderUserId",
    "UnsubscribeAll",
    "Preferences"
})
public class EmailPreferences {

    @JsonProperty("EmailProviderUserId")
    private String emailProviderUserId;
    @JsonProperty("UnsubscribeAll")
    private Boolean unsubscribeAll;
    @JsonProperty("Preferences")
    private List<Preference> preferences = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("EmailProviderUserId")
    public String getEmailProviderUserId() {
        return emailProviderUserId;
    }

    @JsonProperty("EmailProviderUserId")
    public void setEmailProviderUserId(String emailProviderUserId) {
        this.emailProviderUserId = emailProviderUserId;
    }

    @JsonProperty("UnsubscribeAll")
    public Boolean getUnsubscribeAll() {
        return unsubscribeAll;
    }

    @JsonProperty("UnsubscribeAll")
    public void setUnsubscribeAll(Boolean unsubscribeAll) {
        this.unsubscribeAll = unsubscribeAll;
    }

    @JsonProperty("Preferences")
    public List<Preference> getPreferences() {
        return preferences;
    }

    @JsonProperty("Preferences")
    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
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
