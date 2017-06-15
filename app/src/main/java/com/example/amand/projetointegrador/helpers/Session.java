package com.example.amand.projetointegrador.helpers;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.amand.projetointegrador.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;
    

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public void setUserPrefs(Long userId) {

        editor.putString("usuario", userId.toString());
        editor.commit();
    }

    public void setUserName(String name) {

        editor.putString("nome", name);
        editor.commit();
    }

    public void setUserEmail(String email) {

        editor.putString("email", email);
        editor.commit();
    }

    public void setUserImg(String img) {

        editor.putString("img", img);
        editor.commit();
    }

    public void setToken(String token) {
        editor.putString("authToken", token);
        editor.commit();

    }

    public String getToken() {

        return prefs.getString("authToken", null);
    }

    public Long getUserPrefs() {

        String userId = prefs.getString("usuario", null);

        return Long.parseLong(userId);
    }

    public String getUserName() {

        String userName = prefs.getString("nome", null);

        return userName;
    }

    public String getUserEmail() {

        String userEmail = prefs.getString("email", null);

        return userEmail;
    }

    public String getUserImg() {

        String userImg = prefs.getString("img", null);

        return userImg;
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }
}