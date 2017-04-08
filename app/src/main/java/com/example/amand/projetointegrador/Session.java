package com.example.amand.projetointegrador;


import android.content.Context;
import android.content.SharedPreferences;

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

    public void setUserPrefs(String login, String nome, String senha, String ultimoLogin) {
        editor.putString("login", login);
        editor.putString("nome", nome);
        editor.putString("senha", senha);
        editor.putString("ultimoLogin", ultimoLogin);
        editor.commit();
    }

    public String getUserPrefs() {
        String l = prefs.getString("login", null);
        String n = prefs.getString("nome", null);
        String s = prefs.getString("senha", null);
        String ul = prefs.getString("ultimoLogin", null);

        return "Login: "+ l +"\nUsuario: "+ n +"\nSenha: "+ s +"\nUltimo Login: " +ul;
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }
}