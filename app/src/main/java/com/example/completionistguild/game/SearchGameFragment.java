package com.example.completionistguild.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.completionistguild.Global;
import com.example.completionistguild.R;
import com.example.completionistguild.adapters.adapterResultGames;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.model.modelGameInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchGameFragment extends Fragment implements SearchView.OnQueryTextListener {

    SearchView searchgamebar;
    RecyclerView RecyclerGameResults;
    adapterResultGames AdapterResultGames;

    public SearchGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search_game, container, false);
        searchgamebar = view.findViewById(R.id.SearchGameBar);
        RecyclerGameResults = view.findViewById(R.id.RecyclerGameResults);

        AdapterResultGames = new adapterResultGames();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        RecyclerGameResults.setLayoutManager(gridLayoutManager);


        AdapterResultGames.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), GameView.class);
                i.putExtra("gameAppid", Global.GameResultsList.get(RecyclerGameResults.getChildAdapterPosition(view)).getGameId());
                i.putExtra("gameTitle", Global.GameResultsList.get(RecyclerGameResults.getChildAdapterPosition(view)).getName());
                i.putExtra("gameRate", Global.GameResultsList.get(RecyclerGameResults.getChildAdapterPosition(view)).getRate());
                i.putExtra("gameTime", Global.GameResultsList.get(RecyclerGameResults.getChildAdapterPosition(view)).getCompletion_time());
                i.putExtra("gameGenres", Global.GameResultsList.get(RecyclerGameResults.getChildAdapterPosition(view)).getGenres());
                i.putExtra("cover", Global.GameResultsList.get(RecyclerGameResults.getChildAdapterPosition(view)).getFront());
                startActivity(i);
            }
        });

        RecyclerGameResults.setAdapter(AdapterResultGames);
        searchgamebar.setOnQueryTextListener(this);
        return view;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Calls apicalls = retrofit.create(Calls.class);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("qryParams", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);

        Map<String , String> headers = new HashMap<>();
        headers.put("platform", sharedPreferences.getString("platform", "steam"));

        Call<ArrayList<modelGameInfo>> call = apicalls.getGames(headers, query);

        call.enqueue(new Callback<ArrayList<modelGameInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<modelGameInfo>> call, Response<ArrayList<modelGameInfo>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Error de repsuesta: " + response, Toast.LENGTH_SHORT).show();
                    return;
                }
                Global.GameResultsList = response.body();
                AdapterResultGames.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<modelGameInfo>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}