package com.redditjokes.twistedjokes.room2;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DatabaseTransactions {
    private static final String TAG = "DatabaseTransactions";

    private Context context;
    private LocalDataSource mLocalDataSource;
    private BookmarksViewModel mBookmarksViewModel;
    private CompositeDisposable mDisposibles=new CompositeDisposable();

    public DatabaseTransactions(Context context) {
        this.context = context;

        mLocalDataSource=new LocalDataSource(context,CreateAndOpenCallBack);
        mBookmarksViewModel =new BookmarksViewModel(mLocalDataSource);
    }

    public Completable addBookmark(final BookmarkEntity bookmarkEntity){

        Log.d(TAG, "addPostFav: adding new note to database");

        return mBookmarksViewModel.addBookmark(bookmarkEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<BookmarkEntity>> getAllBookmarks(){
        return mBookmarksViewModel
                .getAllBookmarks()
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<BookmarkEntity>> getBookmarksByThumbsUp(){
        return mBookmarksViewModel
                .getBookmarksByThumbsUp()
                .subscribeOn(Schedulers.io());
    }


    public Observable<List<BookmarkEntity>> getBookmarksByThumbsDown(){
        return mBookmarksViewModel
                .getBookmarksByThumbsDown()
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteBookmark(final String joke_id){

        return mBookmarksViewModel.deleteBookmark(joke_id)
                .subscribeOn(Schedulers.io());

    }

    public Completable updateBookmark(BookmarkEntity bookmarkEntity){
        return mBookmarksViewModel.
                updateBookmark(bookmarkEntity)
                .subscribeOn(Schedulers.io());
    }

    public Observable<BookmarkEntity> getBookmark(String joke_id){
        return mBookmarksViewModel.
                getBookmark(joke_id)
                .subscribeOn(Schedulers.io());
    }


    public List<BookmarkEntity> getBookmarkByThumbsUpIfPresent(String joke_id){
        return mBookmarksViewModel.
                getBookmarkByThumbsUpIfPresent(joke_id);
    }

    public List<BookmarkEntity> getBookmarkIfPresent(String joke_id){
        return mBookmarksViewModel.
                getBookmarkIfPresent(joke_id);
    }


    public List<BookmarkEntity> getBookmarkByThumbsDownIfPresent(String joke_id){
        return mBookmarksViewModel.
                getBookmarkByThumbsDownIfPresent(joke_id);
    }







    RoomDatabase.Callback CreateAndOpenCallBack=new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Log.d("CallBack", "onCreate: database callBack onCreate");

        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            //calls everytime database is opened


        }

    };






}
