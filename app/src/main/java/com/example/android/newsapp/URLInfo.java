package com.example.android.newsapp;

/**
 * Created by Kundan.
 * Time : 01:15
 * Date : 14-12-2017
 */

public class URLInfo {
    private static String baseURL ="https://newsapi.org/v2/everything?";
    private static String keyIs = "q=";
    private static String baseTopHeadlinesURL = "https://newsapi.org/v2/top-headlines?";
    private static String sources = "sources=", sortBy = "sortBy=" , sort1 = "published";
    private static String source1 = "the-hindu", source2 = "bbc-news", source3 = "";
    private static String apiKey = "apiKey=7936f65f411143f4ad417bd1b84d9afd";
    static String getBasicURL(){
        return baseTopHeadlinesURL + sources + source1 + "," + source2
                + "&" + sortBy + sort1
                + "&" + apiKey;

    }
    public static String getKeyNews(String key){
        return baseURL + keyIs + key
                + "&" + sortBy + sort1
                + "&" + apiKey;
    }

}
