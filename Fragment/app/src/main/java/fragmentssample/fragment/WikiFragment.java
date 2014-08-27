package fragmentssample.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class WikiFragment extends SupportMapFragment implements  WordViewInterface{


    private GoogleMap googleMap;
    private String location;
    MarkerOptions markerOptions;
    LatLng latLng;
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

        super.onCreate(savedInstanceState);
        lookupView("Salman Khan");
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

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{


        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(activity);
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text

                Log.e("location from doInbackgroud ********** ",locationName[0]);
                addresses = geocoder.getFromLocationName(locationName[0], 3);
                Log.e("location from","below");
                if(addresses==null || addresses.size()==0) {
                    Log.e("from  doInBackground", "address is null");
                }
                else {
                    Log.e("location from","addresses is not null");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {


            if (addresses == null || addresses.size() == 0) {
                Log.e("from FKFragment ","addresses is null");
                Toast.makeText(activity, "No Location found", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < addresses.size(); i++) {
                Address address = (Address) addresses.get(i);
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
                googleMap.addMarker(markerOptions);
                // Locate the first location
                if (i == 0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}