package com.example.completionistguild.thread;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.completionistguild.R;
import com.example.completionistguild.callInterface.Calls;
import com.example.completionistguild.trade.tradeInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class threadView extends AppCompatActivity implements View.OnClickListener {
    private EditText threadTitle, threadContent;
    private TextView threadResponse, threadFilename, threadResponseWrapper;
    private Spinner threadIssue;
    private Toolbar toolbar;
    private ImageButton threadSend, threadFile, threadAdd;
    private String issueSelected = "bug";
    private String filePath;
    private ProgressBar progressBar;
    private LinearLayout fileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_view);
        Bundle extras = getIntent().getExtras();
        int mode = extras.getInt("mode", 0); //watch
        String issue = extras.getString("issue");
        String title = extras.getString("title");
        String content = extras.getString("content");
        String response = extras.getString("response");
        String media = extras.getString("media", null);


        threadIssue = findViewById(R.id.threadIssue);
        threadTitle = findViewById(R.id.threadTitle);
        threadContent = findViewById(R.id.threadContent);
        threadResponse = findViewById(R.id.threadResponse);
        progressBar = findViewById(R.id.threadProgressBar);
        threadFilename = findViewById(R.id.threadFileName);
        fileInfo = findViewById(R.id.threadFileInfo);
        threadResponseWrapper = findViewById(R.id.threadResponseWrapper);
        toolbar = findViewById(R.id.toolbarThread);
        threadSend = toolbar.findViewById(R.id.toolbarThreadSend);
        threadFile = toolbar.findViewById(R.id.toolbarThreadMedia);
        threadAdd = toolbar.findViewById(R.id.toolbarThreadAdd);
        threadAdd.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        String[] issues = new String[]{"bug", "cheater", "scam"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, issues);

        threadIssue.setAdapter(adapterSpinner);

        threadIssue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                issueSelected = threadIssue.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (mode == 0) {
            threadIssue.setEnabled(false);
            threadIssue.setSelection(adapterSpinner.getPosition(issue));

            threadTitle.setText(title);
            threadTitle.setEnabled(false);

            threadContent.setText(content);
            threadContent.setEnabled(false);

            threadResponse.setText(response);
            threadResponse.setEnabled(false);

            threadResponseWrapper.setVisibility(View.VISIBLE);
        } else {
            threadSend.setVisibility(View.VISIBLE);
            threadFile.setVisibility(View.VISIBLE);
        }

        threadSend.setOnClickListener(this);
        threadFile.setOnClickListener(this);
        if (!(media == null) && !media.equals("NULL")) {
            progressBar.setVisibility(View.VISIBLE);

            SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);

            String userId = sharedPreferences.getString("credentials", null);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Calls apicalls = retrofit.create(Calls.class);

            Map<String, String> body = new HashMap<>();
            body.put("media", media);
            final String fileName = (media.split("\\\\")[1]);
            Call<ResponseBody> call = apicalls.getThreadmedia(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(!response.isSuccessful()){
                        System.out.println("Call error");
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
                                    progressBar.setVisibility(View.GONE);
                                    threadFilename.setText(fileName);
                                    fileInfo.setVisibility(View.VISIBLE);
                                    fileInfo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(writtenToDisk));
                                            i.setType("image/* video/*");
                                            Intent j = Intent.createChooser(i, "Choose an application to open with:");
                                            startActivity(j);
                                        }
                                    });
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
                    System.out.println("eRORR: " + t.getMessage());
                }
            });
        }
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Uri selectedVideo = result.getData().getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};

                Cursor cursor = threadView.this.getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                File file = new File(cursor.getString(columnIndex));
                if (Integer.parseInt(String.valueOf((file.length() / 1024) / 1024)) > 30) {
                    Toast.makeText(threadView.this, "El archivo supera el limite de 30 Mb", Toast.LENGTH_SHORT).show();
                } else {
                    filePath = cursor.getString(columnIndex);
                }
            }
        }
    });

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.toolbarThreadSend) {
            SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);

            String userId = sharedPreferences.getString("userId", null);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            Calls apicalls = retrofit.create(Calls.class);

            Map<String, String> headers = new HashMap<>();
            headers.put("userId", userId);

            Map<String, String> body = new HashMap<>();
            body.put("issue", issueSelected);
            body.put("content", threadContent.getText().toString());
            body.put("title", threadTitle.getText().toString());

            MultipartBody.Part video = null;

            if (filePath != null) {
                File file = new File(filePath);
                RequestBody videoBody = RequestBody.create(MediaType.parse("*/*"), file);
                video = MultipartBody.Part.createFormData("validation", file.getName(), videoBody);
            }

            Call<Boolean>  call = apicalls.addThread(headers, body, video);

            call.enqueue(new Callback<Boolean> () {
                @Override
                public void onResponse(Call<Boolean>  call, Response<Boolean> response) {
                    if (!response.isSuccessful()) {
                        System.out.println("Error in response");
                        return;
                    }
                    if(response.body()){
                        Toast.makeText(threadView.this, "Succesfull added thread", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(threadView.this, threadHistory.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean>  call, Throwable t) {
                    System.out.println("Call error: " + t.getMessage());
                }
            });

        } else {
            Intent chooseFile = new Intent(Intent.ACTION_PICK);
            chooseFile.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(chooseFile);
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

}