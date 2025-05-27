package com.example.completionistguild.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.completionistguild.R;
import com.example.completionistguild.adapters.guidesAdapter;
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

public class achievementGuidesFragment extends Fragment {
    private RecyclerView recyclerGuides;
    private Map<String, String> headers;
    private View writeGuideButton;

    public achievementGuidesFragment(Map<String, String> headers, View writeGuideButton) {
        this.headers = headers;
        this.writeGuideButton = writeGuideButton;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_achievement_guides, container, false);

        recyclerGuides = view.findViewById(R.id.recyclerGuides);
        System.out.println("recived: "+headers.get("gameId"));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Calls apicalls = retrofit.create(Calls.class);
        LinearLayoutManager linearLayoutManagerGuides = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        Map<String, String> hmm = new HashMap<>();

        Call<ArrayList<modelGuides>> call = apicalls.guides(headers, hmm, "read");
        call.enqueue(new Callback<ArrayList<modelGuides>>() {
            @Override
            public void onResponse(Call<ArrayList<modelGuides>> call, Response<ArrayList<modelGuides>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Error de repsuesta: " + response, Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<modelGuides> guides = response.body();
                guidesAdapter guidesAdapter = new guidesAdapter(false, guides);
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
                String auth = sharedPreferences.getString("userId", null);

                writeGuideButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (auth != null) {
                            Toast.makeText(getContext(), "AAAAAAAAA", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getContext(), guideInfo.class);
                            i.putExtra("mode", "write");
                            i.putExtra("userId", auth);
                            for (modelGuides guide : guides) {
                                if (guide.getUserId().equals(auth)) {
                                    Toast.makeText(getContext(), "user has a guide", Toast.LENGTH_SHORT).show();
                                    i.putExtra("mode", "write");
                                    i.putExtra("info", guide.getContent());
                                    i.putExtra("userId", auth);
                                    i.putExtra("guideId", guide.getGuideId());


                                    break;
                                }
                            }
                            Bundle bundle = new Bundle();
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                bundle.putString(entry.getKey(), entry.getValue());
                            }
                            i.putExtras(bundle);
                           startActivity(i);

                        } else {
//u r not auth lmao
                            Toast.makeText(getContext(), "Necesitas estar autenticado para escribir u editar una guia", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                guidesAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean vote = null;
                        switch (view.getId()) {
                            case R.id.guideSmallUpVote:
                                vote = true;
                            case R.id.guideSmallDownVote:
                                if (auth != null) {
                                    //usuario autenticado manda voto
                                    if (vote == null) {
                                        vote = false;
                                    }
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://10.0.2.2:3000/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    Calls apicalls = retrofit.create(Calls.class);

                                    headers.clear();
                                    headers.put("guideId", guides.get(recyclerGuides.getChildAdapterPosition(view)).getGuideId());
                                    headers.put("uderId", auth);

                                    Call<Boolean> call1 = apicalls.vote(headers, vote.toString());
                                    call1.enqueue(new Callback<Boolean>() {
                                        @Override
                                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                            if (response.body() == true) {
                                                Toast.makeText(getContext(), "voto positivo añadido", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "voto negativo añadido", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Boolean> call, Throwable t) {
                                            Toast.makeText(getContext(), "No se pudo establecer contacto con el servidor", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(getContext(), "necesitas autenticarte para poder votar por guias", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.guideSmallAvatar:

                            case R.id.guideSmallAutor:
                                //redirect to autor profile
                                Toast.makeText(getContext(), "guideSmallAutor", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.guideSmallDate:
                                Toast.makeText(getContext(), "guideSmallDate", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.guideSmallVotes:
                                Toast.makeText(getContext(), "guideSmallVotes", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Intent i = new Intent(getContext(), guideInfo.class);
                                TextView guideInfo = (TextView) view.findViewById(R.id.guideSmallInfo);

                                i.putExtra("mode", "read");
                                i.putExtra("info", guideInfo.getText());

                                startActivity(i);
                                break;

                        }
                    }
                });
                recyclerGuides.setLayoutManager(linearLayoutManagerGuides);
                recyclerGuides.setAdapter(guidesAdapter);
                guidesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<modelGuides>> call, Throwable t) {
                System.out.println("Fail" + t.getMessage());

            }
        });

        return view;
    }


}