package com.redditjokes.twistedjokes.userjokes.epoxy.models;

import android.view.View;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.userjokes.epoxy.holders.HeaderHolder;


@EpoxyModelClass(layout = R.layout.user_joke_header_layout)
public class HeaderModel extends EpoxyModelWithHolder<HeaderHolder> {

    @EpoxyAttribute
    View.OnClickListener addJokeBtnClickListener;
    @EpoxyAttribute
    View.OnClickListener filterbtnClickListener;
    @EpoxyAttribute
    View.OnClickListener settingsBtnClickListener;

    @Override
    public void unbind(@NonNull final HeaderHolder holder) {


    }

    @Override
    public void bind(@NonNull final HeaderHolder holder) {

//        holder.progressBar.setVisibility(View.VISIBLE);
//        holder.progressBar.start();
        holder.addBtn.setOnClickListener(addJokeBtnClickListener);

        holder.filterBtn.setOnClickListener(filterbtnClickListener);
        holder.settingsBtn.setOnClickListener(settingsBtnClickListener);

    }


    @Override
    protected HeaderHolder createNewHolder() {
        return new HeaderHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.user_joke_header_layout;
    }
}

