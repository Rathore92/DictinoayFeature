package com.example.lalchand.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

//import org.apache.http.impl.client.HttpClientBuilder;
public class DisplayMessageActivity extends Activity {

 //TextView textView = new TextView(this);
 //   private static final String TAG = "MyActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        TextView tv = (TextView) findViewById(R.id.displayMsg);
        Intent intent = getIntent();
        String message = intent.getStringExtra("id");
        tv.setText(message);
        tv.setText(Html.fromHtml(message));
        Log.e("my","hi this ");

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_message, menu);
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
}
