package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.android.newsapp.NetworkUtils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    private URL mUrl;
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils utils = new Utils();
        mUrl = utils.getUrl(URLInfo.getBasicURL());
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mEmptyStateView = (LinearLayout) findViewById(R.id.empty_state_view);

        getLoaderManager().initLoader(1,null,this);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        mEmptyStateView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        return new NewsLoader(this, mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mProgressBar.setVisibility(View.GONE);
        mAdapter = new NewsAdapter(getBaseContext(),news);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //For Normal View
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(),DividerItemDecoration.VERTICAL));

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

    }

    static class NewsLoader extends AsyncTaskLoader<List<News>>{

        URL mUrl;

        NewsLoader(Context context, URL url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
            super.onStartLoading();
        }

        @Override
        public List<News> loadInBackground() {
            Utils utils = new Utils();
            String JSONResponse = utils.makeHTTPRequest(mUrl);
            return extractDataFromJSON(JSONResponse);
        }
    }

    /**
     *
     * @param JSONResponse the string json Response from the web
     * @return a list of extracted news items from the json
     */
    private static List<News> extractDataFromJSON(String JSONResponse){
        List<News> list = new LinkedList<>();
        try {
            JSONObject json = new JSONObject(JSONResponse);
            String Status = json.getString("status");
            if(Status.equals("ok")){
                int arraySize = json.getInt("totalResults");
                JSONArray array = json.getJSONArray("articles");
                for(int i = 0;i< arraySize;i++){
                    JSONObject object = array.getJSONObject(i);
                    list.add(new News(object.getString("title")
                            ,object.getString("description")
                            ,object.getString("urlToImage")
                            ,object.getString("url")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
