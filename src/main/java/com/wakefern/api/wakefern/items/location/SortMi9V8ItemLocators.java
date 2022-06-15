package com.wakefern.api.wakefern.items.location;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
* The Mi9 V8 item locator utility helper
*
* @author  Danny Zheng
* @version 1.0
* @since   2021-09-28
*/

public class SortMi9V8ItemLocators {
	private final static Logger logger = LogManager.getLogger(SortMi9V8ItemLocators.class);

	/*
	 * 02/03/2020 Purpose: to sort items by AisleAreaSeqNum before return to the UI.
	 * UI would display as the default; sorting in UI for some old phones is slow.
	 */
	public static JSONArray sortItems(JSONArray jsonArray) throws Exception {

		List<JSONObject> list = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(jsonArray.getJSONObject(i));
		}

		Collections.sort(list, new Mi9V8AisleAreaJSONComparator());

		JSONArray array = new JSONArray();
        for (JSONObject jsonObject : list) {
            array.put(jsonObject);
        }
		
		return array;

	}

}

class Mi9V8AisleAreaJSONComparator implements Comparator<JSONObject> {

	@Override
	public int compare(JSONObject o1, JSONObject o2) {
		int seq1 = o1.getJSONObject("itemLocator").getInt("aisleAreaSeqNum");
		int seq2 = o2.getJSONObject("itemLocator").getInt("aisleAreaSeqNum");
		
		if (seq1 < 0) {
			return 1;
		} else {
			return (seq1 - seq2);
		}
	}

}