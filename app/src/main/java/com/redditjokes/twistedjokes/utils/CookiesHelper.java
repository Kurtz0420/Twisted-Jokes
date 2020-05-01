package com.redditjokes.twistedjokes.utils;

import android.app.Activity;
import android.content.Context;

import com.redditjokes.twistedjokes.R;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

public class CookiesHelper {

    private Activity activity;

    public CookiesHelper(Context context) {

        this.activity = (Activity) context;
    }

    //Activity activity = (Activity) context
    public void showCookie(String title, String message, String action, OnActionClickListener actionClickListener ){
        CookieBar.build(activity)
                .setTitle(title)
                .setMessage(message)
                .setDuration(5000)
                .setBackgroundColor(R.color.colorPrimary)
                .setAction(action, actionClickListener)
                .show();
    }
}
