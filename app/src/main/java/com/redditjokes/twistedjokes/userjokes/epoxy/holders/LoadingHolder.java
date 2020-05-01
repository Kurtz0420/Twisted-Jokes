package com.redditjokes.twistedjokes.userjokes.epoxy.holders;

import android.view.View;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.redditjokes.twistedjokes.R;
import com.victor.loading.rotate.RotateLoading;


public class LoadingHolder extends EpoxyHolder {

    public View view;
    public RotateLoading progressBar;

    @Override
    protected void bindView(@NonNull View itemView) {

        view=itemView;
        progressBar=view.findViewById(R.id.rotateloading);

    }
}
