package com.example.completionistguild.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.completionistguild.R;
import com.example.completionistguild.model.modelAchievement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class adapterAchievements extends RecyclerView.Adapter<adapterAchievements.ViewHolderData> implements View.OnClickListener {
    Boolean mode;
    private ArrayList<modelAchievement> list = new ArrayList<>();

    private View.OnClickListener listener;

    public adapterAchievements(Boolean mode, ArrayList list) {
        this.mode = mode;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_view_achievement, null);
        v.setOnClickListener(this);
        adapterAchievements.ViewHolderData obj = new adapterAchievements.ViewHolderData(v);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int pos) {
        Glide.with(holder.itemView.getContext()).
                load(list.get(pos).getIcon()).
                into(holder.icon);
        String description = list.get(pos).getDescription();
        if (description == null) description = "hidden description";
        holder.info.setText(list.get(pos).getName() + "\n" + description);
        holder.tags.setText("sample");
        if (mode == false) {
            holder.rarity.setText(list.get(pos).getRarity().toString());
        } else {
            holder.rarity.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(list.get(pos).getDateAchieved() * 1000L)));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        TextView info, tags, rarity;
        ImageView icon;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.achievementIcon);
            info = itemView.findViewById(R.id.achievementInfo);
            tags = itemView.findViewById(R.id.achievementTags);
            rarity = itemView.findViewById(R.id.achievementRarity);

        }
    }
}
