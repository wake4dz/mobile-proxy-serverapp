package com.wakefern.logging;

import javax.servlet.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* 
 *  author: Danny zheng
 *  date:   7/23/2018
 *  
 *  To load the logUtil class into the Heap memory when the app starts up
 */
public class AppStartup implements ServletContextListener  {
	private final static Logger logger = LogManager.getLogger(AppStartup.class);
	
	// implement the required context init method
    public void contextInitialized(ServletContextEvent sce){
    	for (String message : LogUtil.getWelcomeMessages()) {
    		logger.info(message);
    	}
    	
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
    	
    }
}