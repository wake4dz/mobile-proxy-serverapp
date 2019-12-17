package com.wakefern.checkout.payments;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import java.net.URI;

/**
 * Provides redirects for in-app payment gateway success/failure.
 * When the payment is complete, the payment gateway will direct the browser to
 * either the success/failure callback. This handler will then use the
 * custom app url scheme to notify the app.
 * 
 * @author sfk1j
 *
 */
@Path(ApplicationConstants.CheckoutNotify.CheckoutNotifyURL)
public class RedirectCheckout extends BaseService {
	private final static Logger logger = Logger.getLogger(GetOptions.class);
	private static URI successURI = null;
	private static URI failureURI = null;
	
	static {
		try {
			successURI = new URI(ApplicationConstants.CheckoutNotify.SuccessCallbackURL);		
			failureURI = new URI(ApplicationConstants.CheckoutNotify.FailureCallbackURL);		
		} catch (Exception e) {
			logger.error("Failed to set checkout notify URL's, " + e.getMessage());
		}
	}
    
	@GET
    @Path(ApplicationConstants.CheckoutNotify.SuccessPathURL)
    public Response getSuccess() {
		return Response.temporaryRedirect(successURI).build();
    }
	
	@GET
    @Path(ApplicationConstants.CheckoutNotify.FailurePathURL)
    public Response getFailure() {
		return Response.temporaryRedirect(failureURI).build();
    }
}


