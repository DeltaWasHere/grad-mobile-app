package com.example.completionistguild.trade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.completionistguild.R;
import com.example.completionistguild.adapters.adapterTrade;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.exclusion.strategy2;
import com.example.completionistguild.model.modelTrade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fragmentTradeHistory extends Fragment {
    private RecyclerView recyclerOffers, recyclerTrades;

    public fragmentTradeHistory() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trade_history, container, false);

        recyclerOffers = view.findViewById(R.id.recyclerOffers);
        recyclerTrades = view.findViewById(R.id.recyclerTrades);

        Gson gsonBuilder = new GsonBuilder()
                .setExclusionStrategies(new strategy2())
                .serializeSpecialFloatingPointValues()
                .serializeNulls()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .build();


        Calls apicalls = retrofit.create(Calls.class);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);

        Map<String, String> headers = new HashMap<>();
        headers.put("userId", sharedPreferences.getString("userId", "null"));

        Call<ArrayList<modelTrade>> call = apicalls.getTrade(headers, "tradeHistory");

        call.enqueue(new Callback<ArrayList<modelTrade>>() {
            @Override
            public void onResponse(Call<ArrayList<modelTrade>> call, Response<ArrayList<modelTrade>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("error: " + response.body());
                }
                System.out.println(response.body());
                ArrayList<modelTrade> tradesPublished = new ArrayList<>();
                ArrayList<modelTrade> offers = new ArrayList<>();
                for (modelTrade element : response.body()) {
                    if (element.destinedId == null) {
                        tradesPublished.add(element);
                    } else {
                        offers.add(element);
                    }
                }
                adapterTrade adapterTrades = new adapterTrade(tradesPublished, 2);
                adapterTrade adapterOffers = new adapterTrade(offers, 4);
                LinearLayoutManager llm1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                LinearLayoutManager llm2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

                adapterTrades.setOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = recyclerTrades.getChildAdapterPosition(view);
                        if (tradesPublished.get(pos).getAcceptedId() != null) {

                        } else {
                            Intent i = new Intent(getContext(), tradeInfo.class);

                            i.putExtra("cover", tradesPublished.get(pos).getFront());
                            i.putExtra("name", tradesPublished.get(pos).getUserName());
                            i.putExtra("avatar", tradesPublished.get(pos).getAvatar());
                            i.putExtra("rate", tradesPublished.get(pos).getTradeRate());
                            i.putExtra("gameTradeDate", tradesPublished.get(pos).getDate());
                            i.putExtra("tradeId", tradesPublished.get(pos).getTradeId());
                            i.putExtra("offerId", tradesPublished.get(pos).getOfferId());
                            i.putExtra("media", tradesPublished.get(pos).getMedia());
                            String interestedGames = new Gson().toJson(tradesPublished.get(pos).interestedGames);
                            i.putExtra("interestedGames", interestedGames);
                            i.putExtra("mode", 1);
                            startActivity(i);
                        }
                    }
                });
                adapterOffers.setOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), tradeInfo.class);
                        int pos = recyclerOffers.getChildAdapterPosition(view);
                        i.putExtra("cover", offers.get(pos).getFront());
                        i.putExtra("name", offers.get(pos).getUserName());
                        i.putExtra("avatar", offers.get(pos).getAvatar());
                        i.putExtra("rate", offers.get(pos).getTradeRate());
                        i.putExtra("gameTradeDate", offers.get(pos).getDate());
                        i.putExtra("tradeId", offers.get(pos).getTradeId());
                        i.putExtra("offerId", offers.get(pos).getOfferId());
                        i.putExtra("media", offers.get(pos).getMedia());
                        i.putExtra("acceptedId", offers.get(pos).getAcceptedId());
                        String interestedGames = new Gson().toJson(offers.get(pos).interestedGames);
                        i.putExtra("interestedGames", interestedGames);
                        i.putExtra("mode", 0);

                        startActivity(i);
                    }
                });
                recyclerTrades.setLayoutManager(llm1);
                recyclerOffers.setLayoutManager(llm2);
                recyclerTrades.setAdapter(adapterTrades);
                recyclerOffers.setAdapter(adapterOffers);

                adapterTrades.notifyDataSetChanged();
                adapterOffers.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<modelTrade>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        return view;
    }
}