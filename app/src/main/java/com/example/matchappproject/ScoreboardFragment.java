package com.example.matchappproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class ScoreboardFragment extends Fragment {
    View view;
    Button refresh, local, global;
    int locGlob = 0;    // If 0, local scores. If 1, global scores.
    int radioSelectedID;
    int radioIndex = 0; // 0 for easy, 1 for medium, 2 for hard.
    RadioButton selectedRadioButton;

    TextView playerName;
    TextView playerScore;

    SharedPreferences sharedPref;
    String localUsername = "";
    Map<String, Object> userAndScore;
    Map<String, Object> scoreBoard;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d("ON", "onCreateView called");
        view = inflater.inflate(R.layout.fragment_scoreboard, container, false);
        super.onCreateView(inflater, container, savedInstanceState);



        if (savedInstanceState != null) {
            // Restore last state for checked position.
            for (int i = 1; i <= 9; i++) {
                int id = getResources().getIdentifier("table_r" + i + "c1" , "id", getActivity().getPackageName());
                Log.d("ONN in onCreate", "view id id " + "table_r" + i + "c1");
                playerName = view.findViewById(id);
                Log.d("ONN in oncreate", "foundViewByID for table_r" + i + "c1");
                String p = savedInstanceState.getString("player" + i, "");
                playerName.setText(p);

                id = getResources().getIdentifier("table_r" + i + "c2" , "id", getActivity().getPackageName());
                playerScore = view.findViewById(id);
                String s = savedInstanceState.getString("score" + i, "");
                playerScore.setText(s);


            }

        }

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_scoreboard);


        local = view.findViewById(R.id.button_local);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locGlob = 0;

                radioSelectedID = radioGroup.getCheckedRadioButtonId();
                selectedRadioButton = (RadioButton) view.findViewById(radioSelectedID);
                radioIndex = radioGroup.indexOfChild(selectedRadioButton);
                if (radioIndex == 0) {
                    getLocal(localUsername, "Easy");
                } else if (radioIndex == 1) {
                    getLocal(localUsername, "Medium");
                } else if (radioIndex == 2) {
                    getLocal(localUsername, "Hard");
                }
            }
        });

        global = view.findViewById(R.id.button_global);
        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locGlob = 1;

                radioSelectedID = radioGroup.getCheckedRadioButtonId();
                selectedRadioButton = (RadioButton) view.findViewById(radioSelectedID);
                radioIndex = radioGroup.indexOfChild(selectedRadioButton);
                if (radioIndex == 0) {
                    getGlobal("Easy");
                } else if (radioIndex == 1) {
                    getGlobal("Medium");
                } else if (radioIndex == 2) {
                    getGlobal("Hard");
                }
            }
        });


        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Map<String, ?> map = sharedPref.getAll();

        localUsername = (String) map.get("Username");
        Log.d("SHAREDPREF", "localUsername is " + localUsername);
        refresh = view.findViewById(R.id.button_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioSelectedID = radioGroup.getCheckedRadioButtonId();
                selectedRadioButton = (RadioButton) view.findViewById(radioSelectedID);
                radioIndex = radioGroup.indexOfChild(selectedRadioButton);

                Log.d("CLICK", "radioIndex = " + radioIndex + " locGlob = " + locGlob);
                if (locGlob == 0 && radioIndex == 0) {
                    getLocal(localUsername, "Easy");
                } else if (locGlob == 0 && radioIndex == 1) {
                    getLocal(localUsername, "Medium");
                } else if (locGlob == 0 && radioIndex == 2) {
                    getLocal(localUsername, "Hard");
                } else if (locGlob == 1 && radioIndex == 0) {
                    getGlobal("Easy");
                } else if (locGlob == 1 && radioIndex == 1) {
                    getGlobal("Medium");
                } else if (locGlob == 1 && radioIndex == 2) {
                    getGlobal("Hard");
                }

            }
        });

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


    public void getGlobal(String lvl) {

        Log.d("GLOB", "staring getGlobal");
        CollectionReference docRef = db.collection("High Scores " + lvl);
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                //resets scoreboard
                for (int i = 1; i <= 9; i++) {
                    Log.d("RESET", "reseting global scoreboard");


                    int id = getResources().getIdentifier("table_r" + i + "c1" , "id", getActivity().getPackageName());
                    //Log.d("VIEW" ,"view id is table_r" + i + "c1");
                    playerName = view.findViewById(id);
                    id = getResources().getIdentifier("table_r" + i + "c2" , "id", getActivity().getPackageName());
                    //Log.d("VIEW" ,"view id is table_r" + i + "c2");
                    playerScore = view.findViewById(id);

                    playerName.setText("");
                    playerScore.setText("");

                }

                if (task.isSuccessful()) {
                    Log.d("GLOB", "task is successful");
                    userAndScore = new HashMap<String, Object>();

                    for (QueryDocumentSnapshot docu : task.getResult()) {
                        Log.d("GLOB", docu.getId() + " => " + docu.getData());

                        userAndScore.putAll(docu.getData());
                    }

                    if (Objects.isNull(userAndScore)) {
                        playerName = view.findViewById(R.id.table_r1c1);
                        playerScore = view.findViewById(R.id.table_r1c2);
                        playerName.setText("----");
                        playerScore.setText("----");
                        Toast.makeText(getContext(), "No scores found. Please play a game to get a score", Toast.LENGTH_SHORT).show();
                        Log.d("GLOB", "no documents in global easy");
                    } else {
                        Log.d("GLOB", "documents in global easy found");
                        LinkedHashMap<String, Object> sorted = (LinkedHashMap<String, Object>) sortTimes(userAndScore);

                        int min = Math.min(9, sorted.size());

                        for (int i = 1; i <= min; i++) {
                            String user = (String) sorted.keySet().toArray()[i-1];

                            String time = (String) sorted.get(user);

                            int id = getResources().getIdentifier("table_r" + i + "c1" , "id", getActivity().getPackageName());
                            Log.d("VIEW" ,"view id is table_r" + i + "c1");
                            playerName = view.findViewById(id);
                            id = getResources().getIdentifier("table_r" + i + "c2" , "id", getActivity().getPackageName());
                            Log.d("VIEW" ,"view id is table_r" + i + "c2");
                            playerScore = view.findViewById(id);

                            playerName.setText(user);
                            playerScore.setText(time);

                        }
                        Log.d("GLOB", "userAndScore is " + userAndScore);
                        Log.d("GLOB", "sorted is " + sorted);

                    }

                } else {
                    Log.d("GLOB", "get failed with " + task.getException());
                }

            }


        });

    }

    public void getLocal(String user, String lvl) {


        playerName = view.findViewById(R.id.table_r1c1);
        playerScore = view.findViewById(R.id.table_r1c2);

        DocumentReference docRef = db.collection("High Scores " + lvl).document(user + " Score");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                //resets scoreboard
                for (int i = 1; i <= 9; i++) {
                    Log.d("RESET", "reseting local scoreboard");


                    int id = getResources().getIdentifier("table_r" + i + "c1" , "id", getActivity().getPackageName());
                    //Log.d("VIEW" ,"view id is table_r" + i + "c1");
                    playerName = view.findViewById(id);
                    id = getResources().getIdentifier("table_r" + i + "c2" , "id", getActivity().getPackageName());
                    //Log.d("VIEW" ,"view id is table_r" + i + "c2");
                    playerScore = view.findViewById(id);

                    playerName.setText("");
                    playerScore.setText("");

                }
                playerName = view.findViewById(R.id.table_r1c1);
                playerScore = view.findViewById(R.id.table_r1c2);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DB", "setting user and score");
                        userAndScore = document.getData();
                        Log.d("DB", "DocumentSnapshot data: " + document.getData());


                        String time = (String) userAndScore.get(user);
                        playerName.setText(user);
                        playerScore.setText(time);

                    } else {
                        playerName.setText(user);
                        playerScore.setText("----");
                        Log.d("DB", "no such document");
                    }
                } else {
                    Log.d("DB", "get failed with " + task.getException());
                }
            }
        });


    }


    public static Map<String, Object> sortTimes (Map<String, Object> unsorted) {
        List<Map.Entry<String, Object>> list = new LinkedList<Map.Entry<String, Object>>(unsorted.entrySet());


        Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> a, Map.Entry<String, Object> b) {
                String time1 = (String) a.getValue();
                String[] s = time1.split(":");
                double min = Double.parseDouble(s[0]);
                double sec = Double.parseDouble(s[1]);
                double time1Doub = (min * 60) + sec;

                String time2 = (String) b.getValue();
                s = time2.split(":");
                min = Double.parseDouble(s[0]);
                sec = Double.parseDouble(s[1]);
                double time2Doub = (min * 60) + sec;

                return Double.compare(time1Doub, time2Doub);
            }
        });

        Map<String, Object> sorted = new LinkedHashMap<String, Object>();
        for (Map.Entry<String, Object> entry : list) {
            sorted.put(entry.getKey(), entry.getValue());
        }
        return sorted;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 1; i <= 9; i++) {
            int id = getResources().getIdentifier("table_r" + i + "c1" , "id", getActivity().getPackageName());
            Log.d("ONN", "view id id " + "table_r" + i + "c1");
            playerName = view.findViewById(id);
            String p = (String) playerName.getText();
            id = getResources().getIdentifier("table_r" + i + "c2" , "id", getActivity().getPackageName());
            playerScore = view.findViewById(id);
            String s = (String) playerScore.getText();

            outState.putString("player" + i, p);
            outState.putString("score" + i, s);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("ON", "onActivityCreated called");
        super.onActivityCreated(savedInstanceState);
        }
    }








