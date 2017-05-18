package com.example.amand.projetointegrador;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class LoginActivity extends Activity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button loginFace;
    private CallbackManager callbackManager;

    private Button registerView;
    private Button forgotView;

    private EditText loginEmail;
    private EditText loginPass;
    private Button btnLogin;
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.getActionBar().hide();

        session = new Session(this);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        //Login Button
        btnLogin = (Button) findViewById(R.id.email_sign_in_button);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginService loginService = new LoginService();

                if(mEmailView.getText().toString().isEmpty()){
                    mEmailView.setError("Campo obrigatório");
                    return;
                }

                if(mPasswordView.getText().toString().isEmpty()) {
                    mPasswordView.setError("Campo obrigatório");
                    return;
                }

                loginService.execute(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        registerView = (Button) findViewById(R.id.registerBtn);

        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this , RegistroActivity.class);
                startActivity(i);
            }
        });

        //setContentView(R.layout.activity_login);

    }

   /* @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }*/

    private class LoginService extends AsyncTask<String, Void, String> {

        private String
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/usuario/login";

        @Override
        protected String doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(webAdd);
            HttpResponse resposta = null;
            String systemRes = "";

            try {

                List<NameValuePair> parametros = new ArrayList<>(2);
                parametros.add(new BasicNameValuePair("email", params[0]));
                parametros.add(new BasicNameValuePair("senha", params[1]));

                chamada.setHeader("Authorization", "Basic " + new String(Base64.encode((params[0]+":"+params[1]).getBytes(), Base64.NO_WRAP)));
                session.setToken(new String (Base64.encode((params[0]+":"+params[1]).getBytes(), Base64.NO_WRAP)));

                chamada.setEntity(new UrlEncodedFormEntity(parametros));
                resposta = cliente.execute(chamada);
                systemRes = EntityUtils.toString(resposta.getEntity());

                System.out.println(resposta.getStatusLine().getStatusCode());
                System.out.println(resposta.getStatusLine().getReasonPhrase());


            } catch (IOException e) {
                e.printStackTrace();
            }

            return systemRes;
        }

        @Override
        protected void onPostExecute(String s) {

            String systemRes = "";

            if (!s.equals("Erro")) {
                //Ok

                try {

                    JSONObject obj = new JSONObject(s);
                    JSONObject perfil = obj.getJSONObject("perfil");

                    session.setUserPrefs(obj.getLong("id"));
                    session.setUserEmail(obj.getString("email"));
                    session.setUserName(obj.getString("nome"));
                    session.setUserImg(perfil.getString("fotoPerfil"));

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {

                    Toast.makeText(LoginActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                }

            } else {

                System.out.println(systemRes);

                Toast.makeText(LoginActivity.this, "Erro", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

