package com.wakefern.dao.product;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestProductMain {

	public static void main(String[] args) {
		System.out.println("Hello World!");
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			ProductDetail productDetail = mapper.readValue(new File("product.json"), ProductDetail.class);
//			System.out.println(productDetail.getId());
//			System.out.println(productDetail.getCurrentUnitPrice());//.getdgetDateText());
//		} catch (JsonParseException e) {
//			System.out.println("JsonParseException");
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			System.out.println("JsonMappingException");
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			System.out.println("IOException");
//			e.printStackTrace();
//		}
		String test = "Save $.50 On Crystal Farms Chunk Cheese";
		String [] testArr = test.split(" ");
//		for(int i = 0; i< testArr.length && testArr[i].contains("$"); i++){
//			System.out.println("value: "+testArr[i]);
//		}
		for(String testSt : testArr){
			if(testSt.contains("$")){
				System.out.println("value: "+testSt);
				break;
			}
		}
	}

}
