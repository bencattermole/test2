package uea.ac.uk.SmartTelescope_V1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class newStar extends AppCompatActivity {
    private Button goBack;
    private Button confirm;
    private EditText RA1;
    private EditText RA2;
    private EditText RA3;
    private EditText AZ1;
    private EditText AZ2;
    private EditText AZ3;

    private EditText name;
    private String enteredName;

    private String ra1Text;
    private String ra2Text;
    private String ra3Text;

    private String az1Text;
    private String az2Text;
    private String az3Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_star);

        RA1 = findViewById(R.id.Declination1);
        RA2 = findViewById(R.id.Declination2);
        RA3 = findViewById(R.id.Declination2);

        AZ1 = findViewById(R.id.Azimuth1);
        AZ2 = findViewById(R.id.Azimuth2);
        AZ3 = findViewById(R.id.Azimuth3);

        name = findViewById(R.id.enterName);

        goBack = findViewById(R.id.newBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newStar.this, index.class);
                startActivity(intent);
            }
        });

        confirm = findViewById(R.id.confirmed);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Value of Check Null is -" + checkNull().toString());
                //if the values aren't null
                if (checkNull() == true) {
                    openDialog();
                } else {
                    String entered = createNew();
                    writeToFile(entered);
                }
            }
        });


    }

    public void openDialog() {
        dialogClass3 dialog1 = new dialogClass3();
        dialog1.show(getSupportFragmentManager(), "example dialog");
    }

    public Boolean checkNull() {
        boolean check = false;

        if (RA1.getText().toString() == null) {
            check = true;
        } else if (RA2.getText().toString() == null) {
            check = true;
        } else if (RA3.getText().toString() == null) {
            check = true;
        } else if (AZ1.getText().toString() == null) {
            check = true;
        } else if (AZ2.getText().toString() == null) {
            check = true;
        } else if (AZ3.getText().toString() == null) {
            check = true;
        } else if (name.getText().toString() == null) {
            check = true;
        }
        return check;
    }

    public String createNew(){
        String enteredInfo;
        enteredName = name.getText().toString();
        ra1Text = RA1.getText().toString();
        ra2Text = RA2.getText().toString();
        ra3Text = RA3.getText().toString();
        az1Text = AZ1.getText().toString();
        az2Text = AZ2.getText().toString();
        az3Text = AZ3.getText().toString();

        StringBuilder stringBuilder = new StringBuilder(enteredName);
        stringBuilder.append(",");
        stringBuilder.append(ra1Text);
        stringBuilder.append("-");
        stringBuilder.append(ra2Text);
        stringBuilder.append("-");
        stringBuilder.append(ra3Text);
        stringBuilder.append(",");
        stringBuilder.append(az1Text);
        stringBuilder.append("-");
        stringBuilder.append(az2Text);
        stringBuilder.append("-");
        stringBuilder.append(az3Text);

        enteredInfo = stringBuilder.toString();

        System.out.println(enteredInfo);

        return enteredInfo;
    }

    private void writeToFile(String message)
    {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(String.valueOf(getAssets().open("list.txt")),
                    Context.MODE_PRIVATE));
            outputStreamWriter.write(message);
            outputStreamWriter.close();

        } catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}