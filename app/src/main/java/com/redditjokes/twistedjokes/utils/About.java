package com.redditjokes.twistedjokes.utils;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.redditjokes.twistedjokes.R;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;


public class About extends AppCompatActivity {


    AnimationDrawable logoAnimation;
    private TextView mAbout_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.activity_about);
//       mAbout_tv=findViewById(R.id.about_tv);

        AboutBuilder builder = AboutBuilder.with(this)
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .setPhoto(R.mipmap.profile_picture)
                .setCover(R.mipmap.profile_cover)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setName("Kurtz_sopn")
                .setSubTitle("Mobile Developer")
                .setLinksColumnsCount(4)
                .setBrief("Three words : Innovative, Steadfast, Meticulous") //innovative,steadfast,meticulous
                .addGooglePlayStoreLink("http://play.google.com/store/apps/details?id=com.homie.psychq")
                .addGitHubLink("Kurtz0420")
                .addBitbucketLink("")
                .addFacebookLink("user")
                .addTwitterLink("user")
                .addInstagramLink("distorted.reality__")
                .addGooglePlusLink("")
                .addYoutubeChannelLink("")
                .addDribbbleLink("user")
                .addLinkedInLink("")
                .addEmailLink("skgismos999@gmail.com")
                .addWhatsappLink("Jr", "+0000000000")
                .addSkypeLink("user")
                .addGoogleLink("www.gismostec.com")
                .addAndroidLink("www.gismostec.com")
                .addWebsiteLink("www.gismostec.com")
                .addFiveStarsAction()
                .addMoreFromMeAction("")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .addUpdateAction()
                .setActionsColumnsCount(2)
                .addFeedbackAction("skurtz00420.com")
                .addIntroduceAction((Intent) null)
                .addHelpAction((Intent) null)
                .addChangeLogAction((Intent) null)
                 //ad link for donation
                .setWrapScrollView(true)
                .setShowAsCard(true);

        AboutView view = builder.build();

        ViewGroup.LayoutParams layoutParams=new ViewGroup.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        addContentView(view,layoutParams);


       //mAbout_tv.setText(getString(R.string.about_tv));




    }








}

