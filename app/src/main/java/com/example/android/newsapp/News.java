package com.example.android.newsapp;

class News {

    final private String mTitle;
    final private String mDate;
    final private String mAuthor;
    final private String mCategory;
    final private String mURL;

    News(String title, String date, String author, String category, String url) {

        mTitle = title;
        mDate = date;
        mAuthor = author;
        mCategory = category;
        mURL = url;
    }

    String getmTitle() {
        return mTitle;
    }

    String getmDate() {
        return mDate;
    }

    String getmAuthor() {
        return mAuthor;
    }

    String getmCategory() {
        return mCategory;
    }

    String getmURL() {
        return mURL;
    }
}