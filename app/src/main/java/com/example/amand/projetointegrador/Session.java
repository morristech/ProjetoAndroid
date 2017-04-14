package com.example.amand.projetointegrador;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.amand.projetointegrador.model.Usuario;
import com.google.gson.Gson;

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

    public void setUserPrefs(Usuario user) {

        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("Usuario", json);
        editor.commit();

    }

    public Usuario getUserPrefs() {

        Gson gson = new Gson();
        String json = prefs.getString("Usuario", null);
        return gson.fromJson(json, Usuario.class);

    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }
}