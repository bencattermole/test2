package uea.ac.uk.SmartTelescope_V1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;


//This class is the dialog for the RIN privacy statemernt
public class dialogClass extends AppCompatDialogFragment {

    @Override
    //Calling method created in LoginScreen
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("RIN Privacy Statement")
                .setMessage("By using this app you are agreeing that your information will be " +
                        "held for the purposes of Research on UEA systems. You are free to withdraw " +
                        "from this Research at any time. The University will ensure that your " +
                        "information is processed under Data Protection laws. We won't be able " +
                        "to support this app beyond the study end date. ")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}