package com.redditjokes.twistedjokes.userjokes.epoxy.controllers;







import android.util.Log;
import android.view.View;

import androidx.room.Database;

import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.OnModelClickListener;
import com.airbnb.epoxy.Typed4EpoxyController;
import com.redditjokes.twistedjokes.api.models.OurJoke;
import com.redditjokes.twistedjokes.api.models.UserJoke;
import com.redditjokes.twistedjokes.room2.DatabaseTransactions;
import com.redditjokes.twistedjokes.userjokes.epoxy.holders.HeaderHolder;
import com.redditjokes.twistedjokes.userjokes.epoxy.holders.UserJokeHolder;
import com.redditjokes.twistedjokes.userjokes.epoxy.models.HeaderModel_;
import com.redditjokes.twistedjokes.userjokes.epoxy.models.LoadingModel_;
import com.redditjokes.twistedjokes.userjokes.epoxy.models.NativeAdModel_;
import com.redditjokes.twistedjokes.userjokes.epoxy.models.UserJokeModel_;
import com.redditjokes.twistedjokes.utils.SharedPreferences;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UserJokesController extends Typed4EpoxyController<List<UserJoke>, Boolean, HashMap<Integer, Integer>, String> {

    private static final String TAG = "UserJokesController";





    //17-34-51-68-85-102 - positions for categories
    //ads start from 7-14-21-28-34-41-49.....
    //37, tags
    //FurtherMore we eliminate even numbers from above




    private DatabaseTransactions databaseTransactions;
    private SharedPreferences sharedPreferences;

    public UserJokesController(AdapterCallbacks callbacks, DatabaseTransactions databaseTransactions, SharedPreferences sharedPreferences) {
        this.callbacks=callbacks;
        this.databaseTransactions = databaseTransactions;
        this.sharedPreferences= sharedPreferences;

    }

    public AdapterCallbacks callbacks;



    public interface AdapterCallbacks {
        /*Add CallBacks for every viewtype/nested category/ads*/
        void addJoke(HeaderModel_ model, int position, View clickedView);
        void filterJokes(HeaderModel_ model, int position, View clickedView);
        void settingsJoke(HeaderModel_ model, int position, View clickedView);
        void upvoteClicked(UserJokeModel_ model, int position, View clickedView);
        void downVoteClick(UserJokeModel_ model, int position, View clickedView);
//        void articleClicked(ArticleModel_ articleModel_, int position, CardView clickedView);
//        void onSubscribeClicked();

    }






    @Override
    protected void buildModels(List<UserJoke> jokeList, Boolean isLoadingMore, HashMap<Integer, Integer> pageNumberHash, String firstItemId) {




        for(UserJoke joke : jokeList){

            if(joke.getId().equals(firstItemId)){
                add(new HeaderModel_()
                .id(getRandomNumber())
                        .addJokeBtnClickListener(addJokeClickListener)
                        .filterbtnClickListener(filterJokesClickListener)
                        .settingsBtnClickListener(settingsClickListener)
                );
            }


            int ad_position = getJokeIndex(jokeList,joke);
            if(ad_position % 5 == 0 && ad_position != 0){
                Log.d(TAG, "buildModels: Ad loaded at position : "+ad_position);

                if(!sharedPreferences.getBooleanPref("isPaidUser",false)){

                    add(new NativeAdModel_()
                            .id(joke.getId() + getRandomNumber()));

                }

            }

            add(new UserJokeModel_()
            .id(joke.getId())
            .joke(joke)
            .UpvoteClickListener(upvoteClickLitener)
                    .DownvoteClicklistener(downvoteClickLitener)
                    .databaseTransactions(databaseTransactions)
            );


        }

        if(isLoadingMore){
            add(new LoadingModel_()
                    .id("LoadingModel")
            );
        }


    }

    private int getJokeIndex(List<UserJoke> list,UserJoke ourJoke) {
        return list.indexOf(ourJoke);
    }

    public int getRandomNumber(){
        int min = 100;
        int max = 8000;

        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;

        return i1;
    }

    @Override
    public void setData(List<UserJoke> jokes, Boolean isLoadingMore, HashMap<Integer, Integer> pageNumberHash, String firstItemId) {
        super.setData(jokes, isLoadingMore, pageNumberHash,firstItemId);
    }

    EpoxyModel.SpanSizeOverrideCallback OneSpanSIze=new EpoxyModel.SpanSizeOverrideCallback() {
        @Override
        public int getSpanSize(int totalSpanCount, int position, int itemCount) {
            //totalSpanCount will give us 1 span size
            return totalSpanCount;
        }
    };

    OnModelClickListener<HeaderModel_, HeaderHolder> addJokeClickListener=new OnModelClickListener<HeaderModel_, HeaderHolder>() {
        @Override
        public void onClick(HeaderModel_ model, HeaderHolder parentView, View clickedView, int position) {
            callbacks.addJoke(model,position,clickedView);
        }
    };

    OnModelClickListener<HeaderModel_, HeaderHolder> filterJokesClickListener=new OnModelClickListener<HeaderModel_, HeaderHolder>() {
        @Override
        public void onClick(HeaderModel_ model, HeaderHolder parentView, View clickedView, int position) {
            callbacks.filterJokes(model,position,clickedView);
        }
    };

    OnModelClickListener<HeaderModel_, HeaderHolder> settingsClickListener=new OnModelClickListener<HeaderModel_, HeaderHolder>() {
        @Override
        public void onClick(HeaderModel_ model, HeaderHolder parentView, View clickedView, int position) {
            callbacks.settingsJoke(model,position,clickedView);
        }
    };


    OnModelClickListener<UserJokeModel_, UserJokeHolder> upvoteClickLitener=new OnModelClickListener<UserJokeModel_, UserJokeHolder>() {
        @Override
        public void onClick(UserJokeModel_ model, UserJokeHolder parentView, View clickedView, int position) {
            callbacks.upvoteClicked(model,position,clickedView);
        }
    };


    OnModelClickListener<UserJokeModel_, UserJokeHolder> downvoteClickLitener=new OnModelClickListener<UserJokeModel_, UserJokeHolder>() {
        @Override
        public void onClick(UserJokeModel_ model, UserJokeHolder parentView, View clickedView, int position) {
            callbacks.downVoteClick(model,position,clickedView);
        }
    };





}
