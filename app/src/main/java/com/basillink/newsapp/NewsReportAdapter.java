package com.basillink.newsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A {@link NewsReportAdapter} knows how to create a list item layout for each news article
 * in the data source (a list of {@link NewsReport} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsReportAdapter extends ArrayAdapter<NewsReport> {
    /**
     * Constructs a new {@link NewsReportAdapter}.
     *
     * @param context     of the app
     * @param newsReports is the list of news articles, which is the data source of the adapter
     */
    public NewsReportAdapter(@NonNull Context context, @NonNull ArrayList<NewsReport> newsReports) {
        super(context, 0, newsReports);
    }

    /**
     * Returns a list item view that displays information about the news article at the given
     * position in the list of news reports.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.highlights, parent, false);
        }

        // Find the news article at the given position in the list of news reports
        NewsReport currentNewsReport = getItem(position);

        ViewHolder holder = new ViewHolder();

        // Find the ImageView with view ID image
        holder.imageView = listItemView.findViewById(R.id.image);
        // Display the image of the current news article in that ImageView
        new DownloadImage(holder.imageView).execute();

        // Find the TextView with view ID headline_tv
        holder.tvTitle = listItemView.findViewById(R.id.headline_tv);
        // Display the headline of the current news article in that TextView
        holder.tvTitle.setText(currentNewsReport.getHeadline());

        // Create a new Date object from the time in date and time of the news article
        Date dateObject = new Date(currentNewsReport.getPublishedTime());
        // Find the TextView with view ID published_time_tv
        holder.tvTime = listItemView.findViewById(R.id.published_time_tv);
        // Display the published time of the current news article in that TextView
        holder.tvTime.setText(formatDateTime(dateObject));

        // Find the TextView with view ID description_tv
        holder.tvDescription = listItemView.findViewById(R.id.description_tv);
        // Display the description of the current news article in that TextView
        holder.tvDescription.setText(currentNewsReport.getDescription());

        // Find the TextView with view ID url_path_tv
        holder.tvUrl = listItemView.findViewById(R.id.url_path_tv);
        // Display the url path of the current news article in that TextView
        holder.tvUrl.setText(currentNewsReport.getUrlPath());

        // Find the TextView with view ID source_name_tv
        holder.sourceName = listItemView.findViewById(R.id.source_name_tv);
        // Display the news source of the current news article in that TextView
        holder.sourceName.setText(currentNewsReport.getSourceName());

        return listItemView;
    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView tvTitle;
        public TextView tvTime;
        public TextView tvDescription;
        public TextView tvUrl;
        public TextView sourceName;
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        ImageView mImageView;

        private DownloadImage(ImageView imageView) {
            this.mImageView = imageView;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlToImagePath = urls[0];
            Bitmap urlImage = null;

            try {
                InputStream stream = new URL(urlToImagePath).openStream();
                urlImage = BitmapFactory.decodeStream(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return urlImage;
        }
    }

    /**
     * Return the formatted date and time string (i.e. "4:30 PM | 1-Jan-2018") from a Date object.
     */
    private String formatDateTime(Date dateObject){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a | d-MMM-YYYY");
        return simpleDateFormat.format(dateObject);
    }
}
