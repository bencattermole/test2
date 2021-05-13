package uea.ac.uk.SmartTelescope_V1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;


public class Star_Info extends AppCompatActivity {

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

        //This is getting the user input from the home page
        Bundle extras = getIntent().getExtras();
        //Calling the key that was sent in the index page, the keys for all the intent getting has to match
        String str = extras.getString("messageKey");
//        System.out.println("The sent string" + str);

        //Testing that it gets the name
        String realName = getName(str);
//        System.out.println("Real name" + realName);

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
        //OnClick listener for the next page
        cont.setOnClickListener(v -> {
            //This sends the star information to the Calculations page
            Intent intent = new Intent(Star_Info.this, User_Location.class);
            //String array sends the name, azimuth and declination of the star
            String[] sending = new String[3];
            sending[0] = a;
            sending[1] = b;
            sending[2] = c;
            intent.putExtra("moving", sending);
            //Sends the page to User Location class
            startActivity(intent);

        });

        goBack = findViewById(R.id.back);
        //This sends the user back to the index page
        goBack.setOnClickListener(v -> startActivity(new Intent(Star_Info.this, index.class)));


    }

    //This is a split string method that will split the string input based on the delimiter
    private String[] splitString(String file) {
        String[] info;
        info = file.split(",");

        return info;
    }

    //This gets the Name of the star from a string
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


    //This is the method that actually reads it in
    public void readingIn(String fileName) {
        //This is to use the user input from the index page and get the correct text file
        //Stringbuilder takes the user input and adds the .txt extension to help them find the file
        StringBuilder s = new StringBuilder(fileName);
        s.append(".txt");
        String name = s.toString();
        String string = "";
        try {
            //Opening the file
            InputStream inputStream = getAssets().open(name);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            string = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Calling the splitString method to get the information about the string
        String[] info = splitString(string);
        a = info[0];
        b = info[1];
        c = info[2];

        starName.setText("The Name of this star is " + a);
        starAz.setText("The Azimuth of this star is " + b);
        starDec.setText("The Declination of this star is " + c);
    }
};