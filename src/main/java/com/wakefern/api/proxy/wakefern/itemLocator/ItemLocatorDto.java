package com.wakefern.api.proxy.wakefern.itemLocator;

import com.wakefern.wakefern.WakefernApplicationConstants;
import org.json.JSONObject;

/**
 * POJO for Item Locator data.
 */
public class ItemLocatorDto {
    private int areaSeqNum;
    private String sectionDesc;
    private String aisle;
    private String aisleStore;
    private int sectionShelfNum;
    private int shelfPositionNum;

    public ItemLocatorDto() {
    }
    
    public ItemLocatorDto(int areaSeqNum, String sectionDesc, String aisle, String aisleStore, int sectionShelfNum, int shelfPositionNum) {
        this.aisle = aisle;
        this.sectionDesc = sectionDesc;
        this.areaSeqNum = areaSeqNum;
        this.aisleStore = aisleStore;
        this.sectionShelfNum = sectionShelfNum;
        this.shelfPositionNum = shelfPositionNum;
    }

    
    public int getAreaSeqNum() {
		return areaSeqNum;
	}


	public void setAreaSeqNum(int areaSeqNum) {
		this.areaSeqNum = areaSeqNum;
	}


	public String getSectionDesc() {
		return sectionDesc;
	}


	public void setSectionDesc(String sectionDesc) {
		this.sectionDesc = sectionDesc;
	}


	public String getAisle() {
		return aisle;
	}


	public void setAisle(String aisle) {
		this.aisle = aisle;
	}


	public String getAisleStore() {
		return aisleStore;
	}


	public void setAisleStore(String aisleStore) {
		this.aisleStore = aisleStore;
	}


	public int getSectionShelfNum() {
		return sectionShelfNum;
	}


	public void setSectionShelfNum(int sectionShelfNum) {
		this.sectionShelfNum = sectionShelfNum;
	}


	public int getShelfPositionNum() {
		return shelfPositionNum;
	}


	public void setShelfPositionNum(int shelfPositionNum) {
		this.shelfPositionNum = shelfPositionNum;
	}


	@Override
	public String toString() {
		return "ItemLocatorDto [areaSeqNum=" + areaSeqNum + ", sectionDesc=" + sectionDesc + ", aisle=" + aisle
				+ ", aisleStore=" + aisleStore + ", sectionShelfNum=" + sectionShelfNum + ", shelfPositionNum="
				+ shelfPositionNum + "]";
	}


	public static ItemLocatorDto fromJSON(JSONObject jsonObject) {
        String aisle = jsonObject.optString(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle);
        String sectionDesc = jsonObject.optString(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc);
        int areaSeqNum = jsonObject.optInt(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum);
        String aisleStore = jsonObject.optString(WakefernApplicationConstants.Mi9V8ItemLocator.AisleStore);
        int sectionShelfNum  = jsonObject.optInt(WakefernApplicationConstants.Mi9V8ItemLocator.SectionShelfNum);
        int shelfPositionNum  = jsonObject.optInt(WakefernApplicationConstants.Mi9V8ItemLocator.ShelfPositionNum);
        return new ItemLocatorDto(areaSeqNum, sectionDesc, aisle, aisleStore, sectionShelfNum, shelfPositionNum);
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
       
        if (aisleStore == null) {
        	newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleStore, JSONObject.NULL);
        } else {
        	newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleStore, aisleStore);
        }
        newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.SectionShelfNum, sectionShelfNum);
        newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.ShelfPositionNum, shelfPositionNum);
        
        return newItemLocator;
    }
}
