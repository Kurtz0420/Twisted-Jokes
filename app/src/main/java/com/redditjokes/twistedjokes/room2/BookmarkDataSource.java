package com.redditjokes.twistedjokes.room2;



import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;


public interface BookmarkDataSource {


    Observable<List<BookmarkEntity>> getAllBookmarks();

    Observable<List<BookmarkEntity>> getBookmarksByThumbsUp();

    Observable<List<BookmarkEntity>> getBookmarksByThumbsDown();

    Completable addBookmark(BookmarkEntity bookmarkEntity);

    Completable updateBookmark(BookmarkEntity bookmarkEntity);

    Completable deleteBookmark(String joke_id);

    Observable<BookmarkEntity> getBookmark(String joke_id);

    List<BookmarkEntity> getBookmarkByThumbsUpIfPresent(String joke_id);

    List<BookmarkEntity> getBookmarkByThumbsDownIfPresent(String joke_id);

    List<BookmarkEntity> getBookmarkIfPresent(String joke_id);




}
