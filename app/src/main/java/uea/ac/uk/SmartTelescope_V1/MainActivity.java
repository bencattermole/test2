package uea.ac.uk.SmartTelescope_V1;
//Load Associated Libraries

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends Activity implements LocationListener, SensorEventListener {
    private SensorManager sensorManager;

    //Test section
    private LocationManager locationManager;
    private double testLatitude;
    private double testLongitude;
    private TextView testLatEditText;
    private TextView testLongEditText;
    private TextView GMTtime;
    private Button button;
    //

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean permissionDenied = false;


    private boolean isColor = false;
    private View view;
    private long lastUpdate;
    private EditText RAhoursEditText;
    private EditText RAminsEditText;
    private EditText RAsecsEditText;
    private EditText DECdEditText;
    private EditText DECcEditText;
    private EditText DECccEditText;
    private EditText LatitudeEditText;
    private EditText LongitudeEditText;
    private EditText GMSTEditText;
    private TextView CurrentRA;
    private TextView CurrentDEC;
    private TextView Altitude;
    private TextView Azimuth;
    public float LHA;
    public float RAsecs;
    public float DECsecs;
    public float dummyAlt;
    public float dummyAZTOP;
    public float dummyAZBOT;
    public float dummyAZTOT;
    public float GTime;
    public double degreeCONV = (Math.PI / 180);
    public String RaFirst;
    public String RaSecond;
    public String RaThird;
    public String DECFirst;
    public String DECSecond;
    public String DECThird;
    public String Latitude;
    public String Longitude;
    public String GMST;
    private TextView mOrientation1TextView;
    private TextView mOrientation2TextView;
    private TextView mOrientation3TextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Layout loaded from activity_main.html
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Getting the star information
        Bundle extras = getIntent().getExtras();
        String[] str = extras.getStringArray("moving");



        //Test Lat and Long and time
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        testLatEditText = findViewById(R.id.testLat);
        testLongEditText = findViewById(R.id.testLong);

        getLocation();


        GMTtime = findViewById(R.id.gmt);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, index.class));
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }


        //

        mOrientation1TextView = findViewById(R.id.Ori1);
        mOrientation2TextView = findViewById(R.id.Ori2);
        mOrientation3TextView = findViewById(R.id.Ori3);
        RAhoursEditText = findViewById(R.id.RAh);
        RAminsEditText = findViewById(R.id.RAm);
        RAsecsEditText = findViewById(R.id.RAs);
        DECdEditText = findViewById(R.id.DECd);
        DECcEditText = findViewById(R.id.DECc);
        DECccEditText = findViewById(R.id.DECcc);
        CurrentRA = findViewById(R.id.textView8);
        CurrentDEC = findViewById(R.id.textView9);
        LatitudeEditText = findViewById(R.id.latitudeEdit);
        LongitudeEditText = findViewById(R.id.LongitudeEdit);
        GMSTEditText = findViewById(R.id.GMSTEdit);
        Altitude = findViewById(R.id.Altitude);
        Azimuth = findViewById(R.id.Azimuth);
        //mThresholdEditText = findViewById(R.id.editTextNumberDecimal);
        lastUpdate = System.currentTimeMillis();

        RAsecs = 0;
        DECsecs = 0;
        LHA = 0;
        dummyAlt = 0;

//        getLongAndLat();
        getGMTtime();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    ///Sensor data collection
    public void onSensorChanged(SensorEvent event) {
        // if (event.sensor.getType() == Sensor.TYPE) {
        //     getAccelerometer(event);
//
        // }

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            getOrientation(event);

        }
    }


    //Get GMT time according to phone
    private String getGMTtime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm:ss");
        // you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        String localTime = date.format(currentLocalTime);
        String converted = convertTime(localTime);
        String finalConverted = decimalTime(converted);

        GMTtime.setText(converted);

//        System.out.println("Converted Original Time - " + converted);
//        System.out.println("Final Converted Time - " + finalConverted);



//        System.out.println("Testing decimal conversion - " + decimalTime(converted));

        return finalConverted;

    }

    //private void getAccelerometer(SensorEvent event) {
    //}

    @SuppressLint("SetTextI18n")
    private void getOrientation(SensorEvent event) {
        float[] values = event.values;

        float o1 = values[0];
        float o2 = values[1] * -1;
        float o3 = values[2];

        mOrientation1TextView.setText(Float.toString(o1));
        mOrientation2TextView.setText(Float.toString(o2));
        mOrientation3TextView.setText(Float.toString(o3));

        RaFirst = RAhoursEditText.getText().toString();
        RaSecond = RAminsEditText.getText().toString();
        RaThird = RAsecsEditText.getText().toString();

        DECFirst = DECdEditText.getText().toString();
        DECSecond = DECcEditText.getText().toString();
        DECThird = DECccEditText.getText().toString();

        Latitude = String.valueOf(testLatitude);
        Longitude = String.valueOf(testLongitude);
        String gettingTime = getGMTtime();

        GTime = Float.parseFloat(gettingTime);

//        Latitude = LatitudeEditText.getText().toString();
//        Longitude = LongitudeEditText.getText().toString();
        GMST = GMSTEditText.getText().toString();

        if (RaFirst.compareTo("") != 0 && RaSecond.compareTo("") != 0 && RaThird.compareTo("") != 0) {
            RAsecs = (float) (Float.parseFloat(RaFirst) * 15 + Float.parseFloat(RaSecond) / 4 + Float.parseFloat(RaThird) * 0.00416);
            CurrentRA.setText(Float.toString(RAsecs));
            //to get RA in units of degrees for calculations, 24hrs equates to a 360 degree turn
        }

        if (DECFirst.compareTo("") != 0 && DECSecond.compareTo("") != 0 && DECThird.compareTo("") != 0) {
            DECsecs = (float) (Float.parseFloat(DECFirst) + Float.parseFloat(DECSecond) * 0.016 + Float.parseFloat(DECThird) * 0.00027);
            CurrentDEC.setText(Float.toString(DECsecs));
            //to get DEC in units of degrees only for calculation, the two multiplication numbers should have a recurring final digit
        }

        // R R R G R


        if (GMST.compareTo("") != 0 && Longitude.compareTo("") != 0) {
            if (Float.parseFloat(GMST) * 15 <= 180) {
                LHA = Float.parseFloat(GMST) * 15 + 180 + Float.parseFloat(Longitude);
            } else {
                LHA = Float.parseFloat(GMST) * 15 - 180 + Float.parseFloat(Longitude);
            }
            //where GMST is given in decimal hours, so 10:30 would be 10.5 (change this)
        }

        if (DECsecs != 0 && Latitude.compareTo("") != 0 && LHA != 0) {
            dummyAlt = (float) (Math.sin(Float.parseFloat(Latitude) * degreeCONV) * Math.sin(DECsecs * degreeCONV) + Math.cos(Float.parseFloat(Latitude) * degreeCONV) * Math.cos(DECsecs * degreeCONV) * Math.cos(LHA * degreeCONV));
            Altitude.setText(Float.toString((float) ((Math.asin(dummyAlt) / degreeCONV))));
            //
        }

        if (dummyAlt != 0) {
            dummyAZTOP = (float) (Math.sin(DECsecs * degreeCONV) - (Math.sin(Float.parseFloat(Latitude) * degreeCONV) * Math.sin((Math.asin(dummyAlt) / degreeCONV) * degreeCONV)));
            dummyAZBOT = (float) (Math.cos(Float.parseFloat(Latitude) * degreeCONV) * Math.cos((Math.asin(dummyAlt) / degreeCONV) * degreeCONV));
            dummyAZTOT = dummyAZTOP / dummyAZBOT;
            Azimuth.setText(Float.toString((float) (Math.acos(dummyAZTOT) / degreeCONV)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {

        super.onPause();
        sensorManager.unregisterListener(this);
    }

    //Getting the location
    @SuppressLint("MissingPermission")
    public void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MainActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String latitude = String.valueOf(addresses.get(0).getLatitude());
            String longitude = String.valueOf(addresses.get(0).getLongitude());
//            System.out.println("Address length is - " + addresses.size());
            testLongEditText.setText(latitude);
            testLatEditText.setText(longitude);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Convert the time from a string to a string with no colons
    public String convertTime(String gmtTime) {
        String conversion = new String();
        for (int i = 0; i < gmtTime.length(); i++) {
            if (gmtTime.charAt(i) == ':') {
                conversion = gmtTime.replace(gmtTime.charAt(i), ' ');
            }
        }

        String strNew = conversion.replace(" ", "");
//        System.out.println("Changed String " + strNew);
        return strNew;
//        gmtTime.replace(":", "");

    }


    //This method gets the decimal version of the time for the equation
    public String decimalTime(String gmtTime) {
        String removingFirst;
        String removingSpace;
        //Gets the sub-strings for minutes and seconds
        String hours = gmtTime.substring(0, 2);
        String minutes = gmtTime.substring(2, 4);
        Float convert = Float.parseFloat(minutes);
        //Turning the minutes into a decimal
        Float calc = convert / 60;


        String convertedMins = String.valueOf(calc);
        removingFirst = convertedMins.replace(convertedMins.charAt(0), ' ');
        removingSpace = removingFirst.trim();
//        System.out.println("New Coverted Mins - " + removingSpace);

        //Create the string to be used for calculations
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(hours);
        stringBuilder.append(removingSpace);

        String newTime = stringBuilder.toString();

        return newTime;
    }
}

