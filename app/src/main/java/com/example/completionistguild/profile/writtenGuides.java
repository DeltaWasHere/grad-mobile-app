package com.example.completionistguild.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.completionistguild.R;
import com.example.completionistguild.adapters.guidesAdapter;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.game.guideInfo;
import com.example.completionistguild.model.modelGameInfo;
import com.example.completionistguild.model.modelGuides;
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

public class writtenGuides extends AppCompatActivity {
    private RecyclerView recyclerGuides;
private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_written_guides);
        recyclerGuides = findViewById(R.id.recyclerWrittenGuides);
        toolbar = findViewById(R.id.toolbarDefault);
        setSupportActionBar(toolbar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Calls apicalls = retrofit.create(Calls.class);
        LinearLayoutManager linearLayoutManagerGuides = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        Bundle extras = getIntent().getExtras();
        String userId = extras.getString("userId", "null");


        Map<String, String> headers = new HashMap<>();
        headers.put("userId", userId);
        Call<JsonElement> call = apicalls.profile(headers, "guidesWrited");
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                System.out.println("ERROR REPSUESTA: "+ response);
                    return;
                }
                ArrayList<modelGuides> guides = new Gson().fromJson(((JsonArray)response.body()), new TypeToken<List<modelGuides>>(){}.getType());
                //ArrayList<modelGuides> guides = (ArrayList<modelGuides>) response.body();
                guidesAdapter guidesAdapter = new guidesAdapter(false, guides);
                System.out.println("guides: "+guides);
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
                String auth = sharedPreferences.getString("userId", null);

                guidesAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean vote = null;
                        switch (view.getId()) {
                            case R.id.guideSmallUpVote:
                                vote = true;
                            case R.id.guideSmallDownVote:
                                if (auth != null) {
                                    //usuario autenticado manda voto
                                    if (vote == null) {
                                        vote = false;
                                    }
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://10.0.2.2:3000/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    Calls apicalls = retrofit.create(Calls.class);

                                    headers.clear();
                                    headers.put("guideId", guides.get(recyclerGuides.getChildAdapterPosition(view)).getGuideId());
                                    headers.put("userId", auth);

                                    Call<Boolean> call1 = apicalls.vote(headers, vote.toString());
                                    call1.enqueue(new Callback<Boolean>() {
                                        @Override
                                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                            if (response.body() == true) {
                                                Toast.makeText(getApplicationContext(), "voto positivo añadido", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "voto negativo añadido", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Boolean> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), "No se pudo establecer contacto con el servidor", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(getApplicationContext(), "necesitas autenticarte para poder votar por guias", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.guideSmallAvatar:

                            case R.id.guideSmallAutor:
                                //redirect to autor profile
                                Toast.makeText(getApplicationContext(), "guideSmallAutor", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.guideSmallDate:
                                Toast.makeText(getApplicationContext(), "guideSmallDate", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.guideSmallVotes:
                                Toast.makeText(getApplicationContext(), "guideSmallVotes", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Intent i = new Intent(getApplicationContext(), guideInfo.class);
                                TextView guideInfo = (TextView) view.findViewById(R.id.guideSmallInfo);

                                i.putExtra("mode", "read");
                                i.putExtra("info", guideInfo.getText());

                                startActivity(i);
                                break;

                        }
                    }
                });
                recyclerGuides.setLayoutManager(linearLayoutManagerGuides);
                recyclerGuides.setAdapter(guidesAdapter);
                guidesAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println("Fail" + t.getMessage());

            }
        });

    }
}