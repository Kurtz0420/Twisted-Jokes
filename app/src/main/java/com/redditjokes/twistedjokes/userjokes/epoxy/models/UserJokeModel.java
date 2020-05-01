package com.redditjokes.twistedjokes.userjokes.epoxy.models;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bumptech.glide.Glide;
import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.api.models.UserJoke;
import com.redditjokes.twistedjokes.room2.BookmarkEntity;
import com.redditjokes.twistedjokes.room2.DatabaseTransactions;
import com.redditjokes.twistedjokes.userjokes.epoxy.holders.UserJokeHolder;
import com.redditjokes.twistedjokes.utils.AppExecutors;
import com.redditjokes.twistedjokes.utils.ColorsHelper;

import java.util.List;

@EpoxyModelClass(layout = R.layout.user_joke_item_layout)
public class UserJokeModel extends EpoxyModelWithHolder<UserJokeHolder> {

    private static final String TAG = "UserJokeModel";

    @EpoxyAttribute
    UserJoke joke;
    @EpoxyAttribute
    View.OnClickListener UpvoteClickListener;
    @EpoxyAttribute
    View.OnClickListener DownvoteClicklistener;

    @EpoxyAttribute
    DatabaseTransactions databaseTransactions;

    @Override
    public void unbind(@NonNull UserJokeHolder holder) {

    }

    @Override
    public void bind(@NonNull final UserJokeHolder holder) {

        if(!joke.getSlug().equals("")){
            Glide.with(holder.view.getContext()).load(joke.getSlug()).into(holder.userProfileIv);
        }
        holder.userProfileIv.setBorderColor(ColorsHelper.getRandomColor());
        holder.buildUpTv.setText(joke.getBuild_up());
        holder.deliveryTv.setText(joke.getDelivery());
        holder.usernameTv.setText(joke.getUsername());

        holder.upvodeBtnLinear.setOnClickListener(UpvoteClickListener);
        holder.downVoteBtnLinear.setOnClickListener(DownvoteClicklistener);

        String upvotes = numberCalculation(joke.getThumbs_ups()) + " Upvotes";
        String downvotes = numberCalculation(joke.getThumbs_downs()) + " Downvotes";
        holder.upvoteTv.setText(upvotes);
        holder.downvoteTv.setText(downvotes);


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                List<BookmarkEntity> bookmarkEntityList = databaseTransactions.getBookmarkByThumbsUpIfPresent(joke.getId());

                if(bookmarkEntityList.size() > 0){
                    //joke is already upvoted by user
                    //change color of button
                    setColorForPressed(holder.upvodeBtnLinear);

                }else {

                    setColorForNotPressed(holder.upvodeBtnLinear);
                }

                List<BookmarkEntity> bookmarkEntityList2 = databaseTransactions.getBookmarkByThumbsDownIfPresent(joke.getId());

                if(bookmarkEntityList2.size() > 0){
                    //joke is already upvoted by user
                    //change color of button
                    setColorForPressed(holder.downVoteBtnLinear);
                }else {

                    setColorForNotPressed(holder.downVoteBtnLinear);
                }
            }
        });

    }

    private void setColorForPressed(final LinearLayout linearLayout){
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                linearLayout.setBackground(linearLayout.getContext().getResources().getDrawable(R.drawable.button_background_our_joke2));

            }
        });

    }
    private void setColorForNotPressed(final LinearLayout linearLayout){
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                linearLayout.setBackground(linearLayout.getContext().getResources().getDrawable(R.drawable.button_background_our_joke));
            }
        });

    }
    private String numberCalculation(long number) {
        if (number < 1000)
            return "" + number;
        int exp = (int) (Math.log(number) / Math.log(1000));
        return String.format("%.1f %c", number / Math.pow(1000, exp), "kMGTPE".charAt(exp-1));
    }



    @Override
    protected UserJokeHolder createNewHolder() {
        return new UserJokeHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.user_joke_item_layout;
    }

    @Override
    public boolean shouldSaveViewState() {
        return false;
    }

}
