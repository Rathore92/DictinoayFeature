package fragmentssample.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lal.chand on 24/07/14.
 */
public class WordSearch implements  WordViewInterface{

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

    private void displayinWebView(Object obj, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }

    public View generateView(){
        return null;
    }

    public void lookupView(String word){
        word = "Salman Khan";
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        containerView = inflater.inflate(R.layout.container,null);
        layout=(LinearLayout)containerView.findViewById(R.id.containerlayout);
        WikiUIHelper wikiUIHelper = new WikiUIHelper(this,activity);
        wikiUIHelper.lookupWordAsync(word);
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


