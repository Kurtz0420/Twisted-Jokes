package com.redditjokes.twistedjokes.room2;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.api.JokesApi;
import com.redditjokes.twistedjokes.api.models.OurJoke;
import com.redditjokes.twistedjokes.api.models.UserJoke;
import com.redditjokes.twistedjokes.utils.AppExecutors;
import com.redditjokes.twistedjokes.utils.CookiesHelper;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DatabaseEntryHelper {

    /*
    * This class will validate joke before entrering into database
    * - plus will also be responsible for entry of bookmarks, upvote and downvotes in database
    * */

    private static final String TAG = "DatabaseEntryHelper";

    private DatabaseTransactions databaseTransactions;
    private Context context;
    private JokesApi jokesApi;
    private CookiesHelper cookiesHelper;

    public DatabaseEntryHelper(DatabaseTransactions databaseTransactions, Context context, JokesApi jokesApi, CookiesHelper cookiesHelper) {
        this.databaseTransactions = databaseTransactions;
        this.context = context;
        this.jokesApi = jokesApi;
        this.cookiesHelper = cookiesHelper;
    }

    public static BookmarkEntity buildEntryForBookmark(String joke_id, String build_up, String delivery){


        BookmarkEntity bookmarkEntity = new BookmarkEntity();
        bookmarkEntity.setJoke_id(joke_id);
        bookmarkEntity.setBuild_up(build_up);
        bookmarkEntity.setDelivery(delivery);
        bookmarkEntity.setBookmark(true);
        bookmarkEntity.setThumbsUp(false);
        bookmarkEntity.setThumbsDown(false);

        return bookmarkEntity;
    }

    public static BookmarkEntity buildEntryForBookmarkByThumbsUp(String joke_id, String build_up, String delivery){


        BookmarkEntity bookmarkEntity = new BookmarkEntity();
        bookmarkEntity.setJoke_id(joke_id);
        bookmarkEntity.setBuild_up(build_up);
        bookmarkEntity.setDelivery(delivery);
        bookmarkEntity.setBookmark(false);
        bookmarkEntity.setThumbsUp(true);
        bookmarkEntity.setThumbsDown(false);

        return bookmarkEntity;
    }


    public static BookmarkEntity buildEntryForBookmarkByThumbsDown(String joke_id, String build_up, String delivery){


        BookmarkEntity bookmarkEntity = new BookmarkEntity();
        bookmarkEntity.setJoke_id(joke_id);
        bookmarkEntity.setBuild_up(build_up);
        bookmarkEntity.setDelivery(delivery);
        bookmarkEntity.setBookmark(false);
        bookmarkEntity.setThumbsUp(false);
        bookmarkEntity.setThumbsDown(true);

        return bookmarkEntity;
    }

    public void downVoteJoke(final UserJoke userJoke,final OurJoke ourJoke, final View clickedView){
        //we check if user has already downvoted the joke, if not then we call increment and enterinDb
        //in addition we'll check, if the user has upvoted firstly or not, if upvoted, we remove that entry and enter into downvoted

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<BookmarkEntity> bookmarkEntities = null;

                if(userJoke != null){

                    bookmarkEntities = databaseTransactions.getBookmarkByThumbsUpIfPresent(userJoke.getId());

                }else {

                    bookmarkEntities = databaseTransactions.getBookmarkByThumbsUpIfPresent(ourJoke.getId());

                }

                if(bookmarkEntities.size() > 0){
                    //joke already exists in upvoted, then delete the entry and proceed to downvote process
//                    deleteAndInitDownVote(joke,clickedView);
                    showToast("You can rate the joke only once");

                }else {
                    //init DownVote Process
                    initDownVoteProcess(userJoke,ourJoke,clickedView);
                }

            }
        });
    }


    public void bookmarkJoke(final OurJoke ourJoke, final ImageView favBtn){
        //we check if the joke is already bookmarked, if not we mark it

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<BookmarkEntity> bookmarkEntities = databaseTransactions.getBookmarkIfPresent(ourJoke.getId());

                if(bookmarkEntities.size() > 0){
                    //joke already bookmarked
                    showToast("Already bookmarked");
                }else {
                    //bookmark the joke
                    addBookmarkToDb(DatabaseEntryHelper.buildEntryForBookmark(ourJoke.getId(),ourJoke.getBuild_up(),ourJoke.getDelivery()));
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(favBtn != null){
                                favBtn.setBackground(context.getResources().getDrawable(R.drawable.button_background_our_joke2));
                            }
                        }
                    });
                }
            }
        });
    }

    public void upVoteJoke(final UserJoke userJoke, final OurJoke ourJoke, final View clickedView){

        //we check if user has already upvoted the joke, if not then we call increment and enterinDb
        //first we check if there is entry in downvote bookmarks
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                List<BookmarkEntity> bookmarkEntities = null;
                if(userJoke !=null){

                    bookmarkEntities = databaseTransactions.getBookmarkByThumbsDownIfPresent(userJoke.getId());
                }
                if(ourJoke != null){
                    bookmarkEntities = databaseTransactions.getBookmarkByThumbsDownIfPresent(ourJoke.getId());
                }

                if(bookmarkEntities.size() > 0 ){
                    //we have an entry in downVotes

                    showToast("You can rate the joke only once");

//                    deleteAndInitUpVote(joke,clickedView);
                }else {
                    //Not Present

                    initUpvoteProcess(userJoke,ourJoke,clickedView);
                }

            }
        });
    }



    private void incrementUpvote(UserJoke userJoke, OurJoke ourJoke) {

        if(userJoke != null){
            int thumbsUp = userJoke.getThumbs_ups() + 1;
            userJoke.setThumbs_ups(thumbsUp);
            updateUserJoke(userJoke);
        }

        if(ourJoke != null){
            int thumbsUp = ourJoke.getThumbs_ups() + 1;
            ourJoke.setThumbs_ups(thumbsUp);
            updateOurJoke(ourJoke);
        }

    }

    private void addEntryToDbInThumbsUp(UserJoke userJoke, OurJoke ourJoke) {

        if(userJoke != null){
            addBookmarkToDb(DatabaseEntryHelper.buildEntryForBookmarkByThumbsUp(userJoke.getId(),userJoke.getBuild_up(),userJoke.getDelivery()));

        }else {
            addBookmarkToDb(DatabaseEntryHelper.buildEntryForBookmarkByThumbsUp(ourJoke.getId(),ourJoke.getBuild_up(),ourJoke.getDelivery()));

        }

    }

    private void incrementDownvote(UserJoke userJoke, OurJoke ourJoke) {



        if(userJoke != null){
            int thumbsUp = userJoke.getThumbs_downs() + 1;
            userJoke.setThumbs_downs(thumbsUp);
            updateUserJoke(userJoke);
        }

        if(ourJoke != null){
            int thumbsUp = ourJoke.getThumbs_downs() + 1;
            ourJoke.setThumbs_downs(thumbsUp);
            updateOurJoke(ourJoke);
        }
    }

    private void updateUserJoke(UserJoke userJoke){

        jokesApi.updateUserJoke(userJoke, userJoke.getId())
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserJoke>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserJoke userJoke) {

                        if(userJoke != null){
                            Toast.makeText(context, "UserJoke updated", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Error while updating Thumbs Up");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void updateOurJoke(OurJoke userJoke){

        jokesApi.updateOurJoke(userJoke, userJoke.getId())
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OurJoke>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(OurJoke userJoke) {

                        if(userJoke != null){
                            Toast.makeText(context, "OurJoke updated", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Error while updating Thumbs Up");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void addEntryToDbInThumbsDown(UserJoke userJoke, OurJoke ourJoke) {

        if(userJoke != null){
            addBookmarkToDb(DatabaseEntryHelper.buildEntryForBookmarkByThumbsDown(userJoke.getId(),userJoke.getBuild_up(),userJoke.getDelivery()));

        }else {
            addBookmarkToDb(DatabaseEntryHelper.buildEntryForBookmarkByThumbsDown(ourJoke.getId(),ourJoke.getBuild_up(),ourJoke.getDelivery()));

        }
    }

    private void addBookmarkToDb(BookmarkEntity bookmarkEntity) {


        //make this method retrun void so we can use executors for it

        databaseTransactions.addBookmark(bookmarkEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        mDisposibles.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(context, "Added to bookmarks", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Error While adding to bookmarks : "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void initDownVoteProcess(final UserJoke userJoke, final OurJoke ourJoke, final View clickedView){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<BookmarkEntity> bookmarkEntities = databaseTransactions.getBookmarkByThumbsDownIfPresent(userJoke.getId());

                if(bookmarkEntities.size() > 0){
                    //joke already downvoted
                    showToast("You can rate the joke only once");
                }else {

                    //downvote and enter the joke in db
                    incrementDownvote(userJoke,ourJoke);
                    addEntryToDbInThumbsDown(userJoke,ourJoke);

                    if(context != null){


                        changeColorToPressed(clickedView);
                    }
                }
            }
        });


    }



    private void showToast(final String message){
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {

                cookiesHelper.showCookie("POPUP!!",message,null,null);
            }
        });
    }

    private void initUpvoteProcess(final UserJoke userJoke, final OurJoke ourJoke, final View clickedView) {


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                List<BookmarkEntity> bookmarkEntities = null;
                if(userJoke != null){
                    bookmarkEntities = databaseTransactions.getBookmarkByThumbsUpIfPresent(userJoke.getId());

                }

                if(ourJoke != null){
                    bookmarkEntities = databaseTransactions.getBookmarkByThumbsUpIfPresent(ourJoke.getId());

                }

                if(bookmarkEntities.size() > 0){
                    //joke already downvoted

                    showToast("You can rate the joke only once");


                }else {

                    //upvote and enter the joke in db
                    incrementUpvote(userJoke,ourJoke);
                    addEntryToDbInThumbsUp(userJoke,ourJoke);

                    if(context != null){

                        changeColorToPressed(clickedView);
                    }
                }
            }
        });

    }


    private void changeColorToPressed(final View view){
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                view.setBackground(context.getResources().getDrawable(R.drawable.button_background_our_joke2));

            }
        });
    }


}
