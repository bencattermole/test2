package uea.ac.uk.SmartTelescope_V1;
//Load Associated Libraries
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.MainThread;

import java.lang.Math;

public class MainActivity extends Activity implements SensorEventListener{
    private SensorManager sensorManager;
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
    public double degreeCONV = (Math.PI/180);
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
    private View aziL;
    private View aziC;
    private View aziR;
    private View altT;
    private View altC;
    private View altB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Layout loaded from activity_main.html
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

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
        aziL = findViewById(R.id.AziL);
        aziC = findViewById(R.id.aziC);
        aziR = findViewById(R.id.aziR);
        altT = findViewById(R.id.altT);
        altC = findViewById(R.id.altC);
        altB = findViewById(R.id.altB);

        aziL.setBackgroundColor(Color.RED);
        aziC.setBackgroundColor(Color.RED);
        aziR.setBackgroundColor(Color.RED);
        altT.setBackgroundColor(Color.RED);
        altC.setBackgroundColor(Color.RED);
        altB.setBackgroundColor(Color.RED);

        RAsecs = 0;
        DECsecs = 0;
        LHA = 0;
        dummyAlt = 0;
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    @Override
    ///Sensor data collection
    public void onSensorChanged(SensorEvent event) {
        // if (event.sensor.getType() == Sensor.TYPE) {
        //     getAccelerometer(event);
//
        // }

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            getOrientation(event);

        }

    }

    //private void getAccelerometer(SensorEvent event) {
    //}

    @SuppressLint("SetTextI18n")
    private void getOrientation(SensorEvent event) {
        float[] values = event.values;

        float o1 = values[0];
        float o2 = values[1]*-1;
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
            RAsecs = (float) (Float.parseFloat(RaFirst)*15 + Float.parseFloat(RaSecond)/4 + Float.parseFloat(RaThird)*0.00416);
            CurrentRA.setText(Float.toString(RAsecs));
            //to get RA in units of degrees for calculations, 24hrs equates to a 360 degree turn
        }

        if (DECFirst.compareTo("") != 0 && DECSecond.compareTo("") != 0 && DECThird.compareTo("") != 0) {
            DECsecs = (float) (Float.parseFloat(DECFirst) + Float.parseFloat(DECSecond)*0.016 + Float.parseFloat(DECThird)*0.00027);
            CurrentDEC.setText(Float.toString(DECsecs));
            //to get DEC in units of degrees only for calculation, the two multiplication numbers should have a recurring final digit
        }

        // R R R G R g


        if (GMST.compareTo("") != 0 && Longitude.compareTo("") != 0){
            if (Float.parseFloat(GMST)*15 <= 180){
                LHA = Float.parseFloat(GMST)*15 + 180 + Float.parseFloat(Longitude);
            }
            else {
                LHA = Float.parseFloat(GMST)*15 - 180 + Float.parseFloat(Longitude);
            }
            //where GMST is given in decimal hours, so 10:30 would be 10.5 (change this)
        }

        if (DECsecs !=0 && Latitude.compareTo("") !=0 && LHA != 0) {
            dummyAlt = (float) (Math.sin(Float.parseFloat(Latitude)*degreeCONV) * Math.sin(DECsecs*degreeCONV) + Math.cos(Float.parseFloat(Latitude)*degreeCONV) * Math.cos(DECsecs*degreeCONV) * Math.cos(LHA*degreeCONV));
            Altitude.setText(Float.toString((float) ((Math.asin(dummyAlt)/degreeCONV))));
            guidancealt(((float) ((Math.asin(dummyAlt)/degreeCONV))), event);
        }

        if (dummyAlt != 0) {
            dummyAZTOP = (float) (Math.sin(DECsecs*degreeCONV)-(Math.sin(Float.parseFloat(Latitude)*degreeCONV) * Math.sin((Math.asin(dummyAlt)/degreeCONV)*degreeCONV)));
            dummyAZBOT = (float) (Math.cos(Float.parseFloat(Latitude)*degreeCONV)* Math.cos((Math.asin(dummyAlt)/degreeCONV)*degreeCONV));
            dummyAZTOT = dummyAZTOP/dummyAZBOT;
            Azimuth.setText(Float.toString((float) (Math.acos(dummyAZTOT)/degreeCONV)));
            guidanceazi(((float) (Math.acos(dummyAZTOT)/degreeCONV)), event);
        }

    }

    private void guidancealt(float valueGuidedTo, SensorEvent event) {

        float[] values = event.values;
        float o1 = values[0];
        float o2 = values[1] * -1;
        float o3 = values[2];

        if (valueGuidedTo-30 <= o2 && o2 < valueGuidedTo-10) {
            altT.setBackgroundColor(Color.GREEN);
            // I dont know how to do coloured squares :(
        } else {
            altT.setBackgroundColor(Color.RED);
            System.out.println(valueGuidedTo);
        }

        //fringe case for if its near 0
        // if (valueGuidedTo+360-30 <= o2 && o2 < valueGuidedTo+360-10) {
        // altT.setBackgroundColor(Color.GREEN);
        // I dont know how to do coloured squares :(
        // } else {
        //altT.setBackgroundColor(Color.RED);
        //}

        if (valueGuidedTo-10 <= o2 && o2 <= valueGuidedTo+10) {
            altC.setBackgroundColor(Color.GREEN);
        } else {
            altC.setBackgroundColor(Color.RED);
        }

        // fringe case for middle where could be 0 or 360
        //if (valueGuidedTo+360-10 <= o2 && o2 <= valueGuidedTo-360+10) {
        // altC.setBackgroundColor(Color.GREEN);
        //} else {
        //  altC.setBackgroundColor(Color.RED);
        //}

        if (valueGuidedTo+10 < o2 && o2 <= valueGuidedTo+30) {
            altB.setBackgroundColor(Color.GREEN);
        } else {
            altB.setBackgroundColor(Color.RED);
        }

        //fringe case for when x is 360
        //if (valueGuidedTo-360+10 < o2 && o2 <= valueGuidedTo-360+30) {
        //  altB.setBackgroundColor(Color.GREEN);
        //} else {
        //  altB.setBackgroundColor(Color.GREEN);
        //}
    }


    private void guidanceazi(float valueGuidedTo, SensorEvent event){
        //my idea is to have three coloured blocks, [     ] [ ] [     ], for both values
        // intially all would start at red and the block "you are currently in" would be green
        // say x is the value you want to turn to the middle block would be green when your current position (p) are x-10 <= p <= x+10
        // for the outer two it would be LEFT: x-30 <= p < x-10 and RIGHT: x+10 < p <= x+30
        //
        //This may have to be split into two methods im not sure
        //difference between when guiding for azimuth and alititude will be if our current position is the value of the first element in the orientation sensor output array or the second one
        // the first is for azimuth the second for alitude
        //ill use p for current posistion
        float[] values = event.values;
        float o1 = values[0];
        float o2 = values[1] * -1;
        float o3 = values[2];

        //so here p = orientationSensor.array[0]
        if (valueGuidedTo-30 <= o1 &&  o1 < valueGuidedTo-10) {
            aziL.setBackgroundColor(Color.GREEN);
            // I dont know how to do coloured squares :(
        } else {
            aziL.setBackgroundColor(Color.RED);
        }

        //fringe case for if its near 0
        //if (valueGuidedTo+360-30 <= o1 && o1< valueGuidedTo+360-10) {
        //aziL.setBackgroundColor(Color.GREEN);
        // I dont know how to do coloured squares :(
        //} else {
        //  aziL.setBackgroundColor(Color.RED);
        //}

        if (valueGuidedTo-10 <= o1 && o1 <= valueGuidedTo+10) {
            aziC.setBackgroundColor(Color.GREEN);
        } else {
            aziC.setBackgroundColor(Color.RED);
        }

        // fringe case for middle where could be 0 or 360
        //if (valueGuidedTo+360-10 <= o1 && o1 <= valueGuidedTo-360+10) {
        //   aziC.setBackgroundColor(Color.GREEN);
        //} else {
        //  aziC.setBackgroundColor(Color.GREEN);
        //}

        if (valueGuidedTo+10 < o1 && o1<= valueGuidedTo+30) {
            aziR.setBackgroundColor(Color.GREEN);
        } else {
            aziR.setBackgroundColor(Color.RED);
        }

        //fringe case for when x is 360
        //if (valueGuidedTo-360+10 < o1 && o1 <= valueGuidedTo-360+30) {
        //  aziR.setBackgroundColor(Color.GREEN);
        //} else {
        // aziR.setBackgroundColor(Color.GREEN);
        //}
    }


    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {

        super.onPause();
        sensorManager.unregisterListener(this);
        //Toast.cancel();
    }
}
