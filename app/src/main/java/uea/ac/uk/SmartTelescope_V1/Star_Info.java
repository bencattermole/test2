package uea.ac.uk.SmartTelescope_V1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Star_Info extends AppCompatActivity {

    private TextView StarName;
    private TextView choice;
    private TextView title;
    private TextView starName;
    private TextView starAz;
    private TextView starDec;
    private String a;
    private String b;
    private String c;

    private String file = "myFile";
    private String fileContents;
    private Button cont;
    private Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star__info);

        Bundle extras = getIntent().getExtras();

        String str = extras.getString("messageKey");
        System.out.println("The sent string" + str);

        String realName = getName(str);
        System.out.println("Real name" + realName);

        //Setting the page name

        StringBuilder s = new StringBuilder(str);
        s.append(" Information Page");
        String userChoice = s.toString();
        title = findViewById(R.id.title);
        title.setText(userChoice);

        starName = findViewById(R.id.info);
        starAz = findViewById(R.id.az);
        starDec = findViewById(R.id.declin);


        readingIn(realName);
        cont = findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This sends the star information to the Calculations page
                Intent intent = new Intent(Star_Info.this, User_Location.class);
                String[] sending = new String[3];
                sending[0] = a;
                sending[1] = b;
                sending[2] = c;
                intent.putExtra("moving", sending);
                startActivity(intent);

            }
        });

        goBack = findViewById(R.id.back);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Star_Info.this, index.class));
            }
        });


    }

    private String[] splitString(String file) {
        String[] info;
        info = file.split(",");

        return info;
    }

    private String getName(String fileName) {
        String txtName = "";
        if (fileName.equals("Star 1")) {
            txtName = "star1";
        } else if (fileName.equals("Star 2")) {
            txtName = "star2";
        }
        return txtName;
    }

    ;


    public void readingIn(String fileName) {
        StringBuilder s = new StringBuilder(fileName);
        s.append(".txt");
        String name = s.toString();
        String string = "";
        try {
            InputStream inputStream = getAssets().open(name);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            string = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] info = splitString(string);
        a = info[0];
        b = info[1];
        c = info[2];

        starName.setText("The Name of this star is " + a);
        starAz.setText("The Azimuth of this star is " + b);
        starDec.setText("The Declination of this star is " + c);
    }
};