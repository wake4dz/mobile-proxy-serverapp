package com.wakefern.global;

import com.wakefern.mywebgrocer.MWGApplicationConstants;

public final class ApplicationConstants {
    public static final String testUser				    = "bbrosemer@gmail.com";
    public static final String password				    = "fuzzy2345";
    public static final String jsonResponseType		    = "application/json";
    public static final String jsonAcceptType		    = "application/json";
    public static final String xmlAcceptType            = "text/xml";
    public static final String authToken 				= "FE8803F0-D4FA-4AFF-B688-1A3BD5915FAA";
    public static final int xmlTabAmount                = 4;

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
        public static final String email = "/email";
        public static final String featured = "/featured";
        public static final String fulfillment = "/fulfillment";
        public static final String fulfillments = "/fulfillments";
        public static final String guest = "/guest";
        public static final String item = "/item";
        public static final String items = "/items";
        public static final String lists = "/lists";
        public static final String messages = "/messages";
        public static final String nutrition = "/nutrition";
        public static final String pickup = "/pickup";
        public static final String promocode = "/promocode";
        public static final String queryParam = "?q=";
        public static final String recipe = "/recipe";
        public static final String review = "/review";
        public static final String search = "/search";
        public static final String settings = "/settings";
        public static final String sku = "/sku";
        public static final String special = "/special";
        public static final String store = "/store";
        public static final String stores = "/stores";
        public static final String subscription = "/subscription";
        public static final String substitutions = "/substitutions";
        public static final String suggest = "/suggest";
        public static final String times = "/times";
        public static final String toCart = "/to/cart";
        public static final String user = "/user";
        public static final String users = "/users";
        public static final String variations = "/variations";
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
        }

        public static final class Recipes{
            public static final String RecipeChain = MWGApplicationConstants.Requests.Recipes.RecipeChain;
        }
    }

    public static class MapVariables{
        public static final String contentType = "Content-type";
        public static final String requestType = "text/xml";
        public static final String auth = "Authorization";
        public static final String authToken = "2433581F-B723-4FCD-BAFF-006791F48027";
    }
}
