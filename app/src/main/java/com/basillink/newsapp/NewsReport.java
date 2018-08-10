package com.basillink.newsapp;

/**
 * An {@link NewsReport} object contains information related to a single news report.
 */
public class NewsReport {

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

    /**
     * The Constructor for a new {@link NewsReport}.
     * @param image         the image relevant to the news article
     * @param headline      the headline of the news article
     * @param description   the description of the news article
     * @param publishedTime the time of publication of the news article
     * @param urlPath       the url link of the news article
     * @param sourceName    the media source of the news article
     */
    public NewsReport(String image, String headline, String description, String publishedTime,
                      String urlPath, String sourceName) {
        this.mNewsIcon = image;
        this.mHeadline = headline;
        this.mDescription = description;
        this.mPublishedTime = publishedTime;
        this.mUrlPath = urlPath;
        this.mSourceName = sourceName;
    }

    /** Get the image icon for the news article. */
    public String getNewsIcon() {
        return mNewsIcon;
    }

    /** Get the headline for the news article. */
    public String getHeadline() {
        return mHeadline;
    }

    /** Get the description for the news article. */
    public String getDescription() {
        return mDescription;
    }

    /** Get the published time for the news article. */
    public String getPublishedTime() {
        return mPublishedTime;
    }

    /** Get the url link for the news article. */
    public String getUrlPath() {
        return mUrlPath;
    }

    /** Get the news source for the news article. */
    public String getSourceName() {
        return mSourceName;
    }
}
