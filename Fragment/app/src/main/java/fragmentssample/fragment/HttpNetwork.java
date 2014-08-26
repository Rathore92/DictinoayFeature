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
    HttpGet post;


    HttpNetwork(){
        client = new DefaultHttpClient();
        url = "http://maps.google.com/maps/api/geocode/json?address=";
    }

    public HttpGet GenerateNetworkRequest(String message) throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        post  = new HttpGet(url+message);
//        urlParameters.add(new BasicNameValuePair("address", "india"));
//        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        return post;
    }

    public InputStream JsonParse (HttpGet post) throws JSONException, IOException {
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
