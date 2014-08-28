package com.example.lalchand.myapplication.GoogleSearch;

import android.util.Log;

/**
 * Created by lal.chand on 27/08/14.
 */
public class GoogleSearch {

    private GoogleSearchInterface listener;

    GoogleSearch(GoogleSearchInterface listener){
        if(listener==null) {
            Log.e("from google search","listener is null");
            throw new NullPointerException();
        }

        this.listener = listener;
    }

    public void startGoogleSearch(String title){
    listener.OnProcessFinished("Google Search description",title);
    }
}
