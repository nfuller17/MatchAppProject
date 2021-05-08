package com.example.matchappproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

public class ExitGameDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);

        builder.setTitle(R.string.gameInProgress)
                .setMessage(R.string.exitConfirmation)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Menu menu = ((MainActivity)getActivity()).getMenu();
                        MenuItem settings = menu.findItem(R.id.action_settings);
                        settings.setVisible(true);
                        Toolbar toolbar = ((MainActivity)getActivity()).findViewById(R.id.toolbar);
                        toolbar.setTitle("");
                        ((MainActivity)getActivity()).setGameState(0);
                        ((MainActivity)getActivity()).onBackPressed();
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {});

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button nButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                nButton.setBackgroundColor(Color.WHITE);
                nButton.setTextColor(Color.DKGRAY);
                Button pButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                pButton.setBackgroundColor((Color.WHITE));
                pButton.setTextColor(Color.DKGRAY);
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
