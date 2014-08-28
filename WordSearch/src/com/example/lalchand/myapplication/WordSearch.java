package com.example.lalchand.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.lalchand.myapplication.GoogleSearch.GoogleUIHelper;

/**
 * Created by lal.chand on 24/07/14.
 */
public class WordSearch implements WordViewInterface {

    private WordViewInterface viewlistner;
    private Activity activity;
    View containerView;
    LinearLayout layout;
    public WordSearch(Activity activity) {
        this.activity=activity;
        containerView=generateView();
    }

    @Override
    public void OnGenerateViewFinished(Object obj,String title,boolean redirect,String Mobileurl){
        //inflate the xml
        if(obj == null)
        {
            Log.e("from wordseach","No defination found");
            // here we can show an alert that not found like something
        }
        else {
            // here you can check and create one web view for that
            if (obj instanceof View) {
                if(redirect == true){
                  //  displayinWebView(obj,Mobileurl);
                  /*
                    Right now this thing is handle by redirection tag so need of function call
                     */
                }
                else
                displayView(obj,title);
            }
        }
    }

    @Override
    public void OnGenerateGoogleViewFinished(Object obj, String title) {

        if(obj == null){
            Log.e("from WordSearch","obj is null for google search");
        }else{
            if (obj instanceof View){
                displayView(obj, title);
            }
        }

    }

    private void displayinWebView(Object obj, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }

    public View generateView(){
        return null;
    }

    public void  lookupView(String word){
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        containerView = inflater.inflate(R.layout.container,null);
        layout=(LinearLayout)containerView.findViewById(R.id.containerlayout);
        WikiUIHelper wikiUIHelper = new WikiUIHelper(this,activity);
        wikiUIHelper.lookupWordAsync(word);
    }

    public void  lookupGoogleView(String searchWord){
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        containerView = inflater.inflate(R.layout.container,null);
        layout=(LinearLayout)containerView.findViewById(R.id.containerlayout);
        GoogleUIHelper googleUIHelper = new GoogleUIHelper(this,activity);
        googleUIHelper.lookupWordAsync(searchWord);
    }

    public void displayView(Object obj, String title){
        Log.e("fasfdffassaf", obj.toString());
        layout.addView((View) obj);
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(layout);
        dialog.setTitle(title);
        dialog.show();
    }

}


