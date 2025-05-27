package com.example.completionistguild;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class AssetsFragment extends Fragment {

    Spinner language, platform;

    public AssetsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = getActivity().getSharedPreferences("qryParams", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        View view = inflater.inflate(R.layout.fragment_assets, container, false);
        language = view.findViewById(R.id.spinnerLanguage);
        platform = view.findViewById(R.id.apinnerPlatform);

        ArrayAdapter<CharSequence> adapterSpinerLanguages = ArrayAdapter.createFromResource(getContext(), R.array.arrayLanguages, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterSpinerPlatforms = ArrayAdapter.createFromResource(getContext(), R.array.arrayPlatforms, android.R.layout.simple_spinner_item);

        adapterSpinerPlatforms.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSpinerLanguages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        language.setAdapter(adapterSpinerLanguages);
        platform.setAdapter(adapterSpinerPlatforms);

        String presetLang = preferences.getString("language", null);
        String presetPlatform = preferences.getString("platform", null);

        if (presetLang != null) {
            int spinnerPosLang = adapterSpinerLanguages.getPosition(presetLang);
            language.setSelection(spinnerPosLang);
            Toast.makeText(getContext(), "Lenguaje presret:" + presetLang, Toast.LENGTH_SHORT).show();
        }

        if (presetPlatform != null) {
            int spinnerPosPlatform = adapterSpinerPlatforms.getPosition(presetPlatform);
            platform.setSelection(spinnerPosPlatform);
            Toast.makeText(getContext(), "platform preset :" + presetPlatform, Toast.LENGTH_SHORT).show();
        }

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), "Idioma: " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                if(adapterView.getItemAtPosition(i).toString().equals("español")){
                    editor.putString("language", "espanol");
                }else{
                    editor.putString("language", adapterView.getItemAtPosition(i).toString());
                }

                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        platform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), "Plataforma: " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                editor.putString("platform", adapterView.getItemAtPosition(i).toString());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}