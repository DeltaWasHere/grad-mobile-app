package com.example.completionistguild.thread;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.completionistguild.R;
import com.example.completionistguild.adapters.adapterThreads;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.model.modelThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class threadHistory extends AppCompatActivity {
    private RecyclerView recyclerThreads;
    private ImageButton addThread;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_history);

        recyclerThreads = findViewById(R.id.recyclerThreads);
        toolbar = findViewById(R.id.toolbarThread);
        addThread = toolbar.findViewById(R.id.toolbarThreadAdd);

        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);

        String userId = sharedPreferences.getString("userId", null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Calls apicalls = retrofit.create(Calls.class);

        Map<String, String> headers = new HashMap<>();
        headers.put("userId", userId);


        Call<ArrayList<modelThread>> call = apicalls.getThreads(headers);

        call.enqueue(new Callback<ArrayList<modelThread>>() {
            @Override
            public void onResponse(Call<ArrayList<modelThread>> call, Response<ArrayList<modelThread>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(threadHistory.this, "Response error", Toast.LENGTH_SHORT).show();
                }
                ArrayList<modelThread> threads;
                threads = response.body();
                System.out.println(threads);
                adapterThreads adapter = new adapterThreads(threads);

                adapter.setOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(threadHistory.this, threadView.class);
                        i.putExtra("mode", 0); //watch
                        i.putExtra("issue", threads.get(recyclerThreads.getChildAdapterPosition(view)).getIssue());
                        i.putExtra("title", threads.get(recyclerThreads.getChildAdapterPosition(view)).getTitle());
                        i.putExtra("content", threads.get(recyclerThreads.getChildAdapterPosition(view)).getContent());
                        i.putExtra("media", threads.get(recyclerThreads.getChildAdapterPosition(view)).getMedia());
                        i.putExtra("response", threads.get(recyclerThreads.getChildAdapterPosition(view)).getResponse());
                        startActivity(i);
                    }
                });

                LinearLayoutManager llm = new LinearLayoutManager(threadHistory.this, LinearLayoutManager.VERTICAL, false);

                recyclerThreads.setLayoutManager(llm);
                recyclerThreads.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<ArrayList<modelThread>> call, Throwable t) {
                Toast.makeText(threadHistory.this, "Call error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        addThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(threadHistory.this, threadView.class);
                i.putExtra("mode", 1);
                startActivity(i);
            }
        });
    }
}