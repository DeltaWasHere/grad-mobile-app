package com.example.completionistguild.game;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.completionistguild.R;
import com.example.completionistguild.adapters.adapterLeaderboard;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.model.modelLeaderEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class leaderboardView extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_view);
        recyclerView = findViewById(R.id.leaderboard);

        Bundle extras = getIntent().getExtras();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Calls calls = retrofit.create(Calls.class);

        SharedPreferences sharedPreferences = getSharedPreferences("qryParams", MODE_PRIVATE);

        String gameId = extras.getString("gameId", "null");
        String achievementId = extras.getString("achievementId", "null");
        String type = extras.getString("type", "null");
        String platform = sharedPreferences.getString("platform", "steam");

        Map<String, String> headers = new HashMap<>();

        headers.put("gameId", gameId);
        headers.put("achievementId", achievementId);
        headers.put("platform", platform);
        Toast.makeText(this, achievementId, Toast.LENGTH_SHORT).show();
        Call<ArrayList<modelLeaderEntry>> call = calls.getLeader(headers, type);
        call.enqueue(new Callback<ArrayList<modelLeaderEntry>>() {
            @Override
            public void onResponse(Call<ArrayList<modelLeaderEntry>> call, Response<ArrayList<modelLeaderEntry>> response) {
                adapterLeaderboard adapter = new adapterLeaderboard(response.body(), type);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(leaderboardView.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<modelLeaderEntry>> call, Throwable t) {
                Toast.makeText(leaderboardView.this, "No se pudo recuperar la tabla de puntuaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}