package com.wakefern.account.users;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.XMLtoJSONConverter;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 10/20/16.
 */
@Path(ApplicationConstants.Requests.Registration.UserRegistration)
public class UserRegistration extends BaseService {
    @POST
    /**
     <User>
     <Email>test@mywebgrocer.com</Email>
     <FirstName>Joe</FirstName>
     <LastName>Grocer</LastName>
     <Password>aaaaaa</Password>
     <ChainId>139</ChainId>
     <PhoneNumbers>
     <Home>1234567890</Home>
     <Mobile>1234567890</Mobile>
     </PhoneNumbers>
     <Addresses>
     <Billing>
     <Address1>20 Winooski Falls Way</Address1>
     <Address2></Address2>
     <Address3></Address3>
     <City>Winooski</City>
     <Region>VT</Region>
     <PostalCode>05404</PostalCode>
     <CountryCode>USA</CountryCode>
     </Billing>
     <Shipping>
     <Address1>20 Winooski Falls Way</Address1>
     <Address2></Address2>
     <Address3></Address3>
     <City>Winooski</City>
     <Region>VT</Region>
     <PostalCode>05404</PostalCode>
     <CountryCode>USA</CountryCode>
     </Shipping>
     </Addresses>
     <PreferredStore>
     <Id>1</Id>
     </PreferredStore>
     <EmailPreferences>
     <EmailProgram>Newsletter</EmailProgram>
     <EmailProgram>Coupons</EmailProgram>
     </EmailPreferences>
     <FSN>412312341234</FSN>
     </User>
     */
    public Response getInfoResponse(@HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        try {
            String path = prepareResponse(authToken);
            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setServiceMappingv1(this, jsonBody);
            String xml = HTTPRequest.executePost(path, secondMapping.getGenericBody(), secondMapping.getgenericHeader());
            XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
            return this.createValidResponse(xmLtoJSONConverter.convert(xml));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String jsonBody, String authToken) throws Exception, IOException {
        String path = prepareResponse(authToken);
        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, jsonBody);
        String xml = HTTPRequest.executePost(path, secondMapping.getGenericBody(), secondMapping.getgenericHeader());
        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
        return xmLtoJSONConverter.convert(xml);
    }

    private String prepareResponse(String authToken) throws Exception{
    	
    		this.requestToken = ApplicationConstants.Requests.Tokens.planningToken;
        return this.requestPath = "https://service.shoprite.com" + ApplicationConstants.Requests.Registration.UserRegistration;
    }

    public UserRegistration(){  this.requestHeader = new MWGHeader(); }
}
