package uea.ac.uk.SmartTelescope_V1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

//This class gets the user location and guides them to true north
public class User_Location extends AppCompatActivity implements LocationListener, SensorEventListener {
    private TextView currentLat;
    private TextView currentLong;
    private TextView currentAddress;
    private Button goBack;
    private Button goForward;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private ImageView compass;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //This gets the list of star information from the Star_Info page
        Bundle extras = getIntent().getExtras();
        String[] str = extras.getStringArray("moving");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        currentLat = findViewById(R.id.CurrentLat);
        currentLong = findViewById(R.id.CurrentLong);
        currentAddress = findViewById(R.id.compass_address);

        //Calling get location which will fill out the above lines
        getLocation();

        goBack = findViewById(R.id.back);
        goBack.setOnClickListener(v -> startActivity(new Intent(User_Location.this, Star_Info.class)));

        compass = findViewById(R.id.imageView);


        goForward = findViewById(R.id.cont);
        goForward.setOnClickListener(v -> {
            //This sends the star information to the next page
            Intent intent = new Intent(User_Location.this, MainActivity.class);
            intent.putExtra("moving", str);
            startActivity(intent);

        });

        //Permission needed to get the actual location
        if (ContextCompat.checkSelfPermission(User_Location.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(User_Location.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

    }

    //Method to get the location using Location Manager
    @SuppressLint("MissingPermission")
    public void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, User_Location.this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //This update the location
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(User_Location.this, Locale.getDefault());
            //Calling the GPS and getting the longitude and latitude. Can get the address if you want
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String latitude = String.valueOf(addresses.get(0).getLatitude());
            String longitude = String.valueOf(addresses.get(0).getLongitude());
//            String address = String.valueOf(addresses.get(0).getAddressLine(0));
//            System.out.println("Address length is - " + addresses.size());
            currentLat.setText("Latitude is - " + latitude);
            currentLong.setText("Longitude is - " + longitude);
//            currentAddress.setText("Your current Address is - " + address);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //This is the compass method
    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        //This is the field above the compass telling them what angle they are
        currentAddress.setText("Heading: " + String.valueOf(degree) + " degrees");
        //If they are facing true north
        if (degree == 0.0) {
            //Call the dialog screen
            openDialog();
        }

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        System.out.println("I'm here");

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        compass.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    //This sends the dialog if the user is facing true north
    public void openDialog() {
        dialogClass2 dialog1 = new dialogClass2();
        dialog1.show(getSupportFragmentManager(), "example dialog");
    }
}