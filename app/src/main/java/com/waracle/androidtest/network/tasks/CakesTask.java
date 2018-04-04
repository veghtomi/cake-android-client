package com.waracle.androidtest.network.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.waracle.androidtest.model.Cake;
import com.waracle.androidtest.network.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CakesTask extends AsyncTask<Void, Void, List<Cake>> {

    private static final String TAG = CakesTask.class.getSimpleName();

    private OnItemsLoadedListener itemsLoadedListener;

    public CakesTask(OnItemsLoadedListener itemsLoadedListener) {
        this.itemsLoadedListener = itemsLoadedListener;
    }

    @Override
    protected List<Cake> doInBackground(Void... voids) {
        final List<Cake> resultCakeList = new ArrayList<>();
        JSONArray jsonArray;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Configuration.JSON_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Can you think of a way to improve the performance of loading data
            // using HTTP headers???

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder json = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json.append(line)
                        .append('\n');
            }

            jsonArray = new JSONArray(json.toString());

            JSONObject actualJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                actualJson = jsonArray.getJSONObject(i);

                final String title = actualJson.getString(Configuration.JSON_FIELD_TITLE_NAME);
                final String description = actualJson.getString(Configuration.JSON_FIELD_DESCRIPTION_NAME);
                final String imageUrl = actualJson.getString(Configuration.JSON_FIELD_IMAGE_URL_NAME);

                resultCakeList.add(new Cake(title, description, imageUrl));
            }

        } catch (IOException | JSONException e) {
            Log.e(TAG, e.getMessage());

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return resultCakeList;
    }

    @Override
    protected void onPostExecute(List<Cake> cakesToAdd) {
        itemsLoadedListener.onItemsLoaded(cakesToAdd);
    }

    public interface OnItemsLoadedListener {
        void onItemsLoaded(List<Cake> newCakes);
    }

    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    private static String parseCharset(String contentType) {
        if (contentType != null) {
            String[] params = contentType.split(",");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }
        return "UTF-8";
    }
}