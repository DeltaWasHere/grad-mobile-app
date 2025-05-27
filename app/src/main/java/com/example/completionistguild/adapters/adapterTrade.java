package com.example.completionistguild.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.completionistguild.R;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.model.modelTrade;
import com.example.completionistguild.trade.tradeInfo;
import com.example.completionistguild.trade.tradeOffers;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class adapterTrade extends RecyclerView.Adapter<adapterTrade.viewHolder> implements View.OnClickListener {
    private ArrayList<modelTrade> list;
    private int mode;
    private View.OnClickListener listener;
    private String tradeId;

    public adapterTrade(ArrayList<modelTrade> list, int mode) {
        this.list = list;
        this.mode = mode;
    }

    public adapterTrade(ArrayList<modelTrade> list, int mode, String tradeId) {
        this.list = list;
        this.mode = mode;
        this.tradeId = tradeId;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_view_trade, null);
        v.setOnClickListener(this);
        adapterTrade.viewHolder obj = new adapterTrade.viewHolder(v);

        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int i) {
        Glide.with(holder.tradeAvatar.getContext())
                .load(list.get(i).getAvatar())
                .into(holder.tradeAvatar);


        if (!(list.get(i).getMedia() == null)) {
            holder.tradeValidation.setImageResource(R.drawable.ic_check);
        }

//publishedtrades, mypublishedtrades, myoffers
        if (mode == 1 || mode == 2 || mode == 4) {
            holder.tradeRestriction.setText((list.get(i).getRestriction()) == 1 ? "none" : (list.get(i).getRestriction() == 2 ? "moderate" : "strict"));
        }
        if (mode == 1 || mode == 3 || mode == 4) {
            holder.tradeUserName.setText(list.get(i).getUserName());
            holder.tradeRate.setRating(list.get(i).getTradeRate());
        }
        if (mode == 4) {
            Glide.with(holder.tradeAvatar.getContext())
                    .load(list.get(i).getFront())
                    .into(holder.tradeAvatar);
        }
        if (mode == 2) {
            holder.tradeuserInfo.setVisibility(View.GONE);

            holder.tradeNumbers.setVisibility(View.VISIBLE);

            holder.tradeNumbers.setText(list.get(i).getTradeNumbers());
            Glide.with(holder.tradeAvatar.getContext())
                    .load(list.get(i).getFront())
                    .into(holder.tradeAvatar);
        }
        if (mode == 3) {
            holder.tradeRestriction.setVisibility(View.GONE);
            holder.tradeOptions.setVisibility(View.VISIBLE);

        }
        holder.tradeNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (list.get(pos).getAcceptedId()!=null) {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://10.0.2.2:3000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Calls apicalls = retrofit.create(Calls.class);

                    Map<String, String> headers = new HashMap<>();
                    headers.put("tradeId", list.get(pos).getTradeId());

                    Call<ArrayList<modelTrade>> call1 = apicalls.getTrade(headers, "tradesOffered");
                    call1.enqueue(new Callback<ArrayList<modelTrade>>() {
                        @Override
                        public void onResponse(Call<ArrayList<modelTrade>> call, Response<ArrayList<modelTrade>> response) {
                            if (!(response.isSuccessful())) {
                                System.out.println("Respuesta no exitosa: " + response);
                            }
                            ArrayList<modelTrade> myOffer = response.body();
                            Intent i = new Intent(view.getContext(), tradeInfo.class);
                            i.putExtra("mode", 2);
                            int myPos = 0;
                            i.putExtra("cover", myOffer.get(myPos).getFront());
                            i.putExtra("name", myOffer.get(myPos).getUserName());
                            i.putExtra("avatar", myOffer.get(myPos).getAvatar());
                            i.putExtra("rate", myOffer.get(myPos).getTradeRate());
                            i.putExtra("gameTradeDate", myOffer.get(myPos).getDate());
                            i.putExtra("tradeId", myOffer.get(myPos).getTradeId());
                            i.putExtra("offerId", myOffer.get(myPos).getOfferId());
                            i.putExtra("media", myOffer.get(myPos).getMedia());
                            i.putExtra("acceptedId", myOffer.get(myPos).getAcceptedId());
                            String interestedGames = new Gson().toJson(myOffer.get(myPos).interestedGames);
                            i.putExtra("interestedGames", interestedGames);
                            view.getContext().startActivity(i);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<modelTrade>> call, Throwable t) {
                            System.out.println("error: " + t.getMessage());
                        }
                    });
                } else {

                    Intent i = new Intent(view.getContext(), tradeOffers.class);
                    i.putExtra("tradeId", list.get(pos).getTradeId());
                    view.getContext().startActivity(i);
                    Toast.makeText(view.getContext(), "AAa:" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.tradeAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerTransaction(1, holder, view.getContext());
            }
        });
        holder.tradeCancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerTransaction(0, holder, view.getContext());
            }
        });

    }

    public void offerTransaction(int transaction, viewHolder holder, Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Calls apicalls = retrofit.create(Calls.class);

        int pos = holder.getAdapterPosition();

        Map<String, String> headers = new HashMap<>();
        System.out.println(tradeId);
        headers.put("tradeId", tradeId);

        Map<String, String> body = new HashMap<>();
        body.put("tradeTransaction", String.valueOf(transaction));
        body.put("destinedId", list.get(pos).getTradeId());
        Call<Boolean> call = apicalls.tradeTransaction(headers, body, "tradeTransaction");
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "response not succesfull: " + response.body(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "offer " + (response.body() ? "accepted" : "declined"), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, tradeOffers.class);
                i.putExtra("tradeId", tradeId);
                context.startActivity(i);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, "Call fail: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public void setOnclickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tradeUserName, tradeRestriction, tradeNumbers;
        ImageView tradeAccept, tradeCancell, tradeValidation, tradeAvatar;
        LinearLayout tradeOptions, tradeuserInfo;
        RatingBar tradeRate;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tradeAvatar = itemView.findViewById(R.id.tradeAvatar);
            tradeUserName = itemView.findViewById(R.id.tradeUser);
            tradeRestriction = itemView.findViewById(R.id.tradeRestriction);
            tradeNumbers = itemView.findViewById(R.id.tradeNumbers);
            tradeValidation = itemView.findViewById(R.id.tradeValidation);
            tradeRate = itemView.findViewById(R.id.tradeRating);
            tradeAccept = itemView.findViewById(R.id.tradeAccept);
            tradeCancell = itemView.findViewById(R.id.tradeCancel);
            tradeOptions = itemView.findViewById(R.id.tradeOptions);
            tradeuserInfo = itemView.findViewById(R.id.tradeUserInfo);

        }
    }
}
