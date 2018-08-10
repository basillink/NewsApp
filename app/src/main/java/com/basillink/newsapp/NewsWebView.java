package com.basillink.newsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class NewsWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web_view);

        // Retrieve the url passed on item clicked from the NewsActivity
        String newsUrl = getIntent().getExtras().getString("KEY_URL");

        // Find a reference to the WebView in the layout
        WebView webView = findViewById(R.id.news_webview);
        // Load the web page in the WebView using the loadUrl() method
        webView.loadUrl(newsUrl);
    }
}
