package com.wakefern.logging;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wakefern.mywebgrocer.MWGApplicationConstants;
/* 
 *  author: Danny zheng
 *  date:   7/23/2018
 *  
 *  To generated the error status summary for the email distribution
 */
public class EmailReportScheduleJob implements Job{ 
	final static Logger logger = LogManager.getLogger(EmailReportScheduleJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		if (LogUtil.isEmailOn == true) {
			StringBuffer sb = new StringBuffer();
			
		    for (Map.Entry<String, String> entry : LogUtil.toEmailAddressesMap.entrySet()) {
	            sb.append(entry.getKey() + ", " );
		    }
		    
		    if ((LogUtil.errorMap.size() == 0) && (LogUtil.error4xxMap.size() == 0)) {
		    	MailUtil.sendEmail("For the " + MWGApplicationConstants.getTargetAPI() + " " +
		    			LogUtil.emailNoErrorSubject, LogUtil.generateErrorReport());
		    } else {
		    	MailUtil.sendEmail("For the " + MWGApplicationConstants.getTargetAPI() + " " + 
		    			LogUtil.emailWithErrorSubject, LogUtil.generateErrorReport());
		    }
			

		} else {
			logger.warn("Error report email setting is OFF: no error report email!");
		}

		// reset the log tracking data after sending email
		LogUtil.currentDateTime = (new Date()).getTime();
		LogUtil.errorMap.clear();
		LogUtil.error4xxMap.clear();
	} 
}