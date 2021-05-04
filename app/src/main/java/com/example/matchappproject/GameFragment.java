package com.example.matchappproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class GameFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // TODO: get spinner position from menu fragment
        // TODO: or we could make separate classes for each level


        int pos = 0;
        //pos = spinner.getSelectedItemPosition()

        View view;
        if (pos == 0) {
             view = inflater.inflate(R.layout.fragment_game_easy, container, false);
        } else if (pos == 1) {
            view = inflater.inflate(R.layout.fragment_game_medium, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_game_hard, container, false);
        }








        // OnCreate code here


        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
