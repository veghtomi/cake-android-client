package com.waracle.androidtest.network.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.waracle.androidtest.util.ImageUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> imageView;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        return downloadImage(strings[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null && imageView.get() != null) {
            imageView.get().setImageBitmap(bitmap);
        }
    }

    private Bitmap downloadImage(String imageUrl) {
        Bitmap resultBitmap = null;
        HttpURLConnection connection = null;

        try {
            final URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();

            final int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                resultBitmap = transformStreamToBitmap(connection.getInputStream());
            } else if (responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                    responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                final String newAddress = connection.getHeaderField("Location");
                final URL newUrl = new URL(newAddress);

                connection = (HttpURLConnection) newUrl.openConnection();

                resultBitmap = transformStreamToBitmap(connection.getInputStream());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        if (resultBitmap != null) {
            ImageUtil.cacheImage(imageUrl, resultBitmap);
        }

        return resultBitmap;
    }

    private Bitmap transformStreamToBitmap(InputStream inputStream) {
        if (inputStream != null) {
            return BitmapFactory.decodeStream(inputStream);
        }

        return null;
    }
}