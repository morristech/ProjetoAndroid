package com.example.amand.projetointegrador;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.amand.projetointegrador.model.PerfilUsuario;
import com.example.amand.projetointegrador.model.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class registroActivity extends AppCompatActivity {

    private EditText emailNovaConta;

    private EditText nomeNovaConta;
    private EditText dataNascimentoNovaConta;
    private EditText senhaNovaConta;
    private EditText confirmaSenhaNovaConta;
    private Button abrirNovaConta;
    private AwesomeValidation awesomeValidation;
    private ProgressBar pb;
    private Session session;

    public static final String ENDERECO_WEB = "http://10.42.0.1:8888";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        session = new Session(this);
        pb = (ProgressBar) findViewById(R.id.progressoRegistro);
        pb.setMax(10);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        emailNovaConta = (EditText) findViewById(R.id.emailNovaConta);
        nomeNovaConta = (EditText) findViewById(R.id.nomeNovaConta);
        dataNascimentoNovaConta = (EditText) findViewById(R.id.dataNascimentoNovaConta);
        senhaNovaConta = (EditText) findViewById(R.id.senhaNovaConta);
        confirmaSenhaNovaConta = (EditText) findViewById(R.id.confirmaSenhaNovaConta);
        abrirNovaConta = (Button) findViewById(R.id.abrirNovaConta);

        awesomeValidation.addValidation(this, R.id.emailNovaConta, Patterns.EMAIL_ADDRESS, R.string.erroemail);
        awesomeValidation.addValidation(this, R.id.nomeNovaConta, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.erronome);

        dataNascimentoNovaConta.addTextChangedListener(new TextWatcher() {
            int prevL = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevL = dataNascimentoNovaConta.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prevL < length) && (length == 2 || length == 5)) {
                    s.append("/");
                }
            }
        });

        abrirNovaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer count = 1;

                if (!confirmaSenhaNovaConta.getText().toString().equals(senhaNovaConta.getText().toString())) {

                    Toast.makeText(registroActivity.this, "Senhas não correspondem", Toast.LENGTH_SHORT).show();
                }

                if (awesomeValidation.validate()) {
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
                    count = 1;
                    pb.setVisibility(View.VISIBLE);
                    pb.setProgress(0);


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

    private class WebService extends AsyncTask<JSONObject, Integer, HttpResponse> {

        private String webAdd;

        private WebService(String endereco) {
            webAdd = endereco;
        }

        @Override
        protected HttpResponse doInBackground(JSONObject... params) {
            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(webAdd);
            HttpResponse resposta = null;

            try {
                chamada.addHeader("Accept", "application/json");
                chamada.addHeader("Content-type", "application/json");
                chamada.setEntity(new ByteArrayEntity(params[0].toString().getBytes("UTF8")));
                resposta = cliente.execute(chamada);

                System.out.println(resposta.getStatusLine().getStatusCode());
                System.out.println(resposta.getStatusLine().getReasonPhrase());


            } catch (IOException e) {
                e.printStackTrace();
            }

            return resposta;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            abrirNovaConta.setVisibility(View.GONE);
            pb.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(HttpResponse s) {
            pb.setVisibility(View.GONE);
            abrirNovaConta.setVisibility(View.VISIBLE);
            if (s.getStatusLine().getStatusCode() == 200) {
                Toast.makeText(registroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();

                LoginService ls = new LoginService();
                ls.execute(emailNovaConta.getText().toString(), senhaNovaConta.getText().toString());

            } else if (s.getStatusLine().getStatusCode() == 409) {
                Toast.makeText(registroActivity.this, "Conta já existente!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(registroActivity.this, "Não sei o que aconteceu", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class LoginService extends AsyncTask<String, Void, HttpResponse> {

        private String
            webAdd = ENDERECO_WEB + "/adotapet-servidor/api/usuario/login";

        @Override
        protected HttpResponse doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(webAdd);
            HttpResponse resposta = null;

            try {

                List<NameValuePair> parametros = new ArrayList<>(2);
                parametros.add(new BasicNameValuePair("email", params[0]));
                parametros.add(new BasicNameValuePair("senha", params[1]));

                chamada.setHeader("Authorization", "Basic " + new String(Base64.encode((params[0]+":"+params[1]).getBytes(), Base64.NO_WRAP)));

                chamada.setEntity(new UrlEncodedFormEntity(parametros));
                resposta = cliente.execute(chamada);

                System.out.println(resposta.getStatusLine().getStatusCode());
                System.out.println(resposta.getStatusLine().getReasonPhrase());


            } catch (IOException e) {
                e.printStackTrace();
            }

            return resposta;
        }

        @Override
        protected void onPostExecute(HttpResponse s) {

            String systemRes = "";

            if (s.getStatusLine().getStatusCode() == 200) {
                //Ok

                try {
                    systemRes = EntityUtils.toString(s.getEntity());
                    JSONObject obj = new JSONObject(systemRes);

                    Usuario usuario = new Usuario();
                    usuario.setId(obj.getLong("id"));
                    usuario.setNome(obj.getString("nome"));
                    usuario.setEmail(obj.getString("email"));
                    usuario.setSenha(obj.getString("senha"));

                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date;
                    date = formatter.parse(obj.getString("data"));

                    usuario.setDataNascimento(date);
                    usuario.setAuthToken(obj.getString("authToken"));

                    JSONObject perfil = obj.getJSONObject("perfil");
                    PerfilUsuario perfilUsuario = new PerfilUsuario();
                    perfilUsuario.setId(perfil.getLong("id"));

                    usuario.setPerfil(perfilUsuario);

                    session.setUserPrefs(usuario);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(registroActivity.this, FinalizaCadastroActivity.class);
                startActivity(intent);

            } else {

                System.out.println(systemRes);

                Toast.makeText(registroActivity.this, "Erro" +s.getStatusLine().getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
