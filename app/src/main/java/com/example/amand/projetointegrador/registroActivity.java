package com.example.amand.projetointegrador;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amand.projetointegrador.model.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
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

   // public static final String ENDERECO_WEB = "http://192.168.10.106:8080";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        emailNovaConta = (EditText) findViewById(R.id.emailNovaConta);
        nomeNovaConta = (EditText) findViewById(R.id.nomeNovaConta);
        dataNascimentoNovaConta = (EditText) findViewById(R.id.dataNascimentoNovaConta);
        senhaNovaConta = (EditText) findViewById(R.id.senhaNovaConta);
        confirmaSenhaNovaConta = (EditText) findViewById(R.id.confirmaSenhaNovaConta);
        abrirNovaConta = (Button) findViewById(R.id.abrirNovaConta);

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
                    //o.put("perfil", null);

                    System.out.println(o.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                WebService newUsuario = new WebService("http://192.168.1.8:8888/adotapet-servidor/api/usuario/cadastro");
                newUsuario.execute(o);

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

    private class WebService extends AsyncTask<JSONObject, Void, String> {

        private String webAdd;

        public WebService(String endereco) {
            webAdd = endereco;
        }

        String retorno = "";

        @Override
        protected String doInBackground(JSONObject... params) {
            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(webAdd);

            try {
                /*List<NameValuePair> parametros = new ArrayList<NameValuePair>(1);
                parametros.add(new BasicNameValuePair("usuario", params[0].toString()));

                chamada.setEntity(new UrlEncodedFormEntity(parametros)); */
                chamada.addHeader("Accept", "application/json");
                chamada.addHeader("Content-type", "application/json");
                chamada.setEntity(new ByteArrayEntity(params[0].toString().getBytes("UTF8")));
                HttpResponse resposta = cliente.execute(chamada);

                System.out.println(resposta.getStatusLine().getStatusCode());
                System.out.println(resposta.getStatusLine().getReasonPhrase());
                retorno = EntityUtils.toString(resposta.getEntity());


            } catch (IOException e) {
                e.printStackTrace();
            }

            return retorno;
        }

        @Override
        protected void onPostExecute(String s) {

            System.out.println(s);

            Toast.makeText(registroActivity.this, s, Toast.LENGTH_SHORT).show();

        }
    }
}
