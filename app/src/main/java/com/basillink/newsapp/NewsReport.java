package com.basillink.newsapp;

/**
 * An {@link NewsReport} object contains information related to a single news report.
 */
public class NewsReport {

    /** Website URL of the earthquake */
    private String mUrl;

    /** Image of the News Report */
    private String mNewsIcon;

    /** Headline of the News Report */
    private String mHeadline;

    /** Details of the News Report */
    private String mDescription;

    /** Time of publication of the News Report */
    private String mPublishedTime;

    /** Url path to the source of the News Report */
    private String mUrlPath;

    /** Media source of the News Report */
    private String mSourceName;

    public NewsReport(String image, String headline, String description, String publishedTime,
                      String urlPath, String sourceName) {
        this.mNewsIcon = image;
        this.mHeadline = headline;
        this.mDescription = description;
        this.mPublishedTime = publishedTime;
        this.mUrlPath = urlPath;
        this.mSourceName = sourceName;
    }

    public NewsReport(String image, String headline, String description, String publishedTime,
                      String urlPath, String sourceName, String url) {
        this.mNewsIcon = image;
        this.mHeadline = headline;
        this.mDescription = description;
        this.mPublishedTime = publishedTime;
        this.mUrlPath = urlPath;
        this.mSourceName = sourceName;
        this.mUrl = url;
    }

    public String getNewsIcon() {
        return mNewsIcon;

    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPublishedTime() {
        return mPublishedTime;
    }

    public String getUrlPath() {
        return mUrlPath;
    }

    public String getSourceName() {
        return mSourceName;
    }

    public String getUrl() {
        return mUrl;
    }
}
