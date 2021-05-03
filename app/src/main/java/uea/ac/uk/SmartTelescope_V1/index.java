package uea.ac.uk.SmartTelescope_V1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class index extends AppCompatActivity {

    private TextView GMTtime;
    private Spinner dropdown;
    private Button select;
    private Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        //Set the Time
        GMTtime = findViewById(R.id.CurrentTime);
        getGMTtime();

        //Dropdown List
        dropdown = findViewById(R.id.spinner);
        fillList();

        //Get spinner text


        //Setting the action with the Select button
        select = findViewById(R.id.button2);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = dropdown.getSelectedItem().toString();
                if (selection != "Select Star") {
                    //Will go to the next page now
                    System.out.println("Selection - " + selection);
                    Intent intent = new Intent(index.this, Star_Info.class);
                    intent.putExtra("messageKey", selection);
                    startActivity(intent);

                } else {
                    openDialog();
                }

            }
        });
    }


    //Get GMT time according to phone
    private void getGMTtime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm:ss a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        String localTime = date.format(currentLocalTime);

        GMTtime.setText(localTime);

    }

    //Will be edited later on to get the list from the API
    private void fillList() {
        String[] items = new String[]{"Select Star", "Star 1", "Star 2", "Star 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    public void openDialog() {
        dialogClass dialog1 = new dialogClass();
        dialog1.show(getSupportFragmentManager(), "example dialog");
    }
}