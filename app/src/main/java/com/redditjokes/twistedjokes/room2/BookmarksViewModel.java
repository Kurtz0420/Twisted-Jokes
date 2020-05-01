package com.redditjokes.twistedjokes.room2;

import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class BookmarksViewModel extends ViewModel {


    private LocalDataSource mLocalDataSource;

    public BookmarksViewModel(LocalDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public Observable<List<BookmarkEntity>> getAllBookmarks(){
        return mLocalDataSource.getAllBookmarks();
    }

    public Completable updateBookmark(BookmarkEntity bookmarkEntity){
        return mLocalDataSource.updateBookmark(bookmarkEntity);
    }

    public Observable<BookmarkEntity> getBookmark(String  joke_id){
        return mLocalDataSource.getBookmark(joke_id);
    }

    public List<BookmarkEntity> getBookmarkByThumbsUpIfPresent(String  joke_id){
        return mLocalDataSource.getBookmarkByThumbsUpIfPresent(joke_id);
    }

    public List<BookmarkEntity> getBookmarkByThumbsDownIfPresent(String  joke_id){
        return mLocalDataSource.getBookmarkByThumbsDownIfPresent(joke_id);
    }

    public List<BookmarkEntity> getBookmarkIfPresent(String  joke_id){
        return mLocalDataSource.getBookmarkIfPresent(joke_id);
    }



    public Completable addBookmark(BookmarkEntity bookmarkEntity){
        return mLocalDataSource.addBookmark(bookmarkEntity);
    }

    public Completable deleteBookmark(String joke_id){
        return mLocalDataSource.deleteBookmark(joke_id);
    }


    public Observable<List<BookmarkEntity>> getBookmarksByThumbsUp(){
        return mLocalDataSource.getBookmarksByThumbsUp();
    }


    public Observable<List<BookmarkEntity>> getBookmarksByThumbsDown(){
        return mLocalDataSource.getBookmarksByThumbsDown();
    }




}
