package com.example.android.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refresh;
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new NewsAdapter(this);
        ListView listView = (ListView) findViewById(R.id.listID);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News news = mAdapter.getItem(position);
                String url = null;
                if (news != null) {
                    url = news.getmURL();
                }
                Intent i = new Intent(Intent.ACTION_VIEW);

                if (i.resolveActivity(getPackageManager()) != null) {
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        refresh = (SwipeRefreshLayout) findViewById(R.id.refreshID);
        refresh.setOnRefreshListener(this);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        refresh.setRefreshing(false);
        if (data != null) {
            mAdapter.setNotifyOnChange(false);
            mAdapter.clear();
            mAdapter.setNotifyOnChange(true);
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }
}