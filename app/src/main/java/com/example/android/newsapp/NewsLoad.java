package com.example.android.newsapp;

import android.content.Context;
import android.util.Log;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.URL;
import java.util.List;

class NewsLoad extends AsyncTaskLoader<List<News>> {

    private String LOG_TAG = NewsLoad.class.getName();

    NewsLoad(Context context) {
        super(context);
    }

    @Override
    public List<News> loadInBackground() {

        List<News> newses = null;

        try {
            URL url = NewsQuery.createUrl();
            String json = NewsQuery.makeHttpRequest(url);
            newses = NewsQuery.fromJson(json);

        } catch (IOException e) {
            Log.e(LOG_TAG, "NewsLoad Error: ", e);
        }
        return newses;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}