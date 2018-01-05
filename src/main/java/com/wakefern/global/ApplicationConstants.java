package com.wakefern.global;

import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

public final class ApplicationConstants {

    public static final String xmlAcceptType  = "text/xml";
    public static final int xmlTabAmount      = 4;
    public static final String ErrorMessage   = "ErrorMessage";

    public static class StringConstants{
        public static final String address = "/address";
        public static final String all = "/all";
        public static final String authenticate = "/authenticate";
        public static final String backSlash = "/";
        public static final String billing = "/billing";
        public static final String categories = "/categories";
        public static final String category = "/category";
        public static final String city = "/city";
        public static final String cities = "/cities";
        public static final String circulars = "/circulars";
        public static final String circularItem = "/circular-item";
        public static final String chainid = "/chainid";
        public static final String changedOrder = "/changed/order";
        public static final String comments = "/comments";
        public static final String contact = "/contact";
        public static final String dates = "/dates";
        public static final String delivery = "/delivery";
        public static final String distrcit = "/district";
        public static final String DistrictJSON = "District";
        public static final String duplicate = "/duplicate";
        public static final String email = "/email";
        public static final String emailParam = "?email=";
        public static final String fakeJson = "{\"Test\":\"Test\"}";
        public static final String featured = "/featured";
        public static final String fulfillment = "/fulfillment";
        public static final String fsn = "/fsn";
        public static final String FulfillmentJSON = "Fulfillment";
        public static final String fulfillments = "/fulfillments";
        public static final String fq = "&fq=";
        public static final String guest = "/guest";
        public static final String id = "id";
        public static final String Id = "Id";
        public static final String id1 = "?id1=";
        public static final String isMember = "?isMember";
        public static final String isMemberAmp = "&isMember";
        public static final String item = "/item";
        public static final String itemCount = "ItemCount";
        public static final String items = "/items";
        public static final String list = "/list";
        public static final String lists = "/lists";
        public static final String mergeFrom = "/merge-from";
        public static final String messages = "/messages";
        public static final String name = "&name=";
        public static final String nutrition = "/nutrition";
        public static final String page = "/page";
        public static final String pages = "/pages";
        public static final String pastPurchases = "/pastPurchases";
        public static final String payment = "/payment";
        public static final String pickup = "/pickup";
        public static final String postalCode = "/postalCode";
        public static final String promocode = "/promocode";
        public static final String queryParam = "?q=";
        public static final String radius = "/radius";
        public static final String recipe = "/recipe";
        public static final String region = "/region";
        public static final String regions = "/regions";
        public static final String referFriend = "/refer/friend";
        public static final String review = "/review";
        public static final String search = "/search";
        public static final String sessid = "/sessid";
        public static final String settings = "/settings";
        public static final String size = "/size";
        public static final String skip = "&skip=";
        public static final String sku = "/sku";
        public static final String sort = "&sort=";
        public static final String special = "/special";
        public static final String store = "/store";
        public static final String stores = "/stores";
        public static final String storeid = "/storeid";
        public static final String subscription = "/subscription";
        public static final String substitutions = "/substitutions";
        public static final String suggest = "/suggest";
        public static final String take = "?take=";
        public static final String takeAmp = "&take=";
        public static final String times = "/times";
        public static final String toCart = "/to/cart";
        public static final String twenty = "20";
        public static final String unit = "/unit";
        public static final String user = "/user";
        public static final String users = "/users";
        public static final String variations = "/variations";
        public static final String zero = "0";
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
        
        public static class Rewards{
            public static final String Points = MWGApplicationConstants.Requests.Rewards.Points;
            public static final String BasePointsURL = MWGApplicationConstants.Requests.Rewards.baseURL;
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
