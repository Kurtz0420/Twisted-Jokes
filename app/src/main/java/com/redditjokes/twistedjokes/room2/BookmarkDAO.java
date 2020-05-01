package com.redditjokes.twistedjokes.room2;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface BookmarkDAO {
    //Data Access Object
    //annotations do most of the job for inserting, just needs contact

    @Insert
    Completable addBookmark(BookmarkEntity bookmarkEntity);

    @Update
    Completable updateBookmark(BookmarkEntity bookmarkEntity);

    @Query("DELETE FROM bookmarks WHERE joke_id = :joke_id")
    Completable deleteBookmark(String joke_id);

    @Query("select * from bookmarks where isBookmark = 1")
    Observable<List<BookmarkEntity>> getAllBookmarks();


    @Query("select * from bookmarks where joke_id ==:joke_id")
    Observable<BookmarkEntity> getBookmark(String joke_id);

    @Query("select * from bookmarks where joke_id ==:joke_id AND isThumbsUp =1")
    List<BookmarkEntity> getBookmarkByThumbsUpIfPresent(String joke_id);

    @Query("select * from bookmarks where joke_id ==:joke_id AND isThumbsDown =1")
    List<BookmarkEntity> getBookmarkByThumbsDownIfPresent(String joke_id);

    @Query("select * from bookmarks where joke_id ==:joke_id AND isBookmark =1")
    List<BookmarkEntity> getBookmarkIfPresent(String joke_id);


    @Query("select * from bookmarks where isThumbsUp =1")
    Observable<List<BookmarkEntity>> getBookmarksByThumbsUp();

    @Query("select * from bookmarks where isThumbsDown =1")
    Observable<List<BookmarkEntity>> getBookmarksByThumbsDown();
}
