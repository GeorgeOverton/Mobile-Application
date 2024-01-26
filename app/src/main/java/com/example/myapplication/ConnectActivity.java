package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

//import net.runelite.client.plugins.timetracking.clocks


//import java.net.runelite.client.plugins.timetracking.clocks.ClockManager;

//page to connect to the users runelite account (Not implemented Yet)
public class ConnectActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;

    //Creates the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        //sets the fields in the activity
        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
    }

    //method makes a Query to the runelite api to retrieve the users data
    private void makeGithubSearchQuery() {
        String nameQuery = mSearchBoxEditText.getText().toString();
        URL timerSearchUrl = NetworkUtils.buildUrl(nameQuery);
        mUrlDisplayTextView.setText(timerSearchUrl.toString());
        // Create a new RunescapeQueryTask and call its execute method, passing in the url to query
        new RunescapeQueryTask().execute(timerSearchUrl);
    }

    //class called RunescapeQueryTask that extends AsyncTask<URL, Void, String>
    public class RunescapeQueryTask extends AsyncTask<URL, Void, String> {

        //runs the AsyncTask/ creates the thread
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String runeliteSearchResults = null;
            //sends a request to retrieve data from the URL
            try {
                System.out.println("network request sent");
                runeliteSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return runeliteSearchResults;
        }

        //Displays the result in a text view
        @Override
        protected void onPostExecute(String runeliteSearchResults) {
            if (runeliteSearchResults != null && !runeliteSearchResults.equals("")) {
                mSearchResultsTextView.setText(runeliteSearchResults);
            }
        }
    }

    // cretes the options menu for this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //check to see if the item is clicked and perform the search on the API
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}