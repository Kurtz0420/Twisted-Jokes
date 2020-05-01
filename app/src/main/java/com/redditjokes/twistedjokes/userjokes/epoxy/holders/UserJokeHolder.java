package com.redditjokes.twistedjokes.userjokes.epoxy.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.airbnb.epoxy.EpoxyHolder;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.redditjokes.twistedjokes.R;



public class UserJokeHolder extends EpoxyHolder {

    public View view;
    public TextView buildUpTv;
    public TextView deliveryTv;
    public TextView usernameTv;
    public TextView upvoteTv;
    public TextView downvoteTv;
    public CircularImageView userProfileIv;
    public LinearLayout upvodeBtnLinear,downVoteBtnLinear;


    @Override
    protected void bindView(@NonNull View itemView) {
        view=itemView;
        buildUpTv = view.findViewById(R.id.user_joke_buildUp_tv);
        deliveryTv = view.findViewById(R.id.user_joke_deliverytv);
        usernameTv = view.findViewById(R.id.usernameTvOurJoke);
        userProfileIv = view.findViewById(R.id.profile_image);
        upvodeBtnLinear = view.findViewById(R.id.upvoteLinear);
        downVoteBtnLinear = view.findViewById(R.id.downvoteLinear);
        upvoteTv = view.findViewById(R.id.upvoteTv_userJoke);
        downvoteTv = view.findViewById(R.id.downvoteTv_userJoke);

//        description_tv=view.findViewById(R.id.psych_feautred_item_descriptiontv);
    }
}
