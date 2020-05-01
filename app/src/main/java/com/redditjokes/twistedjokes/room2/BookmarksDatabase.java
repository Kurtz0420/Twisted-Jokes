package com.redditjokes.twistedjokes.room2;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {BookmarkEntity.class},version = 2)
public abstract class BookmarksDatabase extends RoomDatabase {

    public static final String DATABASE_NAME="BookmarksDB";

    public abstract BookmarkDAO getPostFavDAO();

    private static volatile BookmarksDatabase INSTANCE;


    public static BookmarksDatabase getInstance(Context context, RoomDatabase.Callback callback) {
        if (INSTANCE == null) {
            synchronized (BookmarksDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookmarksDatabase.class, DATABASE_NAME).addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static BookmarksDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BookmarksDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookmarksDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
