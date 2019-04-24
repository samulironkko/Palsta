package com.example.palsta;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static android.os.Build.ID;
import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.String.valueOf;

public class NewAdActivity extends AppCompatActivity {

    double pointerLatitude;
    double pointerLongitude;

    double longitude;
    double latitude;

    private static final int PLACE_SELECTION_REQUEST_CODE = 56789;
    private static final int RESULT_LOAD_IMAGE = 9999;
    private static final int MY_PERMISSION_ACCESS_GALLERY = 3928;

    private StorageReference mStorageRef;

    private final int PICK_IMAGE_REQUEST = 71;

    private StorageReference storageRef;



    Button mapButton;
    Button addressButton;
    TextView addressTextView;
    ImageButton addImageButton;
    EditText priceEditText;
    Spinner unitSpinner;
    RadioGroup sellGiveRadioGroup;
    Button publishButton;
    Ad ad = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        longitude = bundle.getDouble("EXTRA_LONGITUDE");
        latitude = bundle.getDouble("EXTRA_LATITUDE");
        Log.d("chad", Double.toString(latitude));
        Log.d("chad", Double.toString(longitude));


        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://palsta-b6497.appspot.com/");

        Mapbox.getInstance(this, "pk.eyJ1Ijoic2FtdWxpcm9ua2tvIiwiYSI6ImNqdHF4Z2ViczBpZmI0ZGxsdDF1eHczZzgifQ.wBTnY_6-AdYQKk7dYqFDlQ");
        

        mapButton = findViewById(R.id.address_button);
        addressButton = findViewById(R.id.address_button);
        addImageButton = findViewById(R.id.add_image_button);
        addressTextView = findViewById(R.id.address_textview);
        unitSpinner = findViewById(R.id.unit_spinner);
        sellGiveRadioGroup = findViewById(R.id.sell_give_group);
        priceEditText = findViewById(R.id.price_edit_text);
        publishButton = findViewById(R.id.publish_button);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        unitSpinner.setAdapter(spinnerAdapter);

        if((Ad)bundle.getSerializable("EDITABLEAD") != null) {
            ad = (Ad) bundle.getSerializable("EDITABLEAD");
            Log.d("chad", "wattafak");
            ((EditText)findViewById(R.id.product_name_edit_text)).setText(ad.getProduct());
            ((TextView)findViewById(R.id.address_textview)).setText(ad.getAddress());
            ((EditText)findViewById(R.id.price_edit_text)).setText(Float.toString(ad.getPrice()));
            ((EditText)findViewById(R.id.desc_edit_text)).setText(ad.getDescription());
            Spinner mySpinner = (Spinner) findViewById(R.id.unit_spinner);
            pointerLatitude = latitude;
            pointerLongitude = longitude;
            Log.d("chadoget", ad.getPricedescription());
            if(ad.getPricedescription().equals("Kg"))
                mySpinner.setSelection(0);
            if(ad.getPricedescription().equals("Kpl"))
                mySpinner.setSelection(1);
            if(ad.getPricedescription().equals("Litra"))
                mySpinner.setSelection(2);
        }

        sellGiveRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_give) {
                    priceEditText.setVisibility(View.GONE);
                    unitSpinner.setVisibility(View.GONE);
                    priceEditText.setText(null);
                } else {
                    priceEditText.setVisibility(View.VISIBLE);
                    unitSpinner.setVisibility(View.VISIBLE);
                }
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToPickerActivity();
                // Intent intent = new Intent(NewAdActivity.this, MapActivity.class);
               // startActivity(intent);
            }
        });
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(NewAdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(NewAdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_ACCESS_GALLERY);

                    return;
                }
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishAdActivity();
                finish();
            }
        });

    }

    /**
     * Set up the PlacePickerOptions and startActivityForResult
     */
    private void publishAdActivity(){

        String product = new String();
        String address = new String();
        //float price;
        //String pricedescription = new String();
        String description = new String();

        mStorageRef = FirebaseStorage.getInstance().getReference();


        product = ((EditText)findViewById(R.id.product_name_edit_text)).getText().toString();
        address = ((TextView)findViewById(R.id.address_textview)).getText().toString();

        EditText edt = (EditText) findViewById(R.id.price_edit_text);
        float price = Float.valueOf(edt.getText().toString());

        description = ((EditText)findViewById(R.id.desc_edit_text)).getText().toString();

        Spinner mySpinner = (Spinner) findViewById(R.id.unit_spinner);
        String pricedescription = mySpinner.getSelectedItem().toString();

        Log.d("ass", product);
        Log.d("ass", address);
        Log.d("ass", description);
        Log.d("ass", String.valueOf(price));
        Log.d("ass", pricedescription);
        Log.d("ass", valueOf(pointerLongitude));
        Log.d("ass", valueOf(pointerLatitude));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = new String(sharedPreferences.getString("UUID", null));
        Log.d("lol", UID);
        Log.d("1234", sharedPreferences.getString("UUID", null));

        //Location lastLocation = locationEngine.getLastLocation();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();

        DocumentReference ref = db.collection("ad").document();
        String myId = ref.getId();
        //data.put("photourl", photo);


        if (ad == null) {
            data.put("UUID", UID);
            data.put("ADID", myId);
            data.put("product", product);
            data.put("address", address);
            data.put("price", price);
            data.put("pricedescription", pricedescription);
            data.put("description", description);
            data.put("geo", new GeoPoint(pointerLatitude, pointerLongitude));
            //location: new firebase.firestore.GeoPoint(pointerLatitude, pointerLongitude);
            //new GeoPoint(latitude = pointerLatitude, longitude = pointerLongitude);


            db.collection("ad").document(myId)
                    .set(data)
                    //.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    //    @Override
                    //    public void onSuccess(DocumentReference documentReference) {
                    //        Log.d("lol", "DocumentSnapshot written with ID: " + documentReference.getId());
                    //    }
                    //})
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("lol", "Error adding document", e);
                        }
                    });
        } else {

            data.put("UUID", UID);
            data.put("ADID", ad.getAdid());
            data.put("product", product);
            data.put("address", address);
            data.put("price", price);
            data.put("pricedescription", pricedescription);
            data.put("description", description);
            data.put("geo", new GeoPoint(pointerLatitude, pointerLongitude));



            DocumentReference washingtonRef = db.collection("ad").document(ad.getAdid());

// Set the "isCapital" field of the city 'DC'
            washingtonRef
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("update", "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("update", "Error updating document", e);
                        }
                    });


            /*
            data.put("UUID", "mun on ID XDDDD");
            data.put("ADID", myId);
            data.put("product", "myydään TOMAAATTIA!");
            data.put("address", address);
            data.put("price", price);
            data.put("pricedescription", pricedescription);
            data.put("description", description);
            data.put("geo", new GeoPoint(pointerLatitude, pointerLongitude));
            Log.d("päivitys", "woop woop");

            db
                    .collection("ad")
                    .document(myId)
                    .update(data);
                    */
        }
    }

    private void goToPickerActivity() {
        startActivityForResult(
                new PlacePicker.IntentBuilder()
                        .accessToken("pk.eyJ1Ijoic2FtdWxpcm9ua2tvIiwiYSI6ImNqdHF4Z2ViczBpZmI0ZGxsdDF1eHczZzgifQ.wBTnY_6-AdYQKk7dYqFDlQ")
                        .placeOptions(PlacePickerOptions.builder()
                                .statingCameraPosition(new CameraPosition.Builder()
                                        .target(new LatLng(latitude, longitude)).zoom(16).build())
                                .build())
                        .build(this), PLACE_SELECTION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PLACE_SELECTION_REQUEST_CODE && resultCode == RESULT_OK){

            // Retrieve the information from the selected location's CarmenFeature

            CarmenFeature carmenFeature = PlacePicker.getPlace(data);

            addressTextView.setText(carmenFeature.placeName());
            Point point = carmenFeature.center();
            pointerLatitude = point.latitude();
            pointerLongitude = point.longitude();

        }else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri SelectedImage = data.getData();
            String[] FilePathColumn = {MediaStore.Images.Media.DATA};

            Cursor SelectedCursor = getContentResolver().query(SelectedImage, FilePathColumn, null, null, null);
            SelectedCursor.moveToFirst();

            int columnIndex = SelectedCursor.getColumnIndex(FilePathColumn[0]);
            String picturePath = SelectedCursor.getString(columnIndex);
            SelectedCursor.close();

            final File imageFile = new File(picturePath);
            long originalSize = imageFile.length()/1024;
            Log.d("jeps", "original: " + valueOf(originalSize));
/*
            try {
                File compressedImageFile = new Compressor(this).compressToFile(imageFile);

                Log.d("jeps", valueOf(compressedSize));
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
            try {
                Bitmap compressedImageBitmap = new Compressor(this).compressToBitmap(imageFile);
                long compressedSize = compressedImageBitmap.getByteCount()/1024;
                Log.d("jeps", valueOf(compressedSize));
                addImageButton.setImageBitmap(compressedImageBitmap);

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("shoutboard").child("posts").push();
                String key = myRef.getKey();

                Uri file = Uri.fromFile(imageFile);
                StorageReference picture = storageRef.child("gs://palsta-b6497.appspot.com/");

                picture.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri photo = taskSnapshot.getUploadSessionUri();
                                Log.d("kuvaa tulee", imageFile.toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });
                //uploadTask = ref.putFile(file);
                //upload = compressedImageBitmap.putFile("gs://palsta-b6497.appspot.com/");
                /*
                String filelocation = "gs://palsta-b6497.appspot.com/";
                //uploadTask = ref.putFile(file)
                imageFile.putFile(filelocation)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("NEW_POST", uri.toString());
                                //Post post = new Post(0, post_message, uri.toString(), type, System.currentTimeMillis(), FirebaseAuth.getInstance().getUid(), 0, user_name, profile_pic);
                */
            } catch (IOException e) {
                e.printStackTrace();
            }
            //compressedImageBitmap=image to store
          //  addImageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_ACCESS_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        }
    }
}