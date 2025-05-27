package com.example.completionistguild.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.completionistguild.Global;
import com.example.completionistguild.R;
import com.example.completionistguild.adapters.adapterResultGames;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.game.GameView;
import com.example.completionistguild.model.modelGameInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profileGames extends AppCompatActivity {
private String userId, transaction;
private RecyclerView recyclerGames;
private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_games);

        recyclerGames = findViewById(R.id.recyclerGames);
        toolbar = findViewById(R.id.toolbarDefault);
        setSupportActionBar(toolbar);

        adapterResultGames adapter = new adapterResultGames();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);

        recyclerGames.setLayoutManager(gridLayoutManager);

        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GameView.class);
                i.putExtra("gameAppid", Global.GameResultsList.get(recyclerGames.getChildAdapterPosition(view)).getGameId());
                i.putExtra("gameTitle", Global.GameResultsList.get(recyclerGames.getChildAdapterPosition(view)).getName());
                i.putExtra("gameRate", Global.GameResultsList.get(recyclerGames.getChildAdapterPosition(view)).getRate());
                i.putExtra("gameTime", Global.GameResultsList.get(recyclerGames.getChildAdapterPosition(view)).getCompletion_time());
                i.putExtra("gameGenres", Global.GameResultsList.get(recyclerGames.getChildAdapterPosition(view)).getGenres());
                i.putExtra("cover", Global.GameResultsList.get(recyclerGames.getChildAdapterPosition(view)).getFront());
                startActivity(i);
            }
        });

        recyclerGames.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Calls apicalls = retrofit.create(Calls.class);

        SharedPreferences sharedPreferencesSettings = this.getSharedPreferences("qryParams", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferencesCredentials = this.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        userId = extras.getString("userId", "null");
        transaction = extras.getString("transaction", "null");

        Map<String , String> headers = new HashMap<>();
        headers.put("userId", userId);

        Call<JsonElement> call = apicalls.profile(headers, transaction);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error de repsuesta: " + response, Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<modelGameInfo> games = new Gson().fromJson(((JsonArray)response.body()), new TypeToken<List<modelGameInfo>>(){}.getType());
                //ArrayList<modelGameInfo> games = (ArrayList<modelGameInfo>) response.body();
                Global.GameResultsList = games;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }
}