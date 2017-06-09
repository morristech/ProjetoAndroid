package com.example.amand.projetointegrador.doacao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.amand.projetointegrador.adapters.CustomPagerAdapter;
import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.RegistroActivity;
import com.example.amand.projetointegrador.helpers.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;
import me.relex.circleindicator.CircleIndicator;

public class DoacaoDetalhesActivity extends AppCompatActivity {

    private final List<String> images = new ArrayList<>();
    private final List<Bitmap> bitmaps = new ArrayList<>();

    private Long id;
    private TextView nomeAnimal;
    private TextView racaAnimal;
    private TextView sexoAnimal;
    private TextView idadeAnimal;
    private TextView corAnimal;
    private TextView castradoAnimal;
    private TextView deficienteAnimal;
    private TextView observacaoAnimal;
    private CircleIndicator indicator;
    Context ctx;


    private ViewPager viewPager;
    Session s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doacao_detalhes);
        s = new Session(this);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Detalhes de doação");


        ctx = this;
        Intent i = getIntent();

        id = i.getLongExtra("doacao", 0);

        //DoacaoAdapter doacaoAdapter = new DoacaoAdapter(this);
        // Get intent data

        indicator = (CircleIndicator) findViewById(R.id.indicator);

        viewPager = (ViewPager) findViewById(R.id.imgSliderDoacao);

        nomeAnimal = (TextView) findViewById(R.id.nomeAnimal);

        racaAnimal = (TextView) findViewById(R.id.racaAnimal);

        sexoAnimal = (TextView) findViewById(R.id.sexoAnimal);

        idadeAnimal = (TextView) findViewById(R.id.idadeAnimal);

        corAnimal = (TextView) findViewById(R.id.corAnimal);

        castradoAnimal = (TextView) findViewById(R.id.castradoAnimal);

        deficienteAnimal = (TextView) findViewById(R.id.deficienteAnimal);

        observacaoAnimal = (TextView) findViewById(R.id.observacoesAnimal);

        GetService get = new GetService();
        get.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetService extends AsyncTask<String, Void, String> {

        private String
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/get-doacao/" +id;

        @Override
        protected String doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpGet chamada = new HttpGet(webAdd);
            HttpResponse resposta = null;
            String systemRes = "";

            try {
                chamada.setHeader("Authorization", "Basic " + s.getToken());

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

            try {
                JSONObject o = new JSONObject(s);

                nomeAnimal.setText(o.getString("nome"));

                if (o.getString("raca").equals("null") || o.getString("raca").isEmpty()) {
                    racaAnimal.setText("Raça: Sem raça definida");
                } else {
                    String raca = "Raça: " +o.getString("raca");
                    racaAnimal.setText(raca);
                }

                if (o.getString("sexo").equals("null") || o.getString("sexo").isEmpty()) {
                    sexoAnimal.setText("Sexo: Indefinido");
                } else {
                    String sexo = "Sexo: " +o.getString("sexo");
                    sexoAnimal.setText(sexo);
                }

                if (o.getString("cor").equals("null") || o.getString("cor").isEmpty()) {
                    corAnimal.setText("Cor: Indefinido");
                } else {
                    String cor = "Cor: " +o.getString("cor");
                    corAnimal.setText(cor);
                }

                if(o.getInt("idade") == 0) {
                    idadeAnimal.setVisibility(View.GONE);
                } else {
                    idadeAnimal.setText(String.valueOf(o.getInt("idade")));
                } //Setar idade em string

                if(o.getString("descricao").equals("null") || o.getString("descricao").isEmpty()) {
                    observacaoAnimal.setVisibility(View.GONE);
                } else {
                    String obs = "Descrição: " +o.getString("descricao");
                    observacaoAnimal.setText(obs);
                }

                castradoAnimal.setText(o.getBoolean("castrado") ? "Castrado: Sim" : "Castrado: Não");
                deficienteAnimal.setText(o.getBoolean("deficiencia") ? "Possui Deficiencia: Sim" : "Possui Deficiencia: Não");


                JSONArray imgs = o.getJSONArray("imgAnuncio");

                if (imgs.length() > 0) {
                    for (int j = 0; j < imgs.length(); j++) {
                        images.add(imgs.get(j).toString());
                    }
                }

                for (String str : images) {
                    DownloadImageTask down = new DownloadImageTask();
                    down.execute(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/doacao/" + id + "/" + str);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

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
            bitmaps.add(result);

            PagerAdapter pAdapter = new CustomPagerAdapter(ctx, bitmaps);
            viewPager.setAdapter(pAdapter);
            indicator.setViewPager(viewPager);
        }
    }
}
