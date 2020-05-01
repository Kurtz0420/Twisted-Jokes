package com.redditjokes.twistedjokes.ourjokes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.api.models.OurJoke;
import com.redditjokes.twistedjokes.utils.NativeAdHelper;
import com.redditjokes.twistedjokes.utils.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class JokesAdapter2 extends PagerAdapter {

    private static final String TAG = "JokesAdapter";

    private Context context;
    private List<OurJoke> jokesList;

    private JokeAdapterCallbacks jokeAdapterCallbacks;
    private SharedPreferences sharedPreferences;

    public JokesAdapter2(Context context, JokeAdapterCallbacks jokeAdapterCallbacks, SharedPreferences sharedPreferences) {
    this.context = context;
    this.jokesList = new ArrayList<>();
    this.jokeAdapterCallbacks = jokeAdapterCallbacks;
    this.sharedPreferences = sharedPreferences;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater mInflater = LayoutInflater.from(context);

        View item_view ;

        if(!sharedPreferences.getBooleanPref("isPaidUser",false)) {
        //if user is not paid we make user of position to show ads
            //otherwise we show ad at every position

            if(position % 5 == 0 && position != 0 ){
                //inflate native ad layout
                item_view = mInflater.inflate(R.layout.native_ad_holder, container, false);
                FrameLayout frameLayout = item_view.findViewById(R.id.id_native_ad_frame);
                setDataForNativeAd(frameLayout);


            }else {
                item_view = mInflater.inflate(R.layout.our_joke_item_layout2, container, false);

                SwipeButton revealBtn = item_view.findViewById(R.id.swipe_btn);
                final ConstraintLayout rootLayout = item_view.findViewById(R.id.rootLayout_our_joke_item);


                TextView buildupTv = item_view.findViewById(R.id.our_joke_buildupTv);
                final TextView deliveryTv = item_view.findViewById(R.id.our_joke_deliveryTv);

                buildupTv.setText(jokesList.get(position).getBuild_up());
                deliveryTv.setText(jokesList.get(position).getDelivery());
                deliveryTv.setVisibility(View.INVISIBLE);

                revealBtn.setOnStateChangeListener(new OnStateChangeListener() {
                    @Override
                    public void onStateChange(boolean active) {


                        if(active){

                            slideDown(deliveryTv);
                            jokeAdapterCallbacks.onRevealClick();

                        }else {
                            slideUp(deliveryTv);
                        }


                    }
                });

            }

        }else {

            //user isPaid, so we show joke at every position
            item_view = mInflater.inflate(R.layout.our_joke_item_layout2, container, false);

            SwipeButton revealBtn = item_view.findViewById(R.id.swipe_btn);
            final ConstraintLayout rootLayout = item_view.findViewById(R.id.rootLayout_our_joke_item);


            TextView buildupTv = item_view.findViewById(R.id.our_joke_buildupTv);
            final TextView deliveryTv = item_view.findViewById(R.id.our_joke_deliveryTv);

            buildupTv.setText(jokesList.get(position).getBuild_up());
            deliveryTv.setText(jokesList.get(position).getDelivery());
            deliveryTv.setVisibility(View.INVISIBLE);

            revealBtn.setOnStateChangeListener(new OnStateChangeListener() {
                @Override
                public void onStateChange(boolean active) {


                    if(active){

                        slideDown(deliveryTv);
                        jokeAdapterCallbacks.onRevealClick();

                    }else {
                        slideUp(deliveryTv);
                    }


                }
            });

        }






        container.addView(item_view);
        return item_view;
    }



    private void setDataForNativeAd(final FrameLayout frameLayout) {

        if(!sharedPreferences.getBooleanPref("isPaidUser",false)){

            NativeAdHelper nativeAdHelper = new NativeAdHelper(frameLayout.getContext());
            nativeAdHelper.loadNative(frameLayout,R.layout.native_ad_item_layout);
        }


    }


    public void slideUp(final TextView view){

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight()/4,  // fromYDelta
                - view.getHeight());                // toYDelta
        animate.setDuration(250);

        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view.setVisibility(View.INVISIBLE);



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animate);

    }

    public void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                - view.getHeight() ,                 // fromYDelta
                view.getHeight()/4); // toYDelta
        animate.setDuration(250);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void animationUsingConstraintSet() {
//        final boolean[] set = {false};
//        final ConstraintSet constraint1 = new ConstraintSet();
//        constraint1.clone(revealRootLayout);
//        final ConstraintSet constraint2 = new ConstraintSet();
//        constraint2.clone(context, R.layout.snippet_reveal_after_btn);
//
//
//        revealBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    TransitionManager.beginDelayedTransition(revealRootLayout);
//
//                    ConstraintSet constraint = set[0] ?  constraint1 : constraint2;
//                    constraint.applyTo(revealRootLayout);
//                    set[0] = !set[0];
//                }
//            }
//        });
    }


    @Override
    public int getCount() {
        return jokesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if(!sharedPreferences.getBooleanPref("isPaidUser",false)) {
            //user is not paid
            if(position % 5 == 0 && position != 0 ){
                container.removeView((FrameLayout) object);
            }else {
                container.removeView((ConstraintLayout) object);
            }

        }else {
            //user isPaid
            container.removeView((ConstraintLayout) object);
        }

    }

    public void addjokes(List<OurJoke> jokes){
        int lastCount = getCount();
        jokesList.addAll(jokes);
//        notifyItemRangeInserted(lastCount, jokes.size());
    }


    public interface JokeAdapterCallbacks{

        void onRevealClick();
    }


}
