package com.redditjokes.twistedjokes.userjokes.epoxy.holders;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.redditjokes.twistedjokes.R;


public class HeaderHolder extends EpoxyHolder {

    public View view;
    public ImageView addBtn,filterBtn,settingsBtn;
    @Override
    protected void bindView(@NonNull View itemView) {

        view=itemView;
        addBtn = view.findViewById(R.id.addjokeuserJoke);
        filterBtn = view.findViewById(R.id.filterJokesUserJoke);
        settingsBtn = view.findViewById(R.id.settingsUserJoke);
//        progressBar=view.findViewById(R.id.rotateloading);

    }
}
