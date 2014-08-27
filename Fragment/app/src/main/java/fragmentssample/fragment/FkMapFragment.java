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
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class FkMapFragment extends SupportMapFragment {

    private Activity activity;
    private GoogleMap googleMap;
    private String location;
    MarkerOptions markerOptions;
    LatLng latLng;
    private boolean isFirstSelected = false;

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

    public static FkMapFragment newInstance(String location) {

        FkMapFragment f = new FkMapFragment();
        Bundle b = new Bundle();
        b.putString("msg",location);
        f.setArguments(b);
        return f;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        location = getArguments().getString("msg");//savedInstanceState.getString("msg");
        googleMap = getMap();
        location = getArguments().getString("msg");
        if (location != null && !location.equals("")) {
            Log.e("location ", location);
            new GeocoderTask().execute(location);
        }
    }


    public void setUpFocus() {

    }

    public  void onPageSelected(){
        setUpFocus();
        }


    public void onResume() {
        super.onResume();
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