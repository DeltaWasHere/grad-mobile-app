package com.example.completionistguild.game;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.completionistguild.R;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.model.modelGuides;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class guideInfo extends AppCompatActivity {

    private EditText guideInfo;
    private Toolbar toolbar;
    private ImageView sendGuide;
    private String transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_info);

        Bundle extras = getIntent().getExtras();
        String mode, content, guideId, userId;

        guideId =  extras.getString("guideId", "null");
        mode =  extras.getString("mode", "null");
        content =  extras.getString("info", null);
        userId = extras.getString("userId", "null");


        guideInfo = findViewById(R.id.guideInfo);
        toolbar = findViewById(R.id.guideToolbar);
        sendGuide = toolbar.findViewById(R.id.sendButton);
        System.out.println("guide info gameID: "+ extras.getString("gameId", null));

        //verificar si el modo es escritura
        if (mode.equals("write")) {
            sendGuide.setVisibility(View.VISIBLE);
            guideInfo.setEnabled(true);
            if (content == null) { //si el contenido es nulo significa que la transaccion es "crear"
                transaction = "add";
            } else {
                transaction = "edit";
                guideInfo.setText(content);
            }

        } else {
            guideInfo.setText(content);

        }

        sendGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guideInfo.getText().toString().trim().isEmpty()) {
                    Toast.makeText(guideInfo.this, "La guia no puede estar vacia", Toast.LENGTH_SHORT).show();
                } else if (guideInfo.getText().toString().trim().split("\\s+").length < 10) {
                    Toast.makeText(guideInfo.this, "La guia no puede tener menos de 10 palabras", Toast.LENGTH_SHORT).show();
                } else if (guideInfo.getText().toString().trim().split("\\s+").length > 300) {
                    Toast.makeText(guideInfo.this, "La guia no puede tener mas de 500 palabras", Toast.LENGTH_SHORT).show();
                } else {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://10.0.2.2:3000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Calls apicalls = retrofit.create(Calls.class);

                    Map<String, String> headers = new HashMap<>();
                    headers.put("guideId", guideId);
                    headers.put("userId", userId);
                    headers.put("achievementId", extras.getString("achievementId"));
                    headers.put("gameId", extras.getString("gameId"));

                    Map<String, String> hmm = new HashMap<>();
                    hmm.put("content", guideInfo.getText().toString());

                    Call<ArrayList<modelGuides>> call = apicalls.guides(headers, hmm, transaction);

                    call.enqueue(new Callback<ArrayList<modelGuides>>() {
                        @Override
                        public void onResponse(Call<ArrayList<modelGuides>> call, Response<ArrayList<modelGuides>> response) {
                            Toast.makeText(guideInfo.this, "Guia enviada exitosamente para revision", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<modelGuides>> call, Throwable t) {
                            Toast.makeText(guideInfo.this, "No se pudo establecer conexion con el servidor", Toast.LENGTH_SHORT).show();
                            System.out.println(t.getMessage());

                        }
                    });
                }
            }
        });


    }
}