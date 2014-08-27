package fragmentssample.fragment;


import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
    HttpGet get;
    String mobileUrl = "";
    String multipleResultUrl = "";
    String wikiUrl = "";
    HttpPost httpPost;

    HttpNetwork(){
        client = new DefaultHttpClient();
        url = "http://maps.google.com/maps/api/geocode/json?address=";
        wikiUrl = "http://en.wikipedia.org/w/api.php?explaintext&redirects&exchars=500";
        mobileUrl = "http://en.wikipedia.org/w/api.php?";
        multipleResultUrl = "http://en.wikipedia.org/w/api.php?";
    }


    public HttpPost GenerateWikiRequest(String message) throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        httpPost  = new HttpPost(wikiUrl);
        urlParameters.add(new BasicNameValuePair("format", "json"));
        urlParameters.add(new BasicNameValuePair("action", "query"));
        urlParameters.add(new BasicNameValuePair("prop", "extracts|links"));
        urlParameters.add(new BasicNameValuePair("titles", message));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        return httpPost;
    }
    /*
            It generating a URL for api to get the wiki link
    */

    public HttpPost GenerateMobileNetworkRequest(String pageid) throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        httpPost  = new HttpPost(mobileUrl);
        urlParameters.add(new BasicNameValuePair("action", "query"));
        urlParameters.add(new BasicNameValuePair("format", "json"));
        urlParameters.add(new BasicNameValuePair("prop", "info"));
        urlParameters.add(new BasicNameValuePair("pageids", pageid));
        urlParameters.add(new BasicNameValuePair("inprop", "url"));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        return  httpPost;

    }


    public HttpPost GenerateMultipleResultNetworkRequest(String title) throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        httpPost  = new HttpPost(multipleResultUrl);
        urlParameters.add(new BasicNameValuePair("action", "query"));
        urlParameters.add(new BasicNameValuePair("format", "json"));
        urlParameters.add(new BasicNameValuePair("prop", "info"));
        urlParameters.add(new BasicNameValuePair("titles", title));
        urlParameters.add(new BasicNameValuePair("inprop", "url"));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        return  httpPost;

    }

    public HttpGet GenerateNetworkRequest(String message) throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        get  = new HttpGet(url+message);
        return get;
    }


    public InputStream JsonParseGet (HttpPost post) throws JSONException, IOException {
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

    public InputStream JsonParse (HttpGet post) throws JSONException, IOException {

        HttpResponse response = client.execute(post);
        try {
            return response.getEntity().getContent();
        }catch (IOException e){
            Log.e("From catch json parsing","from catch");
            return null;
        }
    }

}
