package fragmentssample.fragment;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class GoogleMapFragment extends SupportMapFragment implements GoogleMapsInterface{

    private Activity activity;
    private GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    private boolean isFirstSelected = false;
    LatLng point = new LatLng(0.0, 0.0);
    Results results = new Results();
    Number lat,lng;


    public class GenerateUrl {
        private GoogleMapsInterface listener;

        public GenerateUrl(GoogleMapsInterface listener) {
            this.listener =listener;
        }

        HttpNetwork httpNetwork = new HttpNetwork();

        public void generateGoogleUrl(String location) throws IOException, JSONException {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Results.class, new LocationSearializer());
            Gson gson = gsonBuilder.create();
            InputStream source = httpNetwork.JsonParse(httpNetwork.GenerateNetworkRequest(location));
            if (source == null) {
                Log.e("response from json parsing is : ", source.toString());
                return;
            }

            Reader reader = new InputStreamReader(source);
            results = gson.fromJson(reader, Results.class);
            if (results == null || results.getGeometry().getLocation().isConsistant == false || results.getGeometry().getLocation().isGeomatry == false) {
                Log.e("address_component ", " is null");
                listener.onLocationReceived(null);
            } else {
                lat = results.getGeometry().getLocation().getLat();
                lng =  results.getGeometry().getLocation().getLng();
                point = new LatLng(lat.doubleValue(),lng.doubleValue());
                listener.onLocationReceived(point);
                Log.w("from mapsActivity","" + results.getGeometry().getLocation().getLat());
                Log.w("from mapsActivity","" + results.getGeometry().getLocation().getLng());
                Log.w("from mapsActivity",results.getFormatted_address());
                Log.e("Till here is "," working fine");
            }
        }
    }

    public void startSearchUrl(final GoogleMapsInterface mapsInterface, final String location){
        Runnable rn=new Runnable() {
            @Override
            public void run() {
                GenerateUrl generateUrl = new GenerateUrl(mapsInterface);
                try {
                    generateUrl.generateGoogleUrl(location);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t=new Thread(rn);
        t.start();
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

    public static GoogleMapFragment newInstance(String location) {

        GoogleMapFragment f = new GoogleMapFragment();
        Bundle b = new Bundle();
        b.putString("msg",location);
        f.setArguments(b);
        return f;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String location = getArguments().getString("msg");
        startSearchUrl(this,location);
//        location = getArguments().getString("msg");//savedInstanceState.getString("msg");
//        googleMap = getMap();
//        location = getArguments().getString("msg");
//        if (location != null && !location.equals("")) {
//            Log.e("location ", location);
//            new GeocoderTask().execute(location);
//        }
    }


    public void setUpFocus() {

    }

    public  void onPageSelected(){
//        Toast.makeText(activity.getBaseContext(),"No Location found",Toast.LENGTH_SHORT).show();
        Log.e("GoogleMapFragment ","display now");
    }


    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationReceived(final  LatLng loc) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loc == null) {
                    return;
                } else {
                    try {
                        if (googleMap == null) {
                            googleMap = getMap();
                        }
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        Marker TP = googleMap.addMarker(new MarkerOptions().
                                position(loc).draggable(true));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(loc));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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