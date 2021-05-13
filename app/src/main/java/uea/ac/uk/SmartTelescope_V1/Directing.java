package uea.ac.uk.SmartTelescope_V1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/*
This class needs the following things -
- Using your calculations to find the difference between the location of the star and current location
- Directing the user to the right azimuth and declination
- I have already imported the time, location and star info for you, as well as your equations, all that is left is tying it up
- I've left the XML page blank so you can build it as you want, if you want me to I can create it I have a plan if needs be

 */
public class Directing extends AppCompatActivity implements LocationListener, SensorEventListener {
    private TextView currentLat;
    private TextView currentLong;
    private Button reverse;
    private Button goAhead;
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directing);

        getLocation();

        reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(v -> startActivity(new Intent(Directing.this, User_Location.class)));

        //You can go to the page you made if you'd like, the code is there
        goAhead = findViewById(R.id.forward);
        goAhead.setOnClickListener(v -> startActivity(new Intent(Directing.this, MainActivity.class)));


        //This gets the list of star information from the Star_Info page
        Bundle extras = getIntent().getExtras();
        String[] str = extras.getStringArray("moving");
        // 0 - Star name, 1 = Azimuth, 2 = Declination
        // The files are in the Assets file

        //Permission needed to get the actual location
        if (ContextCompat.checkSelfPermission(Directing.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Directing.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @SuppressLint("MissingPermission")
    public void getLocation(){
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, Directing.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(Directing.this, Locale.getDefault());
            //Calling the GPS and getting the longitude and latitude. Can get the address if you want
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String latitude = String.valueOf(addresses.get(0).getLatitude());
            String longitude = String.valueOf(addresses.get(0).getLongitude());
            currentLat.setText("Latitude is - " + latitude);
            currentLong.setText("Longitude is - " + longitude);

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
        return strNew;

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

        //Create the string to be used for calculations
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(hours);
        stringBuilder.append(removingSpace);

        String newTime = stringBuilder.toString();

        return newTime;
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

        //This returns the time with no spaces and in a 6 digit string
        return finalConverted;

    }

    //Your equations, they need to be edited to work with this page
    //Call the RA and DEC from the intent (Star Info page)
    //Call the GMT time from the method above

//    @SuppressLint("SetTextI18n")
//    private void getOrientation(SensorEvent event) {
//        float[] values = event.values;
//
//        float o1 = values[0];
//        float o2 = values[1] * -1;
//        float o3 = values[2];
//
//        mOrientation1TextView.setText(Float.toString(o1));
//        mOrientation2TextView.setText(Float.toString(o2));
//        mOrientation3TextView.setText(Float.toString(o3));
//
//        RaFirst = RAhoursEditText.getText().toString();
//        RaSecond = RAminsEditText.getText().toString();
//        RaThird = RAsecsEditText.getText().toString();
//
//        DECFirst = DECdEditText.getText().toString();
//        DECSecond = DECcEditText.getText().toString();
//        DECThird = DECccEditText.getText().toString();
//
//        Latitude = String.valueOf(testLatitude);
//        Longitude = String.valueOf(testLongitude);
//        String gettingTime = getGMTtime();
//
//        GTime = Float.parseFloat(gettingTime);
//
////        Latitude = LatitudeEditText.getText().toString();
////        Longitude = LongitudeEditText.getText().toString();
//        GMST = GMSTEditText.getText().toString();
//
//        if (RaFirst.compareTo("") != 0 && RaSecond.compareTo("") != 0 && RaThird.compareTo("") != 0) {
//            RAsecs = (float) (Float.parseFloat(RaFirst) * 15 + Float.parseFloat(RaSecond) / 4 + Float.parseFloat(RaThird) * 0.00416);
//            CurrentRA.setText(Float.toString(RAsecs));
//            //to get RA in units of degrees for calculations, 24hrs equates to a 360 degree turn
//        }
//
//        if (DECFirst.compareTo("") != 0 && DECSecond.compareTo("") != 0 && DECThird.compareTo("") != 0) {
//            DECsecs = (float) (Float.parseFloat(DECFirst) + Float.parseFloat(DECSecond) * 0.016 + Float.parseFloat(DECThird) * 0.00027);
//            CurrentDEC.setText(Float.toString(DECsecs));
//            //to get DEC in units of degrees only for calculation, the two multiplication numbers should have a recurring final digit
//        }
//
//        // R R R G R
//
//
//        if (GMST.compareTo("") != 0 && Longitude.compareTo("") != 0) {
//            if (Float.parseFloat(GMST) * 15 <= 180) {
//                LHA = Float.parseFloat(GMST) * 15 + 180 + Float.parseFloat(Longitude);
//            } else {
//                LHA = Float.parseFloat(GMST) * 15 - 180 + Float.parseFloat(Longitude);
//            }
//            //where GMST is given in decimal hours, so 10:30 would be 10.5 (change this)
//        }
//
//        if (DECsecs != 0 && Latitude.compareTo("") != 0 && LHA != 0) {
//            dummyAlt = (float) (Math.sin(Float.parseFloat(Latitude) * degreeCONV) * Math.sin(DECsecs * degreeCONV) + Math.cos(Float.parseFloat(Latitude) * degreeCONV) * Math.cos(DECsecs * degreeCONV) * Math.cos(LHA * degreeCONV));
//            Altitude.setText(Float.toString((float) ((Math.asin(dummyAlt) / degreeCONV))));
//            //
//        }
//
//        if (dummyAlt != 0) {
//            dummyAZTOP = (float) (Math.sin(DECsecs * degreeCONV) - (Math.sin(Float.parseFloat(Latitude) * degreeCONV) * Math.sin((Math.asin(dummyAlt) / degreeCONV) * degreeCONV)));
//            dummyAZBOT = (float) (Math.cos(Float.parseFloat(Latitude) * degreeCONV) * Math.cos((Math.asin(dummyAlt) / degreeCONV) * degreeCONV));
//            dummyAZTOT = dummyAZTOP / dummyAZBOT;
//            Azimuth.setText(Float.toString((float) (Math.acos(dummyAZTOT) / degreeCONV)));
//        }
//    }
}