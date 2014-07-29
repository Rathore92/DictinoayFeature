package com.example.lalchand.myapplication;


import android.util.Log;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lal.chand on 21/07/14.
 */
public class HttpNetwork {

    public HttpClient client;
    String url = "";
    String MobileUrl = "";
    HttpPost post;
    HttpPost post1;

    HttpNetwork(){
        client = new DefaultHttpClient();
        url = "http://en.wikipedia.org/w/api.php?explaintext&exintro";
        MobileUrl = "http://en.wikipedia.org/w/api.php?";

    }

    //MyActivity myActivity = new MyActivity();

    public HttpPost GenerateNetworkRequest(String message) throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        post  = new HttpPost(url);
        urlParameters.add(new BasicNameValuePair("format", "json"));
        urlParameters.add(new BasicNameValuePair("action", "query"));
        urlParameters.add(new BasicNameValuePair("prop", "extracts"));
        urlParameters.add(new BasicNameValuePair("titles", message));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        return post;
    }

    public HttpPost GenerateMobileNetworkRequest() throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        post  = new HttpPost(MobileUrl);
        urlParameters.add(new BasicNameValuePair("action", "query"));
        urlParameters.add(new BasicNameValuePair("format", "json"));
        urlParameters.add(new BasicNameValuePair("prop", "info"));
        urlParameters.add(new BasicNameValuePair("pageids", "302167"));
        urlParameters.add(new BasicNameValuePair("inprop", "url"));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
       return  post;

    }



    public InputStream JsonParse (HttpPost post) throws JSONException, IOException {
// Parsing using Gson
        HttpResponse response = client.execute(post);
        try {
            return response.getEntity().getContent();
        }
        catch (IOException e){
            Log.e("From catch json parsing","from catch");
            return null;
        }
    }


}
