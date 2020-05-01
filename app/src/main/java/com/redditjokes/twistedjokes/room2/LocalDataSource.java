package com.redditjokes.twistedjokes.room2;

import android.content.Context;

import androidx.room.RoomDatabase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class LocalDataSource implements BookmarkDataSource {

    private BookmarkDAO mBookmarkDao;


    public LocalDataSource(Context context){
        BookmarksDatabase notesDatabase= BookmarksDatabase.getInstance(context);
        this.mBookmarkDao =notesDatabase.getPostFavDAO();
    }

    public LocalDataSource(Context context, RoomDatabase.Callback callback) {

        BookmarksDatabase notesDatabase = BookmarksDatabase.getInstance(context, callback);
        this.mBookmarkDao = notesDatabase.getPostFavDAO();
    }



    @Override
    public Observable<List<BookmarkEntity>> getAllBookmarks() {
        return mBookmarkDao.getAllBookmarks();
    }

    @Override
    public Observable<List<BookmarkEntity>> getBookmarksByThumbsUp() {
        return mBookmarkDao.getBookmarksByThumbsUp();
    }

    @Override
    public Observable<List<BookmarkEntity>> getBookmarksByThumbsDown() {
        return mBookmarkDao.getBookmarksByThumbsDown();
    }

    @Override
    public Completable addBookmark(BookmarkEntity bookmarkEntity) {
        return mBookmarkDao.addBookmark(bookmarkEntity);
    }

    @Override
    public Completable updateBookmark(BookmarkEntity bookmarkEntity) {
        return mBookmarkDao.updateBookmark(bookmarkEntity);
    }

    @Override
    public Completable deleteBookmark(String joke_id) {
        return mBookmarkDao.deleteBookmark(joke_id);
    }

    @Override
    public Observable<BookmarkEntity> getBookmark(String joke_id) {
        return mBookmarkDao.getBookmark(joke_id);
    }

    @Override
    public List<BookmarkEntity> getBookmarkByThumbsUpIfPresent(String joke_id) {
        return mBookmarkDao.getBookmarkByThumbsUpIfPresent(joke_id);
    }

    @Override
    public List<BookmarkEntity> getBookmarkByThumbsDownIfPresent(String joke_id) {
        return mBookmarkDao.getBookmarkByThumbsDownIfPresent(joke_id);
    }

    @Override
    public List<BookmarkEntity> getBookmarkIfPresent(String joke_id) {
        return mBookmarkDao.getBookmarkIfPresent(joke_id);
    }

}
