package com.example.palsta;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import java.util.ArrayList;

public class NewAdActivity extends AppCompatActivity {

    double longitude;
    double latitude;

    private static final int PLACE_SELECTION_REQUEST_CODE = 56789;
    private static final int RESULT_LOAD_IMAGE = 9999;
    Button mapButton;
    Button addressButton;
    TextView addressTextView;
    ImageButton addImageButton;
    EditText priceEditText;
    Spinner unitSpinner;
    RadioGroup sellGiveRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        longitude = bundle.getDouble("EXTRA_LONGITUDE");
        latitude = bundle.getDouble("EXTRA_LATITUDE");


        Mapbox.getInstance(this, "pk.eyJ1Ijoic2FtdWxpcm9ua2tvIiwiYSI6ImNqdHF4Z2ViczBpZmI0ZGxsdDF1eHczZzgifQ.wBTnY_6-AdYQKk7dYqFDlQ");

        mapButton = findViewById(R.id.address_button);
        addressButton = findViewById(R.id.address_button);
        addImageButton = findViewById(R.id.add_image_button);
        addressTextView = findViewById(R.id.address_textview);
        unitSpinner = findViewById(R.id.unit_spinner);
        sellGiveRadioGroup = findViewById(R.id.sell_give_group);
        priceEditText = findViewById(R.id.price_edit_text);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        unitSpinner.setAdapter(spinnerAdapter);

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
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
    }

    /**
     * Set up the PlacePickerOptions and startActivityForResult
     */
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
        }else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri SelectedImage = data.getData();
            String[] FilePathColumn = {MediaStore.Images.Media.DATA};

            Cursor SelectedCursor = getContentResolver().query(SelectedImage, FilePathColumn, null, null, null);
            SelectedCursor.moveToFirst();

            int columnIndex = SelectedCursor.getColumnIndex(FilePathColumn[0]);
            String picturePath = SelectedCursor.getString(columnIndex);
            SelectedCursor.close();

            addImageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));


        }

    }


}
