package com.example.completionistguild.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.completionistguild.R;


public class achievementInfoFragment extends Fragment {
    String name, description, achievementId, gameId;
    Float rarity;
    TextView achievementName, achievementDescription, achievementRarity;
    Button achievementLeaderboard;

    public achievementInfoFragment(String name, String description, Float rarity, String gameId, String achievementId) {
        this.name = name;
        this.description = (description == "null") ? "hidden description" : description;
        this.rarity = rarity;
        this.gameId = gameId;
        this.achievementId = achievementId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievement_info, container, false);
        achievementName = view.findViewById(R.id.achievementName);
        achievementDescription = view.findViewById(R.id.achievementDesctiption);
        achievementRarity = view.findViewById(R.id.achievementRarity);
        achievementLeaderboard = view.findViewById(R.id.achievementLeaderboard);

        achievementName.setText(achievementName.getText() + name);
        achievementDescription.setText(achievementDescription.getText() + description);
        achievementRarity.setText(achievementRarity.getText() + rarity.toString());
        achievementLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), leaderboardView.class);
                i.putExtra("gameId", gameId);
                i.putExtra("achievementId", achievementId);
                i.putExtra("type", "0");
                startActivity(i);
            }
        });
        return view;
    }
}