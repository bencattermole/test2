package uea.ac.uk.SmartTelescope_V1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

//This class is the dialog for the true North dialog
public class dialogClass2 extends AppCompatDialogFragment {

    @Override
    //Calling method created in LoginScreen
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Aligned!")
                .setMessage("Please click continue you have reached true north!")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}