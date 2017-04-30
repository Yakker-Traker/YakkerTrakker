package com.yakkertrakker.www.yakkertrakker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by joe on 4/28/17.
 */

public class routeDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Add a route name").setPositiveButton(getResources().getString(R.string.key_addRoute), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

            }
        }).setNegativeButton(getResources().getString(R.string.key_cancel), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

            }
        });
        return builder.create();
    }
}
