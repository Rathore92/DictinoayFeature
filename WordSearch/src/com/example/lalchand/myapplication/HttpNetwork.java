package com.example.lalchand.myapplication;


import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lal.chand on 21/07/14.
 */
public class HttpNetwork {

    public HttpClient client;
    String url = "";
    String MobileUrl = "";
    String MultipleResultUrl = "";
    HttpPost post;
    HttpPost post1;

    HttpNetwork(){
        client = new DefaultHttpClient();
        url = "http://en.wikipedia.org/w/api.php?explaintext&redirects&exchars=500";
        MobileUrl = "http://en.wikipedia.org/w/api.php?";
        MultipleResultUrl = "http://en.wikipedia.org/w/api.php?";
    }

    /*
          It creating a URL for api call to get the discription
     */

    public HttpPost GenerateNetworkRequest(String message) throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        post  = new HttpPost(url);
        urlParameters.add(new BasicNameValuePair("format", "json"));
        urlParameters.add(new BasicNameValuePair("action", "query"));
        urlParameters.add(new BasicNameValuePair("prop", "extracts|links"));
        urlParameters.add(new BasicNameValuePair("titles", message));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        return post;
    }
    /*
            It generating a URL for api to get the wiki link
    */

    public HttpPost GenerateMobileNetworkRequest(String pageid) throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        post  = new HttpPost(MobileUrl);
        urlParameters.add(new BasicNameValuePair("action", "query"));
        urlParameters.add(new BasicNameValuePair("format", "json"));
        urlParameters.add(new BasicNameValuePair("prop", "info"));
        urlParameters.add(new BasicNameValuePair("pageids", pageid));
        urlParameters.add(new BasicNameValuePair("inprop", "url"));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
       return  post;

    }


    public HttpPost GenerateMultipleResultNetworkRequest(String title) throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        post  = new HttpPost(MobileUrl);
        urlParameters.add(new BasicNameValuePair("action", "query"));
        urlParameters.add(new BasicNameValuePair("format", "json"));
        urlParameters.add(new BasicNameValuePair("prop", "info"));
        urlParameters.add(new BasicNameValuePair("titles", title));
        urlParameters.add(new BasicNameValuePair("inprop", "url"));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        return  post;

    }

    /*
            Initiate the JSON parsing
     */

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
