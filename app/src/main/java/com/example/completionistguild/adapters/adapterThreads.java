package com.example.completionistguild.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.completionistguild.R;
import com.example.completionistguild.model.modelThread;

import java.util.ArrayList;

public class adapterThreads extends RecyclerView.Adapter<adapterThreads.viewHolder> implements View.OnClickListener {
    private ArrayList<modelThread> list;
    private View.OnClickListener listener;


    public adapterThreads(ArrayList<modelThread> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_view_thread, parent, false);
        v.setOnClickListener(this);
        adapterThreads.viewHolder obj = new adapterThreads.viewHolder(v);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int i) {
        holder.title.setText(list.get(i).getTitle());
        holder.issue.setText(list.get(i).getIssue());
        if (list.get(i).getResponse() != null) {
            holder.check.setImageResource(R.drawable.ic_thread_check);
        }
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
        private TextView issue, title;
        private ImageView check;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            issue = itemView.findViewById(R.id.itemThreadIssue);
            title = itemView.findViewById(R.id.itemThreadTitle);
            check = itemView.findViewById(R.id.itemThreadCheck);

        }
    }
}