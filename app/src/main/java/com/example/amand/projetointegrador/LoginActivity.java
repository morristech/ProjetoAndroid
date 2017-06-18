package com.example.amand.projetointegrador;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.amand.projetointegrador.helpers.Session;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private Button registerView;
    private Button forgotView;

    private EditText loginEmail;
    private EditText loginPass;
    private Button btnLogin;
    Session session;

    private String token;
    String id;
    String name;
    String email;
    String birthday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginFace = (Button) findViewById(R.id.facebookLogin);

        callbackManager = CallbackManager.Factory.create();

        session = new Session(this);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        //Login Button
        btnLogin = (Button) findViewById(R.id.email_sign_in_button);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEmailView.getText().toString().isEmpty()) {
                    mEmailView.setError("Campo obrigatório");
                    return;
                }

                if (mPasswordView.getText().toString().isEmpty()) {
                    mPasswordView.setError("Campo obrigatório");
                    return;
                }

                LoginService loginService = new LoginService();

                loginService.execute(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        registerView = (Button) findViewById(R.id.registerBtn);

        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(i);
            }
        });

        List<String> permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile", "AccessToken");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");

                final String accessToken = loginResult.getAccessToken()
                        .getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                Log.i("LoginActivity",
                                        response.toString());
                                try {
                                    id = object.getString("id");
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + id + "/picture?type=large");
                                        Log.i("profile_pic",
                                                profile_pic + "");

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    name = object.getString("name");
                                    email = object.getString("email");
                                    birthday = object.getString("birthday");
                                    Long dataNasc = Long.valueOf(birthday);
                                    Date data = new Date(dataNasc);

                                    JSONObject o = new JSONObject();
                                    o.put("nome", name);
                                    o.put("email", email);
                                    o.put("authToken", accessToken);
                                    o.put("dataNascimento", data);
                                    o.put("senha", null);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


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

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    public void onClick(View v) {
        if (v == loginFace) {
            loginButton.performClick();
        }
    }

    private class LoginService extends AsyncTask<String, Void, String> {

        private String
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/usuario/login";

        @Override
        protected String doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(webAdd);
            HttpResponse resposta;
            String systemRes = "";

            try {

                List<NameValuePair> parametros = new ArrayList<>(2);
                parametros.add(new BasicNameValuePair("email", params[0]));
                parametros.add(new BasicNameValuePair("senha", params[1]));

                token = new String(Base64.encode((params[0] + ":" + params[1]).getBytes(), Base64.NO_WRAP));
                chamada.setHeader("Authorization", "Basic " + token);

                chamada.setEntity(new UrlEncodedFormEntity(parametros));
                resposta = cliente.execute(chamada);
                try {
                    systemRes = EntityUtils.toString(resposta.getEntity(), StandardCharsets.UTF_8);
                } catch (Exception e) {
                    systemRes = "Erro";
                }

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
                    session.setToken(token);


                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {

                    mPasswordView.setError("E-mail ou senha incorretas");
                }

            } else {

                System.out.println(systemRes);

                mPasswordView.setError("E-mail ou senha incorretas");
            }
        }
    }
}

