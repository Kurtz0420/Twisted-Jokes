package com.redditjokes.twistedjokes.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.redditjokes.twistedjokes.R;

public class NativeAdHelper {
    private static final String TAG = "NativeAdHelper";

    private Context context;

    public NativeAdHelper(Context context) {
        this.context = context;

        MobileAds.initialize(context);
    }


    public void loadNative(final FrameLayout frameLayout, final int native_ad_item_layout) {

        AdLoader adLoader = new AdLoader.Builder(frameLayout.getContext(), context.getString(R.string.native_ad_test_id))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        //the native ad will be available inside this method  (unifiedNativeAd)

                        LayoutInflater inflater = LayoutInflater.from(frameLayout.getContext());

                        UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView)
                                inflater.inflate(native_ad_item_layout, null);

                        mapUnifiedNativeAdToLayout(unifiedNativeAd, unifiedNativeAdView);

                        frameLayout.removeAllViews();
                        frameLayout.addView(unifiedNativeAdView);
                    }
                })
                .withAdListener(NativeAdListener)
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }



    public void mapUnifiedNativeAdToLayout(UnifiedNativeAd adFromGoogle, UnifiedNativeAdView myAdView) {
        MediaView mediaView = myAdView.findViewById(R.id.ad_media);
        myAdView.setMediaView(mediaView);

        myAdView.setHeadlineView(myAdView.findViewById(R.id.ad_headline));
        myAdView.setBodyView(myAdView.findViewById(R.id.ad_body));
        myAdView.setCallToActionView(myAdView.findViewById(R.id.ad_call_to_action));
        myAdView.setIconView(myAdView.findViewById(R.id.ad_icon));
        myAdView.setPriceView(myAdView.findViewById(R.id.ad_price));
        myAdView.setStarRatingView(myAdView.findViewById(R.id.ad_rating));
        myAdView.setStoreView(myAdView.findViewById(R.id.ad_store));
        myAdView.setAdvertiserView(myAdView.findViewById(R.id.ad_advertiser));

        ((TextView) myAdView.getHeadlineView()).setText(adFromGoogle.getHeadline());

        if (adFromGoogle.getBody() == null) {
            myAdView.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getBodyView()).setText(adFromGoogle.getBody());
        }

        if (adFromGoogle.getCallToAction() == null) {
            myAdView.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((Button) myAdView.getCallToActionView()).setText(adFromGoogle.getCallToAction());
        }

        if (adFromGoogle.getIcon() == null) {
            myAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) myAdView.getIconView()).setImageDrawable(adFromGoogle.getIcon().getDrawable());
        }

        if (adFromGoogle.getPrice() == null) {
            myAdView.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getPriceView()).setText(adFromGoogle.getPrice());
        }

        if (adFromGoogle.getStarRating() == null) {
            myAdView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) myAdView.getStarRatingView()).setRating(adFromGoogle.getStarRating().floatValue());
        }

        if (adFromGoogle.getStore() == null) {
            myAdView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getStoreView()).setText(adFromGoogle.getStore());
        }

        if (adFromGoogle.getAdvertiser() == null) {
            myAdView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getAdvertiserView()).setText(adFromGoogle.getAdvertiser());
        }

        myAdView.setNativeAd(adFromGoogle);
    }

    AdListener NativeAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            Log.d(TAG, "onAdLoaded: Native ");
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
            Log.d(TAG, "onAdFailedToLoad: native");
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
            Log.d(TAG, "onAdOpened: Native");
        }

        @Override
        public void onAdClicked() {
            // Code to be executed when the user clicks on an ad.
            Log.d(TAG, "onAdClicked: Native");
        }

        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
            Log.d(TAG, "onAdLeftApplication: Native");
        }

        @Override
        public void onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on an ad.
//            AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                    .build();
//            adView.loadAd(adRequest);
            Log.d(TAG, "onAdClosed: Native");

        }
    };

}
