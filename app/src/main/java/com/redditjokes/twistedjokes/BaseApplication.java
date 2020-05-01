package com.redditjokes.twistedjokes;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.redditjokes.twistedjokes.utils.SharedPreferences;

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";

    public static final String CHANNEL_ID="Channel_1";
    public static final String CHANNEL_NAME="Channel__1";

    private static BaseApplication instance;

    public BaseApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        subscribeToFCM();
        generateToken();
    }

    private void subscribeToFCM() {

        //if account type is free we save a variable in shared prefs with two values freeVersion and paidVersion
        SharedPreferences sharedPreferences=new SharedPreferences(instance);
        String accountType = sharedPreferences.getString("AccountType","freeVersion");
        if(accountType.equals("freeVersion")){

            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.FreeVersion_NotificationSub))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
        }

        if(accountType.equals(getString(R.string.paidVersion_accountType))){
            FirebaseMessaging.getInstance().subscribeToTopic("paidVersion")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
        }


    }

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME
                    , NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);


        }

    }

    private void generateToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = token;
                        Log.d(TAG, msg);
                    }
                });
    }

    public static Context getContext() {
        return instance;
    }
}
