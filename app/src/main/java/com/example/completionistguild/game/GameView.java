package com.example.completionistguild.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.completionistguild.Global;
import com.example.completionistguild.R;
import com.example.completionistguild.adapters.adapterAchievements;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.model.modelAchievement;
import com.example.completionistguild.model.modelGameView;
import com.example.completionistguild.model.modelStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameView extends AppCompatActivity implements View.OnClickListener {
    ImageButton deployGameInfo, retractGameInfo, rateButton, pinButton;
    Button gameViewLeader;
    ImageView gameIcon;
    TextView gameViewTitle, gameViewCompletionTime, gameViewRate, gameViewGenres;
    RecyclerView achievementsTable, unlockedAchievementsTable;
    TableLayout unlockedAchievementsTableLayout;
    Toolbar mToolbar;
    public String gameId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

        gameViewTitle = findViewById(R.id.gameViewTittle);
        gameViewCompletionTime = findViewById(R.id.gameViewCompletionTime);
        gameViewRate = findViewById(R.id.gameViewRate);
        gameViewGenres = findViewById(R.id.gameViewGenres);
        gameIcon = findViewById(R.id.gameIcon);
        gameViewLeader = findViewById(R.id.gameLeaderboard);
        deployGameInfo = findViewById(R.id.deployGameInfo);
        retractGameInfo = findViewById(R.id.retractGameInfo);
        mToolbar = findViewById(R.id.toolbarGameView);
        rateButton = mToolbar.findViewById(R.id.rateButton);
        pinButton = mToolbar.findViewById(R.id.pinButton);
        unlockedAchievementsTableLayout = findViewById(R.id.unlockedAchievementsTableLayout);

        Bundle gameInfo = getIntent().getExtras();
        gameId = gameInfo.getString("gameAppid");
        System.out.println("gid:" + gameInfo.getString("gameAppid"));

        Glide.with(GameView.this).
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

        LinearLayoutManager linearLayoutManagerAchievements = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManagerUnlockedAchievements = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        achievementsTable = findViewById(R.id.achievementsTable);
        unlockedAchievementsTable = findViewById(R.id.unlockedAchievementsTable);
        achievementsTable.setLayoutManager(linearLayoutManagerAchievements);
        unlockedAchievementsTable.setLayoutManager(linearLayoutManagerUnlockedAchievements);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        final Gson gsonBuilder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeSpecialFloatingPointValues()
                .serializeNulls()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .build();

        Calls apicalls = retrofit.create(Calls.class);

        SharedPreferences sharedPreferences = this.getSharedPreferences("qryParams", MODE_PRIVATE);
        SharedPreferences authPreferences = this.getSharedPreferences("credentials", MODE_PRIVATE);
        userId = authPreferences.getString("userId", null);

        if (authPreferences.getString("userId", null) != null) {
            pinButton.setVisibility(View.VISIBLE);
            rateButton.setVisibility(View.VISIBLE);
        }

        Map<String, String> headers = new HashMap<>();

        headers.put("platform", sharedPreferences.getString("platform", "steam"));
        headers.put("language", sharedPreferences.getString("language", "english"));

        String userId = authPreferences.getString("userId", null);
        if (userId != null) {
            headers.put("userid", userId);

        } else {
            headers.put("userid", "0");
        }

        Call<modelGameView> call = apicalls.getAchievements(headers, gameInfo.getString("gameAppid"));
        call.enqueue(new Callback<modelGameView>() {
            @Override
            public void onResponse(Call<modelGameView> call, Response<modelGameView> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(GameView.this, "Error de repsuesta: " + response, Toast.LENGTH_SHORT).show();
                    return;
                }

                modelGameView shit = response.body();
                ArrayList<modelAchievement> aux = new ArrayList<>();
                aux = shit.achievements;
                Global.GameAchievementsList.clear();
                Global.GameUnlockedAchievementsList.clear();
                for (modelAchievement element : aux) {
                    if (element.getAchieved() == 0) {
                        Global.GameAchievementsList.add(element);
                    } else {
                        Global.GameUnlockedAchievementsList.add(element);
                    }
                }

                if (Global.GameUnlockedAchievementsList.size() > 0) {
                    unlockedAchievementsTableLayout.setVisibility(View.VISIBLE);
                }
                adapterAchievements adapterAchievements = new adapterAchievements(false, Global.GameAchievementsList);
                adapterAchievements.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), achievementView.class);
                        i.putExtras(gameInfo);
                        i.putExtra("achievementId", Global.GameAchievementsList.get(achievementsTable.getChildAdapterPosition(view)).getAchievementId());
                        i.putExtra("achievementTitle", Global.GameAchievementsList.get(achievementsTable.getChildAdapterPosition(view)).getName());
                        i.putExtra("achievementDescription", Global.GameAchievementsList.get(achievementsTable.getChildAdapterPosition(view)).getDescription());
                        i.putExtra("achievementRarity", Global.GameAchievementsList.get(achievementsTable.getChildAdapterPosition(view)).getRarity());
                        startActivity(i);
                    }
                });
                adapterAchievements adapterUnlockedAchievements = new adapterAchievements(true, Global.GameUnlockedAchievementsList);
                adapterUnlockedAchievements.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), achievementView.class);
                        i.putExtras(gameInfo);
                        i.putExtra("achievementId", Global.GameUnlockedAchievementsList.get(achievementsTable.getChildAdapterPosition(view)).getAchievementId());
                        i.putExtra("achievementTitle", Global.GameUnlockedAchievementsList.get(achievementsTable.getChildAdapterPosition(view)).getName());
                        i.putExtra("achievementDescription", Global.GameUnlockedAchievementsList.get(achievementsTable.getChildAdapterPosition(view)).getDescription());
                        i.putExtra("achievementRarity", Global.GameUnlockedAchievementsList.get(achievementsTable.getChildAdapterPosition(view)).getRarity());
                        startActivity(i);
                    }
                });

                achievementsTable.setAdapter(adapterAchievements);
                unlockedAchievementsTable.setAdapter(adapterUnlockedAchievements);

                adapterAchievements.notifyDataSetChanged();
                adapterUnlockedAchievements.notifyDataSetChanged();
                System.out.println("response:" + shit.achievements.get(0).getIcon());
            }

            @Override
            public void onFailure(Call<modelGameView> call, Throwable t) {
                System.out.println("Fail" + t.getMessage());
            }
        });

        retractGameInfo.setOnClickListener(this);
        deployGameInfo.setOnClickListener(this);
        rateButton.setOnClickListener(this);
        pinButton.setOnClickListener(this);
        gameViewLeader.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deployGameInfo:
                gameViewTitle.setVisibility(View.VISIBLE);
                gameViewRate.setVisibility(View.VISIBLE);
                gameViewCompletionTime.setVisibility(View.VISIBLE);
                gameViewGenres.setVisibility(View.VISIBLE);
                deployGameInfo.setVisibility(View.GONE);
                retractGameInfo.setVisibility(View.VISIBLE);
                gameViewLeader.setVisibility(View.VISIBLE);
                //arriba tengo q aparecer todos los elementos  y aparecer el retract
                break;
            case R.id.retractGameInfo:
                gameViewTitle.setVisibility(View.GONE);
                gameViewRate.setVisibility(View.GONE);
                gameViewCompletionTime.setVisibility(View.GONE);
                gameViewGenres.setVisibility(View.GONE);
                retractGameInfo.setVisibility(View.GONE);
                gameViewLeader.setVisibility(View.GONE);
                deployGameInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.rateButton:
                Bundle args = new Bundle();
                args.putString("gameId", gameId);
                rateDialog rateDialog = new rateDialog();
                rateDialog.setArguments(args);

                rateDialog.show(getSupportFragmentManager(), "test");
                System.out.println("attp");
                break;
            case R.id.pinButton:
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Calls apicalls = retrofit.create(Calls.class);
                Map<String, String> headers = new HashMap<>();
                headers.put("gameId", gameId);
                Call<modelStatus> call = apicalls.pin(headers, userId);
                call.enqueue(new Callback<modelStatus>() {
                    @Override
                    public void onResponse(Call<modelStatus> call, Response<modelStatus> response) {
                        modelStatus status = response.body();
                        if (status.getStatus() == true) {
                            Toast.makeText(GameView.this, "Game pinned", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GameView.this, "Game unpinned", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<modelStatus> call, Throwable t) {
                        System.out.println("Pin error");
                    }
                });
                break;
            case R.id.gameLeaderboard:
                Intent i = new Intent(this, leaderboardView.class);
                i.putExtra("gameId", gameId);
                i.putExtra("type", "1");
                startActivity(i);
                break;
        }
    }
}