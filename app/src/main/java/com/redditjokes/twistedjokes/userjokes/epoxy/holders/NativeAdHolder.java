package com.redditjokes.twistedjokes.userjokes.epoxy.holders;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.redditjokes.twistedjokes.R;


public class NativeAdHolder extends EpoxyHolder {

    public View view;
    public FrameLayout frameLayout;
    @Override
    protected void bindView(@NonNull View itemView) {

        view=itemView;
        frameLayout = view.findViewById(R.id.id_native_ad_frame);
//        progressBar=view.findViewById(R.id.rotateloading);

    }
}
