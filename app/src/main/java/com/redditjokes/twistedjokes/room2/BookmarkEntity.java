package com.redditjokes.twistedjokes.room2;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class BookmarkEntity implements Parcelable {


    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "joke_id")
    private String joke_id;


    @ColumnInfo(name = "build_up")
    private String build_up;


    @ColumnInfo(name = "delivery")
    private String delivery;


    @ColumnInfo(name = "isBookmark")
    private boolean isBookmark;

    @ColumnInfo(name = "isThumbsUp")
    private boolean isThumbsUp;

    @ColumnInfo(name = "isThumbsDown")
    private boolean isThumbsDown;




    public BookmarkEntity() {
    }


    protected BookmarkEntity(Parcel in) {
        id = in.readInt();
        joke_id = in.readString();
        build_up = in.readString();
        delivery = in.readString();
        isBookmark = in.readByte() != 0;
        isThumbsUp = in.readByte() != 0;
        isThumbsDown = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(joke_id);
        dest.writeString(build_up);
        dest.writeString(delivery);
        dest.writeByte((byte) (isBookmark ? 1 : 0));
        dest.writeByte((byte) (isThumbsUp ? 1 : 0));
        dest.writeByte((byte) (isThumbsDown ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookmarkEntity> CREATOR = new Creator<BookmarkEntity>() {
        @Override
        public BookmarkEntity createFromParcel(Parcel in) {
            return new BookmarkEntity(in);
        }

        @Override
        public BookmarkEntity[] newArray(int size) {
            return new BookmarkEntity[size];
        }
    };

    @Override
    public String toString() {
        return "BookmarkEntity{" +
                "id=" + id +
                ", joke_id='" + joke_id + '\'' +
                ", build_up='" + build_up + '\'' +
                ", delivery='" + delivery + '\'' +
                ", isBookmark=" + isBookmark +
                ", isThumbsUp=" + isThumbsUp +
                ", isThumbsDown=" + isThumbsDown +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJoke_id() {
        return joke_id;
    }

    public void setJoke_id(String joke_id) {
        this.joke_id = joke_id;
    }

    public String getBuild_up() {
        return build_up;
    }

    public void setBuild_up(String build_up) {
        this.build_up = build_up;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }

    public boolean isThumbsUp() {
        return isThumbsUp;
    }

    public void setThumbsUp(boolean thumbsUp) {
        isThumbsUp = thumbsUp;
    }

    public boolean isThumbsDown() {
        return isThumbsDown;
    }

    public void setThumbsDown(boolean thumbsDown) {
        isThumbsDown = thumbsDown;
    }

    public BookmarkEntity(int id, String joke_id, String build_up, String delivery, boolean isBookmark, boolean isThumbsUp, boolean isThumbsDown) {
        this.id = id;
        this.joke_id = joke_id;
        this.build_up = build_up;
        this.delivery = delivery;
        this.isBookmark = isBookmark;
        this.isThumbsUp = isThumbsUp;
        this.isThumbsDown = isThumbsDown;
    }
}