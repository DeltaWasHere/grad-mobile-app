package com.example.completionistguild.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.completionistguild.R;

public class authPlatform extends AppCompatActivity implements View.OnClickListener {

    ImageView button_steamAuth, button_xboxAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_platform);
        button_steamAuth = findViewById(R.id.button_steamAuth);
        button_xboxAuth = findViewById(R.id.button_xboxAuth);

        button_steamAuth.setOnClickListener(this);
        button_xboxAuth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, AuthWindow.class);
        if (view == button_steamAuth) {
            i.putExtra("url", "http://localhost:3000/steam/auth");
        } else {
            i.putExtra("url", "https://xbl.io/app/auth/8cd2a5fd-60b6-493a-944b-678eb528d32f");
        }
        startActivity(i);
        finish();

    }
}