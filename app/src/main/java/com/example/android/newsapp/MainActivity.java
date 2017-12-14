package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.newsapp.NetworkUtils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private URL mUrl;
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private LinearLayout mProgressBar;
    private int i = 0;
    private LinearLayout mEmptyStateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.app_name));

        Utils utils = new Utils();
        mUrl = utils.getUrl(URLInfo.getBasicURL());
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressBar = (LinearLayout) findViewById(R.id.progressLayer);
        mEmptyStateView = (LinearLayout) findViewById(R.id.empty_state_view);
        //Start Loading news when activity is created
        StartLoadingData();

        final JellyRefreshLayout mJellyLayout = (JellyRefreshLayout) findViewById(R.id.jelly_layout);
        mJellyLayout.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                StartLoadingData();
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mJellyLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        View loadingView = LayoutInflater.from(this).inflate(R.layout.loading_view, null);
        mJellyLayout.setLoadingView(loadingView);
    }

    private void StartLoadingData(){
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr != null) {
            NetworkInfo nInfo = conMgr.getActiveNetworkInfo();
            if(nInfo != null && nInfo.isConnected()){
                i++;
                getLoaderManager().initLoader(i,null,this);
            } else {
                mEmptyStateView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                Toast.makeText(this,R.string.errorNoInternetConnection,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        mEmptyStateView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        return new NewsLoader(this, mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        if(news != null) {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new NewsAdapter(getBaseContext(), news);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            //For Normal View
            //mRecyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(),DividerItemDecoration.VERTICAL));

            mRecyclerView.setAdapter(mAdapter);
        } else {
            mEmptyStateView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),R.string.errorRetrievingData,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

    }

    static class NewsLoader extends AsyncTaskLoader<List<News>> {

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
     * @param JSONResponse the string json Response from the web
     * @return a list of extracted news items from the json
     */
    private static List<News> extractDataFromJSON(String JSONResponse) {
        if(JSONResponse != null) {
            List<News> list = new LinkedList<>();
            try {
                JSONObject json = new JSONObject(JSONResponse);
                String Status = json.getString("status");
                if (Status.equals("ok")) {
                    int arraySize = json.getInt("totalResults");
                    if(arraySize == 0){
                        return null;
                    }
                    JSONArray array = json.getJSONArray("articles");
                    for (int i = 0; i < arraySize; i++) {
                        JSONObject object = array.getJSONObject(i);
                        list.add(new News(object.getString("title")
                                , object.getString("description")
                                , object.getString("urlToImage")
                                , object.getString("url")));
                        if( i== 20 ){
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refreshMenuButton:
                StartLoadingData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
