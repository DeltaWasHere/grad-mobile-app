package com.example.completionistguild.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.completionistguild.R;
import com.example.completionistguild.model.modelGuides;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class guidesAdapter extends RecyclerView.Adapter<guidesAdapter.ViewHolderData> implements View.OnClickListener {
    private View.OnClickListener listener;
    Boolean mode;
    private ArrayList<modelGuides> list = new ArrayList<>();
    public guidesAdapter(Boolean mode, ArrayList list) {
        this.mode = mode;
        this.list = list;
    }

    @NonNull
    @Override
    public guidesAdapter.ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_view_guides, null);
        v.setOnClickListener(this);
        guidesAdapter.ViewHolderData obj = new guidesAdapter.ViewHolderData(v);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull guidesAdapter.ViewHolderData holder, int position) {
        Glide.with(holder.itemView.getContext()).
                load(list.get(position).getAvatar()).
                into(holder.avatar);
        holder.autor.setText(holder.autor.getText() + list.get(position).getName());
        holder.date.setText(holder.date.getText() + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(list.get(position).getDate() * 1000L)));
        holder.votes.setText(holder.votes.getText() + String.valueOf(list.get(position).getVotes()));
        holder.content.setText(list.get(position).getContent());
        holder.upVote.setOnClickListener(this);
        holder.downVote.setOnClickListener(this);
        holder.avatar.setOnClickListener(this);
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
        TextView autor, date, content, votes;
        ImageButton upVote, downVote;
        ImageView avatar;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            votes = itemView.findViewById(R.id.guideSmallVotes);
            avatar = itemView.findViewById(R.id.guideSmallAvatar);
            autor = itemView.findViewById(R.id.guideSmallAutor);
            date = itemView.findViewById(R.id.guideSmallDate);
            content = itemView.findViewById(R.id.guideSmallInfo);
            upVote = itemView.findViewById(R.id.guideSmallUpVote);
            downVote = itemView.findViewById(R.id.guideSmallDownVote);
        }
    }
}
