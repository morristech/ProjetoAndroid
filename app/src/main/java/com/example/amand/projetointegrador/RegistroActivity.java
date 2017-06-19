package com.example.amand.projetointegrador;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.amand.projetointegrador.helpers.Session;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class RegistroActivity extends AppCompatActivity {

    private EditText emailNovaConta;

    private EditText nomeNovaConta;
    private MaskedEditText dataNascimentoNovaConta;
    private EditText senhaNovaConta;
    private EditText confirmaSenhaNovaConta;
    private CircularProgressButton abrirNovaConta;
    private Session session;
    static Long id;
    private String token;

    public static final String ENDERECO_WEB = "http://31.220.58.131:8080";
    //public static final String ENDERECO_WEB = "http://192.168.25.6:8888";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        session = new Session(this);

        emailNovaConta = (EditText) findViewById(R.id.emailNovaConta);
        nomeNovaConta = (EditText) findViewById(R.id.nomeNovaConta);
        dataNascimentoNovaConta = (MaskedEditText) findViewById(R.id.dataNascimentoNovaConta);
        senhaNovaConta = (EditText) findViewById(R.id.senhaNovaConta);
        confirmaSenhaNovaConta = (EditText) findViewById(R.id.confirmaSenhaNovaConta);
        abrirNovaConta = (CircularProgressButton) findViewById(R.id.abrirNovaConta);

        abrirNovaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer count = 1;

                if (!confirmaSenhaNovaConta.getText().toString().equals(senhaNovaConta.getText().toString())) {
                    confirmaSenhaNovaConta.setError("Senhas não correspondem");
                }

                else if (emailNovaConta.getText().toString().isEmpty() || emailNovaConta.getText().toString().equals("")) {
                    emailNovaConta.setError("Digite seu e-mail");
                    emailNovaConta.requestFocus();
                }

                else if (nomeNovaConta.getText().toString().isEmpty() || nomeNovaConta.getText().toString().equals("")) {
                    nomeNovaConta.setError("Digite seu nome");
                    nomeNovaConta.requestFocus();
                }

                else if (senhaNovaConta.getText().toString().isEmpty() || senhaNovaConta.getText().toString().equals("")) {
                    senhaNovaConta.setError("Digite sua senha");
                    senhaNovaConta.requestFocus();
                }

                else if (confirmaSenhaNovaConta.getText().toString().isEmpty() || confirmaSenhaNovaConta.getText().toString().equals("")) {
                    confirmaSenhaNovaConta.setError("Confirme sua senha");
                    confirmaSenhaNovaConta.requestFocus();
                }

                else if (dataNascimentoNovaConta.getText().toString().isEmpty() || dataNascimentoNovaConta.getText().toString().equals("")) {
                    dataNascimentoNovaConta.setError("Digite sua data de nascimento");
                    dataNascimentoNovaConta.requestFocus();

                } else {

                    abrirNovaConta.startAnimation();

                    JSONObject o = new JSONObject();
                    try {
                    /*DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    date = (Date) formatter.parse(dataNascimentoNovaConta.getText().toString());*/

                        o.put("email", emailNovaConta.getText().toString());
                        o.put("nome", nomeNovaConta.getText().toString());
                        o.put("senha", senhaNovaConta.getText().toString());
                        o.put("dataNascimento", dataNascimentoNovaConta.getText().toString());
                        o.put("authToken", null);

                        System.out.println(o.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    WebService newUsuario = new WebService(ENDERECO_WEB + "/adotapet-servidor/api/usuario/cadastro");
                    newUsuario.execute(o);
                }

            }
        });


        this.getSupportActionBar().setTitle("Abrir uma nova conta");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private class WebService extends AsyncTask<JSONObject, Integer, String> {

        private String webAdd;

        private WebService(String endereco) {
            webAdd = endereco;
        }

        @Override
        protected String doInBackground(JSONObject... params) {
            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(webAdd);
            HttpResponse resposta = null;
            String systemRes = "";

            try {
                chamada.addHeader("Accept", "application/json");
                chamada.addHeader("Content-type", "application/json");
                chamada.setEntity(new ByteArrayEntity(params[0].toString().getBytes("UTF8")));
                resposta = cliente.execute(chamada);

                System.out.println(resposta.getStatusLine().getStatusCode());
                System.out.println(resposta.getStatusLine().getReasonPhrase());

                systemRes = EntityUtils.toString(resposta.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return systemRes;
        }

        @Override
        protected void onPostExecute(String s) {

            abrirNovaConta.revertAnimation();

            if (s.equals("Erro")) {
                emailNovaConta.setError("Este e-mail já está em uso");
                emailNovaConta.requestFocus();
                abrirNovaConta.setClickable(true);
                abrirNovaConta.setBackgroundResource(R.color.colorAccent);
            } else {
                Toast.makeText(RegistroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();

                LoginService ls = new LoginService();
                ls.execute(emailNovaConta.getText().toString(), senhaNovaConta.getText().toString());
                id = Long.valueOf(s);
            }

        }
    }

    private class LoginService extends AsyncTask<String, Void, String> {

        private String
                webAdd = ENDERECO_WEB + "/adotapet-servidor/api/usuario/login";

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

                token = new String(Base64.encode((params[0] + ":" + params[1]).getBytes(), Base64.NO_WRAP));
                chamada.setHeader("Authorization", "Basic " + token);

                chamada.setEntity(new UrlEncodedFormEntity(parametros));
                resposta = cliente.execute(chamada);
                systemRes = EntityUtils.toString(resposta.getEntity(), StandardCharsets.UTF_8);

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

                    System.out.println(session.getUserPrefs() + "*************");

                    Intent intent = new Intent(RegistroActivity.this, FinalizaCadastroActivity.class);
                    startActivity(intent);
                } catch (Exception e) {

                    Toast.makeText(RegistroActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                }

            } else {

                System.out.println(systemRes);
                Toast.makeText(RegistroActivity.this, "Erro", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
