package com.example.matchappproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class ScoreboardFragment extends Fragment {
    Button refresh, local, global;
    int locGlob = 0;    // If 0, local scores. If 1, global scores.
    int radioIndex = 0; // 0 for easy, 1 for medium, 2 for hard.

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_scoreboard, container, false);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_scoreboard);
        radioIndex = radioGroup.getCheckedRadioButtonId();
        Log.i("scoreboard", "radio index " + radioIndex + " selected.");
        
        local = view.findViewById(R.id.button_local);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locGlob = 0;
            }
        });

        global = view.findViewById(R.id.button_global);
        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locGlob = 1;
            }
        });

        refresh = view.findViewById(R.id.button_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Insert code here to requery data results
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_sc2menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ScoreboardFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}