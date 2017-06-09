package com.example.amand.projetointegrador.encontrado;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.RegistroActivity;
import com.example.amand.projetointegrador.adapters.CustomPagerAdapter;
import com.example.amand.projetointegrador.helpers.Session;
import com.example.amand.projetointegrador.perdido.PerdidoDetalhesActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class EncontradoDetalhesActivity extends AppCompatActivity {

    private final List<String> images = new ArrayList<>();
    private final List<Bitmap> bitmaps = new ArrayList<>();

    private Long id;
    private Double lat;
    private Double lng;
    private TextView tituloAnuncio;
    private TextView resgatadoAnimal;
    private TextView racaAnimal;
    private TextView sexoAnimal;
    private TextView corAnimal;
    private TextView porteAnimal;
    private TextView observacaoAnimal;
    private CircleIndicator indicator;
    Context ctx;
    private GoogleMap map;
    private View mapa;

    private ViewPager viewPager;
    Session s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encontrado_detalhes);
        s = new Session(this);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Detalhes de animal encontrado");


        ctx = this;
        Intent i = getIntent();

        id = i.getLongExtra("encontrado", 0);

        //DoacaoAdapter doacaoAdapter = new DoacaoAdapter(this);
        // Get intent data
        mapa = findViewById(R.id.mapaDetalhesEncontrado);

        indicator = (CircleIndicator) findViewById(R.id.indicator);

        viewPager = (ViewPager) findViewById(R.id.imgSliderEncontrado);

        tituloAnuncio = (TextView) findViewById(R.id.nomeAnimal);

        racaAnimal = (TextView) findViewById(R.id.racaAnimal);

        sexoAnimal = (TextView) findViewById(R.id.sexoAnimal);

        corAnimal = (TextView) findViewById(R.id.corAnimal);

        porteAnimal = (TextView) findViewById(R.id.porteAnimal);

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
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/get-encontrado/" +id;

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

                tituloAnuncio.setText(o.getString("titulo"));

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

                if(o.getString("porte").equals("null") || o.getString("porte").isEmpty()) {
                    porteAnimal.setVisibility(View.GONE);
                } else {
                    String porte = "Porte: " +o.getString("porte");
                    porteAnimal.setText(porte);
                } //Setar idade em string

                if(o.getString("descricao").equals("null") || o.getString("descricao").isEmpty()) {
                    observacaoAnimal.setVisibility(View.GONE);
                } else {
                    String obs = "Descrição: " +o.getString("descricao");
                    observacaoAnimal.setText(obs);
                }

                lat = Double.parseDouble(o.getString("latitude"));
                lng = Double.parseDouble(o.getString("longitude"));

                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaDetalhesEncontrado);

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng ll = new LatLng(lat, lng);
                        map = googleMap;
                        map.addMarker(new MarkerOptions().position(ll).title("Última posição do animal"));
                        map.moveCamera(CameraUpdateFactory.newLatLng(ll));
                        map.animateCamera(CameraUpdateFactory.zoomTo(16f));

                        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                Uri gmmIntentUri = Uri.parse("geo:"+lat+","+lng+", ?q="+lat+","+lng+", Última localização do animal");
                                final Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        });
                    }

                });

                JSONArray imgs = o.getJSONArray("imgAnuncio");

                if (imgs.length() > 0) {
                    for (int j = 0; j < imgs.length(); j++) {
                        images.add(imgs.get(j).toString());
                    }
                }

                for (String str : images) {
                    DownloadImageTask down = new DownloadImageTask();
                    down.execute(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/encontrado/" + id + "/" + str);
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
