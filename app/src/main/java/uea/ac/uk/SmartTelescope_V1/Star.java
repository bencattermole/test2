package uea.ac.uk.SmartTelescope_V1;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Star {
    private String name;
    private int azimuth;
    private int alituide;
    String path = "";


    public String readFromFile(Context context, String filename) {

        System.out.println("WE got here");

        StringBuilder s = new StringBuilder(path);
        s.append(filename);
        s.append(".txt");
        filename = s.toString();

        System.out.println("filename - " + filename);

        String ret = "";

        try {
            FileInputStream in = new FileInputStream(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            inputStreamReader.close();


//
//            InputStream inputStream = context.openFileInput(filename);
//            System.out.println("No we're here");
//
//            if ( inputStream != null ) {
//                System.out.println("HA wrong again!");
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ( (receiveString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append("\n").append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


}
