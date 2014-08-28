package com.example.lalchand.myapplication.GoogleSearch;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lalchand.myapplication.R;
import com.example.lalchand.myapplication.WikiParser;
import com.example.lalchand.myapplication.WordViewInterface;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lal.chand on 27/08/14.
 */
public class GoogleUIHelper implements GoogleSearchInterface{

    private WordViewInterface viewlistener;
    private Activity activity;
    private EditText editText;
    private WebView webView;
    String url;
    private Button button;

    public GoogleUIHelper(WordViewInterface viewlistener,Activity activity){
        if (viewlistener == null){
            Log.e("From GoogleUIHelper","viewlistener is null");
            throw new NullPointerException();
        }

        this.viewlistener = viewlistener;
        this.activity = activity;
    }


    @Override
    public void OnProcessFinished(final String description, final String title) {
        if (description == null ) {
            viewlistener.OnGenerateGoogleViewFinished(null, null);
        }else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        updateUI(description,title);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private View generateView(String description) {
        return null;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void lookupWordAsync(String searchWord)
    {
        GoogleSearch googleSearch = new GoogleSearch(this);
        googleSearch.startGoogleSearch(searchWord);
    }


    public void setSearchResult(View v,String title) {
        try {
            String query = title;
            url  = "https://www.google.co.in/?gfe_rd=cr&ei=8Tf0U6vBOI3M8gfK4YGwBA#q=";
            url = url + query ;
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(url);
        } catch (Exception e) {
            Log.e("from second fragment ", "Something happened with button click");
            // TODO: handle exception
        }
    }

    private void updateUI(String description, final String title ) throws IOException {
        View v = generateView(description);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_second, null);
        Log.e("from on create view start", " second fragment");
//        editText = (EditText) v.findViewById(R.id.editText);
        webView = (WebView) v.findViewById(R.id.webview);
        webView.setWebViewClient(new MyBrowser());
        Log.e("from on create view start", " second fragment");
        setSearchResult(v,title);
//        button = (Button) v.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//        });
        viewlistener.OnGenerateGoogleViewFinished(v, title);
    }
}
