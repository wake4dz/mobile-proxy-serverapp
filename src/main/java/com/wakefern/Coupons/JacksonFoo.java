package com.wakefern.Coupons;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@JsonFilter("filter properties by name")  
class PropertyFilterMixIn {}  

class Bar  
{  
public String id = "42";  
public String name = "Fred";  
public String color = "blue";  
public Foo foo = new Foo();  
}  

class Foo  
{  
public String id = "99";  
public String size = "big";  
public String height = "tall";  
}  

public class JacksonFoo  
{  
public static void main(String[] args) throws Exception  
{  
ObjectMapper mapper = new ObjectMapper();  
//mapper.getSerializationConfig().addMixInAnnotations(  
//    Object.class, PropertyFilterMixIn.class);  

String[] ignorableFieldNames = { "id", "color" };  
FilterProvider filters = new SimpleFilterProvider()  
  .addFilter("filter properties by name",   
      SimpleBeanPropertyFilter.serializeAllExcept(  
          ignorableFieldNames));  
ObjectWriter writer = mapper.writer(filters);  

System.out.println(writer.writeValueAsString(new Bar()));  
// output:  
// {"name":"James","foo":{"size":"big","height":"tall"}}  
}  
} 