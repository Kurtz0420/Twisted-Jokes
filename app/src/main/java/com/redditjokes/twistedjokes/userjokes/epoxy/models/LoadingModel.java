package com.redditjokes.twistedjokes.userjokes.epoxy.models;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.userjokes.epoxy.holders.LoadingHolder;


@EpoxyModelClass(layout = R.layout.loading_layout)
public class LoadingModel extends EpoxyModelWithHolder<LoadingHolder> {

    //@EpoxyAttribute CharSequence catTitle;
    //@EpoxyAttribute CharSequence catImage;


    @Override
    public void unbind(@NonNull LoadingHolder holder) {

    }

    @Override
    public void bind(@NonNull LoadingHolder holder) {

//        holder.progressBar.setVisibility(View.VISIBLE);
        holder.progressBar.start();

    }


    @Override
    protected LoadingHolder createNewHolder() {
        return new LoadingHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.loading_layout;
    }
}

