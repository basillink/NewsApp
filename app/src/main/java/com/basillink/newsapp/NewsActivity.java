package com.basillink.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsReport>> {

    /**
     * Constant value for the news report loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_REPORT_LOADER_ID = 1;

    /** Tag for the log messages */
    private static final String LOG_TAG = NewsActivity.class.getSimpleName();

    /** URL for news report data from the News Api */
    private static final String NEWS_REPORT_REQUEST_URL =
            "https://newsapi.org/v2/top-headlines?country=ng&apiKey=bb7cba34610f4e27afb54cdb4dfced70";

    /** Adapter for the list of news report */
    private NewsReportAdapter newsAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Find a reference to the {@link ListView} in the layout
        ListView newsList = findViewById(R.id.news_list_view);

        // Find a reference to the empty TextView in the layout
        mEmptyStateTextView = findViewById(R.id.empty_view);
        // Update the UI with an Empty state if no news report was returned
        newsList.setEmptyView(mEmptyStateTextView);

        // Create an {@link NewsReportAdapter}, whose data source is a list of {@link NewsReport}s
        newsAdapter = new NewsReportAdapter(this, new ArrayList<NewsReport>());

        //Make the {@link ListView} use the {@link NewsReportAdapter} created above, so that the
        //{@link ListView} will display news article for each {@link NewsReport} in the list.
        newsList.setAdapter(newsAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news report.
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news article that was clicked on
                NewsReport currentNewsReport = newsAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNewsReport.getUrlPath());

                // Convert the news URI to a string
                String newsArticleUrl = newsUri.toString();

                // Create a new intent to view the news report URI
                Intent websiteIntent = new Intent(getApplicationContext(), NewsWebView.class);
                // Pass in the news url string to the websiteIntent
                websiteIntent.putExtra("KEY_URL", newsArticleUrl);
                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above
            // and pass in null for the bundle.
            // Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_REPORT_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    /**
     * This is called when a new Loader needs to be created.
     * First, the base URI is parsed to the URI for processing.
     * @return a {@link NewsLoader}
     */
    @Override
    public Loader<List<NewsReport>> onCreateLoader(int id, Bundle bundle) {
        Uri baseUri = Uri.parse(NEWS_REPORT_REQUEST_URL);
        return new NewsLoader(this, baseUri.toString());
    }

    /**
     * This is called when a previously created loader has finished its load.
     * @param loader that has finished.
     * @param newsReports generated by the loader
     */
    @Override
    public void onLoadFinished(Loader<List<NewsReport>> loader, List<NewsReport> newsReports) {
        // Hide loading indicator after the data has finished loading
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No News Report found."
        mEmptyStateTextView.setText(R.string.no_news_report_found);

        // If there is a valid list of {@link NewsReport}s, then add them to the adapter's data set.
        // This will trigger the ListView to update.
        if (newsReports != null && !newsReports.isEmpty()) {
            newsAdapter.addAll(newsReports);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsReport>> loader) {
        // Loader reset, so we can clear out our existing data.
        newsAdapter.clear();
    }
}
