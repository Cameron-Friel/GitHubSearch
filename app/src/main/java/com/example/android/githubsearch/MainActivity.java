package com.example.android.githubsearch;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;
    private GitHubSearchAdapter mGitHubSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mGitHubSearchAdapter = new GitHubSearchAdapter();
        mSearchResultsRV.setAdapter(mGitHubSearchAdapter);

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    doGitHubSearch(searchQuery);
                }
            }
        });
    }

    private void doGitHubSearch(String searchQuery) {
        URL githubSearchURL = GitHubUtils.buildGitHubSearchURL(searchQuery);
        Log.d(TAG, "querying search URL: " + githubSearchURL);
        new GitHubSearchTask().execute(githubSearchURL);
    }

    public class GitHubSearchTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL githubSearchURL = urls[0];

            String searchResults = null;
            try {
                searchResults = NetworkUtils.doHTTPGet(githubSearchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                ArrayList<String> searchResultsList = new ArrayList<>();
                searchResultsList.add(s);
                mGitHubSearchAdapter.updateSearchResults(searchResultsList);
                mSearchBoxET.setText("");
            }
        }
    }
}
