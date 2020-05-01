package com.redditjokes.twistedjokes.purchases;

public class BillingResponse {

    //Billing API version is not supported for the type requested
    public static final int BILLING_UNAVAILABLE = 3;

    //Invalid arguments provided to the API. This error can also indicate that the application was not correctly signed
    public static final int DEVELOPER_ERROR  = 5;

    //Fatal error during the API action
    public static final int ERROR = 6;

    //Requested feature is not supported by Play Store on the current device.
    public static final int FEATURE_NOT_SUPPORTED = -2;

    //Failure to purchase since item is already owned
    public static final int ITEM_ALREADY_OWNED = 7;

    //Failure to consume since item is not owned
    public static final int ITEM_NOT_OWNED = 8;

    //Requested product is not available for purchase
    public static final int ITEM_UNAVAILABLE = 4;

    //Success
    public static final int OK = 0;

    //Play Store service is not connected now - potentially transient state.
    public static final int SERVICE_DISCONNECTED = -1 ;

    //Network connection is down
    public static final int SERVICE_UNAVAILABLE = 2;

    //User pressed back or canceled a dialog
    public static final int USER_CANCELED = 1;

}
