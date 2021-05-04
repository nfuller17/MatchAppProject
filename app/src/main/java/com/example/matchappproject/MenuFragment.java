package com.example.matchappproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import org.w3c.dom.ls.LSOutput;

public class MenuFragment extends Fragment {
    Button play;
    Button scores;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_difficulty);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.difficulty_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // TODO: carry over last selected difficulty in spinner?

        // Inflate the layout for this fragment

        play = view.findViewById(R.id.button_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItemPosition() == 0) {
                    NavHostFragment.findNavController(MenuFragment.this)
                            .navigate(R.id.action_FirstFragment_to_EasyFragment);
                } else if (spinner.getSelectedItemPosition() == 1) {
                    NavHostFragment.findNavController(MenuFragment.this)
                            .navigate(R.id.action_FirstFragment_to_MediumFragment);
                } else {
                    NavHostFragment.findNavController(MenuFragment.this)
                            .navigate(R.id.action_FirstFragment_to_HardFragment);
                }

            }
        });

        scores = view.findViewById(R.id.button_scoreboard);
        scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MenuFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });



        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MenuFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}