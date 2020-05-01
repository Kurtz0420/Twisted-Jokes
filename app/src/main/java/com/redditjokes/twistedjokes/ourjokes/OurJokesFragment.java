package com.redditjokes.twistedjokes.ourjokes;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.DrawerTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTransformer;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jaeger.library.StatusBarUtil;
import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.api.ApiClient;
import com.redditjokes.twistedjokes.api.JokesApi;
import com.redditjokes.twistedjokes.api.models.OurJoke;
import com.redditjokes.twistedjokes.room2.BookmarkEntity;
import com.redditjokes.twistedjokes.room2.DatabaseEntryHelper;
import com.redditjokes.twistedjokes.room2.DatabaseTransactions;
import com.redditjokes.twistedjokes.utils.ApiCallsHelper;
import com.redditjokes.twistedjokes.utils.AppExecutors;
import com.redditjokes.twistedjokes.utils.CookiesHelper;
import com.redditjokes.twistedjokes.utils.CustomViewPager;
import com.redditjokes.twistedjokes.utils.SharedPreferences;
import com.redditjokes.twistedjokes.utils.ZoomOutTransformation;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class OurJokesFragment extends Fragment implements JokesAdapter2.JokeAdapterCallbacks {
    private static final String TAG = "OurJokesFragment";


    //APi setup
    private Retrofit retrofit;
    private JokesApi jokesApi;
    private ViewPager viewPager;
    private JokesAdapter2 jokesAdapter;

    private ImageView sharebtn;
    private ImageView fav_btn ;
    private LinearLayout upvoteBtnLinear ;
    private LinearLayout downvoteBtnLinear ;


    AlphaAnimation buttonClickAnimation;

    private List<OurJoke> jokesList=new ArrayList<>();
    private DatabaseTransactions databaseTransactions;
    private CompositeDisposable mDisposibles = new CompositeDisposable();

    private DatabaseEntryHelper databaseEntryHelper;

    private Button skipAdBtn;
    private ConstraintLayout rootLayoutConstraint;

    final Handler handler = new Handler();
    private CookiesHelper cookiesHelper;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_our_jokes,container,false);
        StatusBarUtil.setTranslucent(getActivity());
        viewPager = (CustomViewPager)view.findViewById(R.id.viewPagerourJokes);
        sharebtn = view.findViewById(R.id.shareBtn);
        fav_btn = view.findViewById(R.id.fav_btn);
        upvoteBtnLinear = view.findViewById(R.id.upvoteLinear);
        downvoteBtnLinear = view.findViewById(R.id.downvoteLinear);
        databaseTransactions = new DatabaseTransactions(getActivity());
        skipAdBtn = view.findViewById(R.id.skipAdBtn);
        rootLayoutConstraint = view.findViewById(R.id.rootourJoke);
        cookiesHelper = new CookiesHelper(getActivity());
        sharedPreferences = new SharedPreferences(getActivity());


        buttonClickAnimation = new AlphaAnimation(1F, 0.8F);


        viewPager.requestLayout();
        int height = getResources().getDisplayMetrics().heightPixels;
        viewPager.getLayoutParams().height = height / 2;

        setupViewPager();
        apiSetup();
        fetchJokesAndSet();


        databaseEntryHelper = new DatabaseEntryHelper(databaseTransactions,getActivity(),jokesApi,cookiesHelper);


        sharebtn.setOnClickListener(ShareButtonClickListener);

        upvoteBtnLinear.setOnClickListener(UpVoteClickListener);

        downvoteBtnLinear.setOnClickListener(DownVoteButtonClickListener);
        fav_btn.setOnClickListener(FavButtonClickListener);




        return view;
    }

    View.OnClickListener ShareButtonClickListener  =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.startAnimation(buttonClickAnimation);
            shareJoke(jokesList.get(viewPager.getCurrentItem()));
        }
    };



    View.OnClickListener FavButtonClickListener  =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.startAnimation(buttonClickAnimation);

            OurJoke joke = jokesList.get(viewPager.getCurrentItem());

            databaseEntryHelper.bookmarkJoke(joke, fav_btn);


        }
    };

    private void shareJoke(OurJoke ourJoke){
        String shareBody = "Twisted humor \n\n" + ourJoke.getBuild_up() + "\n\n" + ourJoke.getDelivery() + "\n\n\n" +getPackageName() ;

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Twisted Humor");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

        startActivity(Intent.createChooser(sharingIntent, "Twisted Humor"));
    }

    private String getPackageName(){
        String app_link = null;
        if(getActivity() != null){
            app_link = "Check out the biggest pool of dark, twisted and advanced humor jokes \n\n"+"https://play.google.com/store/apps/details?id="+getActivity().getPackageName();

        }
        return app_link;
    }

    private void addBookmarkToDb(BookmarkEntity bookmarkEntity) {


        databaseTransactions.addBookmark(bookmarkEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposibles.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getActivity(), "Added to bookmarks", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Error While adding to bookmarks : "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }


    View.OnClickListener DownVoteButtonClickListener  =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.startAnimation(buttonClickAnimation);

            OurJoke joke = jokesList.get(viewPager.getCurrentItem());

            databaseEntryHelper.downVoteJoke(null,joke,upvoteBtnLinear);

        }
    };

    View.OnClickListener UpVoteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getActivity() != null){

                OurJoke joke = jokesList.get(viewPager.getCurrentItem());

                databaseEntryHelper.upVoteJoke(null,joke,upvoteBtnLinear);


            }
        }
    };



    private void shakeOtherViews(LinearLayout upvoteBtnLinear,
                                 LinearLayout downvoteBtnLinear, ImageView sharebtn, ImageView fav_btn) {


        YoYo.with(Techniques.Shake)
                .duration(100)
                .repeat(1)
                .playOn(upvoteBtnLinear);

        YoYo.with(Techniques.Shake)
                .duration(100)
                .repeat(1)
                .playOn(downvoteBtnLinear);



        YoYo.with(Techniques.Shake)
                .duration(100)
                .repeat(1)
                .playOn(sharebtn);

        YoYo.with(Techniques.Shake)
                .duration(100)
                .repeat(1)
                .playOn(fav_btn);


    }


    private void fetchJokesAndSet() {


        //        YoYo.with(Techniques.BounceInLeft)
//                .duration(500)
//                .repeat(2)
//                .playOn(toAnimateBtn);



        jokesApi.getAllOurJokes().toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<OurJoke>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<OurJoke> ourJokes) {

                        if(ourJokes !=null){
                            Log.d(TAG, "onNext: jokes List : "+ourJokes.toString());

                            jokesList.addAll(ourJokes);

                            if(!sharedPreferences.getBooleanPref("isPaidUser",false)){

                                for(int i=0;i<jokesList.size();i++){
                                    //this will add empty jokes in place of native ads
                                    if(i % 5 == 0 && i != 0){
                                        jokesList.add(i,new OurJoke());
                                    }
                                }
                            }



                            jokesAdapter.addjokes(jokesList);
                            jokesAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setupViewPager() {

        jokesAdapter = new JokesAdapter2(getActivity(),this,sharedPreferences);
        viewPager.setAdapter(jokesAdapter);

        ZoomOutTransformation zoomOutTransformation=new ZoomOutTransformation();
        //AccordianTransformer  -  list
        //RotateUpTransformer
        //BackgroundToForegroundTransformer
        //CubeInTransformer
        //CubeOutTransformer
        //DepthPageTransformer
        //FlipHorizontalTransformer
        //FlipVerticalTransformer
        //ForegroundToBackgroundTransformer
        //RotateDownTransformer
        //RotateUpTransformer
        //ScaleInOutTransformer - on list
        //StackTransformer    - on list
        //TabletTransformer
        //ZoomInTransformer
        //ZoomOutSlideTransformer
        //ZoomOutTransformer  - on list
        //DrawerTransformer   - on list
        viewPager.setPageTransformer(true,new StackTransformer());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                //for necessary code
                //here we change the colors of upvote, downvote and favourite Btn if they have been already tapped by the user for that joke


                //we show the skip_ad button when ad is being loaded, because its hard to swipe the ad with match_parent
                if(!sharedPreferences.getBooleanPref("isPaidUser",false)) {

                    if(position % 5 == 0 && position !=  0){

                        //delay the appearance of skip ad btn to load the ad first
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                animateRoot();
                                skipAdBtn.setVisibility(View.VISIBLE);
                                skipAdBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                    }
                                });



                            }
                        },2000);

                    }else {
                        animateRoot();
                        skipAdBtn.setVisibility(View.GONE);
                    }
                }else {
                    //user is paid, so don't show skipAd btn
                }




                Log.d(TAG, "onPageSelected: ");
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        OurJoke joke = jokesList.get(viewPager.getCurrentItem());

                        List<BookmarkEntity> bookmarkEntityList = databaseTransactions.getBookmarkByThumbsUpIfPresent(joke.getId());

                        if(bookmarkEntityList.size() > 0){
                            //joke is already upvoted by user
                            //change color of button
                            changeColorToPressed(upvoteBtnLinear);
                        }else {

                            changeColorToNotPressed(upvoteBtnLinear);

                        }

                        List<BookmarkEntity> bookmarkEntityList2 = databaseTransactions.getBookmarkByThumbsDownIfPresent(joke.getId());

                        if(bookmarkEntityList2.size() > 0){
                            //joke is already upvoted by user
                            //change color of button
                            changeColorToPressed(downvoteBtnLinear);
                        }else {

                            changeColorToNotPressed(downvoteBtnLinear);
                        }

                        List<BookmarkEntity> bookmarkEntityList3 = databaseTransactions.getBookmarkIfPresent(joke.getId());

                        if(bookmarkEntityList3.size() > 0){
                            //joke is already upvoted by user
                            //change color of button
                            changeColorToPressedBtn(fav_btn);
                        }else {

                            changeColorToNotPressedBtn(fav_btn);
                        }
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeColorToPressed(final LinearLayout linearLayout){
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                if(getActivity() != null){
                    linearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.button_background_our_joke2));

                }
            }
        });
    }

    private void changeColorToNotPressed(final LinearLayout linearLayout){
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                if(getActivity() != null){
                    linearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.button_background_our_joke));

                }
            }
        });
    }

    private void animateRoot(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(rootLayoutConstraint);
        }
    }

    private void changeColorToPressedBtn(final ImageView linearLayout){
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                if(getActivity() != null){
                    linearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.button_background_our_joke2));

                }
            }
        });
    }

    private void changeColorToNotPressedBtn(final ImageView linearLayout){
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                if(getActivity() != null){
                    linearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.button_background_our_joke));

                }
            }
        });
    }

    private void apiSetup() {
        retrofit = ApiClient.getPsychAPIClient();
        jokesApi = retrofit.create(JokesApi.class);
    }


    @Override
    public void onRevealClick() {

        shakeOtherViews(upvoteBtnLinear,downvoteBtnLinear,sharebtn,fav_btn);
    }
}
