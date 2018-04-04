package com.example.android.newsapp;

import android.view.View;

/**
 * Created by Kundan.
 * Time : 23:30
 * Date : 22-12-2017
 */

public interface NewsItemClickListener {
    public void onItemClick(View v, int position, NewsAdapter.ViewHolder holder, News newsItem);
}
