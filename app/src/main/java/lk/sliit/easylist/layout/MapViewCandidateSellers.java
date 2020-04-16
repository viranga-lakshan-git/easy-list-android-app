package lk.sliit.easylist.layout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import lk.sliit.easylist.R;
import lk.sliit.easylist.module.Order;

public class MapViewCandidateSellers extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener {   //, GeoDataClient.OnConnectionFailedListener {
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    List<String> list = new ArrayList<>();
    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private  static  final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168), new LatLng(71,136));

    //widgets
    private ImageView mGps;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public double lat,lng;
    String shopName;
    private  GoogleApiClient mGoogleApiClient;
    private GeoDataClient mGeoDataClient;
    Location currentLocation;
    ArrayList<Order> orderArrayList;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission
                    (Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view_candidate_sellers);
        mGps = findViewById(R.id.ic_gps);

        Bundle extras = getIntent().getExtras();
        lat= extras.getDouble("LAT");
        lng= extras.getDouble("LNG");
        shopName = extras.getString("SHOP_NAME");
        orderArrayList = this.getIntent().getExtras().getParcelableArrayList("CANDIDATE_ORDERS");

        getLocationPermission();
    }

    private void init() {
        Log.d(TAG, "init: Initializing");

        GoogleApiClient mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        LatLng latLng = new LatLng(lat,lng);                                                            //add a marker to the selected seller's location
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Shop :"+shopName);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Click gps icon");
                getDeviceLocation();
            }
        });
    }

    private void getDeviceLocation() {                                                              //get device current location
        Log.d(TAG, "getDeviceLocation: Getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Found location");
                        currentLocation = (Location) task.getResult();

                        lat=currentLocation.getLatitude();                                          //take device current location(latitude)
                        lng=currentLocation.getLongitude();                                         //take device current location(longitude)

                        moveCamera(new LatLng(currentLocation.getLatitude(),
                                currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");

                    } else {
                        Log.d(TAG, "onComplete: Current location is null");
                        Toast.makeText(MapViewCandidateSellers.this, "Unable to get current location", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {                              //search a new location

        Log.d(TAG, "moveCamera: Moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(markerOptions);                                                          //put a marker to new location

            lat=markerOptions.getPosition().latitude;                                               //getting the searched location(latitude)
            lng=markerOptions.getPosition().longitude;                                              //getting the searched location(longitude)
        }

        //hideSoftKeyboard();
    }


    private void initMap() {
        Log.d(TAG, "initMap: Initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapViewCandidateSellers.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: Getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Called");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    //ArrayList<String>locationData=new ArrayList<>();
    public void onclick(View view) {                                                                //show all grocery locations
        mMap.clear();
        for(Order o : orderArrayList){
            double lat=o.getSeller().getLat();
            double lng=o.getSeller().getLng();
            LatLng latLng = new LatLng(lat,lng);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Shop: "+o.getSeller().getShopName());
            mMap.addMarker(markerOptions);
        }
    }

    public void onclickNearestGrocery(View view) {

        mMap.clear();
        double R=6371;
        double[] distances=new double[orderArrayList.size()];
        double a,c,d;

        getDeviceLocation();
        double lat=this.lat,lng=this.lng;

        int l=0;
        int closest=-1;

        for (Order o : orderArrayList){
            double tlat=o.getSeller().getLat();
            double tlng=o.getSeller().getLng();

            double dlat=rad(tlat-lat);
            double dlng=rad(tlng-lng);

            a = Math.sin(dlat/2) * Math.sin(dlat/2) + Math.cos(rad(lat)) * Math.cos(rad(lat)) * Math.sin(dlng/2) * Math.sin(dlng/2);
            c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            d = R * c;

            distances[l] = d;

            if ( closest == -1 || d < distances[closest] ) {
                closest = l;
            }
            l++;
        }

        Order o = orderArrayList.get(closest);
        double tlat=o.getSeller().getLat();
        double tlng=o.getSeller().getLng();
        LatLng tlatLng = new LatLng(tlat,tlng);

        MarkerOptions markerOptions = new MarkerOptions().position(tlatLng).title("Shop: "+o.getSeller().getShopName());
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tlatLng, DEFAULT_ZOOM));
    }

    public double rad(double x){
        return x*Math.PI/180;
    }
}

