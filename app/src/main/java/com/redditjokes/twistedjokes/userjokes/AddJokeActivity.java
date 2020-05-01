package com.redditjokes.twistedjokes.userjokes;


import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.api.ApiClient;
import com.redditjokes.twistedjokes.api.JokesApi;
import com.redditjokes.twistedjokes.api.models.UserJoke;
import com.redditjokes.twistedjokes.utils.ColorsHelper;
import com.redditjokes.twistedjokes.utils.CookiesHelper;
import com.redditjokes.twistedjokes.utils.SharedPreferences;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class AddJokeActivity extends AppCompatActivity {
    private static final String TAG = "AddJokeActivity";

    public static final String JOKE_PREP_BUTTON_TEXT = "NEXT";
    public static final String SUBMIT_PART_BUTTON_TEXT = "FINISH";


    private LinearLayout rootLayout; //root layout of activity
    private RelativeLayout tvsContainerRelative, finalPreviewContainerRelative, selectPartContainerRelative; //root of tv's relativeLayout
    private RadioGroup radioButtons;
    private Button forwardBtn,backwardbtn;
    private EditText build_upEt, deliveryEt;

    private boolean isJokeOnePart = false;

    private View dividerBtwTvs;

    private String buildUpInput, deliveryInput ;

    //final preview view
    private TextView builupTvFinal, deliveryTvFinal, usernameTvFinal;
    private String profileUsername,profilePicSlug, profileEmail;
    private CircularImageView profile_imageIv;

    //api setup
    private Retrofit retrofit;
    private JokesApi jokesApi;

    private CookiesHelper cookiesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_joke);
        selectPartContainerRelative = findViewById(R.id.selectPartConstraintContainer);
        tvsContainerRelative = findViewById(R.id.tvsRelativeLayout);
        finalPreviewContainerRelative = findViewById(R.id.finalPreviewRelative);
        rootLayout = findViewById(R.id.rootLayout);
        radioButtons = findViewById(R.id.radioGroup1);
        forwardBtn = findViewById(R.id.forwardbtn);
        build_upEt = findViewById(R.id.user_joke_buildup_input);
        deliveryEt = findViewById(R.id.user_joke_delivery_input);
        dividerBtwTvs = findViewById(R.id.divider);
        backwardbtn = findViewById(R.id.backwardbtn);
        usernameTvFinal = findViewById(R.id.usernameTvOurJoke);
        builupTvFinal = findViewById(R.id.user_joke_buildUp_tv);
        deliveryTvFinal = findViewById(R.id.user_joke_deliverytv);
        profile_imageIv = findViewById(R.id.profile_image);
        cookiesHelper = new CookiesHelper(this);



        retrieveUserInfoFromPrefs();
        apiSetup();

        forwardBtn.setOnClickListener(forwardBtnClickListener);


        backwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show all input containers
                //set his joke in input
                hideFinalPreview();
                showInputViews();
                setInputDataOnViews();
                changeButtonTextToNext();
            }
        });

        radioButtons.check(R.id.radio1);
        radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio0:
                        // one part is selected by user
                        animateRoot();
                        showOneTv();

                        isJokeOnePart = true;
//                        slideDown(tvsContainerRelative);
                        break;
                    case R.id.radio1:
                        // two part is selected by user
                        animateRoot();
                        showTwoTv();

                        isJokeOnePart = false;
//                        slideDown(tvsContainerRelative);
                        break;

                }
            }
        });


    }

    private void changeButtonTextToNext() {
        forwardBtn.setText(JOKE_PREP_BUTTON_TEXT);
    }

    private void changeButtonTextToFinalize() {
        forwardBtn.setText(SUBMIT_PART_BUTTON_TEXT);
    }

    private void setInputDataOnViews() {

        if(isJokeOnePart){
            //set input on just build up
            if(!buildUpInput.equals("")){
                build_upEt.setText(buildUpInput);
            }


        }else {
            //set input on both buildup and delivery
            if(!buildUpInput.equals("") && !deliveryInput.equals("")){
                build_upEt.setText(buildUpInput);
                deliveryEt.setText(deliveryInput);
            }
        }
    }

    private void showInputViews() {

        animateRoot();
        selectPartContainerRelative.setVisibility(View.VISIBLE);
        tvsContainerRelative.setVisibility(View.VISIBLE);
    }

    private void hideFinalPreview() {

        finalPreviewContainerRelative.setVisibility(View.GONE);
        backwardbtn.setVisibility(View.GONE);
    }

    private void showOneTv() {
        deliveryEt.setVisibility(View.GONE);
        build_upEt.setHint("Drop the whole joke here");
        dividerBtwTvs.setVisibility(View.GONE);
//        tvsContainerRelative.setVisibility(View.VISIBLE);

    }

    private void showTwoTv() {
//        tvsContainerRelative.setVisibility(View.VISIBLE);
        deliveryEt.setVisibility(View.VISIBLE);
        deliveryEt.setHint("Drop the delivery here");
        build_upEt.setHint("Drop the buildup here");
        dividerBtwTvs.setVisibility(View.VISIBLE);

    }

    private void animateRoot(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(rootLayout);
        }
    }



    public void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                - view.getHeight() ,                 // fromYDelta
                view.getHeight()/4); // toYDelta
        animate.setDuration(250);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    View.OnClickListener forwardBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.d(TAG, "onClick: Forward On Click");

            if(forwardBtn.getText().equals(JOKE_PREP_BUTTON_TEXT)){
                //user has entered the joke, proceed to final preview
                if(isJokeOnePart){
                    //joke is one part part so only check buildup Et
                    if(TextUtils.isEmpty(build_upEt.getText())){
                        //user input is empty
                        cookiesHelper.showCookie("Invalid Entry!!","Try entering a joke first",null,null);
                    }else {
                        //we have a one part joke
                        buildUpInput = build_upEt.getText().toString();

                        //show the final preview and back button
                        hideInputViews();
                        changeButtonTextToFinalize();
                        showFinalPreview();

                    }

                }else {
                    //joke is two part so check both Et's
                    if(TextUtils.isEmpty(build_upEt.getText()) || TextUtils.isEmpty(deliveryEt.getText())){
                        //both inputs are empty
                        cookiesHelper.showCookie("Joke Incomplete!!","Try completing the joke first",null,null);
                    }else {
                        //we have a two part joke
                        buildUpInput = build_upEt.getText().toString();
                        deliveryInput = deliveryEt.getText().toString();

                        //show the final preview and back button
                        //show the final preview and back button
                        hideInputViews();
                        changeButtonTextToFinalize();
                        showFinalPreview();

                    }

                }

            }else if(forwardBtn.getText().equals(SUBMIT_PART_BUTTON_TEXT)){
                //submit joke to api
                setAndPostJoke();
            }


        }
    };

    private void setAndPostJoke() {

        UserJoke userJoke = new UserJoke();


        if(isJokeOnePart){
            userJoke.setBuild_up(buildUpInput);
        }else {
            userJoke.setBuild_up(buildUpInput);
            userJoke.setDelivery(deliveryInput);
        }

        userJoke.setUsername(profileUsername);
        userJoke.setSlug(profilePicSlug);
        userJoke.setEmail(profileEmail);


        postJoke(userJoke);

    }

    private void postJoke(final UserJoke userJoke) {
        jokesApi.adduserJoke(userJoke)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserJoke>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserJoke userJoke) {

                        if(userJoke != null){
                            cookiesHelper.showCookie("Submission Successful","It will appear in the feeds shortly",null,null);
                            finish();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {


                        cookiesHelper.showCookie("Submission Failed!!","Check your internet connection and try again",null,null);



                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void hideInputViews() {
        animateRoot();
        selectPartContainerRelative.setVisibility(View.GONE);
        tvsContainerRelative.setVisibility(View.GONE);

    }

    private void showFinalPreview() {
        backwardbtn.setVisibility(View.VISIBLE);
        finalPreviewContainerRelative.setVisibility(View.VISIBLE);
        setDataForFinalPreview();

    }

    private void setDataForFinalPreview() {
        //set data on final preview
        if(isJokeOnePart){
            builupTvFinal.setText(buildUpInput);
            deliveryTvFinal.setVisibility(View.GONE);
        }else {
            builupTvFinal.setText(buildUpInput);
            deliveryTvFinal.setText(deliveryInput);
        }

        if(!profileUsername.equals("")){
            usernameTvFinal.setText(profileUsername);
        }


        profile_imageIv.setBorderColor(ColorsHelper.getRandomColor());
        if(!profilePicSlug.equals("")){
            Glide.with(this).load(profilePicSlug).into(profile_imageIv);
        }

    }

    private void retrieveUserInfoFromPrefs() {

        SharedPreferences sharedPreferences = new SharedPreferences(this);

        String userInfoString =   sharedPreferences.getString(getString(R.string.user_info_map),"No Info Found");
        Map<String,String> userInfomap= sharedPreferences.getMapFromString(userInfoString);

        if(userInfomap != null){
            String username = userInfomap.get("Username");
            String email = userInfomap.get("Email");
            String uid = userInfomap.get("Uid");
            String photoUrl = userInfomap.get("ProfilePic");




            if(username == null && email != null){
                List<String> modified = Arrays.asList(email.split("@"));
                username = modified.get(0);

            }
            if(photoUrl == null){
                photoUrl = "https://picsum.photos/id/237/200/300";
            }

            profileUsername = username;
            profilePicSlug = photoUrl;
            profileEmail = email;



            Log.d(TAG, "retrieveUserInfoFromPrefs: Info "+username + " : "+email + " : "+uid + " : "+photoUrl);
        }

    }

    private void apiSetup() {
        retrofit = ApiClient.getPsychAPIClient();
        jokesApi = retrofit.create(JokesApi.class);
    }


}
