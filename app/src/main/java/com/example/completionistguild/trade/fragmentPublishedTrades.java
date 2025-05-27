package com.example.completionistguild.trade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.completionistguild.R;
import com.example.completionistguild.adapters.adapterTrade;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.exclusion.strategy1;
import com.example.completionistguild.model.modelGameInfo;
import com.example.completionistguild.model.modelTrade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class fragmentPublishedTrades extends Fragment implements SearchView.OnQueryTextListener {
    private SearchView searchTrade;
    private RecyclerView recyclerPublishedTrades;
    private ScrollView tradeTable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_published_trades, container, false);
        recyclerPublishedTrades = view.findViewById(R.id.recyclerPublishedTrade);
        searchTrade = view.findViewById(R.id.searchPublishedTrade);
        tradeTable = view.findViewById(R.id.tradeTable);
        searchTrade.setOnQueryTextListener(this);
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.isEmpty()) {
            Toast.makeText(getContext(), "Este Campo no puede estar vacio", Toast.LENGTH_SHORT).show();
            return false;
        }
        tradeTable.setVisibility(View.VISIBLE);

        Gson gsonBuilder= new GsonBuilder()
                .setExclusionStrategies(new strategy1())
                .serializeSpecialFloatingPointValues()
                .serializeNulls()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .build();

        Calls apicalls = retrofit.create(Calls.class);



        Map<String, String> headers = new HashMap<>();
        headers.put("gameId", query);

        Call<ArrayList<modelTrade>> call = apicalls.getTrade(headers, "searchTrades");

        call.enqueue(new Callback<ArrayList<modelTrade>>() {
            @Override
            public void onResponse(Call<ArrayList<modelTrade>> call, Response<ArrayList<modelTrade>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("error: " + response.body());
                }
                ArrayList<modelTrade> trades = response.body();
                adapterTrade adapter = new adapterTrade(trades, 1);

                LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerPublishedTrades.setLayoutManager(llm);
                recyclerPublishedTrades.setAdapter(adapter);
                adapter.setOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), tradeInfo.class);
                        int pos = recyclerPublishedTrades.getChildAdapterPosition(view);
                        i.putExtra("cover", trades.get(pos).getFront());
                        i.putExtra("name", trades.get(pos).getUserName());
                        i.putExtra("avatar", trades.get(pos).getAvatar());
                        i.putExtra("rate", trades.get(pos).getTradeRate());
                        i.putExtra("gameTradeDate", trades.get(pos).getDate());
                        i.putExtra("tradeId", trades.get(pos).getTradeId());
                        i.putExtra("offerId",trades.get(pos).getOfferId());
                        i.putExtra("media", trades.get(pos).getMedia());
                        String interestedGames = new Gson().toJson(trades.get(pos).interestedGames);
                        i.putExtra("interestedGames", interestedGames);

                        startActivity(i);
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<modelTrade>> call, Throwable t) {
                System.out.println("Call error: "+t.getMessage());
            }
        });


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}