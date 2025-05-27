package com.example.completionistguild.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.completionistguild.R;
import com.example.completionistguild.model.modelLeaderEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class adapterLeaderboard extends RecyclerView.Adapter<adapterLeaderboard.ViewHolderData> {

    private ArrayList<modelLeaderEntry> list;
    private String type;

    public adapterLeaderboard(ArrayList<modelLeaderEntry> list, String type) {
        this.list = list;
        this.type = type;
    }

    @NonNull
    @Override
    public adapterLeaderboard.ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_view_leaderboard, null);
        adapterLeaderboard.ViewHolderData obj = new adapterLeaderboard.ViewHolderData(v);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterLeaderboard.ViewHolderData holder, int position) {
        holder.place.setText(String.valueOf(position + 1));
        holder.name.setText(list.get(position).getName());
        if(type.equals("2")){
            holder.score.setText(String.valueOf(list.get(position).getScore()));
        }else{
            holder.score.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(list.get(position).getScore() * 1000L)));
        }
        Glide.with(holder.itemView.getContext()).
                load(list.get(position).avatar).
                into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        TextView name, place, score;
        ImageView avatar;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.leaderboardAvatar);
            name = itemView.findViewById(R.id.leaderboardName);
            place = itemView.findViewById(R.id.leaderboardPlace);
            score = itemView.findViewById(R.id.leaderboardScore);
        }
    }
}
