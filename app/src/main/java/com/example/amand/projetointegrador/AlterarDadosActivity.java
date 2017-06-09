package com.example.amand.projetointegrador;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;

import com.example.amand.projetointegrador.helpers.Session;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class AlterarDadosActivity extends AppCompatActivity {

    private EditText email;
    private EditText nome;
    //private EditText senha;
    private EditText dataNascimento;
    private EditText celular;
    private EditText telefone;
    private EditText whatsapp;

    private CircleImageView imagem;
    private FloatingActionButton btnUpload;
    private Button enviar;

    Context ctx;
    Session s;
    String emailShared;
    Long idShared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados);

        email = (EditText) findViewById(R.id.email);
        nome = (EditText) findViewById(R.id.nome);
        dataNascimento = (EditText) findViewById(R.id.dataNascimento);
        celular = (EditText) findViewById(R.id.celular);
        telefone = (EditText) findViewById(R.id.telefone);
        whatsapp = (EditText) findViewById(R.id.whatsapp);

        imagem = (CircleImageView) findViewById(R.id.imgAlterarDados);
        btnUpload = (FloatingActionButton) findViewById(R.id.uploadImageButton);
        enviar = (Button) findViewById(R.id.enviaAnuncio);

        s = new Session(this);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Alterar dados");


        ctx = this;
        emailShared = s.getUserEmail();
        idShared = s.getUserPrefs();

        GetDados getuserdata = new GetDados();
        getuserdata.execute();


    }

    private class GetDados extends AsyncTask<String, Void, String> {

        private String
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/usuario/getuserdata/"+emailShared;

        @Override
        protected String doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpGet chamada = new HttpGet(webAdd);
            HttpResponse resposta = null;
            String systemRes = "";

            try {

                chamada.setHeader("Authorization", "Basic " + s.getToken());

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

            try {
                JSONObject obj = new JSONObject(s);
                JSONObject perfil = obj.getJSONObject("perfil");

                email.setText(obj.getString("email"));
                nome.setText(obj.getString("nome"));

                String cel = perfil.getString("celular");
                String tel = perfil.getString("telefone");
                String whats = perfil.getString("whatsapp");

                if (!cel.equals("null") && !cel.isEmpty() && !cel.equals(""))  {
                    celular.setText(cel);
                }

                if (!tel.equals("null") && !tel.isEmpty() && !tel.equals("")) {
                    telefone.setText(tel);
                }

                if (!whats.equals("null") && !whats.isEmpty() && !whats.equals("")) {
                    whatsapp.setText(whats);
                }

                String imgurl = perfil.getString("fotoPerfil");

                Date data = new Date(obj.getLong("dataNascimento"));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                dataNascimento.setText(sdf.format(data));

                if (!imgurl.equals("null") && !imgurl.isEmpty() && !imgurl.equals("")) {
                    new DownloadImageTask((CircleImageView) findViewById(R.id.imgAlterarDados))
                            .execute(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/" + idShared + "/" + imgurl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
