package com.example.completionistguild.trade;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.completionistguild.R;
import com.example.completionistguild.callInterface.Calls;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tradeDialog extends AppCompatDialogFragment {
    private Spinner restriction;
    private EditText tradeGameId, tradeGameKey, tradeInterestedGameId1, tradeInterestedGameId2, tradeInterestedGameId3;
    TextView tradeValidationName;
    private Button uploadValidation;
    private int restrictionLevel = 1;
    private String gameId, filePath;
    private File videoFile;
    private static final int PICKFILE_RESULT_CODE = 8778;
    private Bitmap bitmap;
    private LinearLayout tradeRestrictions;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.rateDialog));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_trade, null);

        tradeGameId = view.findViewById(R.id.inputGameId);
        tradeGameKey = view.findViewById(R.id.inputGameKey);
        tradeInterestedGameId1 = view.findViewById(R.id.inputInterestedGameId1);
        tradeInterestedGameId2 = view.findViewById(R.id.inputInterestedGameId2);
        tradeInterestedGameId3 = view.findViewById(R.id.inputInterestedGameId3);
        restriction = view.findViewById(R.id.inputGameRestriction);
        tradeValidationName = view.findViewById(R.id.tradeValidationName);
        uploadValidation = view.findViewById(R.id.tradeValidationUpload);
        tradeRestrictions = view.findViewById(R.id.tradeRestrictions);

        String transaction = getArguments().getString("transaction", "null");
        String tradeId = getArguments().getString("tradeId", "null");
        int mode = getArguments().getInt("mode", 0);

        if (mode == 1) {//offer mode
            tradeRestrictions.setVisibility(View.GONE);
        }

        String[] restrictions = new String[]{"ninguno", "moderada", "estricto"};
        ArrayAdapter<String> adapterSpinnerRates = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, restrictions);
        adapterSpinnerRates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restriction.setAdapter(adapterSpinnerRates);

        restriction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                restrictionLevel = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        restriction.setSelection(0);
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Uri selectedVideo = result.getData().getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};

                    Cursor cursor = getContext().getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                    File file = new File(cursor.getString(columnIndex));
                    if (Integer.parseInt(String.valueOf((file.length() / 1024) / 1024)) > 30) {
                        Toast.makeText(getContext(), "El archivo supera el limite de 30 Mb", Toast.LENGTH_SHORT).show();
                    } else {
                        filePath = cursor.getString(columnIndex);
                    }


                }
            }
        });
        uploadValidation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_PICK);
                chooseFile.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(chooseFile);
            }
        });


        builder.setView(view).
                setTitle("Enter the trade data required")
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
                        Map<String, String> body = new HashMap<>();

                        MultipartBody.Part video = null;

                        if (filePath != null) {
                            File file = new File(filePath);
                            RequestBody videoBody = RequestBody.create(MediaType.parse("*/*"), file);
                            video = MultipartBody.Part.createFormData("validation", file.getName(), videoBody);
                        }


                        body.put("userId", authPreferences.getString("userId", null));
                        body.put("gameId", tradeGameId.getText().toString().trim());
                        body.put("restriction", String.valueOf(restrictionLevel));
                        body.put("gameKey", tradeGameKey.getText().toString().trim());
                        body.put("interestedGameId1", tradeInterestedGameId1.getText().toString().trim());
                        body.put("interestedGameId2", tradeInterestedGameId2.getText().toString().trim());
                        body.put("interestedGameId3", tradeInterestedGameId3.getText().toString().trim());

                        body.put("destinedId", tradeId);
                        body.put("date", String.valueOf(System.currentTimeMillis() / 1000));

                        Call<Boolean> call = apicalls.addTrade(headers, body, video, transaction);
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (!response.isSuccessful()) {
                                    System.out.println("response error");
                                }
                                if (response.body()) {
                                    System.out.println("success");
                                } else {
                                    System.out.println("Not success");
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                System.out.println("response error" + t.getMessage());
                            }
                        });


                    }
                });


        return builder.create();
    }


}

