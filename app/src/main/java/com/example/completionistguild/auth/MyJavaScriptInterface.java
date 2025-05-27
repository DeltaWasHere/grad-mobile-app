package com.example.completionistguild.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;

import com.example.completionistguild.MainActivity;
import com.example.completionistguild.model.UserAuthData;
import com.google.gson.Gson;

class MyJavaScriptInterface {

    private Context ctx;

    MyJavaScriptInterface(Context ctx) {
        this.ctx = ctx;
    }

    @JavascriptInterface
    public void showHTML(String html) {
        Gson g = new Gson();
        System.out.println(html);
        UserAuthData data = g.fromJson(html, UserAuthData.class);

        SharedPreferences preferences = ctx.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("userId", data.getUserId()).commit();
        editor.putString("avatar", data.getAvatar()).commit();
        editor.putString("name", data.getName()).commit();

        Intent i = new Intent(ctx, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(i);
        //System.out.println("userId: "+data.getUserId()+ " avatar: "+ data.getAvatar() + " name: "+data.getName());
    }
}