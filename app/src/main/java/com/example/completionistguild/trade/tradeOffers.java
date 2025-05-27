package com.example.completionistguild.trade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.completionistguild.R;
import com.example.completionistguild.adapters.adapterTrade;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.model.modelTrade;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tradeOffers extends AppCompatActivity {
private RecyclerView recyclerOffers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_offers);

        recyclerOffers = findViewById(R.id.recyclerTradeOffers);

        Bundle extras = getIntent().getExtras();
        String tradeId = extras.getString("tradeId");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Calls apicalls = retrofit.create(Calls.class);

        Map<String, String> headers = new HashMap<>();
        headers.put("tradeId", tradeId);

        Call<ArrayList<modelTrade>> call = apicalls.getTrade(headers, "tradesOffered");

        call.enqueue(new Callback<ArrayList<modelTrade>>() {
            @Override
            public void onResponse(Call<ArrayList<modelTrade>> call, Response<ArrayList<modelTrade>> response) {
                if (!(response.isSuccessful())) {
                    System.out.println("Respuesta no exitosa: " + response);
                }
                ArrayList<modelTrade> offers = response.body();
                adapterTrade adapter = new adapterTrade(offers, 3, tradeId);
                adapter.setOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                                Intent i = new Intent(tradeOffers.this, tradeInfo.class);
                                i.putExtra("mode", 2);
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
                                startActivity(i);
                    }
                });
                LinearLayoutManager ll = new LinearLayoutManager(tradeOffers.this, LinearLayoutManager.VERTICAL, false);
                recyclerOffers.setLayoutManager(ll);
                recyclerOffers.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<modelTrade>> call, Throwable t) {
                System.out.println("error: " + t.getMessage());
            }
        });
    }
}