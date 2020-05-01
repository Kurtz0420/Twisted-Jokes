package com.redditjokes.twistedjokes.bookmarks;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.redditjokes.twistedjokes.R;
import com.redditjokes.twistedjokes.room2.BookmarkEntity;
import com.redditjokes.twistedjokes.room2.DatabaseTransactions;
import com.redditjokes.twistedjokes.userjokes.filtering_popup.ListPopupItem;
import com.redditjokes.twistedjokes.userjokes.filtering_popup.ListPopupWindowAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookmarksActivity extends AppCompatActivity implements BookmarksAdapter.ItemClickListener {

    private static final String TAG = "BookmarksActivity";

    private RecyclerView recyclerView;
    private List<BookmarkEntity> items_list;
    private BookmarksAdapter adapter;
    private LinearLayoutManager layoutManager;

    private DatabaseTransactions databaseTransactions;
    private CompositeDisposable mDisposibles = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        //Here we will fetch the bookmarks and allow them to filter between, Bookmarks, Bookmarks which are thumbsUp & Thumbs Down

        databaseTransactions = new DatabaseTransactions(this);

        recyclerView = findViewById(R.id.bookmarksRv);


        setupRecyclerView();

        getAndSetAllBookmarks();





    }

    private void getAndSetAllBookmarks() {

        databaseTransactions.getAllBookmarks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookmarkEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposibles.add(d);
                    }

                    @Override
                    public void onNext(List<BookmarkEntity> bookmarkEntityList) {

                        if(bookmarkEntityList !=null){


                            checkAndClearItemsList();
                            items_list.addAll(bookmarkEntityList);
                            adapter.addBookmarks(items_list);


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

    private void checkAndClearItemsList() {
            if(items_list.size() > 0){
                items_list.clear();
                items_list = new ArrayList<>();
                adapter.clearList();
            }
        items_list.add(new BookmarkEntity()); //For Header

    }

    private void getAndSetBookmarksByThumbsUp() {

        databaseTransactions.getBookmarksByThumbsUp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookmarkEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposibles.add(d);
                    }

                    @Override
                    public void onNext(List<BookmarkEntity> bookmarkEntityList) {

                        if(bookmarkEntityList !=null){


                            checkAndClearItemsList();
                            items_list.addAll(bookmarkEntityList);
                            adapter.addBookmarks(items_list);


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

    private void getAndSetBookmarksByThumbsDown() {

        databaseTransactions.getBookmarksByThumbsDown()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookmarkEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposibles.add(d);
                    }

                    @Override
                    public void onNext(List<BookmarkEntity> bookmarkEntityList) {

                        if(bookmarkEntityList !=null){


                            checkAndClearItemsList();
                            items_list.addAll(bookmarkEntityList);
                            adapter.addBookmarks(items_list);


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

    private void setupRecyclerView(){


        items_list=new ArrayList<>();

        adapter=new BookmarksAdapter(this,this);
        layoutManager=new LinearLayoutManager(this);
        final StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(View view, int position, BookmarkEntity clipEntity) {

    }

    @Override
    public void onFilterJokesClick(View view, int position) {

        showListPopupWindow(view);

    }


    private void showListPopupWindow(View anchor) {
        List<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem("All Bookmarks", R.drawable.ic_active));
        listPopupItems.add(new ListPopupItem("Upvoted", R.drawable.ic_active));
        listPopupItems.add(new ListPopupItem("Downvoted", R.drawable.ic_active));


        final ListPopupWindow listPopupWindow =
                createListPopupWindow(anchor, ViewGroup.LayoutParams.MATCH_PARENT, listPopupItems);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                switch(position){
                    case 0: //All Bookmarks
                        getAndSetAllBookmarks();
                        break;

                    case 1://Upvoted
                        getAndSetBookmarksByThumbsUp();

                        break;

                    case 2:

                        getAndSetBookmarksByThumbsDown();

                        break;

                }
            }
        });
        listPopupWindow.show();
    }

    private ListPopupWindow createListPopupWindow(View anchor, int width,
                                                  List<ListPopupItem> items) {
        final ListPopupWindow popup = new ListPopupWindow(this);
        ListAdapter adapter = new ListPopupWindowAdapter(items);
        popup.setAnchorView(anchor);
        popup.setWidth(width);
        popup.setAdapter(adapter);
        return popup;
    }
}
