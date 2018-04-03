package com.waracle.androidtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waracle.androidtest.adapter.CakeAdapter;
import com.waracle.androidtest.model.Cake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fragment is responsible for loading in some JSON and
     * then displaying a list of cakes with images.
     * Fix any crashes
     * Improve any performance issues
     * Use good coding practices to make code more secure
     */
    public static class PlaceholderFragment extends ListFragment {

        private static final String TAG = PlaceholderFragment.class.getSimpleName();

        private ListView mListView;
        private CakeAdapter mAdapter;

        private CakesTask cakesTask;

        public PlaceholderFragment() { /**/ }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mListView = (ListView) rootView.findViewById(android.R.id.list);
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Create and set the list adapter.
            mAdapter = new CakeAdapter();
            mListView.setAdapter(mAdapter);

            // Load data from net.
            cakesTask = new CakesTask(mAdapter);
            cakesTask.execute();
        }

        @Override
        public void onStop() {
            cakesTask.cancel(true);

            super.onStop();
        }

        /**
         * Returns the charset specified in the Content-Type of this header,
         * or the HTTP default (ISO-8859-1) if none can be found.
         */
        public static String parseCharset(String contentType) {
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

        private static class CakesTask extends AsyncTask<Void, Void, List<Cake>> {

            private static String JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
                    "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

            private static final String FIELD_TITLE_NAME = "title";
            private static final String FIELD_DESCRIPTION_NAME = "desc";
            private static final String FIELD_IMAGE_URL_NAME = "image";

            private CakeAdapter adapter;

            public CakesTask(CakeAdapter adapter) {
                this.adapter = adapter;
            }

            @Override
            protected List<Cake> doInBackground(Void... voids) {
                final List<Cake> resultCakeList = new ArrayList<>();
                JSONArray jsonArray;
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    // Can you think of a way to improve the performance of loading data
                    // using HTTP headers???

                    // Also, Do you trust any utils thrown your way????

                    byte[] bytes = StreamUtils.readUnknownFully(in);

                    // Read in charset of HTTP content.
                    String charset = parseCharset(urlConnection.getRequestProperty("Content-Type"));

                    // Convert byte array to appropriate encoded string.
                    // Read string as JSON.
                    jsonArray = new JSONArray(new String(bytes, charset));

                    JSONObject actualJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        actualJson = jsonArray.getJSONObject(i);

                        final String title = actualJson.getString(FIELD_TITLE_NAME);
                        final String description = actualJson.getString(FIELD_DESCRIPTION_NAME);
                        final String imageUrl = actualJson.getString(FIELD_IMAGE_URL_NAME);

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
                adapter.setItems(cakesToAdd);
            }
        }
    }
}
