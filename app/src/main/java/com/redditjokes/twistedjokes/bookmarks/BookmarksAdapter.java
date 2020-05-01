package com.redditjokes.twistedjokes.bookmarks;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.room2.BookmarkEntity;
import com.redditjokes.twistedjokes.room2.DatabaseTransactions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookmarksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BookmarksAdapter";

    public static final int CLIP_VIEWTYPE = 101;
    public static final int HEADER_VIEWTYPE = 102;

    private List<BookmarkEntity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    BookmarksAdapter(Context context, ItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        this.mClickListener = itemClickListener;
        this.context = context;

    }

    // inflates the row layout from xml when needed
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == CLIP_VIEWTYPE){
            View view = mInflater.inflate(R.layout.bookmark_item, parent, false);
            return new BookmarkHolder(view);
        }else {
            View view = mInflater.inflate(R.layout.bookmarks_header_layout, parent, false);
            return new HeaderHolder(view);
        }

    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        switch (holder.getItemViewType()){

            case CLIP_VIEWTYPE:

                BookmarkHolder holder1 = (BookmarkHolder) holder;

                setUiDataForClip(holder1,position);

                break;

            case HEADER_VIEWTYPE:

                HeaderHolder holder2 = (HeaderHolder) holder;
                setHeaderData(holder2,position);

                break;
        }




    }

    private void setHeaderData(HeaderHolder holder2, final int position) {
        //here we can set data for header
        holder2.filterJokesBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onFilterJokesClick(v,position);
            }
        });
    }

    private void setUiDataForClip(final BookmarkHolder holder, final int position) {


        holder.buildUpTv.setText(mData.get(position).getBuild_up());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mClickListener.onItemClick(holder.view, position, mData.get(position));

                animateRoot(holder.rootLinear);
                holder.deliveryTv.setText(mData.get(position).getDelivery());
                holder.deliveryTv.setVisibility(View.VISIBLE);
                holder.deleteBtn.setVisibility(View.VISIBLE);
                holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //delete that bookmark
                        new AlertDialog.Builder(holder.view.getContext())
                                .setTitle("Delete Bookmark")
                                .setMessage("Are you sure you want to delete this entry?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        deleteBookmark(position);
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            }
        });
    }

    private void deleteBookmark(int position) {

        DatabaseTransactions databaseTransactions = new DatabaseTransactions(context);
        databaseTransactions.deleteBookmark(mData.get(position).getJoke_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(context, "Entry Deleted", Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    private void animateRoot(LinearLayout linearLayout){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(linearLayout);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HEADER_VIEWTYPE;
        }
        return CLIP_VIEWTYPE;
    }

    // stores and recycles views as they are scrolled off screen
    public class BookmarkHolder extends RecyclerView.ViewHolder{

        public View view;

        public TextView buildUpTv;
        public TextView deliveryTv;
        public LinearLayout rootLinear;
        public ImageView deleteBtn;

        BookmarkHolder(View itemView) {
            super(itemView);
            view = itemView;

            buildUpTv = view.findViewById(R.id.buildUpBookmark);
            deliveryTv = view.findViewById(R.id.deliveryBookmark);
            rootLinear = view.findViewById(R.id.rootBookmarkItem);
            deleteBtn = view.findViewById(R.id.deleteBookmark);


        }

    }

    public class HeaderHolder extends RecyclerView.ViewHolder{



        public View view;
        public ImageView filterJokesBTn;

        HeaderHolder(View itemView) {
            super(itemView);
            view = itemView;
            filterJokesBTn = view.findViewById(R.id.filterJokesBookmarks);

        }

    }

    public void addBookmarks(List<BookmarkEntity> bookmarkEntityList){
        if(bookmarkEntityList.size() > 0){

            int lastCount = getItemCount();
            mData.addAll(bookmarkEntityList);
            notifyItemRangeInserted(lastCount, bookmarkEntityList.size());

        }

    }

    public void clearList(){
        if(mData !=null){
            mData.clear();
            notifyDataSetChanged();
        }
    }

    // convenience method for getting data at click position
    BookmarkEntity getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, BookmarkEntity clipEntity);
        void onFilterJokesClick(View view, int position);
    }
}