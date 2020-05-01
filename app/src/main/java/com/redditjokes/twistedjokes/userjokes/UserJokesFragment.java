package com.redditjokes.twistedjokes.userjokes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.epoxy.EpoxyRecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.api.ApiClient;
import com.redditjokes.twistedjokes.api.JokesApi;
import com.redditjokes.twistedjokes.api.models.ResultsUserJoke;
import com.redditjokes.twistedjokes.api.models.UserJoke;
import com.redditjokes.twistedjokes.room2.BookmarkEntity;
import com.redditjokes.twistedjokes.room2.DatabaseEntryHelper;
import com.redditjokes.twistedjokes.room2.DatabaseTransactions;
import com.redditjokes.twistedjokes.userjokes.epoxy.controllers.UserJokesController;
import com.redditjokes.twistedjokes.userjokes.epoxy.models.HeaderModel_;
import com.redditjokes.twistedjokes.userjokes.epoxy.models.UserJokeModel_;
import com.redditjokes.twistedjokes.userjokes.filtering_popup.ListPopupItem;
import com.redditjokes.twistedjokes.userjokes.filtering_popup.ListPopupWindowAdapter;
import com.redditjokes.twistedjokes.utils.AppExecutors;
import com.redditjokes.twistedjokes.utils.CookiesHelper;
import com.redditjokes.twistedjokes.utils.SharedPreferences;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import retrofit2.Retrofit;

public class UserJokesFragment extends Fragment implements UserJokesController.AdapterCallbacks {

    private static final String TAG = "UserJokesFragment";
    public static final int RC_SIGN_IN=3;


    //APi setup
    private Retrofit retrofit;
    private JokesApi jokesApi;
    GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private SignInButton signInButton;


    private SharedPreferences sharedPreferences;
    private TextView message_tv, noteTv;
    private EpoxyRecyclerView recyclerView;
    private UserJokesController controller;
    private LinearLayoutManager layoutManager;
    //Pagination
    private int pageNumber = 1;
    private boolean loading = false;
    private final int VISIBLE_THRESHOLD = 1;
    private int lastVisibleItem;
    private int totalItemCount;
    private int totalPages = 134;


    private CookiesHelper cookiesHelper;

    private List<UserJoke> jokesList = new ArrayList<>();

    private ConstraintLayout rootlayoutConstraint ;
    private WaveSwipeRefreshLayout swipeRefreshLayout;

    private MaterialEditText usernameEt;
    private ImageView proceedBtn;//when user enters username and hits confirm

    private String inputUsername;

    private CompositeDisposable mDisposibles = new CompositeDisposable();
    private DatabaseTransactions databaseTransactions;
    private DatabaseEntryHelper databaseEntryHelper;


    private View triggeredView;//if upvote is clicked first, we ll assign its view to this and change its color when downvote is clicked

    String firstItemid;

    private String orderingField = "-timestamp";

    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_jokes,container,false);
        signInButton = view.findViewById(R.id.sign_in_button);
        recyclerView = view.findViewById(R.id.userJokesEpoxy);
        rootlayoutConstraint = view.findViewById(R.id.userJokeRootConstraint);
        message_tv = view.findViewById(R.id.message_tv);
        noteTv = view.findViewById(R.id.noteTv);
        swipeRefreshLayout = view.findViewById(R.id.pullToRefresh);
        usernameEt = view.findViewById(R.id.usernameEt);
        proceedBtn = view.findViewById(R.id.proceed);
        sharedPreferences = new SharedPreferences(getActivity());
        cookiesHelper = new CookiesHelper(getActivity());
        databaseTransactions = new DatabaseTransactions(getActivity());

        apiSetup();
        databaseEntryHelper = new DatabaseEntryHelper(databaseTransactions,getActivity(),jokesApi,cookiesHelper);




        if(!sharedPreferences.getBooleanPref(getString(R.string.loginStatus),false)){
            //User input Ui is set as default

            proceedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //when user click proceeds, first check the ET then initGoogleSign In and Show Google Login Button

                    if(!TextUtils.isEmpty(usernameEt.getText())){
                        //user has entered a username

                        inputUsername = usernameEt.getText().toString();

                        hideUserInputUi();
                        showAndInitGoogleBtn();



                    }else {
                        cookiesHelper.showCookie("POPUP!!","Try Picking a username first",null,null);
                    }


                }
            });


        }else {
            //UserJokes will be initialized
            loadContent();

        }









        return view;
    }

    private void showAndInitGoogleBtn() {

        showLoginUI();

        initGoogleSignIn();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });


    }

    private void showLoginUI() {

        noteTv.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.VISIBLE);
        message_tv.setVisibility(View.VISIBLE);

    }

    private void hideUserInputUi() {

        animateRoot();
        usernameEt.setVisibility(View.GONE);
        proceedBtn.setVisibility(View.GONE);
    }


    WaveSwipeRefreshLayout.OnRefreshListener SwipeRefreshListener = new WaveSwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            fetchAndSetData(false);
        }
    };
    private void animateRoot(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(rootlayoutConstraint);
        }
    }

    private void loadContent() {

        hideLoginUI();
        showContentUI();
        setupRv();
        fetchAndSetData(false);
    }

    private void fetchAndSetData(final boolean isUserSubmission) {

        pageNumber = 1;
        if(isUserSubmission){

            String userInfoString =   sharedPreferences.getString(getString(R.string.user_info_map),"No Info Found");
            Map<String,String> userInfomap= sharedPreferences.getMapFromString(userInfoString);

            if(userInfomap != null){
                username = userInfomap.get("Username");
            }

            jokesApi.getUserJokesQueryAndOrderedBy("-timestamp",pageNumber,username)
                    .toObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultsUserJoke>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResultsUserJoke resultsUserJoke) {

                            if(resultsUserJoke != null){
                                if(resultsUserJoke.getUserJokes().size() > 0){
                                    setDataWithResults(resultsUserJoke);
                                }else {
                                    //user has not submitted any jokes
                                    cookiesHelper.showCookie("POPUP!!","You have no submissions yet!",null,null);
                                }


                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                            cookiesHelper.showCookie("POPUP!!","You have no submissions yet!",null,null);

                        }

                        @Override
                        public void onComplete() {

                        }
                    });


        }else {

            jokesApi.getUserJokesOrderedBy(orderingField,pageNumber)
                    .toObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultsUserJoke>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResultsUserJoke resultsUserJoke) {
                            if(resultsUserJoke != null){


                                setDataWithResults(resultsUserJoke);

                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            cookiesHelper.showCookie("Error While Loading Data : "+e.getMessage(),
                                    "Please Check Your Internet Connection & Try Refreshing",
                                    null,
                                    null);
                            if(swipeRefreshLayout.isRefreshing()){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }



        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int[] lastVisibleItemPositions = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
//                totalItemCount= staggeredGridLayoutManager.getItemCount();
//                lastVisibleItem= getLastVisibleItem(lastVisibleItemPositions);

                totalItemCount=layoutManager.getItemCount();
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();


                if (!loading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {


                    loading = true;







                    pageNumber++;

                    //loadingProgress.setVisibility(View.VISIBLE);

                    if(pageNumber <= totalPages){


                        if(isUserSubmission){


                            if(username != null){

                                jokesApi.getUserJokesQueryAndOrderedBy("-timestamp",pageNumber,username)
                                        .toObservable()
                                        .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<ResultsUserJoke>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(ResultsUserJoke resultsUserJoke) {

                                        if(resultsUserJoke != null){
                                            setAdditionalDataForResults(resultsUserJoke);
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


                        }else {

                            jokesApi.getUserJokesOrderedBy(orderingField,pageNumber)
                                    .toObservable()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<ResultsUserJoke>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(ResultsUserJoke resultsUserJoke) {

                                            if(resultsUserJoke != null){


                                                setAdditionalDataForResults(resultsUserJoke);


                                            }


                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            cookiesHelper.showCookie("Error While Loading Data : "+e.getMessage(),
                                                    "Please Check Your Internet Connection & Try Refreshing",
                                                    null,
                                                    null);
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }




                    }else {
                        //end of the content
                    }



                }
            }
        });

    }

    private void setAdditionalDataForResults(ResultsUserJoke resultsUserJoke) {

        jokesList.addAll(resultsUserJoke.getUserJokes());


        if(pageNumber < totalPages){
            //when pageNumber is less than last page isLoadingMore will be true
            controller.setData(jokesList,true,null,null);
        }else {
            //when pageNumber equals last page, isLoadingMore will be false
            controller.setData(jokesList,false,null,null);
        }

        loading = !loading;
    }

    private void setDataWithResults(ResultsUserJoke resultsUserJoke) {

        totalPages = resultsUserJoke.getCount()/10;
        firstItemid = resultsUserJoke.getUserJokes().get(0).getId();
        if(resultsUserJoke.getCount() % 10 > 0){
            //if we have an additional page of items less than 10
            totalPages++;
        }

        if(jokesList.size() > 0){
            jokesList = new ArrayList<>();
            controller.setData(jokesList,true,null,firstItemid);
        }
        jokesList.addAll(resultsUserJoke.getUserJokes());
        controller.setData(jokesList,true,null,firstItemid);

        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setupRv() {

        //pinterest layout
        //final StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        controller=new UserJokesController(this,databaseTransactions,sharedPreferences);
        layoutManager=new LinearLayoutManager(getActivity());
//        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

//        controller.setSpanCount(2);
//        layoutManager.setSpanSizeLookup(controller.getSpanSizeLookup());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(controller.getAdapter());

    }

    private void showContentUI() {
        animateRoot();
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshListener);
    }

    private void hideLoginUI() {
        animateRoot();
        signInButton.setVisibility(View.GONE);
        message_tv.setVisibility(View.GONE);
        noteTv.setVisibility(View.GONE);
        if(usernameEt.getVisibility() == View.VISIBLE){
            usernameEt.setVisibility(View.GONE);
        }

        if(proceedBtn.getVisibility() == View.VISIBLE){
            proceedBtn.setVisibility(View.GONE);
        }


    }

    private void initGoogleSignIn() {

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        if(getActivity()!=null){
            mGoogleSignInClient= GoogleSignIn.getClient(getActivity(),gso);
        }


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void apiSetup() {
        retrofit = ApiClient.getPsychAPIClient();
        jokesApi = retrofit.create(JokesApi.class);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Results returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);


                //Account : "+task.getResult().getAccount().toString());
                //Account Display Name : "+task.getResult().getDisplayName());
                //Account Email : "+task.getResult().getEmail());
                //Account Family Name : "+task.getResult().getFamilyName());
                //Account Given Name: "+task.getResult().getGivenName());
                //Account ID : "+task.getResult().getId());
                //Account ID Token : "+task.getResult().getIdToken());
                //Account Server Auth Code : "+task.getResult().getServerAuthCode());
                //Account Granted Scope : "+task.getResult().getGrantedScopes());
                //Account Photo Url : "+task.getResult().getPhotoUrl());
                //Account Requested Scope : "+task.getResult().getRequestedScopes());


                /*
                 * Saving user info in a map after fetched successfully
                 * User info can be retrieved from sharedPreferences with user_info_map_pref and then call
                 * function getMapFromString in sharedPreferences
                 * */

                Map<String,String> userInfoMap = new HashMap<>();
//

                if(!inputUsername.equals("")){

                    userInfoMap.put(getString(R.string.username), inputUsername);

                }else {

                    if(task.getResult().getDisplayName() != null ){
                        userInfoMap.put(getString(R.string.username), task.getResult().getDisplayName());
                    }

                }

//                if(task.getResult().getId() != null ){
//                    userInfoMap.put(getString(R.string.uid), task.getResult().getId());
//                }
                if(task.getResult().getPhotoUrl() != null ){
                    userInfoMap.put(getString(R.string.profilePic), task.getResult().getPhotoUrl().toString());
                }
                if(task.getResult().getEmail() != null ){
                    userInfoMap.put(getString(R.string.email), task.getResult().getEmail());
                }
//                    //we make sure that email is stored in sharedPreferences for subscription Validation
//                    // if not email, save Username
//                    if(task.getResult().getDisplayName() != null){
//                        userInfoMap.put(getString(R.string.email), task.getResult().getDisplayName());
//                    }
//                }
//
//
                String infoMapString = sharedPreferences.getStringFromMap(userInfoMap);
                sharedPreferences.saveStringPref(getString(R.string.user_info_map),infoMapString);
//
//
//
                if(account != null ){
                    firebaseAuthWithGoogle(account);

                }

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                message_tv.setText("Google Sign In Failed : "+e.getMessage());
//                hideProgress();
                // ...
            }
        }
    }



    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            User Info  User : : "+task.getResult().getUser());
//                            User Info ProviderId : "+task.getResult().getAdditionalUserInfo().getProviderId());
//                            User Info Username : "+task.getResult().getAdditionalUserInfo().getUsername());
//                            User Info Profile : "+task.getResult().getAdditionalUserInfo().getProfile());
                            // Toast.makeText(AuthActivity.this, "Log In Success", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();


//                            sessionManager.authenticateUser(getUserLive(user));

                            sharedPreferences.saveBooleanPref(getString(R.string.loginStatus),true);
                            loadContent();


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            String exception = String.format("signInWithCredential:failure%s", task.getException());
                            message_tv.setText(exception);
                            // Toast.makeText(MyApplication.getAppContext(), "Login Failed", Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void addJoke(HeaderModel_ model, int position, View clickedView) {

        startActivity(new Intent(getActivity(),AddJokeActivity.class));
    }

    private void showListPopupWindow(View anchor) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem("Recent", R.drawable.ic_active));
        listPopupItems.add(new ListPopupItem("Popular", R.drawable.ic_active));
        listPopupItems.add(new ListPopupItem("Most Toxic", R.drawable.ic_active));
        listPopupItems.add(new ListPopupItem("My jokes", R.drawable.ic_active));

        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, ViewGroup.LayoutParams.MATCH_PARENT, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                switch(position){
                    case 0: //
                        orderingField = "-timestamp";
                        fetchAndSetData(false);
                        break;
                    case 1:
                        orderingField = "-thumbs_ups";
                        fetchAndSetData(false);
                        break;
                    case 2:
                        orderingField = "-thumbs_downs";
                        fetchAndSetData(false);
                        break;
                    case 3:
                        orderingField = "-timestamp";
                        fetchAndSetData(true);
                        break;

                }
            }
        });
        listPopupWindow.show();
    }

    private ListPopupWindow createListPopupWindow(View anchor, int width,
                                                  List<ListPopupItem> items) {
        final ListPopupWindow popup = new ListPopupWindow(getActivity());
        ListAdapter adapter = new ListPopupWindowAdapter(items);
        popup.setAnchorView(anchor);
        popup.setWidth(width);
        popup.setAdapter(adapter);
        return popup;
    }

    @Override
    public void filterJokes(HeaderModel_ model, int position, View clickedView) {

        showListPopupWindow(clickedView);

    }

    @Override
    public void settingsJoke(HeaderModel_ model, int position, View clickedView) {

    }


    private void updateUserJoke(UserJoke userJoke){

        jokesApi.updateUserJoke(userJoke, userJoke.getId())
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
                            Toast.makeText(getActivity(), "UserJoke updated", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Error while updating Thumbs Up");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void upvoteClicked(final UserJokeModel_ model, int position, final View clickedView) {



        //You can hit either upvote or downvote only once for each joke
        databaseEntryHelper.upVoteJoke(model.joke(),null,clickedView);





    }


    @Override
    public void downVoteClick(final UserJokeModel_ model, int position, final View clickedView) {


        databaseEntryHelper.downVoteJoke(model.joke(),null,clickedView);




    }





}
