package com.redditjokes.twistedjokes.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.redditjokes.twistedjokes.api.models.UserJoke;


import java.io.Serializable;
import java.util.List;

public class ResultsUserJoke implements Serializable {

    @SerializedName("count")
    @Expose
    int count;


    @SerializedName("next")
    @Expose
    String next;

    @SerializedName("previous")
    @Expose
    String previous;

    @SerializedName("results")
    @Expose
    List<UserJoke> userJokes;


    public ResultsUserJoke() {
    }

    public ResultsUserJoke(int count, String next, String previous, List<UserJoke> userJokes) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.userJokes = userJokes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<UserJoke> getUserJokes() {
        return userJokes;
    }

    public void setUserJokes(List<UserJoke> userJokes) {
        this.userJokes = userJokes;
    }

    @Override
    public String toString() {
        return "ResultsUserJoke{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", userJokes=" + userJokes +
                '}';
    }
}
