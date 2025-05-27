package com.example.completionistguild.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.completionistguild.Global;
import com.example.completionistguild.R;

public class adapterResultGames extends RecyclerView.Adapter<adapterResultGames.ViewHolderData> implements View.OnClickListener {
    public Context context;
    private View.OnClickListener listener;

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.recyclergame, null);
        v.setOnClickListener(this);
        ViewHolderData obj = new ViewHolderData(v);

        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        final int pos = position;
        holder.GameHoursResult.setText(String.valueOf(Global.GameResultsList.get(pos).getCompletion_time()));
        holder.GameRateResult.setText(String.valueOf(Global.GameResultsList.get(pos).getRate()));
        String[] Genres = Global.GameResultsList.get(pos).getGenres();
        holder.GameGenresResult.setText("");
        if(Global.GameResultsList.get(pos).getGenres()==null){
            holder.GameGenresResult.setText("No genres");
        }else{
            for (String element : Genres) {
                if (holder.GameGenresResult.getText().toString() == "") {
                    holder.GameGenresResult.setText(element);
                } else {
                    holder.GameGenresResult.setText(holder.GameGenresResult.getText().toString() + ", " + element);
                }
            }
        }

        Glide.with(holder.itemView.getContext()).
                load(Global.GameResultsList.get(pos).getFront()).
                into(holder.GameImageResult);

    }

    @Override
    public int getItemCount() {
        return Global.GameResultsList.size();
    }

    public void setOnclickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        TextView GameHoursResult, GameGenresResult, GameRateResult;
        ImageView GameImageResult;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            GameHoursResult = itemView.findViewById(R.id.GameResultTime);
            GameGenresResult = itemView.findViewById(R.id.GameResultGenres);
            GameRateResult = itemView.findViewById(R.id.GameResultValoration);
            GameImageResult = itemView.findViewById(R.id.GameResultImage);

        }
    }
}
