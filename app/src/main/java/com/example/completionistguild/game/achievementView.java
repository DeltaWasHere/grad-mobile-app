package com.example.completionistguild.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.completionistguild.R;

import java.util.HashMap;
import java.util.Map;

public class achievementView extends AppCompatActivity implements View.OnClickListener {
    ImageView gameIcon;
    TextView gameViewTitle, gameViewCompletionTime, gameViewRate, gameViewGenres;
    Button infoButton, tagsButton, guidesButton;
    ImageButton writeGuideButton;
    Bundle gameInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_view);
        gameViewTitle = findViewById(R.id.gameViewTittle);
        gameViewCompletionTime = findViewById(R.id.gameViewCompletionTime);
        gameViewRate = findViewById(R.id.gameViewRate);
        gameViewGenres = findViewById(R.id.gameViewGenres);
        gameIcon = findViewById(R.id.gameIcon);
        infoButton = findViewById(R.id.infoButton);
        tagsButton = findViewById(R.id.tagsButton);
        guidesButton = findViewById(R.id.guidesButton);
        writeGuideButton = findViewById(R.id.editGuide);

         gameInfo = getIntent().getExtras();
        System.out.println("gid: "+ gameInfo.getString("gameAppid"));


        Glide.with(achievementView.this).
                load(gameInfo.getString("cover")).
                into(gameIcon);
        gameViewTitle.setText(gameViewTitle.getText().toString() + gameInfo.getString("gameTitle"));
        gameViewRate.setText(gameViewRate.getText().toString() + gameInfo.getInt("gameRate"));
        gameViewCompletionTime.setText(gameViewCompletionTime.getText().toString() + gameInfo.getFloat("gameTime"));
        String[] genres = gameInfo.getStringArray("gameGenres");

         for (int i = 0; i < genres.length; i++) {
            if (i == 0) {
                gameViewGenres.setText(gameViewGenres.getText().toString() + genres[i]);

            } else {
                gameViewGenres.setText(gameViewGenres.getText().toString() + ", " + genres[i]);
            }

        }

        infoButton.setOnClickListener(this);
        tagsButton.setOnClickListener(this);
        guidesButton.setOnClickListener(this);
        writeGuideButton.setOnClickListener(this);

        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.achievementContent, new achievementInfoFragment(gameInfo.getString("achievementTitle", "null"),
                gameInfo.getString("achievementDescription", "null"), gameInfo.getFloat("achievementRarity", 0.11F), gameInfo.getString("gameAppid", "null"), gameInfo.getString("achievementId", null))).commit();


    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Map<String, String> map = new HashMap<>();
        switch (view.getId()){
            case R.id.infoButton:
                ft.replace(R.id.achievementContent, new achievementInfoFragment(gameInfo.getString("achievementTitle", "null"),
                        gameInfo.getString("achievementDescription", "null"), gameInfo.getFloat("achievementRarity", 0.11F), gameInfo.getString("gameAppid", "null"), gameInfo.getString("achievementId", null))).commit();
                writeGuideButton.setVisibility(View.GONE);
                break;
            case R.id.tagsButton:

                map.put("gameId", gameInfo.getString("gameAppid", "null"));
                map.put("achievementId", gameInfo.getString("achievementId", "null"));
                ft.replace(R.id.achievementContent, new achievementTagsFragment(map)).commit();
                writeGuideButton.setVisibility(View.GONE);
                break;
            case R.id.guidesButton:
                System.out.println("sending gid to g : "+ gameInfo.getString("gameAppid"));
                map.put("gameId", gameInfo.getString("gameAppid", "null"));
                map.put("achievementId", gameInfo.getString("achievementId", "null"));
                ft.replace(R.id.achievementContent, new achievementGuidesFragment(map, writeGuideButton) ).commit();
                writeGuideButton.setVisibility(View.VISIBLE);
                break;


        }
    }

}