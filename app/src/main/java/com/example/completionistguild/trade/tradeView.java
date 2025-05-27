package com.example.completionistguild.trade;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.completionistguild.R;
import com.example.completionistguild.game.rateDialog;

public class tradeView extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout tradeWindow;
    private ImageButton tradeHistory, addTrade, tradeSearch;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_view);

        tradeWindow = findViewById(R.id.tradeWindow);
        tradeHistory = findViewById(R.id.tradeHistory);
        tradeSearch = findViewById(R.id.tradeSearch);
        addTrade = findViewById(R.id.addTrade);
        toolbar = findViewById(R.id.toolbarTrade);

        setSupportActionBar(toolbar);

        tradeHistory.setOnClickListener(this);
        addTrade.setOnClickListener(this);
        tradeSearch.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.tradeWindow, new fragmentPublishedTrades()).commit();
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.tradeHistory:

                ft.replace(R.id.tradeWindow, new fragmentTradeHistory()).commit();
                tradeHistory.setVisibility(View.GONE);
                tradeSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.addTrade:
                Bundle args = new Bundle();
                args.putString("transaction", "addTrade");
                tradeDialog tradeDialog = new tradeDialog();
                tradeDialog.setArguments(args);

                tradeDialog.show(getSupportFragmentManager(), "test");
                break;
            case R.id.tradeSearch:
                ft.replace(R.id.tradeWindow, new fragmentPublishedTrades()).commit();
                tradeHistory.setVisibility(View.VISIBLE);
                tradeSearch.setVisibility(View.GONE);
                break;
        }
    }
}