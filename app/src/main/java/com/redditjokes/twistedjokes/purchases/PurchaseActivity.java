package com.redditjokes.twistedjokes.purchases;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.redditjokes.twistedjokes.MainActivity;
import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.utils.CookiesHelper;
import com.redditjokes.twistedjokes.utils.SharedPreferences;
import java.util.ArrayList;
import java.util.List;



import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PurchaseActivity extends AppCompatActivity implements PurchasesUpdatedListener, AcknowledgePurchaseResponseListener {


    private static final String TAG = "PurchaseActivity";
    public static final String PRODUCT_IN_APP = Products.PRODUCT1;
    public static final String AFTER_PURCHASE_TEXT = "ENJOY";

    private Button removeAdsBtn;
    private BillingClient billingClient;
    private SkuDetails skuDetailsGlobal;
    private SharedPreferences sharedPreferences;
    private CookiesHelper cookiesHelper;
    private TextView messageTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucent(this);

        setContentView(R.layout.purchase_activity);
        messageTv = findViewById(R.id.removeAdsMessage);
        removeAdsBtn=findViewById(R.id.removeAdsBtn);
        sharedPreferences = new SharedPreferences(this);
        cookiesHelper = new CookiesHelper(this);



//        Glide.with(this).load(R.drawable.meeseeks_full)
//                .transform(new BlurTransformation(20,3))
//                .transition(withCrossFade())
//                .error(R.drawable.ic_pitcher)
//                .into(backgroundIv);




        initBillingProcess();


        removeAdsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(removeAdsBtn.getText().equals(getString(R.string.remove_ads))){

                    vibrate();
                    //launch Billing Flow
                    launchBillingScreen();
                }else if(removeAdsBtn.getText().equals(AFTER_PURCHASE_TEXT)){
                    //After Purchase UI
                    finish();
                }






            }
        });





    }

    private void vibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);

    }




    private void launchBillingScreen() {

        Log.d(TAG, "launchBillingScreen: "+skuDetailsGlobal);
        if(skuDetailsGlobal != null){


            Log.d(TAG, "launchBillingScreen: Attempting to launch Billing Screen");

            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetailsGlobal)

                    .build();
            BillingResult responseCode = billingClient
                    .launchBillingFlow(PurchaseActivity.this,flowParams);

            Log.d(TAG, "launchBillingScreen: Response Code : "+responseCode);
            if(responseCode.getResponseCode() == BillingResponse.ITEM_ALREADY_OWNED){
                AfterSubscriptionUI();
            }



        }
    }



    private void initBillingProcess() {
        //Billing Client Initiation and Products querying
        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        //




            //Subscriptions Are Supported
            //start connection
            Log.d(TAG, "onCreate: Subscription Supported on Device");

            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {


                    if (billingResult.getResponseCode() == BillingResponse.OK) {
                        // The BillingClient is ready. You can query purchases here.
                        Log.d(TAG, "onBillingSetupFinished: ");

                        //then we request for query products on Google Play
                        querySkuProducts();

                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                    //It's strongly recommended that you implement your own connection retry policy


                    //Error for billing service init
                    Log.d(TAG, "onBillingServiceDisconnected: ");
                }
            });




    }

    private void querySkuProducts() {

        List<String> skuList = new ArrayList<>();
        skuList.add(PRODUCT_IN_APP);//here we add the ID's of the products added in Play


        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();

        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        // Process the result.

                        Log.d(TAG, "onSkuDetailsResponse:  "+skuDetailsList.size());
                        if (billingResult.getResponseCode() == BillingResponse.OK && skuDetailsList != null) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                String sku = skuDetails.getSku();
                                String price = skuDetails.getPrice();
                                Log.d(TAG, "onSkuDetailsResponse: "+skuDetails.toString());
                                if (PRODUCT_IN_APP.equals(sku)) {
                                    skuDetailsGlobal = skuDetails;
                                    Log.d(TAG, "onSkuDetailsResponse: Both Are Same");
                                }
//                                } else if ("gas".equals(sku)) {
//                                    gasPrice = price;
//                                }
                            }
                        }
                    }
                });
    }










    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        // to receive updates on purchases initiated by your app, as well as those initiated by the Google Play Store.
        //Google Play calls the onPurchasesUpdated() method to deliver the result of the purchase operation

        if (billingResult.getResponseCode() == BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                //here we verify the purchase from a secure backend server and acknowledge purchase
                handlePurchase(purchase);

            }
        } else if (billingResult.getResponseCode() == BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            cookiesHelper.showCookie("You have Canceled The Subscription Process", "You can still enjoy a bunch of free content and subscribe later",null,null);

        } else {
            // Handle any other error codes.
            if(billingResult.getResponseCode() == BillingResponse.SERVICE_DISCONNECTED){
                cookiesHelper.showCookie("Service Disconnected", "Please Check Your Internet Connection And Try Again",null,null);

            }

            if(billingResult.getResponseCode() == BillingResponse.ERROR){
                cookiesHelper.showCookie("Error While Attempting Subscription", "Encountered a problem while subscribing. Please check your internet connection and try again",null,null);

            }
        }
    }




    private String getPurchaseState(int purchaseState) {



        if(purchaseState == 0){
            return "UNSPECIFIED_STATE";
        }
        else if(purchaseState == 1){
            return "PURCHASED";
        }else {
            //PENDING
            return "PENDING";
        }

    }

    @Override
    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {

        if(billingResult.getResponseCode() == BillingResponse.OK){
            //Whole Purchase Process is Complete

            Log.d(TAG, "onAcknowledgePurchaseResponse: "+billingResult.getResponseCode());
            Log.d(TAG, "onAcknowledgePurchaseResponse: "+billingResult.getDebugMessage());
        }
    }

    private void gotoMainActivity(){
        Intent intent = new Intent(PurchaseActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    void handlePurchase(Purchase purchase) {

        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {

            // Acknowledge the purchase if it hasn't already been acknowledged.
            acknowledgeThePurchase(purchase);

            // Grant entitlement to the user.
            unlockTheContent();
            //After unlocking we finish the activity
            AfterSubscriptionUI();
            Log.d(TAG, "handlePurchase: Purchase Completed. Awaiting Acknowledgement");


        }
    }

    private void AfterSubscriptionUI(){
        cookiesHelper.showCookie("Ads Removed","",null,null);
        messageTv.setText("Purchase Completed Successfully. You can enjoy your ad-free app now. Cheers!");
        removeAdsBtn.setText(AFTER_PURCHASE_TEXT);
    }


    private void acknowledgeThePurchase(Purchase purchase) {

        if (!purchase.isAcknowledged()) {
            AcknowledgePurchaseParams acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .setDeveloperPayload(purchase.getDeveloperPayload())
                            .build();
            billingClient.acknowledgePurchase(acknowledgePurchaseParams, this);
        }

    }



    private void loadImage(int image, ImageView mainIv) {

        try {
            Glide.with(this).load(image)
                    .transition(withCrossFade())
                    .into(mainIv);

        }catch (Exception e){

            cookiesHelper.showCookie("Please check your internet connection and restart the app"
            ,"",null,null);

        }

    }



    private void unlockTheContent() {
        //change isSubscribed Variable to True
        sharedPreferences.saveBooleanPref(getString(R.string.isPaidUser),true);
    }


}
