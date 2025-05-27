package com.example.completionistguild.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.completionistguild.R;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.game.achievementView;
import com.example.completionistguild.model.modelAchievement;
import com.example.completionistguild.model.modelProfile;
import com.example.completionistguild.thread.threadHistory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profileView extends AppCompatActivity implements View.OnClickListener {

    private TextView writenGuides, pinList, ownedGames, threadsGenerated, profileViewPlace, profileViewScore, profileName;
    private ImageView supporterBadge, profileViewAvatar;
    private Toolbar toolbar;
    private ImageView[] valuableAchievements = new ImageView[5], recentAchievements = new ImageView[5];
    private static final int[] IDS = {
            R.id.recentAchievement1,
            R.id.recentAchievement2,
            R.id.recentAchievement3,
            R.id.recentAchievement4,
            R.id.recentAchievement5,
            R.id.valuableAchievement1,
            R.id.valuableAchievement2,
            R.id.valuableAchievement3,
            R.id.valuableAchievement4,
            R.id.valuableAchievement5
    };
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        profileName = findViewById(R.id.profileViewName);
        profileViewPlace = findViewById(R.id.profileViewPosition);
        profileViewScore = findViewById(R.id.profileViewPoints);
        profileViewAvatar = findViewById(R.id.profileViewAvatar);
        writenGuides = findViewById(R.id.guidesWrited);
        toolbar = findViewById(R.id.toolbarDefault);
        pinList = findViewById(R.id.pinnedGames);
        ownedGames = findViewById(R.id.ownedGames);
        threadsGenerated = findViewById(R.id.threadsGenerated);
        for (int i = 0; i < 5; i++) {
            recentAchievements[i] = findViewById(IDS[i]);
            valuableAchievements[i] = findViewById(IDS[i + 5]);
        }
setSupportActionBar(toolbar);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Calls apicalls = retrofit.create(Calls.class);

        SharedPreferences sharedPreferencesSettings = this.getSharedPreferences("qryParams", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferencesCredentials = this.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId = extras.getString("userId", "null");

        }
        if(userId==null){
            userId = sharedPreferencesCredentials.getString("userId", "null");
            threadsGenerated.setVisibility(View.VISIBLE);
        }



        Map<String, String> headers = new HashMap<>();
        headers.put("platform", sharedPreferencesSettings.getString("platform", "steam"));
        headers.put("language", sharedPreferencesSettings.getString("language", "espanol"));
        headers.put("userId", userId);

        Call<JsonElement> call = apicalls.profile(headers, "preview");

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(profileView.this, "Respuesta no exitosa: " + response.body(), Toast.LENGTH_SHORT).show();
                }

                modelProfile profile = new Gson().fromJson(response.body().getAsJsonObject(), modelProfile.class);
                System.out.println(profile.position);

                profileName.setText(profile.getName());
                profileViewPlace.setText(String.valueOf(profile.getPosition()));
                profileViewScore.setText(String.valueOf(profile.getScore()));
                Glide.with(getApplicationContext())
                        .load(profile.getAvatar())
                        .into(profileViewAvatar);
                for (int i = 0; i < 5; i++) {
                    Glide.with(getApplicationContext())
                            .load(profile.getRecentAchievements()[i].getIcon())
                            .into(recentAchievements[i]);
                    modelAchievement achievement = profile.getRecentAchievements()[i];
                    recentAchievements[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), achievementView.class);
                            intent.putExtra("achievementId", achievement.getAchievementId());
                            intent.putExtra("dateAchieved", achievement.getDateAchieved());
                            intent.putExtra("achieved", achievement.getAchieved());
                            intent.putExtra("icon", achievement.getIcon());
                            intent.putExtra("description", achievement.getDescription());
                            intent.putExtra("name", achievement.getName());
                            intent.putExtra("rarity", achievement.getRarity());
                            intent.putExtra("gameId", achievement.getGameId());
                            startActivity(intent);

                        }
                    });

                    Glide.with(getApplicationContext())
                            .load(profile.getValuableAchievements()[i].getIcon())
                            .into(valuableAchievements[i]);
                    modelAchievement achievement1 = profile.getRecentAchievements()[i];
                    valuableAchievements[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), achievementView.class);
                            intent.putExtra("achievementId", achievement1.getAchievementId());
                            intent.putExtra("dateAchieved", achievement1.getDateAchieved());
                            intent.putExtra("achieved", achievement1.getAchieved());
                            intent.putExtra("icon", achievement1.getIcon());
                            intent.putExtra("description", achievement1.getDescription());
                            intent.putExtra("name", achievement1.getName());
                            intent.putExtra("rarity", achievement1.getRarity());
                            intent.putExtra("gameId", achievement1.getGameId());
                            startActivity(intent);
                        }
                    });
                }




            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(profileView.this, "Call fail: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    ownedGames.setOnClickListener(this);
    writenGuides.setOnClickListener(this);
    pinList.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.ownedGames:
                i = new Intent(this, profileGames.class);
                i.putExtra("userId", userId);
                i.putExtra("transaction", "ownedGames");
            break;
            case R.id.pinnedGames:
                i = new Intent(this, profileGames.class);
                i.putExtra("userId", userId);
                i.putExtra("transaction", "pins");
                break;
            case R.id.guidesWrited:

                i = new Intent(this, writtenGuides.class);
                i.putExtra("userId", userId);
                i.putExtra("transaction", "guidesWrited");
                break;
            case R.id.threadsGenerated:
                i = new Intent(this, threadHistory.class);
                break;

            default:
                i = new Intent(this, profileGames.class);
                break;
        }
        startActivity(i);
    }
}