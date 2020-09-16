package com.wakefern.api.wakefern.items.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
* The item locator utility helper
*
* @author  Danny Zheng
* @version 1.0
* @since   2020-02-06
*/

public class SortItemLocators {
	private final static Logger logger = Logger.getLogger(SortItemLocators.class);

	/*
	 * 02/03/2020 Purpose: to sort items by AisleAreaSeqNum before return to the UI.
	 * UI would display as the default; sorting in UI for some old phones is slow.
	 */
	public static JSONArray sortItems(JSONArray jsonArray) throws Exception {

		List<JSONObject> list = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(jsonArray.getJSONObject(i));
		}

		Collections.sort(list, new AisleAreaJSONComparator());

		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			array.put(list.get(i));
		}
		
		return array;

	}

}

class AisleAreaJSONComparator implements Comparator<JSONObject> {

	@Override
	public int compare(JSONObject o1, JSONObject o2) {
		int seq1 = o1.getInt("AisleAreaSeqNum");
		int seq2 = o2.getInt("AisleAreaSeqNum");

		if (seq1 < 0) {
			return 1;
		} else {
			return (seq1 - seq2);
		}
	}

}