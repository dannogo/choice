package com.elantix.dopeapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by oleh on 4/9/16.
 */
public class FragmentLogOutConfirmation extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.logout_confirmation_dialog_title);
        builder.setMessage(R.string.logout_confirmation_dialog_question);
        builder.setNegativeButton(R.string.logout_confirmation_dialog_negative_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(R.string.logout_confirmation_dialog_positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HttpKit http = new HttpKit(getActivity());
                http.logOut(Utilities.sToken);
                Utilities.sToken = null;
                Utilities.sUid = null;
                SharedPreferences preferences = getActivity().getSharedPreferences(Utilities.MY_PREFS_NAME, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("token");
                editor.remove("uid");
                editor.commit();
                ((MainActivity) getActivity()).switchPageHandler(MainActivity.Page.Profile);
            }
        });

        Dialog dialog = builder.create();

        return dialog;
    }
}
