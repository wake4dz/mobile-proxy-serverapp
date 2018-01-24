package com.wakefern.global;

import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

public final class ApplicationConstants {

    public static final String xmlAcceptType  = "text/xml";
    public static final int xmlTabAmount      = 4;
    public static final String ErrorMessage   = "ErrorMessage";

    public static class StringConstants {
        public static final String fq = "&fq=";
        public static final String isMember = "?isMember";
        public static final String isMemberAmp = "&isMember";
        public static final String payment = "/payment";
        public static final String skip = "&skip=";
        public static final String sort = "&sort=";
        public static final String store = "/store";
        public static final String takeAmp = "&take=";
    }

    public static class Requests{
        public static String baseURLV5 = MWGApplicationConstants.baseURL;
        public static String baseURLV1 = MWGApplicationConstants.baseURLv1;
        public static String serviceURLV1 =  MWGApplicationConstants.serviceURLv1;
        public static String buildErrorJsonOpen = "{\"ErrorMessage\":\"";
        public static String buildErrorJsonClose = "\"}";
        public static String forbiddenError = "Session is not valid";

        public static class Header{
            public static final String contentType  = "Content-Type";
            public static final String contentAccept = "Accept";
            public static final String contentAuthorization = "Authorization";
        }

        public static class Tokens{
            public static final String couponToken = "noowhTBIYfzVrXOcFrNSwIFbkMoqRh19";
            public static final String planningToken = "486806CF-CF9A-4087-8C05-ED1B0008AF03";
        }

        public static final class Registration{
            public static final String UserRegistration = MWGApplicationConstants.Requests.Registration.UserRegistration;
        }

        public static final class Coupons{
            public static final String GetPPCCoupons = WakefernApplicationConstants.Requests.Coupons.Metadata.PPCCoupons;
            public static final String GetCoupons = WakefernApplicationConstants.Requests.Coupons.Metadata.Metadata;
            public static final String GetCouponId = WakefernApplicationConstants.Requests.Coupons.ListId.CouponId;
            public static final String GetCouponIdByPPC = WakefernApplicationConstants.Requests.Coupons.ListId.CouponByPPC;
            public static final String CouponAddPPC = WakefernApplicationConstants.Requests.Coupons.ListId.CouponAddPPC;
            public static final String BaseCouponURL = WakefernApplicationConstants.Requests.Coupons.baseURL;
            public static final String GetCouponsRecommendations = WakefernApplicationConstants.Requests.Coupons.Metadata.MetadataRecommendations;
        }

        public static final class Wakefern{
            public static final String ItemLocator = MWGApplicationConstants.Requests.Wakefern.ItemLocator;
            public static final String ItemLocatorAuth = MWGApplicationConstants.Requests.Wakefern.ItemLocatorAuth;
            public static final String ItemLocatorJson = MWGApplicationConstants.Requests.Wakefern.ItemLocatorJson;
        }

        public static class Recommendations{
            public static final String ProductRecommendations = MWGApplicationConstants.Requests.Recommendations.ProductRecommendations;
            public static final String BaseRecommendationsURL = MWGApplicationConstants.Requests.Recommendations.baseURL; 
            public static final String UPCRecommendations = MWGApplicationConstants.Requests.Recommendations.UPCRecommendations;
            public static final String BaseUPCRecommendationsURL = MWGApplicationConstants.Requests.Recommendations.baseUPC_URL;
        }
        
    }

    public static class shoppingListItemPost{
        public static final String contentType = "application/vnd.mywebgrocer.grocery-list+json";
        public static final String contentAccept = "*/*";
    }
    
    public static class Payment{
        public static final String AllowsMultiple = "AllowsMultiple";
        public static final String Amount = "Amount";
        public static final String AmountLabel = "AmountLabel";
        public static final String CancelCallbackUri = "CancelCallbackUri";
        public static final String CancelCallbackURL = "/checkout/ProcessPayment?authorized=False";
        public static final String CardNumber = "CardNumber";
        public static final String CardNumberLabel = "CardNumberLabel";
        public static final String FulfillmentType = "FulfillmentType";
        public static final String HardCodedId = "59";
        public static final String Id = "Id";
        public static final String Image = "Image";
        public static final String IsVendor = "IsVendor";
        public static final String Items = "Items";
        public static final String MaximumLength = "MaximumLength";
        public static final String MinimumLength = "MinimumLength";
        public static final String Name = "Name";
        public static final String PayMethodTooltipUri = "PayMethodTooltipUri";
        public static final String PaymentMethods = "PaymentMethods";
        public static final String PaymentMethodMessage = "PaymentMethodMessage";
        public static final String PrimaryOption = "PrimaryOption";
        public static final String RequiresAmount = "RequiresAmount";
        public static final String RequiredNumeric = "RequiredNumeric";
        public static final String RequiresCardNumber = "RequiresCardNumber";
        public static final String SuccessCallbackUri = "SuccessCallbackUri";
        public static final String SuccessCallbackURL = "/checkout/ProcessPayment?authorized=True";
    }

    public static class AisleItemLocator{
        public static final String Aisle = "Aisle";
        public static final String area_desc = "wf_area_desc";
        public static final String Items = "Items";
        public static final String Other = "OTHER";
        public static final String Sku = "Sku";
        public static final String upc_13_num = "upc_13_num"; //
        public static final String WakefernAuth = "eyJleHAiOjE0NzYxMDQyMTM1NDYsInN1YiI6InNmamMxcGFzc3dkIiwiaXNzIjoiaHR0cDovL3dha2VmZXJuLmNvbSJ9";
        public static final String item_locations = "item_locations";
        public static final String area_seq_num = "area_seq_num";
        public static final String wf_area_code = "wf_area_code";
        public static final String TotalPrice = "TotalPrice";
        public static final String RegularPrice = "RegularPrice";
        public static final String Quantity = "Quantity";
        public static final String CurrentPrice = "CurrentPrice";
        public static final String Size = "Size";
        public static final String Sale = "Sale";
        public static final String LimitText = "LimitText";
    }
}
