package com.redditjokes.twistedjokes.fcm;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.redditjokes.twistedjokes.BaseApplication;
import com.redditjokes.twistedjokes.MainActivity;
import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.utils.SharedPreferences;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.blankj.utilcode.util.StringUtils.getString;

public class MyFCMService extends FirebaseMessagingService {

    private static final String TAG = "MyFCMService";
    public static final String SPLIT_INTO_LINES_BY="-";
    public static int NOTIFICATION_ID=01;
    private Intent intent;
    private String image;
    private String nStyle;
    public static NotificationCompat.Builder notificationBuilder;
    public static NotificationManager notificationManager;
    public static String channelId = "channel-updates";
    public static String channelName = "Jokes-updates";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title=remoteMessage.getNotification().getTitle();
        String message=remoteMessage.getNotification().getBody();


        Log.d(TAG, "onMessageReceived: "+message);


        Map bundle=remoteMessage.getData();



        String goTo= (String) bundle.get(getString(R.string.Activity_Notification));
        image=(String)bundle.get(getString(R.string.Image_Notification));
        nStyle=(String) bundle.get(getString(R.string.Style_Notification));

        if(goTo != null){

            if(goTo.equals(getString(R.string.Incoming_ForMainActivity))){
                intent=new Intent(BaseApplication.getContext(), MainActivity.class);
            }
        }


        if(nStyle == null){
            nStyle = "Inbox";
        }


        notify(title,message,intent,image,nStyle);


    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: ");

    }

    private void notify(String title, String message, Intent intent, String imageUrl, String notificationStyle){

        if(notificationStyle.equals(getString(R.string.BigPicture_Style))){
            new generatePictureStyleNotification(BaseApplication.getContext().getApplicationContext(),title,message,imageUrl,intent)
                    .execute();
        }else if(notificationStyle.equals(getString(R.string.Inbox_Style))){
            generateInboxStyleNotification(title,message,intent);
        }


    }
    public static int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.FRENCH).format(now));
        return id;
    }


    public static class  generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl,notifyFor;
        private Intent intent;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl, Intent intent) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
            this.intent=intent;

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);


            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);


            notificationManager =(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            NOTIFICATION_ID = createID();

            int importance = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                importance = NotificationManager.IMPORTANCE_HIGH;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }



            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext,channelId)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(result)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result)
                    .setBigContentTitle(title)
                    .bigLargeIcon(result));


            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }


    public void generateInboxStyleNotification(String title, String message, Intent intent){

        //35

        String[] split_message=message.split(SPLIT_INTO_LINES_BY,getLimit(message));

        PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getContext().getApplicationContext(), 100, intent, PendingIntent.FLAG_ONE_SHOT);

        notificationBuilder =
                new NotificationCompat.Builder(BaseApplication.getContext().getApplicationContext(),channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setLargeIcon(BitmapFactory.decodeResource(BaseApplication.getContext().getApplicationContext().getResources(),R.mipmap.ic_launcher))
                        .setContentIntent(pendingIntent);


        int numberOfLines=split_message.length;

        Log.d(TAG, "generateInboxStyleNotification: Number Of Lines : "+numberOfLines);


        if(numberOfLines == 1){
            notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                    .addLine(split_message[0]));
        }

        if(numberOfLines == 2){


            notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                    .addLine(split_message[0])
                    .addLine(split_message[1]));


        }else if(numberOfLines == 3){


            notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                    .addLine(split_message[0])
                    .addLine(split_message[1])
                    .addLine(split_message[2]));

        }else if(numberOfLines == 4){


            notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                    .addLine(split_message[0])
                    .addLine(split_message[1])
                    .addLine(split_message[2])
                    .addLine(split_message[3]));


        }else if(numberOfLines ==5){


            notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                    .addLine(split_message[0])
                    .addLine(split_message[1])
                    .addLine(split_message[2])
                    .addLine(split_message[3])
                    .addLine(split_message[4]));

        }else if(numberOfLines == 6){


            notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                    .addLine(split_message[0])
                    .addLine(split_message[1])
                    .addLine(split_message[2])
                    .addLine(split_message[3])
                    .addLine(split_message[4])
                    .addLine(split_message[5]));

        }else if(numberOfLines == 7){


            notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                    .addLine(split_message[0])
                    .addLine(split_message[1])
                    .addLine(split_message[2])
                    .addLine(split_message[3])
                    .addLine(split_message[4])
                    .addLine(split_message[5])
                    .addLine(split_message[6]));

        }else if(numberOfLines == 8){

            notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                    .addLine(split_message[0])
                    .addLine(split_message[1])
                    .addLine(split_message[2])
                    .addLine(split_message[3])
                    .addLine(split_message[4])
                    .addLine(split_message[5])
                    .addLine(split_message[6])
                    .addLine(split_message[7]));

        }else if(numberOfLines == 9){

            notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                    .addLine(split_message[0])
                    .addLine(split_message[1])
                    .addLine(split_message[2])
                    .addLine(split_message[3])
                    .addLine(split_message[4])
                    .addLine(split_message[5])
                    .addLine(split_message[6])
                    .addLine(split_message[7])
                    .addLine(split_message[8]));

        }else if(numberOfLines == 10){

            notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                    .addLine(split_message[0])
                    .addLine(split_message[1])
                    .addLine(split_message[2])
                    .addLine(split_message[3])
                    .addLine(split_message[4])
                    .addLine(split_message[5])
                    .addLine(split_message[6])
                    .addLine(split_message[7])
                    .addLine(split_message[8])
                    .addLine(split_message[9]));

        }


        notificationManager = (NotificationManager) BaseApplication.getContext().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    private static String[] splitToNChar(String text, int size) {
        List<String> parts = new ArrayList<>();

        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        return parts.toArray(new String[0]);
    }

    private int getLimit(String message){

        if(message.length() < 70){
            return 2;//returns no of lines that the text is gonna be spliting into
        }else if(message.length() > 70 && message.length() < 105){
            return 3;
        }else if(message.length() > 105 && message.length() < 140){
            return 4;
        }else if(message.length() > 140 && message.length() < 175){
            return 5;
        }else if(message.length() > 175 && message.length() < 210){
            return 6;
        }else {
            return 7;
        }
    }

}
