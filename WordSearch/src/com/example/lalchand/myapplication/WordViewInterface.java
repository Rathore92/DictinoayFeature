package com.example.lalchand.myapplication;

import android.view.View;

import java.util.Objects;

/**
 * Created by lal.chand on 24/07/14.
 */
public interface WordViewInterface {

    public void OnGenerateViewFinished(Object obj, String title,boolean redirect,String Mobileurl);
    public void OnGenerateGoogleViewFinished(Object obj,String word);

}
