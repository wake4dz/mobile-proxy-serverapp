
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
    "Email",
    "AdIdentifier",
    "FirstName",
    "LastName",
    "MiddleInitial",
    "PhoneNumbers",
    "Title",
    "BirthDate",
    "FrequentShopperNumber",
    "PreferredStoreId",
    "OnlineShoppingStoreId",
    "AcceptTerms",
    "Addresses",
    "EmailPreferences",
    "Options",
    "LinkedExternalAuthenticationProviders"
})
public class UserProfileDAO {

    @JsonProperty("Email")
    private String email;
    @JsonProperty("AdIdentifier")
    private String adIdentifier;
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("MiddleInitial")
    private Object middleInitial;
    @JsonProperty("PhoneNumbers")
    private List<PhoneNumber> phoneNumbers = null;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("BirthDate")
    private Object birthDate;
    @JsonProperty("FrequentShopperNumber")
    private String frequentShopperNumber;
    @JsonProperty("PreferredStoreId")
    private String preferredStoreId;
    @JsonProperty("OnlineShoppingStoreId")
    private String onlineShoppingStoreId;
    @JsonProperty("AcceptTerms")
    private Boolean acceptTerms;
    @JsonProperty("Addresses")
    private List<Address> addresses = null;
    @JsonProperty("EmailPreferences")
    private EmailPreferences emailPreferences;
    @JsonProperty("Options")
    private List<Object> options = null;
    @JsonProperty("LinkedExternalAuthenticationProviders")
    private List<Object> linkedExternalAuthenticationProviders = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("Email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("AdIdentifier")
    public String getAdIdentifier() {
        return adIdentifier;
    }

    @JsonProperty("AdIdentifier")
    public void setAdIdentifier(String adIdentifier) {
        this.adIdentifier = adIdentifier;
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

    @JsonProperty("MiddleInitial")
    public Object getMiddleInitial() {
        return middleInitial;
    }

    @JsonProperty("MiddleInitial")
    public void setMiddleInitial(Object middleInitial) {
        this.middleInitial = middleInitial;
    }

    @JsonProperty("PhoneNumbers")
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    @JsonProperty("PhoneNumbers")
    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @JsonProperty("Title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("Title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("BirthDate")
    public Object getBirthDate() {
        return birthDate;
    }

    @JsonProperty("BirthDate")
    public void setBirthDate(Object birthDate) {
        this.birthDate = birthDate;
    }

    @JsonProperty("FrequentShopperNumber")
    public String getFrequentShopperNumber() {
        return frequentShopperNumber;
    }

    @JsonProperty("FrequentShopperNumber")
    public void setFrequentShopperNumber(String frequentShopperNumber) {
        this.frequentShopperNumber = frequentShopperNumber;
    }

    @JsonProperty("PreferredStoreId")
    public String getPreferredStoreId() {
        return preferredStoreId;
    }

    @JsonProperty("PreferredStoreId")
    public void setPreferredStoreId(String preferredStoreId) {
        this.preferredStoreId = preferredStoreId;
    }

    @JsonProperty("OnlineShoppingStoreId")
    public String getOnlineShoppingStoreId() {
        return onlineShoppingStoreId;
    }

    @JsonProperty("OnlineShoppingStoreId")
    public void setOnlineShoppingStoreId(String onlineShoppingStoreId) {
        this.onlineShoppingStoreId = onlineShoppingStoreId;
    }

    @JsonProperty("AcceptTerms")
    public Boolean getAcceptTerms() {
        return acceptTerms;
    }

    @JsonProperty("AcceptTerms")
    public void setAcceptTerms(Boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
    }

    @JsonProperty("Addresses")
    public List<Address> getAddresses() {
        return addresses;
    }

    @JsonProperty("Addresses")
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @JsonProperty("EmailPreferences")
    public EmailPreferences getEmailPreferences() {
        return emailPreferences;
    }

    @JsonProperty("EmailPreferences")
    public void setEmailPreferences(EmailPreferences emailPreferences) {
        this.emailPreferences = emailPreferences;
    }

    @JsonProperty("Options")
    public List<Object> getOptions() {
        return options;
    }

    @JsonProperty("Options")
    public void setOptions(List<Object> options) {
        this.options = options;
    }

    @JsonProperty("LinkedExternalAuthenticationProviders")
    public List<Object> getLinkedExternalAuthenticationProviders() {
        return linkedExternalAuthenticationProviders;
    }

    @JsonProperty("LinkedExternalAuthenticationProviders")
    public void setLinkedExternalAuthenticationProviders(List<Object> linkedExternalAuthenticationProviders) {
        this.linkedExternalAuthenticationProviders = linkedExternalAuthenticationProviders;
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
