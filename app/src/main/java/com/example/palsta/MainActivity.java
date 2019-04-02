package com.example.palsta;

import android.animation.LayoutTransition;
import android.app.ActionBar;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static com.example.palsta.R.id.gone;
import static com.example.palsta.R.id.invisible;
import static com.example.palsta.R.id.productImage;
import static com.example.palsta.R.id.productNameText;
import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.division;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

public class MainActivity extends AppCompatActivity {


    private MapView mapView;
    private MapboxMap mapboxMap;

    ArrayList<AdPart> AdParts = new ArrayList<>();
    double latitude;
    double longitude;

    ListView listView = null;

    public static final String EXTRA_MESSAGE ="com.example.palsta.MESSAGE";

    static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, "pk.eyJ1Ijoic2FtdWxpcm9ua2tvIiwiYSI6ImNqdHF4Z2ViczBpZmI0ZGxsdDF1eHczZzgifQ.wBTnY_6-AdYQKk7dYqFDlQ");
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap map) {

                mapboxMap = map;

                map.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                12.099, -79.045), 3));

                        addClusteredGeoJsonSource(style);
                        style.addImage(
                                "cross-icon-id",
                                BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.baseline_euro_symbol_black_18dp)),
                                true
                        );
                    }
                });
            }
        });






        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            getLocation();

        } else {
            ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSION_ACCESS_FINE_LOCATION );
        }

        listView = findViewById(R.id.adList);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

        final CollectionReference ad = db.collection("ad");
        //ad.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
        db.collection("ad")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String address = document.get("address").toString();
                        String description = document.get("description").toString();
                        float price = document.getLong("price").floatValue();
                        String pricedescription = document.get("pricedescription").toString();
                        String product = document.get("product").toString();
                        //String location = document.get("location").toString();
                        Log.d("asdf", product);
                        Log.d("asdf", address);
                        Log.d("asdf", String.valueOf(price));
                        Log.d("asdf", pricedescription);
                        Log.d("asdf", description);

                        AdPart part = new AdPart(product, address, price, pricedescription, description);
                        AdParts.add(part);

                        //TextView productTextView = findViewById(productNameText);
                        //productTextView.setText(product);

                        Log.d("asdf", document.getId() + " => " + document.getData());

                    }
                    QuerySnapshot doc = task.getResult();
                    //StringBuilder fields = new StringBuilder("");

                    /*
                    add new table

                    Map<String, Object> data1 = new HashMap<>();
                    data1.put("product", "Kurkku");
                    data1.put("address", "Jousimiehentie 2");
                    data1.put("price", 2);
                    data1.put("pricedescription", "kg");
                    data1.put("description", "kasvihuoneessa kasvatettuja kurkkuja");
                    ad.document("Ilmoitus5").set(data1);

                    */

                    //TextView productName = findViewById(R.id.productNameText);
                    //TextView addressField = findViewById(R.id.locationText);


/*
                    productName.append(product);
                    addressField.append(address);
/*
                    Log.d("asdf", product);
                    Log.d("asdf", address);
                    Log.d("asdf", String.valueOf(price));
                    Log.d("asdf", pricedescription);
                    Log.d("asdf", description);
                    Log.d("asdf", location);
*/
                    //fields.append(product + "\n");
                    //fields.append(price).append("€/").append(pricedescription + "\n");
                    //fields.append(address);

                    //sijainti
                    //TextView price = findViewById(R.id.priceText);

                    //productName.setText(fields.toString());
                    //price.setText(fields.toString());


                    AdAdapter adapter = new AdAdapter(MainActivity.this, AdParts);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                long arg3) {
                            Log.d("gona", arg1.toString());
                            if(arg1.findViewById(R.id.descriptionText).getVisibility() == View.GONE){
                                arg1.findViewById(R.id.descriptionText).setVisibility(View.VISIBLE);
                                /*ImageView imageView = (ImageView)arg1.findViewById(R.id.productImage);
                                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams)imageView.getLayoutParams();
                                params.width = -1;
                                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)imageView.getLayoutParams();
                                marginLayoutParams.leftMargin = 0;
                                marginLayoutParams.topMargin = 0;
                                marginLayoutParams.bottomMargin = 0;
                                ConstraintLayout.LayoutParams constraintLayout = (ConstraintLayout.LayoutParams) findViewById(R.id.con2).getLayoutParams();
                                constraintLayout.topToBottom = R.id.productImage;
                                arg1.findViewById(R.id.con2).requestLayout();*/
                            }else{
                                arg1.findViewById(R.id.descriptionText).setVisibility(View.GONE);
                            }
                            //Log.d("asdf","Items " +  AdParts.get(arg2).getStr() );
                            //Intent intent = new Intent(getBaseContext(), SingleAd.class);
                            //intent.putExtra(EXTRA_MESSAGE, AdParts.get(arg2));
                            //startActivity(intent);
                        }

                    });
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

/*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        FirebaseStorage storage = FirebaseStorage.getInstance();

        db = FirebaseApp.getInstance();

        DatabaseReference = FirebaseDatabase.getInstance().getReference().child("singleMessage").push();
*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission Granted
                //Do your work here
                //Perform operations here only which requires permission
                getLocation();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.newAd){
            Intent intent = new Intent(this, NewAdActivity.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("EXTRA_LONGITUDE", longitude);
            bundle.putDouble("EXTRA_LATITUDE", latitude);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.yourAds){
            Intent intent = new Intent(this, YourAds.class);
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void getLocation() {

        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Log.d("asdf", String.valueOf(longitude));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_FINE);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_FINE);

        final Looper looper = null;

        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, looper);

    }


    private void addClusteredGeoJsonSource(@NonNull Style loadedMapStyle) {

// Add a new source from the GeoJSON data and set the 'cluster' option to true.
        try {
            loadedMapStyle.addSource(
// Point to GeoJSON data. This example visualizes all M1.0+ earthquakes from
// 12/22/15 to 1/21/16 as logged by USGS' Earthquake hazards program.
                    new GeoJsonSource("earthquakes",
                            new URL("https://www.mapbox.com/mapbox-gl-js/assets/earthquakes.geojson"),
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(14)
                                    .withClusterRadius(50)
                    )
            );
        } catch (MalformedURLException malformedUrlException) {
            Timber.e("Check the URL %s", malformedUrlException.getMessage());
        }

        // Use the earthquakes GeoJSON source to create three layers: One layer for each cluster category.
// Each point range gets a different fill color.
        int[][] layers = new int[][] {
                new int[] {150, ContextCompat.getColor(this, R.color.mapbox_plugins_white)},
                new int[] {20, ContextCompat.getColor(this, R.color.mapbox_plugins_green)},
                new int[] {0, ContextCompat.getColor(this, R.color.mapbox_blue)}
        };

//Creating a marker layer for single data points
        SymbolLayer unclustered = new SymbolLayer("unclustered-points", "earthquakes");

        unclustered.setProperties(
                iconImage("cross-icon-id"),
                iconSize(
                        division(
                                get("mag"), literal(4.0f)
                        )
                ),
                iconColor(
                        interpolate(exponential(1), get("mag"),
                                stop(2.0, rgb(0, 255, 0)),
                                stop(4.5, rgb(0, 0, 255)),
                                stop(7.0, rgb(255, 0, 0))
                        )
                )
        );
        loadedMapStyle.addLayer(unclustered);

        for (int i = 0; i < layers.length; i++) {
//Add clusters' circles
            CircleLayer circles = new CircleLayer("cluster-" + i, "earthquakes");
            circles.setProperties(
                    circleColor(layers[i][1]),
                    circleRadius(18f)
            );

            Expression pointCount = toNumber(get("point_count"));

// Add a filter to the cluster layer that hides the circles based on "point_count"
            circles.setFilter(
                    i == 0
                            ? all(has("point_count"),
                            gte(pointCount, literal(layers[i][0]))
                    ) : all(has("point_count"),
                            gt(pointCount, literal(layers[i][0])),
                            lt(pointCount, literal(layers[i - 1][0]))
                    )
            );
            loadedMapStyle.addLayer(circles);
        }

//Add the count labels
        SymbolLayer count = new SymbolLayer("count", "earthquakes");
        count.setProperties(
                textField(Expression.toString(get("point_count"))),
                textSize(12f),
                textColor(Color.WHITE),
                textIgnorePlacement(true),
                textAllowOverlap(true)
        );
        loadedMapStyle.addLayer(count);

    }



}
