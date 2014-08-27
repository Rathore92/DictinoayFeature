package fragmentssample.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v4.app.Fragment;

public class WikiFragment extends Fragment implements  WordViewInterface{


//    private GoogleMap googleMap;
    private String location;
//    MarkerOptions markerOptions;
//    LatLng latLng;
    private boolean isFirstSelected = false;

    private WordViewInterface viewlistner;
    private Activity activity;
    View containerView;
    LinearLayout layout;
    public WikiFragment(){

    }

    public WikiFragment(Activity activity) {
        this.activity=activity;
        containerView=generateView();
    }

    public View generateView(){
        return null;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity =activity;
    }

    public static WikiFragment newInstance(String location) {

        WikiFragment f = new WikiFragment();
        Bundle b = new Bundle();
        b.putString("msg",location);
        f.setArguments(b);
        return f;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        String word = getArguments().getString("msg");
        super.onCreate(savedInstanceState);
        lookupView(word);
    }


    public void setUpFocus() {

    }

    public  void onPageSelected(){
        setUpFocus();
    }


    public void onResume() {
        super.onResume();
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

    public void lookupView(String word){
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        containerView = inflater.inflate(R.layout.container,null);
        layout=(LinearLayout)containerView.findViewById(R.id.containerlayout);
        WikiUIHelper wikiUIHelper = new WikiUIHelper(this,activity);
        wikiUIHelper.lookupWordAsync(word);
    }

    public void displayView(Object obj, String title){
        Log.e("From WikiFragment Display View", obj.toString());
        layout.addView((View) obj);
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(layout);
        dialog.setTitle(title);
        dialog.show();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wiki, container, false);
//        return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }


}