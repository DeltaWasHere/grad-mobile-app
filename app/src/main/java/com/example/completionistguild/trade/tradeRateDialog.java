package com.example.completionistguild.trade;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.completionistguild.R;
import com.example.completionistguild.callInterface.Calls;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tradeRateDialog extends AppCompatDialogFragment {
    private Spinner spinner;
    private int rate;
    private String gameId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.rateDialog));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rate, null);

        builder.setView(view).
                setTitle("Selec the ratign you want to give")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
dismiss();
                    }
                })
                .setPositiveButton("evaluate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://10.0.2.2:3000/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        Calls apicalls = retrofit.create(Calls.class);

                        SharedPreferences authPreferences = getActivity().getSharedPreferences("credentials", MODE_PRIVATE);

                        Map<String, String> headers = new HashMap<>();

                        headers.put("userid", authPreferences.getString("userId", null));
                        headers.put("tradeId", getArguments().getString("tradeId"));
                        Map<String, String> body = new HashMap<>();
body.put("rate", String.valueOf(rate));


                        Call<Boolean> call = apicalls.tradeTransaction(headers, body, "rate");

                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(!response.isSuccessful()){
                                    System.out.println("response error: "+ response);
                                }
                                Toast.makeText(getContext(), "Rate added", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Toast.makeText(getContext(), "Rate not added", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

        spinner = view.findViewById(R.id.rateSpinner);
        Integer[] numbers = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> adapterSpinnerRates = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, numbers);
        adapterSpinnerRates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinnerRates);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rate = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return builder.create();
    }


}
