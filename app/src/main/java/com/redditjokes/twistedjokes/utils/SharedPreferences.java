package com.redditjokes.twistedjokes.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.redditjokes.twistedjokes.api.models.OurJoke;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import needle.Needle;

public class SharedPreferences {

    private static final String TAG = "SharedPreferences";

    protected Context context;
    private android.content.SharedPreferences prefs;

    public SharedPreferences(Context context) {

        this.context=context;
         prefs= PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveStringPref(final String key, final String name){

        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                android.content.SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key, name);
                editor.apply();
            }
        });

    }

    public String getString(String key, String defaultValue){
        return prefs.getString(key,defaultValue);
    }

    public void saveIntPref(final String key, final int index){
        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                android.content.SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(key, index);
                editor.apply();
            }
        });

    }

    public void saveLongPref(String key, long index){

        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, index);
        editor.apply();
    }

    public long getLongPref(String key){
        return  prefs.getLong(key, -1);
    }
    public int getIntPref(String key, int defaultValue){
        return  prefs.getInt(key, defaultValue);
    }
    public void saveBooleanPref(final String key, final boolean value){
        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                android.content.SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(key, value);
                editor.apply();
            }
        });

    }
    public boolean getBooleanPref(String key, boolean defaultValue){
        return prefs.getBoolean(key, defaultValue);
    }
    public void saveListPref(final List<String> list, final String key){
        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                android.content.SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(list);
                editor.putString(key, json);
                editor.apply();     // This line is IMPORTANT !!!
            }
        });

    }


    public List<String> getListPref(String key){
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public  void saveObjectsList(final List<OurJoke> list, final String key){
        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                android.content.SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(list);
                editor.putString(key, json);
                editor.apply();     // This line is IMPORTANT !!!
            }
        });

    }

    public Map<String, String> getMapFromString(String mapString){
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        return gson.fromJson(mapString, type);
    }

    public String getStringFromMap(final Map<String, String> map){
        Gson gson = new Gson();
        String hashMapString = gson.toJson(map);
        return hashMapString;

    }


    public List<OurJoke> getObjectsList(String key){
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<OurJoke>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
