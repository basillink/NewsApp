package com.basillink.newsapp;

import android.text.TextUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news articles from the API.
 */
public class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /** An empty private constructor of {@link QueryUtils}. */
    private QueryUtils() {
    }

    /**
     * Query the News Api and return a list of {@link NewsReport} objects.
     * @param requestUrl the query url
     */
    public static List<NewsReport> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform a HTTP request to the URL and receive a JSON response.
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}
        List<NewsReport> latestNews = extractFromJSON(jsonResponse);

        return latestNews;
    }

    /**
     * @param urlRequestPath The Url path to process the "GET" request.
     * @return New URL object from the given string URL path.
     */
    private static URL createUrl(String urlRequestPath) {
        URL url = null;
        try {
            url = new URL(urlRequestPath);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating Url" + e.getMessage(), e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     *
     * @param url to make the request to.
     * @return converted jsonResponse string of data to be presented to the UI.
     * @throws IOException If an I/O exception occurs.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return nothing
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* in milliseconds */);
            urlConnection.setConnectTimeout(15000 /* in milliseconds */);
            urlConnection.connect();

            // If the url connection request is successful,
            // then read the inputStream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * @param inputStream The {@link InputStream} that holds JSON response to be converted.
     * @return The usable data to be presented to the UI.
     * @throws IOException If an I/O exception occurs.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringOutput = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String returnedLine = bufferedReader.readLine();
            while (returnedLine != null) {
                stringOutput.append(returnedLine);
                returnedLine = bufferedReader.readLine();
            }
        }
        return stringOutput.toString();
    }

    /**
     * @param newsJSON received from the URL API
     * @return a list of {@link NewsReport} objects built up from the JSON response.
     */
    private static List<NewsReport> extractFromJSON(String newsJSON) {
        // If JSON string is empty or null, then return null
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that will hold the news articles.
        ArrayList<NewsReport> newsReports = new ArrayList<NewsReport>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject rootJsonObject = new JSONObject(newsJSON);

            // Extract the JSONArray associated with the key called "articles",
            // which represents a list of news reports.
            JSONArray articlesArray = rootJsonObject.getJSONArray("articles");

            // Check if there are results in the articles array to return.
            int lengthOfArticlesArray = articlesArray.length();
            if (lengthOfArticlesArray > 0) {

                // For each news report in the articlesArray, create a {@link NewsReport} object.
                for (int i = 0; i < lengthOfArticlesArray; i++) {

                    // Get a single news report at position i within the list of articles.
                    JSONObject currentArticle = articlesArray.getJSONObject(i);

                    // For a given article, extract the JSONObject associated with the key "sources",
                    // which holds the name of the media that reports the news.
                    JSONObject newsSources = currentArticle.getJSONObject("source");

                    // Extract the value for the key called "name"
                    String authorSources = newsSources.getString("name");

                    // Extract the value for the key called "title"
                    String newsTitle = currentArticle.getString("title");

                    // Extract the value for the key called "description"
                    String description = currentArticle.getString("description");

                    // Extract the value for the key called "url"
                    String urlPath = currentArticle.getString("url");

                    // Extract the value for the key called "urlToImage"
                    String newsImage = currentArticle.getString("urlToImage");

                    // Extract the value for the key called "publishedAt"
                    String publishedTime = currentArticle.getString("publishedAt");

                    // Create a new {@link NewsReport} object with the name, title, description,
                    // url, urlToImage, and publishedAt from the JSON response.
                    NewsReport newsReport = new NewsReport(newsImage, newsTitle,
                            description, publishedTime, urlPath, authorSources);

                    // Add the new {@link Earthquake} to the list of earthquakes.
                    newsReports.add(newsReport);
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news report JSON results", e);
        }
        return newsReports;
    }

}
