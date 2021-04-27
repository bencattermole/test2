package uea.ac.uk.SmartTelescope_V1;
//Load Associated Libraries

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.lang.Math;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;

    //Test section
    private LocationManager locationManager;
    private double testLatitude;
    private double testLongitude;
    private TextView testLatEditText;
    private TextView testLongEditText;
    private TextView GMTtime;
    //

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

        //Test Lat and Long and time
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        testLatEditText = findViewById(R.id.testLat);
        testLongEditText = findViewById(R.id.testLong);
        GMTtime = findViewById(R.id.gmt);
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

        getLongAndLat();
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

    private void getLongAndLat() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        testLatitude = location.getLongitude();
        testLongitude = location.getLongitude();

        String lat = String.valueOf(testLatitude);
        String longitude = String.valueOf(testLongitude);
        System.out.println("Latiude - " + lat);
        System.out.println("Longitude - " + longitude);


        testLatEditText.setText(lat);
        testLongEditText.setText(longitude);

    }

    //Get GMT time according to phone
    private void getGMTtime(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm:ss a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        String localTime = date.format(currentLocalTime);

        GMTtime.setText(localTime);

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

        Latitude = LatitudeEditText.getText().toString();
        Longitude = LongitudeEditText.getText().toString();
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

        getLongAndLat();

    }

    @Override
    protected void onPause() {

        super.onPause();
        sensorManager.unregisterListener(this);
        //Toast.cancel();
    }
}