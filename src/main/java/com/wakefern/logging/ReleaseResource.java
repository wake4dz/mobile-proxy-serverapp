package com.wakefern.logging;

import java.util.ListIterator;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.mywebgrocer.MWGApplicationConstants;

/* 
 *  author: Danny zheng
 *  date:   7/23/2018
 *  
 *  To display all the current and prior release info
 */
@Path(MWGApplicationConstants.Log.release)
public class ReleaseResource {

	final static Logger logger = Logger.getLogger(ReleaseResource.class);

	@GET
	@Path(MWGApplicationConstants.Log.releaseLevel)
	public Response getReleaseLevel(@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
		
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		} 
		
		// use ListIterator to iterate List in reverse order
		ListIterator<Release> listIterator = ReleaseUtil.getReleases().listIterator(ReleaseUtil.getReleases().size());
		
		StringBuffer sb = new StringBuffer();
		
		if (LogUtil.ipAddress == null) {
			sb.append("The current IP address: " + "null\n\n");			
		} else {
			sb.append("The current IP address: " + LogUtil.ipAddress.toString() + "\n\n");
		}
		
		while (listIterator.hasPrevious()) {
			Release release = listIterator.previous();
			
			sb.append("The release level: " + release.getReleaseLevel() + "\n");
			sb.append("The release date: " + release.getReleaseDate() + "\n");
			sb.append("The release description: " + release.getReleaseDescription() + "\n\n");
		}
		
		return Response.status(200).entity(sb.toString()).build();
		
	}

}
