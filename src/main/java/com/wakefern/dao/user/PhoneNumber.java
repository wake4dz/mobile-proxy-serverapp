
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
    "IsMobile",
    "FullNumber",
    "FullNumberFormatted",
    "Description",
    "PhoneNumberType"
})
public class PhoneNumber {

    @JsonProperty("IsMobile")
    private Boolean isMobile;
    @JsonProperty("FullNumber")
    private String fullNumber;
    @JsonProperty("FullNumberFormatted")
    private Object fullNumberFormatted;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("PhoneNumberType")
    private String phoneNumberType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("IsMobile")
    public Boolean getIsMobile() {
        return isMobile;
    }

    @JsonProperty("IsMobile")
    public void setIsMobile(Boolean isMobile) {
        this.isMobile = isMobile;
    }

    @JsonProperty("FullNumber")
    public String getFullNumber() {
        return fullNumber;
    }

    @JsonProperty("FullNumber")
    public void setFullNumber(String fullNumber) {
        this.fullNumber = fullNumber;
    }

    @JsonProperty("FullNumberFormatted")
    public Object getFullNumberFormatted() {
        return fullNumberFormatted;
    }

    @JsonProperty("FullNumberFormatted")
    public void setFullNumberFormatted(Object fullNumberFormatted) {
        this.fullNumberFormatted = fullNumberFormatted;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("PhoneNumberType")
    public String getPhoneNumberType() {
        return phoneNumberType;
    }

    @JsonProperty("PhoneNumberType")
    public void setPhoneNumberType(String phoneNumberType) {
        this.phoneNumberType = phoneNumberType;
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
