package com.wakefern.global;

import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ApplicationConstants {


    public static final String testUser				    = "bbrosemer@gmail.com";
    public static final String password				    = "fuzzy2345";
    public static final String jsonResponseType		    = "application/json";
    public static final String jsonAcceptType		    = "application/json";
    public static final String xmlAcceptType            = "text/xml";
    public static final String authToken 				= "FE8803F0-D4FA-4AFF-B688-1A3BD5915FAA";
    public static final int xmlTabAmount                = 4;


    public static class AuthorizationTokens{
        public static class ApplicationTokenMapping{
            public static final String appQATokenName          = "App-QA-Token";
            public static final String appQAToken              = "U2mf35557JFo7LqPB]0#k9(Q{&%-!@";

            public static final String mwgV5TokenName          = "mwgProdToken";
        }


        public static class MWGAuthorizationTokens{

        }

        public static class WakefernAuthorizationTokens{

        }

        public static class ApplciationAuthorizationTokens{
            public static final Map<String,String> tokenMap;
            static{
                Map<String,String> aMap = new HashMap<>();
                aMap.put(ApplicationTokenMapping.appQATokenName,ApplicationTokenMapping.appQAToken);
                tokenMap = Collections.unmodifiableMap(aMap);
            }
        }
    }

    public static class StringConstants{
        public static final String address = "/address";
        public static final String all = "/all";
        public static final String backSlash = "/";
        public static final String billing = "/billing";
        public static final String categories = "/categories";
        public static final String category = "/category";
        public static final String circulars = "/circulars";
        public static final String comments = "/comments";
        public static final String contact = "/contact";
        public static final String dates = "/dates";
        public static final String delivery = "/delivery";
        public static final String duplicate = "/duplicate";
        public static final String email = "/email";
        public static final String featured = "/featured";
        public static final String fulfillment = "/fulfillment";
        public static final String fulfillments = "/fulfillments";
        public static final String guest = "/guest";
        public static final String Id = "Id";
        public static final String id1 = "?id1=";
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
        public static final String pickup = "/pickup";
        public static final String postalCode = "/postalCode";
        public static final String promocode = "/promocode";
        public static final String queryParam = "?q=";
        public static final String radius = "/radius";
        public static final String recipe = "/recipe";
        public static final String review = "/review";
        public static final String search = "/search";
        public static final String settings = "/settings";
        public static final String size = "/size";
        public static final String skip = "&skip=";
        public static final String sku = "/sku";
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

        public static class Header{
            public static final String contentType	= "Content-Type";
            public static final String contentAccept = "Accept";
            public static final String contentAuthorization	= "Authorization";
        }

        public static class Authentication{
            public static final String Authenticate = MWGApplicationConstants.Requests.Authentication.Authenticate;
            public static final String Authenticatev1 = MWGApplicationConstants.Requests.Authentication.AuthenticateV1;
        }

        public static class Categories{
            public static final String CategoriesFromStoreId = MWGApplicationConstants.Requests.Categories.CategoriesFromStoreId;
            public static final String Subcategories = MWGApplicationConstants.Requests.Categories.SubCategories;
            public static final String ProductId = MWGApplicationConstants.Requests.Categories.ProductId;
            public static final String ProductStore = MWGApplicationConstants.Requests.Categories.ProductStore;
            public static final String ProductsStore = MWGApplicationConstants.Requests.Categories.ProductsStore;
            public static final String ProductCategory = MWGApplicationConstants.Requests.Categories.ProductCategory;
        }

        public static class Circular{
            public static final String Categories = MWGApplicationConstants.Requests.Circular.Categories;
        }

        public static class Checkout{
            public static final String Checkout = MWGApplicationConstants.Requests.Checkout.Checkout;
            public static final String Payments = MWGApplicationConstants.Requests.Checkout.Payments;
            public static final String UserCheckout = MWGApplicationConstants.Requests.Checkout.UserCheckout;
            public static final String UserOrder = MWGApplicationConstants.Requests.Checkout.UserOrder;
            public static final String Order = MWGApplicationConstants.Requests.Checkout.Order;
        }

        public static final class Cart{
            public static final String CartAuth = MWGApplicationConstants.Requests.Cart.CartAuth;
            public static final String CartUser = MWGApplicationConstants.Requests.Cart.CartUser;
        }

        public static final class Shop{
            public static final String ShopStore = MWGApplicationConstants.Requests.Shop.ShopStore;
            public static final String ShopUser = MWGApplicationConstants.Requests.Shop.ShopUser;
        }

        public static final class ShoppingLists{
            public static final String slChains = MWGApplicationConstants.Requests.ShoppingLists.slChains;
            public static final String slUser = MWGApplicationConstants.Requests.ShoppingLists.slUser;
            public static final String slItemsUser = MWGApplicationConstants.Requests.ShoppingLists.slItemsUser;
        }

        public static final class Recipes{
            public static final String RecipeChain = MWGApplicationConstants.Requests.Recipes.RecipeChain;
        }

        public static final class Coupons{
            public static final String GetCoupons = WakefernApplicationConstants.Requests.Coupons.Metadata.Metadata;
            public static final String GetCouponId = WakefernApplicationConstants.Requests.Coupons.ListId.CouponId;
            public static final String BaseCouponURL = WakefernApplicationConstants.Requests.Coupons.baseURL;
        }

        public static final class Planning{
            public static final String StoreLocator = MWGApplicationConstants.Requests.Planning.StoreLocator;
        }
    }

    public static class MapVariables{
        public static final String contentType = "Content-type";
        public static final String requestType = "text/xml";
        public static final String auth = "Authorization";
        public static final String authToken = "2433581F-B723-4FCD-BAFF-006791F48027";
    }

    public static class recipeSearch{
        public static final String recipes = "Recipes";
        public static final String recipeSummary = "RecipeSummary";
        public static final String description = "Description";
        public static final String name = "Name";
        public static final String category = "Category";
        public static final String categories = "Categories";
        public static final String subCategories = "Subcategories";
        public static final String id = "Id";
    }

    public static class shoppingListItemPost{
        public static final String contentType = "application/vnd.mywebgrocer.grocery-list+json";
        public static final String contentAccept = "*/*";
    }

    public static class ProductSearch{
        public static final String items = "Items";
        public static final String facets = "Facets";
        public static final String itemCount = "ItemCount";
        public static final String activeFilters = "ActiveFilters";
        public static final String recentFilters = "RecentFilters";
        public static final String sortLinks = "SortLinks";
        public static final String pages = "Pages";
        public static final String pageLinks = "PageLinks";
        public static final String skip = "Skip";
        public static final String take = "Take";
        public static final String totalQuantity = "TotalQuantity";
        public static final String links = "Links";
        public static final String moreAvailiable = "MoreAvailiable";
    }
}
