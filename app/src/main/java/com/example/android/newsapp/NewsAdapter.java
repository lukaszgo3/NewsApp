package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class NewsAdapter extends ArrayAdapter<News> {

    NewsAdapter(Context context) {
        super(context, 0, new ArrayList<News>());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_news, parent, false);
        }
        News news = getItem(position);

        TextView title = (TextView) convertView.findViewById(R.id.titleID);
        TextView date = (TextView) convertView.findViewById(R.id.dateID);
        TextView author = (TextView) convertView.findViewById(R.id.authorID);
        TextView category = (TextView) convertView.findViewById(R.id.categoryID);

        assert news != null;
        title.setText(news.getmTitle());
        date.setText(news.getmDate());
        author.setText(news.getmAuthor());
        category.setText(news.getmCategory());

        return convertView;
    }
}