package com.example.android.newsapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class NewsQuery {

    private static final String LOG_TAG = NewsQuery.class.getSimpleName();

    static URL createUrl() {
        String url = stringUrl();
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL: ", e);
            return null;
        }
    }

    private static String stringUrl() {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .encodedAuthority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("order-by", "newest")
                .appendQueryParameter("show-references", "author")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("q", "Android")
                .appendQueryParameter("api-key", "test");
        return builder.build().toString();
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readLine = bufferedReader.readLine();
            while (readLine != null) {
                output.append(readLine);
                readLine = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    static String makeHttpRequest(URL url) throws IOException {
        String json = "";

        if (url == null) {
            return json;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream stream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpURLConnection.getInputStream();
                json = readFromStream(stream);
            } else {
                Log.e("MainActivity", "Error response: " + httpURLConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error HTTP: ", e);

        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (stream != null) {
                stream.close();
            }
        }
        return json;
    }

    static List<News> fromJson(String newsJson) {

        ArrayList<News> newses = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(newsJson);
            JSONObject jsonResult = jsonResponse.getJSONObject("response");
            JSONArray result = jsonResult.getJSONArray("results");

            for (int i = 0; i < result.length(); i++) {

                JSONObject currentNews = result.getJSONObject(i);
                String title = currentNews.getString("webTitle");
                String date = currentNews.getString("webPublicationDate");
                date = differentDate(date);
                String category = currentNews.getString("sectionName");
                JSONArray tags = currentNews.getJSONArray("tags");
                String author = "";

                if (tags.length() == 0) {
                    author = null;
                } else {
                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject jsonObject = tags.getJSONObject(j);
                        author += jsonObject.getString("webTitle");
                        if (tags.length() > 1) {
                            author += ", ";
                        }
                    }
                }

                String url = currentNews.getString("webUrl");
                newses.add(new News(title, date, category, author, url));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error JSON", e);
        }
        return newses;
    }

    private static String differentDate(String rawDate) {

        String jsonDate = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat dateFormat = new SimpleDateFormat(jsonDate, Locale.US);
        try {
            Date date = dateFormat.parse(rawDate);
            String formattedDate = "MMM d, yyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formattedDate, Locale.US);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error parsing JSON Date: ", e);
            return "";
        }
    }
}