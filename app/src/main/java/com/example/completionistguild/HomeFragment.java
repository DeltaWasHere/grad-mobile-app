package com.example.completionistguild;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.completionistguild.game.SearchGameFragment;
import com.example.completionistguild.game.leaderboardView;
import com.example.completionistguild.profile.SearchProfileFragment;
import com.example.completionistguild.profile.profileView;
import com.example.completionistguild.thread.threadHistory;
import com.example.completionistguild.trade.tradeView;


public class HomeFragment extends Fragment implements View.OnClickListener {

    public HomeFragment() {
        // Required empty public constructor
    }

    View view;
    Button profileButton, globalScoreButton, searchGameButton, searchUserButton, tradeButton, reportButton, settingsButton, logOutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        profileButton = view.findViewById(R.id.profileButton);
        globalScoreButton = view.findViewById(R.id.globalScoreButton);
        searchGameButton = view.findViewById(R.id.searchGameButton);
        searchUserButton = view.findViewById(R.id.searchUserButton);
        tradeButton = view.findViewById(R.id.tradeButton);
        reportButton = view.findViewById(R.id.reportButton);
        settingsButton = view.findViewById(R.id.settingsButton);
        logOutButton = view.findViewById(R.id.logOutButton);

        profileButton.setOnClickListener(this);
        globalScoreButton.setOnClickListener(this);
        searchGameButton.setOnClickListener(this);
        searchUserButton.setOnClickListener(this);
        tradeButton.setOnClickListener(this);
        reportButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        logOutButton.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Intent i;
        switch (view.getId()) {
            case R.id.profileButton:
                i = new Intent(getContext(), profileView.class);
                startActivity(i);
                break;

            case R.id.globalScoreButton:
i = new Intent (getContext(), leaderboardView.class);
i.putExtra("type", "2");
getContext().startActivity(i);

                break;

            case R.id.searchGameButton:
                Global.GameResultsList.clear();
                ft.replace(R.id.content, new SearchGameFragment()).commit();

                break;

            case R.id.searchUserButton:
                ft.replace(R.id.content, new SearchProfileFragment()).commit();
                break;

            case R.id.tradeButton:
                i = new Intent(getContext(), tradeView.class);
                startActivity(i);
                break;

            case R.id.reportButton:
i = new Intent(getContext(), threadHistory.class);
startActivity(i);
                break;

            case R.id.settingsButton:
                ft.replace(R.id.content, new AssetsFragment()).commit();
                break;

            case R.id.logOutButton:
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("credentials", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                i = new Intent(getContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
        }
    }
}