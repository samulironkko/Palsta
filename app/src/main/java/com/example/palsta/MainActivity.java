package com.example.palsta;

import android.Manifest;
import android.content.Context;
import android.animation.LayoutTransition;
import android.app.ActionBar;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.graphics.PointF;
import android.media.Image;
import android.os.Build;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.GeoJSONObject;
import com.cocoahero.android.geojson.Point;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.ncorti.slidetoact.SlideToActView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import timber.log.Timber;

import static com.example.palsta.R.id.gone;
import static com.example.palsta.R.id.invisible;
import static com.example.palsta.R.id.productImage;
import timber.log.Timber;

import static com.example.palsta.R.id.productNameText;
import static com.example.palsta.R.id.single_ad_bottom_sheet;
import static com.example.palsta.R.id.toolbar;
import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.division;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.geometryType;
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
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class MainActivity extends AppCompatActivity {


    private MapView mapView;
    private MapboxMap mapboxMap;
    JSONObject geoJSON;
    JSONObject singleGeoJSON;
    Feature feature;
    FeatureCollection featureCollection;
    Point point;
    GeoPoint geoPoint;
    double geoLatitude;
    double geoLongitude;
    Ad current;
    LatLng previousLatLng;
    int disableListUpdate;

    List<SymbolLayer> addedLayers;
    ArrayList<Ad> AdParts = new ArrayList<>();
    ArrayList<Ad> tempAdParts = new ArrayList<>();
    ListIterator<Ad> iterator;
    double latitude;
    double longitude;

    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetBehavior singleBottomSheetBehavior;


    ListView listView = null;

    public static final String EXTRA_MESSAGE = "com.example.palsta.MESSAGE";

    static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 100;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("firstTime", true)){
            String uniqueID = UUID.randomUUID().toString();
            Log.d("1234", uniqueID);
            sharedPreferences.edit().putString("UUID", uniqueID).apply();
            sharedPreferences.edit().putBoolean("firstTime", false).apply();
        }else{
            //sharedPreferences.edit().putString("UUID", "5bfbd2ef-6c61-4511-bdf5-5337e5ace31d").apply();
            Log.d("1234", sharedPreferences.getString("UUID", null));
        }

        //if crashes add following line
        FirebaseApp.initializeApp(this);

        Mapbox.getInstance(this, "pk.eyJ1Ijoic2FtdWxpcm9ua2tvIiwiYSI6ImNqdHF4Z2ViczBpZmI0ZGxsdDF1eHczZzgifQ.wBTnY_6-AdYQKk7dYqFDlQ");
        setContentView(R.layout.activity_main);



        CardView llBottomSheet = (CardView) findViewById(R.id.bottom_sheet);
        LinearLayout conBottomSheet = (LinearLayout) findViewById(R.id.single_ad_bottom_sheet);

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        singleBottomSheetBehavior = BottomSheetBehavior.from(conBottomSheet);

        // When listBottomSheets state changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING && !listIsAtTop()) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    listView.setEnabled(false);
                }else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    listView.setEnabled(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        //When singleBottomSheets state changes
        singleBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(previousLatLng, 10));
                    disableListUpdate = 0;
                    bottomSheetBehavior.setHideable(false);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    mapboxMap.getStyle().getLayer("unclustered-points").setProperties(visibility(Property.VISIBLE));
                    mapboxMap.getStyle().getLayer("cluster-0").setProperties(visibility(Property.VISIBLE));
                    mapboxMap.getStyle().getLayer("count").setProperties(visibility(Property.VISIBLE));
                    mapboxMap.getStyle().removeLayer("single-point");
                    mapboxMap.getStyle().removeSource("single_ad");
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);


        listView = findViewById(R.id.adList);
        listView.setEnabled(false);

        //When user clicks litItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Log.d("gona", arg1.toString());
                disableListUpdate = 1;

                bottomSheetBehavior.setHideable(true);

                point = new Point(tempAdParts.get(arg2).getLatLng().getLatitude(), tempAdParts.get(arg2).getLatLng().getLongitude());
                feature = new Feature(point);
               // feature.setIdentifier(id);
                feature.setProperties(new JSONObject());

                try {
                    singleGeoJSON = feature.toJSON();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                addSingleGeoJsonSource(mapboxMap.getStyle());

                //name of product
                TextView name = findViewById(R.id.bottom_productNameText);
                name.setText(tempAdParts.get(arg2).getProduct());

                //price
                TextView price = findViewById(R.id.bottom_priceText);
                price.setText(tempAdParts.get(arg2).getPrice() + "€/" + tempAdParts.get(arg2).getPricedescription());

                //address
              //  TextView address = findViewById(R.id.bottom_locationText);
              //  address.setText(tempAdParts.get(arg2).getAddress());

                //description
                TextView description = findViewById(R.id.bottom_descriptionText);
                description.setText(tempAdParts.get(arg2).getDescription());

                //distance
                TextView distance = findViewById(R.id.bottom_distanceText);
                distance.setText(String.format("%.1f", tempAdParts.get(arg2).getDistance()/1000) + "km");

                //Image
                ImageView image = findViewById(R.id.bottom_productImage);
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/palsta-b6497.appspot.com/o/puhtaasti_tomaatti.jpg?alt=media&token=8d141497-ae6a-4ce4-80e9-9a4b1d3e94c6")
                        .transform(new MaskTransformation(MainActivity.this, R.drawable.list_shape)).into(image);

                //SlideToPay
                SlideToActView slideToActView = (SlideToActView) findViewById(R.id.buy_slider);
                slideToActView.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                    @Override
                    public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                        singleBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        slideToActView.resetSlider();
                    }
                });

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                singleBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                previousLatLng = mapboxMap.getCameraPosition().target;
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(tempAdParts.get(arg2).getLatLng().getLatitude() - 0.01, tempAdParts.get(arg2).getLatLng().getLongitude()), 12));
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        if (item.getItemId() == R.id.newAd) {
            Intent intent = new Intent(this, NewAdActivity.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("EXTRA_LONGITUDE", longitude);
            bundle.putDouble("EXTRA_LATITUDE", latitude);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.yourAds) {
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
        updateFromDb();
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

    @Override
    public void onBackPressed() {
        if (singleBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            singleBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED && !listIsAtTop()) {
            listView.smoothScrollToPosition(0);
            return;
        }else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED && listIsAtTop()) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }

        super.onBackPressed();
    }

    public void getLocation() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateGPSCoordinates(location);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);

            return;
        }
        try {
            updateGPSCoordinates(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }catch (Exception e) {
            e.printStackTrace();
        }

        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, looper);

    }

    private void buildMap() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap map) {
                mapboxMap = map;
                mapboxMap.getUiSettings().setTiltGesturesEnabled(false);
                map.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                12.099, -79.045), 3));
                        if (mapboxMap.getStyle().getSource("ads") == null) {
                            addClusteredGeoJsonSource(style);
                        }
                        style.addImage(
                                "cross-icon-id",
                                BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.icon_palsta)),
                                false
                        );
                        getLocation();
                        mapboxMap.addOnMoveListener(new MapboxMap.OnMoveListener() {
                            @Override
                            public void onMoveBegin(@NonNull MoveGestureDetector detector) {

                            }

                            @Override
                            public void onMove(@NonNull MoveGestureDetector detector) {

                            }

                            @Override
                            public void onMoveEnd(@NonNull MoveGestureDetector detector) {
                                updateTempList();
                            }
                        });
                    }
                });
            }
        });
    }


    private void addClusteredGeoJsonSource(@NonNull Style loadedMapStyle) {

        // Add a new source from the GeoJSON data and set the 'cluster' option to true.


        loadedMapStyle.addSource(
                new GeoJsonSource("ads",
                        geoJSON.toString(),
                        new GeoJsonOptions()
                                .withCluster(true)
                                .withClusterMaxZoom(14)
                                .withClusterRadius(40)
                )
        );

        // Each point range gets a different fill color.
        int[][] layers = new int[][] {
                new int[] {0, ContextCompat.getColor(this, R.color.colorPrimary)}
        };

        //Creating a marker layer for single data points
        SymbolLayer unclustered = new SymbolLayer("unclustered-points", "ads");

        unclustered.setProperties(
                iconImage("cross-icon-id"),
                iconSize(
                        division(
                                get("mag"), literal(1.0f)
                        )
                )
        );
       // addedLayers.add(unclustered);
        loadedMapStyle.addLayer(unclustered);


        for (int i = 0; i < 1; i++) {
            //Add clusters' circles
            CircleLayer circles = new CircleLayer("cluster-" + i, "ads");
            circles.setProperties(
                    circleColor(layers[i][1]),
                    circleRadius(23f)
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
           // addedLayers.add(circles);
        }

        //Add the count labels
        SymbolLayer count = new SymbolLayer("count", "ads");
        count.setProperties(
                textField(Expression.toString(get("point_count"))),
                textSize(12f),
                textColor(Color.WHITE),
                textIgnorePlacement(true),
                textAllowOverlap(true)
        );
        loadedMapStyle.addLayer(count);

    }

    private void addSingleGeoJsonSource(@NonNull Style loadedMapStyle) {

        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
        loadedMapStyle.getLayer("unclustered-points").setProperties(visibility(Property.NONE));
        loadedMapStyle.getLayer("cluster-0").setProperties(visibility(Property.NONE));
        loadedMapStyle.getLayer("count").setProperties(visibility(Property.NONE));

        loadedMapStyle.addSource(
                new GeoJsonSource("single_ad",
                        singleGeoJSON.toString(),
                        new GeoJsonOptions()
                                .withCluster(true)
                                .withClusterMaxZoom(14)
                                .withClusterRadius(50)
                )
        );

        //Creating a marker layer for single data points
        SymbolLayer unclustered = new SymbolLayer("single-point", "single_ad");

        unclustered.setProperties(
                iconImage("cross-icon-id"),
                iconSize(
                        division(
                                get("mag"), literal(1.0f)
                        )
                )

        );
        loadedMapStyle.addLayer(unclustered);
    }


    public boolean listIsAtTop() {
        if (listView.getChildCount() == 0) {
            return true;
        }else {
            return  (listView.getChildAt(0).getTop() == 0 && listView.getFirstVisiblePosition() == 0);
        }
    }

    //Update distance to adItems when location is found and sort list according to distance
    public void updateGPSCoordinates(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        LatLng userLatLng = new LatLng(latitude,longitude);
        iterator = AdParts.listIterator();
        while (iterator.hasNext()) {
            current = iterator.next();
            current.setDistance(userLatLng.distanceTo(current.getLatLng()));
        }
        Collections.sort(AdParts, new Comparator<Ad>() {
            @Override
            public int compare(Ad o1, Ad o2) {
                return Double.compare(o1.getDistance(), o2.getDistance());
            }
        });
        Log.d("asdf", String.valueOf(longitude));
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                latitude, longitude), 10));
        updateTempList();
    }

    //Update list according to where user is looking on map
    public void updateTempList() {
        if (disableListUpdate == 0) {
            tempAdParts.clear();
            LatLngBounds bounds = mapboxMap.getProjection().getVisibleRegion().latLngBounds;
            iterator = AdParts.listIterator();
            while (iterator.hasNext()) {
                current = iterator.next();
                if (bounds.contains(current.getLatLng())) {
                    tempAdParts.add(current);
                }
            }
            AdAdapter adapter = new AdAdapter(MainActivity.this, tempAdParts);
            listView.setAdapter(adapter);
        }
    }

    //Update from database
    public void updateFromDb() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

        featureCollection = new FeatureCollection();

        final CollectionReference ad = db.collection("ad");
        //ad.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
        db.collection("ad")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AdParts.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String address = document.get("address").toString();
                                String description = document.get("description").toString();
                                String adid = document.get("ADID").toString();
                                double price = document.getLong("price").doubleValue();
                                String pricedescription = document.get("pricedescription").toString();
                                String product = document.get("product").toString();
                                String id = document.getId();
                                geoPoint = document.getGeoPoint("geo");
                                geoLatitude = geoPoint.getLatitude();
                                geoLongitude = geoPoint.getLongitude();
                                LatLng geoLatLng = new LatLng(geoLatitude, geoLongitude);
                                point = new Point(geoLatitude, geoLongitude);
                                feature = new Feature(point);
                                feature.setIdentifier(id);
                                feature.setProperties(new JSONObject());

                                featureCollection.addFeature(feature);

                                //String location = document.get("location").toString();
                                Log.d("asdf", product);
                                Log.d("asdf", address);
                                Log.d("asdf", String.valueOf(price));
                                Log.d("asdf", pricedescription);
                                Log.d("asdf", description);
                                Log.d("asdf", String.valueOf(geoLatitude));
                                Log.d("asdf", String.valueOf(geoLongitude));
                                Log.d("asdf", String.valueOf(adid));


                                AdPart part = new AdPart();
                                part.setAddress(address);
                                part.setProduct(product);
                                part.setDescription(description);
                                part.setPricedescription(pricedescription);
                                part.setLatLng(geoLatLng);
                                part.setPrice(price);
                                AdParts.add(part);

                                //TextView productTextView = findViewById(productNameText);
                                //productTextView.setText(product);

                                Log.d("asdf", document.getId() + " => " + document.getData());
                            }
                            try {
                                geoJSON = featureCollection.toJSON();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //QuerySnapshot doc = task.getResult();
                            buildMap();
                            //StringBuilder fields = new StringBuilder("");
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

}
