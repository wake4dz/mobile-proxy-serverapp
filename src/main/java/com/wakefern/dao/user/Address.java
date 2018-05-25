
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
    "Id",
    "AddressLine1",
    "AddressLine2",
    "AddressLine3",
    "City",
    "Region",
    "CountryCode",
    "PostalCode",
    "FirstName",
    "LastName",
    "IsDefaultShipping",
    "WasAddressValidated",
    "DeliveryPointId",
    "NeighborhoodId",
    "IsDefaultBilling",
    "PhoneNumbers"
})
public class Address {

    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("AddressLine1")
    private String addressLine1;
    @JsonProperty("AddressLine2")
    private Object addressLine2;
    @JsonProperty("AddressLine3")
    private Object addressLine3;
    @JsonProperty("City")
    private String city;
    @JsonProperty("Region")
    private String region;
    @JsonProperty("CountryCode")
    private String countryCode;
    @JsonProperty("PostalCode")
    private String postalCode;
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("IsDefaultShipping")
    private Boolean isDefaultShipping;
    @JsonProperty("WasAddressValidated")
    private Boolean wasAddressValidated;
    @JsonProperty("DeliveryPointId")
    private Object deliveryPointId;
    @JsonProperty("NeighborhoodId")
    private Object neighborhoodId;
    @JsonProperty("IsDefaultBilling")
    private Boolean isDefaultBilling;
    @JsonProperty("PhoneNumbers")
    private List<PhoneNumber> phoneNumbers = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("AddressLine1")
    public String getAddressLine1() {
        return addressLine1;
    }

    @JsonProperty("AddressLine1")
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @JsonProperty("AddressLine2")
    public Object getAddressLine2() {
        return addressLine2;
    }

    @JsonProperty("AddressLine2")
    public void setAddressLine2(Object addressLine2) {
        this.addressLine2 = addressLine2;
    }

    @JsonProperty("AddressLine3")
    public Object getAddressLine3() {
        return addressLine3;
    }

    @JsonProperty("AddressLine3")
    public void setAddressLine3(Object addressLine3) {
        this.addressLine3 = addressLine3;
    }

    @JsonProperty("City")
    public String getCity() {
        return city;
    }

    @JsonProperty("City")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("Region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("Region")
    public void setRegion(String region) {
        this.region = region;
    }

    @JsonProperty("CountryCode")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("CountryCode")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @JsonProperty("PostalCode")
    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("PostalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @JsonProperty("FirstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("FirstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("LastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("LastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("IsDefaultShipping")
    public Boolean getIsDefaultShipping() {
        return isDefaultShipping;
    }

    @JsonProperty("IsDefaultShipping")
    public void setIsDefaultShipping(Boolean isDefaultShipping) {
        this.isDefaultShipping = isDefaultShipping;
    }

    @JsonProperty("WasAddressValidated")
    public Boolean getWasAddressValidated() {
        return wasAddressValidated;
    }

    @JsonProperty("WasAddressValidated")
    public void setWasAddressValidated(Boolean wasAddressValidated) {
        this.wasAddressValidated = wasAddressValidated;
    }

    @JsonProperty("DeliveryPointId")
    public Object getDeliveryPointId() {
        return deliveryPointId;
    }

    @JsonProperty("DeliveryPointId")
    public void setDeliveryPointId(Object deliveryPointId) {
        this.deliveryPointId = deliveryPointId;
    }

    @JsonProperty("NeighborhoodId")
    public Object getNeighborhoodId() {
        return neighborhoodId;
    }

    @JsonProperty("NeighborhoodId")
    public void setNeighborhoodId(Object neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }

    @JsonProperty("IsDefaultBilling")
    public Boolean getIsDefaultBilling() {
        return isDefaultBilling;
    }

    @JsonProperty("IsDefaultBilling")
    public void setIsDefaultBilling(Boolean isDefaultBilling) {
        this.isDefaultBilling = isDefaultBilling;
    }

    @JsonProperty("PhoneNumbers")
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    @JsonProperty("PhoneNumbers")
    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
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
