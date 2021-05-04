package com.example.matchappproject;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class EasyGameFragment extends Fragment  {
    ImageButton[][] buttonGrid;
    String[][] cardFaces;
    int row = 6;
    ArrayList<String> cardSpaces = new ArrayList<>();
    int col = 3;
    boolean cardAlreadyFlipped = false;
    String cardAlreadyFlippedID = "";

    ImageButton imgBtn1;
    ImageButton imgBtn2;
    String imgID1;
    String imgID2;
    int cardFlippedOverCounter = 0;
    boolean start = false;
    Chronometer chronometer;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_game_easy, container, false);
        buttonGrid = new ImageButton[row][col];
        cardFaces = new String[row][col];
        Chronometer chronometer = view.findViewById(R.id.chronometer_timer);

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                String buttonID = "button" + r + c;
                int id = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());

                if (Objects.isNull(view.findViewById(id))) {
                    Log.d("NULL", "btdID found wrong");
                }
                buttonGrid[r][c] = view.findViewById(id);

                String roww = Integer.toString(r);
                String coll = Integer.toString(c);

                cardSpaces.add(roww + coll);
            }
        }



        //since there are 18 total cards
        int ind = 0;
        for (int c = 0; c < 18; c++) {
            int lengthLeft = 18 - c;
            System.out.println("lengthleft is " + lengthLeft);
            Random rand = new Random();
            int randSpace = rand.nextInt(lengthLeft);
            Log.d("RANDOM", "rand space in arraylist is" + randSpace);
            int randCard = rand.nextInt(13) + 1;

            String s = cardSpaces.get(randSpace);
            Log.d("RANDOM", "cardspace is" + s);
            char c0 = s.charAt(0);
            char c1 = s.charAt(1);
            int randRow = Integer.parseInt(String.valueOf(c0));
            int randCol = Integer.parseInt(String.valueOf(c1));
            Log.d("RANDOM", "rand row is" + randRow);
            Log.d("RANDOM", "rand col is" + randCol);
            Log.d("POP", "populating cardFaces at " + randRow + " " + randCol);
            cardFaces[randRow][randCol] = "ic_card" + randCard;

            //space is taken up
            int index = cardSpaces.indexOf(s);
            cardSpaces.remove(index);

            //need this repetitive code so there are 2 of each card to match
            c++;
            lengthLeft = 18 - c;
            System.out.println("lengthleft is " + lengthLeft);
            randSpace = rand.nextInt(lengthLeft);
            s = cardSpaces.get(randSpace);
            c0 = s.charAt(0);
            c1 = s.charAt(1);
            randRow = Integer.parseInt(String.valueOf(c0));
            randCol = Integer.parseInt(String.valueOf(c1));
            Log.d("POP", "populating cardFaces at " + randRow + " " + randCol);
            cardFaces[randRow][randCol] = "ic_card" + randCard;

            index = cardSpaces.indexOf(s);
            cardSpaces.remove(index);


            ind++;
        }




        //populates buttonGrid with buttons
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {

                int finalR = r;
                int finalC = c;
                ImageButton im =  buttonGrid[r][c];
                if (Objects.isNull(im)) {
                    Log.d("NULL", "imgBtn not populated right");
                }
                buttonGrid[r][c].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int imageID1 = 0;
                        int imageID2;

                        if (!cardAlreadyFlipped) {
                            if (start == false) {
                                start = true;
                                chronometer.start();
                            }
                            cardAlreadyFlippedID = cardFaces[finalR][finalC];
                            //set button image to be face of card
                            imgID1 = cardFaces[finalR][finalC];
                            Log.d("ID", "image id1 is " + imgID1);
                            imageID1 = getResources().getIdentifier(imgID1 , "mipmap", getActivity().getPackageName());
                            imgBtn1 = buttonGrid[finalR][finalC];
                            imgBtn1.setImageResource(imageID1);
                            cardAlreadyFlipped = true;

                        } else {
                            imgID2 = cardFaces[finalR][finalC];
                            Log.d("ID", "image id2 is " + imgID2);
                            imageID2 = getResources().getIdentifier(imgID2 , "mipmap", getActivity().getPackageName());
                            imgBtn2 = buttonGrid[finalR][finalC];
                            imgBtn2.setImageResource(imageID2);

                            if (!imgID1.equals(imgID2)) {
                                Log.d("CARD", imgID1 + " =/= " + imgID2);
                                Log.d("CARD", "CARDS DONT MATCH");
                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        imgBtn1.setImageResource(R.mipmap.ic_card);
                                        imgBtn2.setImageResource(R.mipmap.ic_card);

                                    }
                                }, 500);


                            } else {
                                cardFlippedOverCounter += 2;
                            }
                            Log.d("COUNT", "cards flipped over is " + cardFlippedOverCounter);

                            //all cards flipped over
                            if (cardFlippedOverCounter >= 18) {
                                chronometer.stop();
                            }

                            cardAlreadyFlipped = false;

                        }
                    }
                });
            }
        }

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
