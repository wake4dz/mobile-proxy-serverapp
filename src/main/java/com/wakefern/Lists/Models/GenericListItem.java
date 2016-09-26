package com.wakefern.Lists.Models;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by brandyn.brosemer on 9/22/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericListItem {
    private static String UserItemType = "userdefined";

    @JsonProperty("ItemType")
    private String ItemType = UserItemType;
    @JsonProperty("ItemKey")
    private String ItemKey = "";
    @JsonProperty("Note")
    private String Note = "";
    @JsonProperty("Quantity")
    private Integer Quantity = 0;


}
