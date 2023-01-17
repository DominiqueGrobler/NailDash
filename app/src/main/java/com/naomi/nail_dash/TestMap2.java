package com.naomi.nail_dash;
// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.naomi.nail_dash.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestMap2 extends AppCompatActivity
        implements OnMapReadyCallback, RoutingListener {
    //region Variables and Classes
    String uid;
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;
    TextView DistanceOfPlace;
    TextView TimeOfPlace;
    TextView AddressOfPlace, NameOfPlace;
    String destinationAddress;
    RatingBar ratingBar;
    fetchPlaces fetchData = new fetchPlaces();
    //code attribution
    //this method was taken from Google Developers
    //https://developers.google.com/maps/documentation/navigation/android-sdk/v2/route
    //Google
    //(Google, 2022)

    public static TypeFilter ESTABLISHMENT;
    //code attribution
    //this method was taken from Google Developers
    //https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    //Google
    //(Google, 2022)
    private static final String TAG = com.naomi.nail_dash.TestMap.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private LatLng end, start;
    private EditText search;

    ArrayList<Object> polylines;
    private String distanceString, markerName;
    private String Units, Mode, mapType, type;

    private ImageView MyCenter;

    RegUser user = new RegUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private LatLng latlng;
    private String fave, locName;
    LatLng latitude;

    Editable inputRate;
    //endregion

    int  count = 0;
    float total = 0;

    private ValueEventListener favListener;
    //region On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //code attribution
        //this method was taken from Google Developers
        //https://developers.google.com/maps/documentation/navigation/android-sdk/v2/route
        //Google
        //(Google, 2022)
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            uid = user.getUid();
        }

        // Retrieve the content view that renders the map.
        pullSettings();
        pullMapSettings();
        setContentView(R.layout.activity_maps);

        search = (EditText) findViewById(R.id.input_Search);
        MyCenter = (ImageView) findViewById(R.id.ic_center);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


    }
    //endregion

    //region Get Fav
    public void getFavLocation() {

        double lat = Double.parseDouble(getIntent().getStringExtra("Latitude"));
        double lng = Double.parseDouble(getIntent().getStringExtra("Longitude"));
        String name = getIntent().getStringExtra("Name");
        latitude = new LatLng(lat, lng);

        Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_favloc);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        MarkerOptions options = new MarkerOptions().title(name)
                .position(latitude)
                .icon(markerIcon);
        map.addMarker(options);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude.latitude,
                        latitude.longitude), 16));
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //endregion

    //region Get Settings
    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    public void pullSettings() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference settings = database.getReference("Users/" + uid + "/settings/mapUnits/");

        settings.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot data = task.getResult();
                        Units = data.child("measurement").getValue().toString();

                    }
                    if (!task.getResult().exists()) {
                        Units = "metric";

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: Retrieval Unsuccessful.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        DatabaseReference tsettings = database.getReference("Users/" + uid + "/settings/Transport/");

        tsettings.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot data = task.getResult();
                        Mode = data.child("mode").getValue().toString();

                    }
                    if (!task.getResult().exists()) {
                        Mode = "DRIVING";

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: Retrieval Unsuccessful.", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

//    public void pullModeSettings() {
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//    }

    public void pullMapSettings() {


        DatabaseReference set = database.getReference("Users/" + uid + "/settings/Map/");
        set.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot data = task.getResult();
                        mapType = data.child("type").getValue().toString();

                        if (mapType.equals("terrain")) {

                            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            type = "ter";
                        }
                        if (mapType.equals("satellite")) {
                            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            type = "sat";
                        }
                        if (mapType.equals("hybrid")) {
                            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            type = "hyp";
                        }
                    }
                    if (!task.getResult().exists()) {
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: Retrieval Unsuccessful.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    //endregion

    //region Map Set

    //code attribution
    //this method was taken from Google Developers
    //https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    //Google
    //(Google, 2022)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    //code attribution
    //this method was taken from Google Developers
    //https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    //Google
    //(Google, 2022)
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;


        pullSettings();
//        pullModeSettings();
        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        init();
        if (getIntent().getStringExtra("Latitude") != null) {
            getFavLocation();
        }

    }

    //endregion

    //region Get Location
    // find the devices location
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {

                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {

                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 16));
                                showPlaces();
                                Toast.makeText(TestMap2.this, "Map Ready", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());

                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                        map.getUiSettings().setZoomControlsEnabled(true);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
        //get destination location when user click on map
//        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                map.clear();
//                showPlaces();
//                if (getIntent().getStringExtra("Latitude") != null) {
//                    getFavLocation();
//                }
//                end = latLng;
//
//                start = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
//
//                try {
//                    getJson(start, end);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                Drawable selectDrawable = getResources().getDrawable(R.drawable.ic_selectloc);
//                BitmapDescriptor markerIcon = getMarkerIconFromDrawable(selectDrawable);
//
//
//
//                //Bottom sheetDialog
//                bottomSheetDialog = new BottomSheetDialog(TestMap2.this);
//                bottomSheetView = getLayoutInflater().inflate(R.layout.maps_popup, null);
//
//
//                LatLng sLocation = new LatLng(latLng.latitude,
//                        latLng.longitude);
//                MarkerOptions options = new MarkerOptions().position(sLocation).icon(markerIcon).title("Selected Location");
//                map.addMarker(options);
//
//
//            }
//        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                start = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                end = marker.getPosition();



                locName = marker.getTitle();
                try {
                    getJson(start, end);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                bottomSheetDialog = new BottomSheetDialog(TestMap2.this);
                bottomSheetView = getLayoutInflater().inflate(R.layout.maps_popup, null);

                return false;
            }
        });
    }

    public void directions(View view) {
        FindBestPath(start, end);
        showPlaces();
    }

    public void showPlaces() {
        try {
            StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            stringBuilder.append("location=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude());
            stringBuilder.append("&radius=400000");
            stringBuilder.append("&type=beauty_salon");
            stringBuilder.append("&key=" + getResources().getString(R.string.places_api_key));

            String url = stringBuilder.toString();

            Object dataFetch[] = new Object[2];
            dataFetch[0] = map;
            dataFetch[1] = url;


            fetchData.execute(dataFetch);
        } catch (Exception ex) {
            Log.d("ShowPlaces", "Returning data= " + ex.getMessage().toString());
        }
    }
//endregion

    //region Get Permision
    //code attribution
    //this method was taken from Google Developers
    //https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    //Google
    //(Google, 2022)
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    //code attribution
    //this method was taken from Google Developers
    //https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    //Google
    //(Google, 2022)
    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    //endregion

    //region Find Path
    //code attribution
    //this method was taken from Google Developers
    //https://developers.google.com/maps/documentation/navigation/android-sdk/v2/route
    //Google
    //(Google, 2022)
    // function to find the best route.
    public void FindBestPath(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(TestMap2.this, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {

            switch (Mode) {
                case "driving":
                case "biking": {
                    Routing routing = new Routing.Builder().travelMode(AbstractRouting.TravelMode.DRIVING).withListener(this).alternativeRoutes(true).waypoints(Start, End).key(getString(R.string.google_maps_api_key)).build();
                    routing.execute();
                    break;
                }
                case "walking": {
                    Routing routing = new Routing.Builder().travelMode(AbstractRouting.TravelMode.WALKING).withListener(this).alternativeRoutes(true).waypoints(Start, End).key(getString(R.string.google_maps_api_key)).build();
                    routing.execute();
                    break;
                }
            }


        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(TestMap2.this, "Unable to reach location", Toast.LENGTH_LONG).show();
    }

    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                if(type.equals("sat"))
                {
                    polyOptions.color(getResources().getColor(R.color.colorPolySat));
                }
                else if(type.equals("ter"))
                {
                    polyOptions.color(getResources().getColor(R.color.colorPoly));
                }
                else
                {
                    polyOptions.color(getResources().getColor(R.color.colorPolySat));
                }
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = map.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);


            }

        }
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(TestMap2.this, "Finding Route...", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRoutingCancelled() {
        FindBestPath(start, end);
    }

    private void init() {
        Log.d(TAG, "init: initialising");
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH
                        || i == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //Call search method
                    Search();
                }
                return false;
            }
        });
        MyCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
    }
//endregion

    //region Search
    //code attribution
    //this method was taken from Google Developers
    //https://developers.google.com/maps/documentation/navigation/android-sdk/v2/route
    //Google
    //(Google, 2022)
    private void Search() {
        Log.d(TAG, "Searching....");
        String searchName = search.getText().toString();
        Geocoder gc = new Geocoder(TestMap2.this);
        List<Address> locate = new ArrayList<>();
        try {
            locate = gc.getFromLocationName(searchName, 1);
        } catch (IOException e) {
            Log.d(TAG, "Search Problem: " + e.getMessage());
        }
        if (locate.size() > 0) {
            Address address = locate.get(0);
            Log.d(TAG, "Search Location: " + address.toString());
            Toast.makeText(this, "Location Found: " + address.getLocality(), Toast.LENGTH_LONG).show();

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(address.getLatitude(),
                            address.getLongitude()), DEFAULT_ZOOM));
            end = new LatLng(address.getLatitude(),
                    address.getLongitude());
            MarkerOptions options = new MarkerOptions().position(end).title(address.getLocality());
            map.addMarker(options);

        }
    }

    //endregion

    //region JSON File
    //code attribution
    //this method was taken from Google Developers
    //https://developers.google.com/maps/documentation/distance-matrix/distance-matrix
    //Google
    //(Google, 2022)
    public void getJson(LatLng start, LatLng end) throws IOException {

        Log.d(TAG, "Json Call: " + start.toString() + end.toString());

        //Convert variable start from LatLng to a string
        String startString = start.toString();
        //Remove the brackets from the string
        startString = startString.replace("(", "");
        //Remove lat/lng from the string
        startString = startString.replace("lat/lng: ", "");
        //Remove the bracket and the end of the string
        startString = startString.replace(")", "");
        Log.d(TAG, "Json Call: " + startString);

        //Convert variable end from LatLng to a string
        String endString = end.toString();
        //Remove the brackets from the string
        endString = endString.replace("(", "");
        //Remove lat/lng from the string
        endString = endString.replace("lat/lng: ", "");
        //Remove the bracket and the end of the string
        endString = endString.replace(")", "");
        //Split up the two strings into two separate strings
        String[] startArray = startString.split(",");
        String[] endArray = endString.split(",");

        HttpUrl mySearchUrl = new HttpUrl.Builder().scheme("https").host("maps.googleapis.com").addPathSegment("maps").addPathSegment("api").addPathSegment("distancematrix").addPathSegment("json").addQueryParameter("origins", startArray[0] + "," + startArray[1] + "&destinations=" + endString + "&mode=" + Mode + "&units=" + Units + "&key=" + getString(R.string.google_maps_api_key)).build();
        String afterDecode = URLDecoder.decode(mySearchUrl.toString(), "UTF-8");

        Log.d(TAG, "Json Call: " + mySearchUrl.toString());

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder().url(afterDecode).method("GET", null).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //do failure stuff
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.body().string());

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {

                            Response response = client.newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());// parse response into json object
                            //Pull origin_addresses from JSON
                            JSONArray destinationAddresses = jsonObject.getJSONArray("destination_addresses");
                            //Pull our duration and distance from the json object
                            JSONArray rows = jsonObject.getJSONArray("rows");
                            JSONObject elements = rows.getJSONObject(0);
                            JSONArray elementsArray = elements.getJSONArray("elements");
                            JSONObject distance = elementsArray.getJSONObject(0);
                            JSONObject duration = elementsArray.getJSONObject(0);
                            JSONObject distanceObject = distance.getJSONObject("distance");
                            JSONObject durationObject = duration.getJSONObject("duration");
                            distanceString = distanceObject.getString("text");
                            String durationString = durationObject.getString("text");
                            Log.d(TAG, "Distance: " + distanceString + " Duration " + durationString);
                            //Update UI using method
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //Format origin_addresses
                                    destinationAddress = destinationAddresses.toString();
                                    destinationAddress = destinationAddress.replace("[", "");
                                    destinationAddress = destinationAddress.replace("]", "");
                                    destinationAddress = destinationAddress.replace("\"", "");


                                    AddressOfPlace = bottomSheetView.findViewById(R.id.AddressOfPlace);
                                    AddressOfPlace.setText("Address: " + destinationAddress);


                                    NameOfPlace = bottomSheetView.findViewById(R.id.NameOfPlace);
                                    NameOfPlace.setText(locName);

                                    DistanceOfPlace = bottomSheetView.findViewById(R.id.DistanceOfPlace);
                                    DistanceOfPlace.setText("Distance: " + distanceString);

                                    TimeOfPlace = bottomSheetView.findViewById(R.id.TimeOfArrival);
                                    TimeOfPlace.setText("Time: " + durationString);

                                    ratingBar = bottomSheetView.findViewById(R.id.ratingBar);
                                    CalcAvg();



                                    bottomSheetDialog.setContentView(bottomSheetView);
                                    bottomSheetDialog.show();


                                }
                            });
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
            }
        });
    }

    //endregion

    //region Calculate Average Rating
    public void CalcAvg()
    {
        // connect to the database and select the correct path to the desired data
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rateData = database.getReference().child("Rate/" +
                AddressOfPlace.getText().toString() + "/");

       rateData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot items : snapshot.getChildren()) {
                    if(items.child("rating").exists())
                    {
                        int rating = items.child("rating").getValue(Integer.class);
                        total = total + rating;
                        count = count + 1;

                    }
                    else
                    {
                        Toast.makeText(TestMap2.this, "Rating: 0", Toast.LENGTH_LONG).show();
                    }

                }
                if (total != 0 && count != 0)
                {
                    float calc = total/count;
                    ratingBar.setRating(calc);
                }
                else if(total==0 && count==0)
                {
                    ratingBar.setRating(0);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TestMap2.this, "There was a problem loading the list", Toast.LENGTH_LONG).show();
            }
        });
    }

    //endregion

    //region Load Fav

    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    public void AddFav(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference faveData = database.getReference().child("Users/" + uid +
                "/favourites/" + AddressOfPlace.getText().toString() + "/");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to favorite this Location?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //call methode to add user to the database
                addFavorite(faveData);

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    //endregion

    //region Redirect
    //redirect to the desired page when the button is clicked
    public void goLogin(View view) {
        Intent intent = new Intent(this, TestMap.class);
        startActivity(intent);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    //endregion

    //region add favourite
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    //methode to add user to the database
    public void addFavorite(DatabaseReference faveData) {

        latlng = end;


        // call the constructor in the book class to pass the data through
        favDetails i = new favDetails(latlng, "" + AddressOfPlace.getText().toString(), "" + NameOfPlace.getText().toString());

        // pass the data through the constructor into the database
        faveData.setValue(i);

        Toast.makeText(TestMap2.this, "Added new Favorite Successfully!", Toast.LENGTH_LONG).show();

    }
    //endregion

    //region Rating
    public void Rate(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Type the path to the data you want to work with
        DatabaseReference rate = database.getReference("Rate/");

        String message = "Rate location (0-5):";
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                .setTitle("Rate Location")
                .setMessage(message)
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(input.getText().toString().equals("0") || input.getText().toString().equals("1") || input.getText().toString().equals("2")
                        || input.getText().toString().equals("3") || input.getText().toString().equals("4") || input.getText().toString().equals("5") )
                        {
                            inputRate = input.getText();
                        }
                        else {
                            Toast.makeText(TestMap2.this, "Please enter a number from 0-5", Toast.LENGTH_LONG).show();
                            return;
                        }
                        setRating(rate);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();


    }

    public void setRating(DatabaseReference rate) {
        // call the constructor in the book class to pass the data through
        ratingDetails i = new ratingDetails(Double.parseDouble(inputRate.toString()));

        // pass the data through the constructor into the database
        rate.child(AddressOfPlace.getText().toString()).child(uid).setValue(i);
        Toast.makeText(TestMap2.this, "Edited Settings Successfully!", Toast.LENGTH_LONG).show();
    }

    //endregion
}

