package com.simpler.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.simpler.myapplication.adapters.RecyclerViewAdapter;
import com.simpler.myapplication.listeners.ResponseListener;
import com.simpler.myapplication.model.JsonCommentsModel;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<JsonCommentsModel> feedList;
    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.docRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0)); //preparing recycler view

        getFirstResponse();

    }
    @SuppressWarnings("unchecked")
    private void getFirstResponse() { // response fetched for the last activity
        Intent i = getIntent();
        List<JsonCommentsModel> comments = (List<JsonCommentsModel>) i.getSerializableExtra("comments");
        feedList = comments;
        adapter = new RecyclerViewAdapter(MainActivity.this, comments);
        recyclerView.setAdapter(adapter);

        checkRecyclerViewScroll(recyclerView);
    }

/**
 * Populates the recyclerView with the comments list
 *
/ */
    private void populateFeedList(int[] bounds, String dir) {
        ResponseListener responseListener = new ResponseListener() {
            @Override
            public void onResponse(List<JsonCommentsModel> response) {
                feedList = response;
                adapter = new RecyclerViewAdapter(MainActivity.this, response);
                recyclerView.setAdapter(adapter);
                checkRecyclerViewScroll(recyclerView);
                if (dir.equals("top")){
                    recyclerView.scrollToPosition(10);// Keeping 10 hidden comment above when scrolling down so that scrolling up afterwards will be smoother
                }else{
                    recyclerView.scrollToPosition(1);// Keeping 1 hidden comment above when scrolling up so that scrolling up would never be stuck
                }
                Log.d(getString(R.string.fetching_list), bounds[0]+" -> "+bounds[1] +" DIR = "+ dir); //dir is direction scrolled
            }
        };

        RequestManager requestManager = new RequestManager(responseListener);
        requestManager.start(bounds, null);
    }

    /**
     * adds listeners to the recyclerView
     * so there will be a reference for when it's scrolled down or up
     *
     / */
    private void checkRecyclerViewScroll(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager == null){
                    return;
                }
                if (!isLoading) {// Wait for the recyclerView to load
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == feedList.size() - 1 &&dy > 0) {

                        recyclerView.setLayoutManager(linearLayoutManager);
                        int scrollPosition = feedList.get(feedList.size()-11).getId();
                        int nextLimit = scrollPosition + 20; //Added 20 comments so the scrolling will be a bit smoother.

                        //list bottom
                        loadMore(getBounds(scrollPosition, nextLimit), getString(R.string.bottom)); // Loading more data and populating list with new bounds, setting
                        isLoading = true;
                    }

                    int itemFirst = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

                    if(itemFirst == 0 && feedList.get(0).getId()>1 && dy < 0){
                        int scrollPosition = feedList.get(feedList.size()-1).getId() - 30;
                        int nextLimit = scrollPosition + 20;

                        //list top
                        loadMore(getBounds(scrollPosition, nextLimit), getString(R.string.top));

                        isLoading = true;
                    }

                }
            }
        });
    }

    private void loadMore(int[] bounds, String dir) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            populateFeedList(bounds,dir);
            adapter.notifyDataSetChanged();// Notifying adapter about populating the comments list
            isLoading = false;
        }, 1);
    }

    private int[] getBounds(Integer scrollPosition, Integer nextLimit) {
        return new int[]{scrollPosition, nextLimit};
    }
}