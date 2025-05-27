package com.example.completionistguild;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.completionistguild.auth.authPlatform;
import com.example.completionistguild.game.SearchGameFragment;
import com.example.completionistguild.game.leaderboardView;
import com.example.completionistguild.profile.SearchProfileFragment;
import com.example.completionistguild.profile.profileView;
import com.example.completionistguild.thread.threadHistory;
import com.example.completionistguild.trade.tradeView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ImageButton homeButton, assetsButton, profileImage, logOut;
    Toolbar mToolbar;
    ActionBarDrawerToggle toggle;
    TextView authButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mToolbar = findViewById(R.id.toolbar);
        homeButton = mNavigationView.findViewById(R.id.home);
        authButton = mToolbar.findViewById(R.id.auth);
        assetsButton = mNavigationView.findViewById(R.id.nav_settings);
        profileImage = mToolbar.findViewById(R.id.profileImage);
        logOut = mNavigationView.findViewById(R.id.navLogOut);
        String userId = sharedPreferences.getString("userId", null);
        if (userId == null) {
            authButton.setOnClickListener(this);
        } else {
            authButton.setVisibility(View.GONE);
            profileImage.setVisibility(View.VISIBLE);
            System.out.println(sharedPreferences.getString("avatar", null));
            Glide.with(this).load(sharedPreferences.getString("avatar", null)).into(profileImage);
        }

        SharedPreferences preferences = this.getSharedPreferences("qryParams", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String lang = preferences.getString("language", null);
        String platform = preferences.getString("platform", null);

        if (platform == null) {
            editor.putString("language", "english");
        }

        if (platform == null) {
            editor.putString("platform", "steam");
        }

        homeButton.setOnClickListener(this);
        assetsButton.setOnClickListener(this);
        logOut.setOnClickListener(this);

        setSupportActionBar(mToolbar);

        toggle = setUpDrawerToggle();
        mDrawerLayout.addDrawerListener(toggle);

        mNavigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, new HomeFragment()).commit();
    }

    private ActionBarDrawerToggle setUpDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectItemNav(item);

        return true;
    }

    private void selectItemNav(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Intent i;
        switch (item.getItemId()) {
            case R.id.nav_profile:
                i = new Intent(this,profileView.class);
                startActivity(i);
                break;
            case R.id.nav_leaderboard:
                i = new Intent (this, leaderboardView.class);
                i.putExtra("type", "2");
                startActivity(i);
                break;
            case R.id.nav_search_game:
                Global.GameResultsList.clear();
                ft.replace(R.id.content, new SearchGameFragment()).commit();
                break;
            case R.id.nav_search_user:
                ft.replace(R.id.content, new SearchProfileFragment()).commit();
                break;
            case R.id.nav_trade:
                i = new Intent(this, tradeView.class);
startActivity(i);
                break;
            case R.id.nav_report:
                i = new Intent(this, threadHistory.class);
                startActivity(i);
                break;



        }
        setTitle(item.getTitle());
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.home:
                ft.replace(R.id.content, new HomeFragment()).commit();
                mDrawerLayout.closeDrawers();
                break;

            case R.id.auth:
                doTheauth();
                break;
            case R.id.nav_settings:
                ft.replace(R.id.content, new AssetsFragment()).commit();
                mDrawerLayout.closeDrawers();
                break;

            case R.id.navLogOut:
                SharedPreferences sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;


        }

    }

    private void doTheauth() {
        Intent intent = new Intent(this, authPlatform.class);
        startActivity(intent);
    }
}