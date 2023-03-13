package com.wakefern.api.proxy.locai.recipes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.wakefern.global.EnvManager;

/*
 *  author:     Danny Zheng
 *  date:       6/16/2020
 *  purpose:    to retrieve the targeted recipe_xxx.json file in the classpath based on the recipe service setting
 */
public class CategoryJsonFileCache {
	private static String jsonCache = null;
	
	private static String errorMessage = null;
	
	//this static code is not run until the class is loaded into the memory for the first time
	//store the content of the recipe_xxx.json file to a static heap memory for quick access
	static {  
		InputStream is = null;
		BufferedReader br = null;
		
		try {
			String jsonFileName = null;
			
			//select which file to be sent based on recipe_service VCAP setting
			if (EnvManager.getRecipeService().trim().equalsIgnoreCase("Staging")) {
				jsonFileName = "recipe_qa.json";
			} else {
				jsonFileName = "recipe_prod.json";
			}

			is = CategoryJsonFileCache.class.getClassLoader().getResourceAsStream(jsonFileName);

			br = new BufferedReader(new InputStreamReader(is));

			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			
			jsonCache = sb.toString();
		
		} catch (Exception e) {
			errorMessage = "Error in loading the recipe_xxx.json file from the classpath: " + e.getLocalizedMessage();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
			
		}
	}

	public static String getJsonCache() throws Exception {
		//let the caller to handle any error
		if (errorMessage != null) {
			throw new Exception(errorMessage);
		}
		
		return jsonCache;
	}
	
}

