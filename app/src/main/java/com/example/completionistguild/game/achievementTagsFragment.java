package com.example.completionistguild.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.completionistguild.R;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.model.modelDlcTag;
import com.example.completionistguild.model.modelTag;
import com.example.completionistguild.model.modelTags;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class achievementTagsFragment extends Fragment implements View.OnClickListener {
    TextView tags, tagsDlc;
    ImageButton addTag, addTagDlc;
    Spinner spinnerTags;
    Map<String, String> headers;
    SharedPreferences preferences;

    public achievementTagsFragment(Map<String, String> headers) {
        this.headers = headers;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievement_tags, container, false);

        tags = view.findViewById(R.id.tags);
        addTag = view.findViewById(R.id.add_tag);
        tagsDlc = view.findViewById(R.id.tags_dlc);
        addTagDlc = view.findViewById(R.id.add_tag_dlc);
        spinnerTags = view.findViewById(R.id.tag_spinner);
        preferences = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);

        addTag.setOnClickListener(this);
        addTagDlc.setOnClickListener(this);

        if (preferences.getString("userId", null) != null) {
            spinnerTags.setVisibility(View.VISIBLE);
            addTag.setVisibility(View.VISIBLE);
            addTagDlc.setVisibility(View.VISIBLE);
            headers.put("userId", preferences.getString("userId", null));
        }

        String[] tagsArray = {"Historia", "Diicultad", "Grindeo", "Roto", "Perdible", "Fecha especifica"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, tagsArray);
        spinnerTags.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Calls apicalls = retrofit.create(Calls.class);
        Call<modelTags> call = apicalls.tags(headers, "read");
        call.enqueue(new Callback<modelTags>() {
            @Override
            public void onResponse(Call<modelTags> call, Response<modelTags> response) {
                //System.out.println(response.body());
                int pos = 0;
                for (modelTag element : response.body().getTags()) {
                    if (pos == 0) {
                        tags.setText(element.getTag());
                        pos++;
                    } else {
                        tags.setText(tags.getText() + ", " + element.getTag());
                    }
                }

                for (modelDlcTag element : response.body().getDlcTags()) {
                    tagsDlc.setText(element.getLink());
                    Linkify.addLinks(tagsDlc, Linkify.WEB_URLS);
                }

            }

            @Override
            public void onFailure(Call<modelTags> call, Throwable t) {
                Toast.makeText(getActivity(), "Cant get tags", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Calls apicalls = retrofit.create(Calls.class);


        switch (view.getId()) {

            case R.id.add_tag:
                headers.put("type", "tag");
                headers.put("tag", spinnerTags.getSelectedItem().toString());
                Call<modelTags> call = apicalls.tags(headers, "add");
                call.enqueue(new Callback<modelTags>() {
                    @Override
                    public void onResponse(Call<modelTags> call, Response<modelTags> response) {
                        Toast.makeText(getActivity(), "Tag añadida", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<modelTags> call, Throwable t) {
                        Toast.makeText(getActivity(), "No se pudo añadir la tag", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.add_tag_dlc:
                Bundle args = new Bundle();
                args.putString("gameId",headers.get("gameId"));
                args.putString("achievementId",headers.get("achievementId"));
                tagDialog tagDialog = new tagDialog();
                tagDialog.setArguments(args);

                tagDialog.show(getActivity().getSupportFragmentManager(), "test");
                break;
        }
    }
}