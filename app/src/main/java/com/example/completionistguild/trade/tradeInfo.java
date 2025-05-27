package com.example.completionistguild.trade;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.completionistguild.R;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.model.modelGameInfo;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tradeInfo extends AppCompatActivity implements View.OnClickListener {
    private ImageView gameTradeCover, gameTradeAvatar;
    private ImageButton playButton, addOffer, getKey, addRate;
    private ProgressBar progress;
    private TextView gameTradeAutor, gameTradeDate;
    private RatingBar gameTradeAutorRate;
    private VideoView validationVideo;
    private LinearLayout[] tradeInterestedGames = new LinearLayout[3];
    private static final int[] IDS = {
            R.id.interestedGame1,
            R.id.interestedGame2,
            R.id.interestedGame3
    };
    private Toolbar toolbar;
    private LinearLayout interestedGamesWindow;
    private String tradeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_info);
        Bundle bundle = getIntent().getExtras();
        String cover = bundle.getString("cover", "null");
        String name = bundle.getString("name", "null");
        String avatar = bundle.getString("avatar", "null");
        Float rate = bundle.getFloat("rate", 0);
        Integer date = bundle.getInt("gameTradeDate", 0);
        tradeId = bundle.getString("tradeId", null);
        String offerId = bundle.getString("offerId", null);
        String arrayInterestedGames = bundle.getString("interestedGames");
        String media = bundle.getString("media", null);
        String acceptedId = bundle.getString("acceptedId", null);
        int mode = bundle.getInt("mode", 0);


        gameTradeCover = findViewById(R.id.tradeGameIcon);
        gameTradeAutor = findViewById(R.id.tradeInfoUserName);
        gameTradeAvatar = findViewById(R.id.tradeInfoAvatar);
        gameTradeDate = findViewById(R.id.tradeInfoDate);
        gameTradeAutorRate = findViewById(R.id.tradeInfoRate);
        validationVideo = findViewById(R.id.validationArchieve);
        progress = findViewById(R.id.tradeInfoValidationProgress);
        playButton = findViewById(R.id.tradeInfo_playButton);
        addOffer = findViewById(R.id.offerSend);
        getKey = findViewById(R.id.getKey);
        addRate = findViewById(R.id.addRate);
        interestedGamesWindow = findViewById(R.id.interestedGames);
        toolbar = findViewById(R.id.toolbarTradeInfo);
        setSupportActionBar(toolbar);

        //trade accepted = getKey option available
        Toast.makeText(this, "ur Offer id: " + offerId + "the acceptedId is: " + acceptedId, Toast.LENGTH_SHORT).show();
        if (offerId != null && offerId.equals(acceptedId)) {//my offersis the same accepted id as the offer acceptedId
            addRate.setVisibility(View.VISIBLE);
            addRate.setOnClickListener(this);
            getKey.setVisibility(View.VISIBLE);
            getKey.setOnClickListener(this);
            addOffer.setVisibility(View.GONE);
        }
//0 mode dafault view published trade with option
//1 mode for ur trade, no offer option available


        if (mode == 0) {
            List<modelGameInfo> listInterestedGames = Arrays.asList(new Gson().fromJson(arrayInterestedGames, modelGameInfo[].class));

            modelGameInfo[] interestedGames = new modelGameInfo[listInterestedGames.size()];
            interestedGames = listInterestedGames.toArray(interestedGames);

            for (int i = 0; i < interestedGames.length; i++) {
                tradeInterestedGames[i] = findViewById(IDS[i]);
                tradeInterestedGames[i].setVisibility(View.VISIBLE);
                Glide.
                        with(this).
                        load(interestedGames[i].getFront()).
                        into((ImageView) tradeInterestedGames[i].findViewById(R.id.GameResultImage));
                TextView genresTv = ((TextView) tradeInterestedGames[i].findViewById(R.id.GameResultGenres));
                for (String genres : interestedGames[i].getGenres()) {
                    genresTv.setText(genresTv.getText().toString() + genres);
                }
                ((TextView) tradeInterestedGames[i].findViewById(R.id.GameResultValoration)).setText(String.valueOf(interestedGames[i].getRate()));
                ((TextView) tradeInterestedGames[i].findViewById(R.id.GameResultTime)).setText(String.valueOf(interestedGames[i].getCompletion_time()));
                tradeInterestedGames[i].setOnClickListener(this);
            }
        }

        if (mode == 1) {
            addOffer.setVisibility(View.GONE);
        }
//2nd mode offer view so no interested games and add offer available
        if (mode == 2) {
            addOffer.setVisibility(View.GONE);
            interestedGamesWindow.setVisibility(View.GONE);
        }


        Glide.with(this).
                load(cover).
                into(gameTradeCover);
        Glide.with(this).
                load(avatar).
                into(gameTradeAvatar);
        gameTradeAutor.setText(name);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        gameTradeDate.setText(dateFormat.format(new Date(date * 1000L)));
        gameTradeAutorRate.setRating(rate);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playButton.setVisibility(View.GONE);
                validationVideo.setVisibility(View.VISIBLE);
                validationVideo.start();
            }
        });
        addOffer.setOnClickListener(this);
        if (!(media == null) && !media.equals("NULL")) {
            progress.setVisibility(View.VISIBLE);
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();


            Calls apicalls = retrofit.create(Calls.class);
            Map<String, String> body = new HashMap<>();
            body.put("media", media);
            final String fileName = (media.split("\\\\")[1]);

            Call<ResponseBody> call = apicalls.getMedia(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()) {
                        System.out.println("Response not succesfull: " + response);
                        return;
                    }
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            String writtenToDisk = writeResponseBodyToDisk(response.body(), fileName);

                            if (!writtenToDisk.equals("false")) {
                                System.out.println("SUCCESSS");
                                System.out.println(writtenToDisk);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.setVisibility(View.GONE);
                                        playButton.setVisibility(View.VISIBLE);
                                        MediaController mediaController = new MediaController(tradeInfo.this);
                                        mediaController.setAnchorView(validationVideo);
                                        validationVideo.setMediaController(mediaController);

                                        validationVideo.setVideoURI(Uri.parse(writtenToDisk));
                                        //validationVideo.start();

                                    }
                                });


                            } else {
                                System.out.println("AYUDAAAA");
                            }
                            return null;
                        }
                    }.execute();


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("Call response: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Ningun archivo de validacion", Toast.LENGTH_SHORT).show();
        }

    }


    private String writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            // todo change the file location/name according to your needs
            File fileLocation = new File(getExternalFilesDir(null) + File.separator + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(fileLocation);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    System.out.println("file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return fileLocation.getPath();
            } catch (IOException e) {
                return "false";
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return "false";
        }
    }


    @Override
    public void onClick(View view) {
        Bundle args = new Bundle();
        switch (view.getId()) {
            case R.id.interestedGame1:
                break;
            case R.id.interestedGame2:
                break;
            case R.id.interestedGame3:
                break;
            case R.id.offerSend:

                args.putString("transaction", "addOffer");
                args.putString("tradeId", tradeId);
                args.putInt("mode", 1);
                tradeDialog tradeDialog = new tradeDialog();
                tradeDialog.setArguments(args);

                tradeDialog.show(getSupportFragmentManager(), "test");
                break;
            case R.id.addRate:
                args.putString("tradeId", tradeId);
                args.putInt("mode", 1);
                tradeRateDialog tradeRateDialog = new tradeRateDialog();
                tradeRateDialog.setArguments(args);
                tradeRateDialog.show(getSupportFragmentManager(), "test");
                break;
            case R.id.getKey:

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                Calls apicalls = retrofit.create(Calls.class);
                Map<String, String> headers = new HashMap<>();
                headers.put("tradeId", tradeId);
                Call<String> call = apicalls.getKey(headers);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            System.out.println("Response not succesfull");
                        }
                        new AlertDialog.Builder(tradeInfo.this)
                                .setTitle("Game key:")
                                .setMessage(response.body())
                                .setNegativeButton("Ok", null)
                                .show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
System.out.println("Call error: "+ t.getMessage());
                    }
                });


                break;
        }
    }
}