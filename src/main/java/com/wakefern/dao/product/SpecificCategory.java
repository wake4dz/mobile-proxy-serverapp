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
    "CategoryName",
    "CategoryId",
    "UniversalCategoryName",
    "UniversalCategoryId",
    "IsSubStoreCategory"
})
public class SpecificCategory {

    @JsonProperty("CategoryName")
    private String categoryName;
    @JsonProperty("CategoryId")
    private Integer categoryId;
    @JsonProperty("UniversalCategoryName")
    private String universalCategoryName;
    @JsonProperty("UniversalCategoryId")
    private Integer universalCategoryId;
    @JsonProperty("IsSubStoreCategory")
    private Boolean isSubStoreCategory;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("CategoryName")
    public String getCategoryName() {
        return categoryName;
    }

    @JsonProperty("CategoryName")
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @JsonProperty("CategoryId")
    public Integer getCategoryId() {
        return categoryId;
    }

    @JsonProperty("CategoryId")
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @JsonProperty("UniversalCategoryName")
    public String getUniversalCategoryName() {
        return universalCategoryName;
    }

    @JsonProperty("UniversalCategoryName")
    public void setUniversalCategoryName(String universalCategoryName) {
        this.universalCategoryName = universalCategoryName;
    }

    @JsonProperty("UniversalCategoryId")
    public Integer getUniversalCategoryId() {
        return universalCategoryId;
    }

    @JsonProperty("UniversalCategoryId")
    public void setUniversalCategoryId(Integer universalCategoryId) {
        this.universalCategoryId = universalCategoryId;
    }

    @JsonProperty("IsSubStoreCategory")
    public Boolean getIsSubStoreCategory() {
        return isSubStoreCategory;
    }

    @JsonProperty("IsSubStoreCategory")
    public void setIsSubStoreCategory(Boolean isSubStoreCategory) {
        this.isSubStoreCategory = isSubStoreCategory;
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
