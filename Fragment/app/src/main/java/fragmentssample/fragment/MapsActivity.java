package fragmentssample.fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import android.util.Log;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.location.Address;


public class MapsActivity extends Activity implements GoogleMapsInterface {

    private GoogleMap googleMap;
    public String title;
//    Geometry geometry =  new Geometry();
      LatLng point = new LatLng(0.0, 0.0);
    Results results = new Results();
    Number lat,lng;
    LatLng  latLng;

    @Override
    public void onLocationReceived(final  LatLng loc) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loc == null) {
                    return;
                } else {
                    try {
                        if (googleMap == null) {
                            googleMap = ((MapFragment) getFragmentManager().
                                    findFragmentById(R.id.map)).getMap();
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


    public class GenerateUrl {
        private GoogleMapsInterface listener;

        public GenerateUrl(GoogleMapsInterface listener) {
            this.listener =listener;
        }

        HttpNetwork httpNetwork = new HttpNetwork();

        public void generateGoogleUrl() throws IOException, JSONException {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Results.class, new LocationSearializer());
            Gson gson = gsonBuilder.create();
            InputStream source = httpNetwork.JsonParse(httpNetwork.GenerateNetworkRequest("ffsdfsfsdfsfasfsfs"));
            if (source == null) {
                Log.e("response from json parsing is : ", source.toString());
//                exit(0);
            }

            Reader reader = new InputStreamReader(source);
            // parse from Gson google library
            results = gson.fromJson(reader, Results.class);
            if (results == null || results.getGeometry().getLocation().isConsistant == false || results.getGeometry().getLocation().isGeomatry == false) {
                Log.e("address_component ", " is null");
                listener.onLocationReceived(null);
//                Toast.makeText(getBaseContext(),"Location not Found",Toast.LENGTH_SHORT).show();
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
    public void startSearchUrl(final GoogleMapsInterface mapsInterface){
        Runnable rn=new Runnable() {
            @Override
            public void run() {
                GenerateUrl generateUrl = new GenerateUrl(mapsInterface);
                try {
                    generateUrl.generateGoogleUrl();
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

//    public void mapRender(){
//
//        try {
//            if (googleMap == null) {
//                googleMap = ((MapFragment) getFragmentManager().
//                        findFragmentById(R.id.map)).getMap();
//            }
//            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//            Marker TP = googleMap.addMarker(new MarkerOptions().
//                    position(point).draggable(true));
//            googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
        startSearchUrl(this);
        Log.e("point lat", "" + point.latitude);
        Log.e("point lng","" + point.longitude);

    }





}