package com.example.completionistguild.profile  ;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.completionistguild.R;
import com.example.completionistguild.callInterface.Calls;

import androidx.appcompat.widget.SearchView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchProfileFragment extends Fragment implements SearchView.OnQueryTextListener {

SearchView searchUser;

    public SearchProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_profile, container, false);
        searchUser = view.findViewById(R.id.searchProfileBar);

        searchUser.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Calls apicalls = retrofit.create(Calls.class);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("qryParams", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);

        Map<String , String> headers = new HashMap<>();
        headers.put("platform", sharedPreferences.getString("platform", "steam"));
        headers.put("userId", query);

        Call<Boolean> call = apicalls.checkProfile(headers);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(!response.body()){
                    Toast.makeText(getContext(), "No se pudo encontrar el perfil intentando añadir estadisticas", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(getContext(), profileView.class);
                i.putExtra("userId", query);
                getContext().startActivity(i);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("Call Error: "+ t.getMessage());
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}