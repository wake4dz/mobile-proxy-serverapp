package com.wakefern.api.proxy.locai.recipes;

import com.wakefern.global.Futures;
import com.wakefern.global.VcapProcessor;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RecipeUtils {
	private static final int MAX_THREAD_POOL_SIZE = VcapProcessor.getRecipeShelfThreadPoolSize();

	/**
	 * Creates a cached thread pool with a limit of MAX_THREAD_POOL_SIZE threads.
	 * Threads are created as needed up to the limit, and kept around idle for 10 minutes.
	 */
	private static final ExecutorService sThreadPoolExecutor = new ThreadPoolExecutor(0, MAX_THREAD_POOL_SIZE,
			10L, TimeUnit.MINUTES, new SynchronousQueue<>());

	private static final Logger logger = LogManager.getLogger(RecipeUtils.class);

	private static Map<String, String> doReq(String id, String jsonBody) throws Exception {
		AtomicReference<Map<String,String>> response = new AtomicReference<>();
		logger.debug("Sending search request on thread " + Thread.currentThread().getName());
		String res = SearchRecipes.searchRecipes(jsonBody);
		Map<String, String> mapping = new HashMap<>();
		mapping.put(id, res);
		response.set(mapping);
		return response.get();
	}

	/**
	 * Fetch recipes associated with a shelf.
	 * @param dishTagId
	 * @param dishQuery
	 * @param jsonBody
	 * @return
	 */
	private static CompletableFuture<Map<String, String>> fetchShelf(String dishTagId,
																	String dishQuery,
																	String jsonBody)
    {
		JSONObject jsonArgs = new JSONObject(jsonBody);

		String tagId = dishQuery != null && !dishQuery.isEmpty() ? dishQuery : dishTagId;
		logger.debug("DishtagId: " + tagId);
		jsonArgs.put("query", JSONObject.NULL);
		jsonArgs.put("rankingType", "Shelf");
		// Override with null if userId is an empty string
		if (jsonArgs.has("userId") && jsonArgs.getString("userId").isEmpty()) {
			jsonArgs.put("userId", JSONObject.NULL);
		}
		ArrayList<String> filterTagIds = new ArrayList<>();
		if (!tagId.isEmpty())
			filterTagIds.add(tagId);
		jsonArgs.put("filterTagIds", filterTagIds);

		return CompletableFuture.supplyAsync(() -> {
			try {
				return doReq(tagId, jsonArgs.toString());
			}
			catch (Exception e) {
				logger.error("Error on searchRecipe request " + e.getMessage());
				throw new CompletionException(e);
			}
		}, sThreadPoolExecutor);
	}

	public static JSONArray fetchAllShelfRecipes(JSONArray layouts, String jsonBody) throws Exception {
		List<CompletableFuture<Map<String, String>>> completableFutures = new ArrayList<>();

		Instant start = Instant.now();
		final int len = layouts.length();

		for (int i = 0; i < len; i++) {
			JSONObject layout = layouts.getJSONObject(i);

			final String dishQuery = layout.getString("dish_query");
			final String dishTagId = layout.getString("dish_tag_id");

			CompletableFuture<Map<String, String>> completableFuture = fetchShelf(dishTagId, dishQuery, jsonBody);
			completableFutures.add(completableFuture);
		}

		List<Map<String,String>> r = Futures.getAllCompleted(completableFutures, VcapProcessor.getApiMediumTimeout());

		Instant end = Instant.now();
		logger.debug("Took: " + DurationFormatUtils.formatDurationHMS(Duration.between(start, end).toMillis()));

		final Optional<Map<String, String>> reduce = r.stream().reduce((firstMap, secondMap) -> {
			return Stream.concat(firstMap.entrySet().stream(), secondMap.entrySet().stream())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
							(countInFirstMap, countInSecondMap) -> countInFirstMap + countInSecondMap));
		});

		Map<String, String> shelfIdRecipesMap = reduce.get();

		for (int i = 0; i < len; i++) {
			// hydrate "recipe_data" property in each "shelf" layout
			JSONObject layout = layouts.getJSONObject(i);
			final String dishQuery = layout.getString("dish_query");
			final String dishTagId = layout.getString("dish_tag_id");

			final String id = dishQuery != null && !dishQuery.isEmpty() ? dishQuery : dishTagId;
			if (shelfIdRecipesMap.containsKey(id)) {
				layout.put("recipe_data", new JSONArray(shelfIdRecipesMap.get(id)));
			}
		}
		return layouts;
	}
}
