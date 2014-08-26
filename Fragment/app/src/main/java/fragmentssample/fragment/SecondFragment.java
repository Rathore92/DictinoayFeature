package fragmentssample.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SecondFragment extends Fragment {

    private EditText editText;
    private WebView webView;
    String url;
    private Button button;
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second, container, false);
       // TextView tv = (TextView) v.findViewById(R.id.tvFragSecond);
        Log.e("from on create view start"," second fragment");
        editText = (EditText) v.findViewById(R.id.editText);
        webView = (WebView) v.findViewById(R.id.webview);
        webView.setWebViewClient(new MyBrowser());
        Log.e("from on create view start"," second fragment");
        button = (Button) v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String query = editText.getText().toString();
                    url  = "https://www.google.co.in/?gfe_rd=cr&ei=8Tf0U6vBOI3M8gfK4YGwBA#q=";
                    url = url + query ;
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webView.loadUrl(url);
                } catch (Exception e) {
                    Log.e("from second fragment ","Something happened with button click");
                    // TODO: handle exception
                }
            }
        });
        return v;
    }

    public static SecondFragment newInstance(String text) {
        SecondFragment f = new SecondFragment();
        Bundle b = new Bundle();
        b.putString("msg",text);
        f.setArguments(b);
        return f;
    }
}