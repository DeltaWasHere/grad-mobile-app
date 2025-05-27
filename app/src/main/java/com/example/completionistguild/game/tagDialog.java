package com.example.completionistguild.game;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.completionistguild.R;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.model.modelTags;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tagDialog extends AppCompatDialogFragment {
    private Spinner spinner;
    private int rate;
    private String gameId;
private EditText link;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.rateDialog));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_tag, null);
link = view.findViewById(R.id.link);
        builder.setView(view).
                setTitle("Inserte el link a enviar")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://10.0.2.2:3000/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        Calls apicalls = retrofit.create(Calls.class);

                        SharedPreferences authPreferences = getActivity().getSharedPreferences("credentials", MODE_PRIVATE);

                        Map<String, String> headers = new HashMap<>();

                        headers.put("gameId", getArguments().getString("gameId"));
                        headers.put("userId", getActivity().getSharedPreferences("credentials", MODE_PRIVATE).getString("userId", null));
                        headers.put("achievementId", getArguments().getString("achievementId"));
                        headers.put("type", "dlc");
                        headers.put("tag", link.getText().toString());
                        Call<modelTags> call = apicalls.tags(headers, "add");

                        call.enqueue(new Callback<modelTags>() {
                            @Override
                            public void onResponse(Call<modelTags> call, Response<modelTags> response) {
                                Toast.makeText(getActivity(), "link sended", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<modelTags> call, Throwable t) {
                                Toast.makeText(getActivity(), "An error ocurred while sending link", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });


        return builder.create();
    }


}
