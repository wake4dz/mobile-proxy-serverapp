package com.wakefern.api.proxy.wakefern.itemLocator;

import com.wakefern.wakefern.WakefernApplicationConstants;
import org.json.JSONObject;

/**
 * POJO for Item Locator data.
 */
public class ItemLocatorDto {
    private final int areaSeqNum;
    private final String sectionDesc;
    private final String aisle;

    private ItemLocatorDto(int areaSeqNum, String sectionDesc, String aisle) {
        this.aisle = aisle;
        this.sectionDesc = sectionDesc;
        this.areaSeqNum = areaSeqNum;
    }

    public static ItemLocatorDto fromJSON(JSONObject jsonObject) {
        String aisle = jsonObject.optString(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle);
        String sectionDesc = jsonObject.optString(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc);
        int areaSeqNum = jsonObject.optInt(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum);
        return new ItemLocatorDto(areaSeqNum, sectionDesc, aisle);
    }

    public JSONObject toJSON()
    {
        JSONObject newItemLocator = new JSONObject();
        newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle, aisle);
        newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum, areaSeqNum);
        if (sectionDesc == null) {
            newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc, JSONObject.NULL);
        } else {
            newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc,
                   sectionDesc);
        }
        return newItemLocator;
    }
}
