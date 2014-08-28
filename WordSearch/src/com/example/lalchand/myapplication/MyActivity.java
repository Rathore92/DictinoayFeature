package com.example.lalchand.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.lang.System.exit;
//import javax.swing.*;

//package​ net.learn2develop.Activities;


public class MyActivity extends Activity {
    String Pageid = "" ;
    public String MobileUrl = "";
    Pages PagesResponse = new Pages();
    String message = "";
    boolean Done = false;
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public  class  Generateurl {
        private String title = "title";
        private String from = "from";
        HttpNetwork httpNetwork = new HttpNetwork();
        Generateurl () {
        };
        public void GenerateWikiurl() throws IOException, JSONException {
            GsonBuilder gsonBuilder=new GsonBuilder();
            // Becoz wikientity is dynamic so need Type adaptor
            gsonBuilder.registerTypeAdapter(Pages.class,new PageSerializer());
            Gson gson = gsonBuilder.create();

            //GenerateNetworkRequest deal with all http request and josn parse gives content for inputstream
            InputStream source = httpNetwork.JsonParse(httpNetwork.GenerateNetworkRequest(message));
            if(source == null)
            {
                Log.e("response from json parsing is : ", source.toString());
                exit(0);
            }
            Reader reader = new InputStreamReader(source);
            // parse from Gson google library
            PagesResponse = gson.fromJson(reader, Pages.class);

            if(PagesResponse == null)      Log.e("PageResponse/myActivity","Page Response is null");
            else {
                 if (PagesResponse.getWikiEntity().IsConsistant == false) {
                     PagesResponse.getWikiEntity().setExtract("No definition found");
                     Log.e("from my activity",PagesResponse.getWikiEntity().getExtract().toString());
                     Done = true;
                 }
                else {
                     Log.e("title from my activity",PagesResponse.getWikiEntity().getTitle().toString());
                     Pageid  = PagesResponse.getWikiEntity().getPageid().toString();
                     Log.e("pageid from my activity",Pageid);
                     Log.e("ns from my activity", PagesResponse.getWikiEntity().getNs().toString());
                     Log.e("extract from my activity",PagesResponse.getWikiEntity().getExtract().toString());
                     GenerateMobileUrl();

                 }

            }

        }


        public void GenerateMobileUrl() throws IOException, JSONException {

            GsonBuilder gsonBuilder=new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Pages.class,new MobileSerializer());
            Gson gson = gsonBuilder.create();
            InputStream source = httpNetwork.JsonParse(httpNetwork.GenerateMobileNetworkRequest(Pageid));
            if(source == null){
                Log.e("response from json parsing is : ", source.toString());
                exit(0);
            }
            Reader reader = new InputStreamReader(source);
            Pages PagesResponse = gson.fromJson(reader, Pages.class);
            if(PagesResponse.getWikiEntity().IsConsistant == true) {
                String WebUrl = PagesResponse.getWikiEntity().getFullurl().toString();
                MobileUrl = WebUrl.substring(0, 11) + "m" + "." + WebUrl.substring(11, WebUrl.length());
            }
            else {
                MobileUrl = "";
            }
            Log.e("from mobile url",MobileUrl);
            Done = true;
}

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);   // hiding the activity title
        setContentView(R.layout.activity_my);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void submitClicked(View view)
    {
        Intent intent = new Intent(this,DisplayMessageActivity.class);

        // Get data from edit text
        EditText editText = (EditText) findViewById(R.id.editText);
        message = editText.getText().toString();
        Runnable rn=new Runnable() {
            @Override
            public void run() {
                Generateurl url1 = new Generateurl();
                try {
                    url1.GenerateWikiurl();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t=new Thread(rn);
        t.start();

        while (Done == false);
        if(PagesResponse.getWikiEntity().IsConsistant == true) {
            TextView mobileUrl = (TextView) findViewById(R.id.t2);
            MobileUrl = MobileUrl.substring(1, (MobileUrl.length() - 1));
            mobileUrl.setText(Html.fromHtml("<a href=\"" + MobileUrl + "\">see more >> </a>"));
            mobileUrl.setMovementMethod(LinkMovementMethod.getInstance());
        }
        // display the text on mobile screen
        TextView discrp = (TextView) findViewById(R.id.t3);
        Log.e("from submit buttom",PagesResponse.getWikiEntity().getExtract().toString());
        discrp.setText(PagesResponse.getWikiEntity().getExtract().toString());

        // For passing data to other activity
//        intent.putExtra("id", MobileUrl);
//        Log.e("msg is", MobileUrl);
   //     startActivity(intent);

    }
}



