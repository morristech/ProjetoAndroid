package com.example.amand.projetointegrador;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.amand.projetointegrador.Adapters.CustomPagerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mvc.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import info.hoang8f.android.segmented.SegmentedGroup;

public class novoEncontradoActivity extends AppCompatActivity {

    private EditText digitarEnderecoEncontrado;
    private RadioGroup localizacaoEncontrado;
    private RadioButton localizacaoAtual;
    private RadioButton digitaEndereco;

    private ViewPager imgPager;
    private FloatingActionButton addImgBtn;
    private SegmentedGroup tipoAnimal;
    private SegmentedGroup sexoAnimal;
    private SegmentedGroup porteAnimal;
    private EditText racaAnimal;
    private EditText corAnimal;
    private EditText observacoesAnimal;
    private EditText nomeAnimal;
    private Button enviaAnuncio;
    private Switch resgatado;
    private View mapa;

    private String tipo;
    private String sexo;
    private String resgatadoString;
    private String porte;


    private double lat;
    private double lng;

    private GoogleMap map;

    private LocationManager locationManager;

    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<File> urlImg = new ArrayList<>();

    Context ctx;

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_encontrado);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Novo animal Encontrado");

        ctx = this;

        session = new Session(this);

        imgPager = (ViewPager) findViewById(R.id.novoEncontradoPager);
        addImgBtn = (FloatingActionButton) findViewById(R.id.novoEncontradoImgBtn);
        tipoAnimal = (SegmentedGroup) findViewById(R.id.tipoAnimal);
        sexoAnimal = (SegmentedGroup) findViewById(R.id.sexoAnimal);
        porteAnimal = (SegmentedGroup) findViewById(R.id.porteAnimal);
        racaAnimal = (EditText) findViewById(R.id.racaAnimal);
        corAnimal = (EditText) findViewById(R.id.corAnimal);
        observacoesAnimal = (EditText) findViewById(R.id.descricaoAnimal);
        enviaAnuncio = (Button) findViewById(R.id.enviaAnuncio);
        nomeAnimal = (EditText) findViewById(R.id.nomeAnimal);
        resgatado = (Switch) findViewById(R.id.switchResgatado);
        mapa = findViewById(R.id.mapaEncontrado);

        digitarEnderecoEncontrado = (EditText) findViewById(R.id.digitarEnderecoEncontrado);
        localizacaoEncontrado = (RadioGroup) findViewById(R.id.localizacaoRadio);
        localizacaoAtual = (RadioButton) findViewById(R.id.localizacaoAtual);
        digitaEndereco = (RadioButton) findViewById(R.id.digitaEndereco);

        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage(v);
            }
        });




        tipoAnimal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.tipoCachorro:
                        tipo = "Cachorro";
                        break;

                    case R.id.tipoGato:
                        tipo = "Gato";
                        break;
                }
            }
        });

        porteAnimal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.portePeq:
                        porte = "Pequeno";
                        break;
                    case R.id.porteMed:
                        porte = "Médio";
                        break;
                    case R.id.porteGra:
                        porte = "Grande";
                        break;
                }
            }
        });

        sexoAnimal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.sexoFem:
                        sexo = "Fêmea";
                        break;
                    case R.id.sexoMasc:
                        sexo = "Macho";
                        break;
                }
            }
        });

        if (resgatado != null) {
            resgatado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        resgatadoString = "Sim";
                        //do stuff when Switch is ON
                    } else {
                        resgatadoString = "Não";
                        //do stuff when Switch if OFF
                    }
                }
            });
        }

        localizacaoEncontrado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.localizacaoAtual:
                        if(digitarEnderecoEncontrado.getVisibility() == View.VISIBLE) {
                            digitarEnderecoEncontrado.setVisibility(View.GONE);
                        }

                        Toast.makeText(ctx, "Localizacao Atual", Toast.LENGTH_SHORT).show();

                        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            ActivityCompat.requestPermissions(novoEncontradoActivity.this, new String[]{
                                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                        lat = location.getLatitude();
                        lng = location.getLongitude();

                        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaEncontrado);

                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                LatLng ll = new LatLng(lat, lng);
                                map = googleMap;
                                map.addMarker(new MarkerOptions().position(ll).title("Você está aqui"));
                                map.moveCamera(CameraUpdateFactory.newLatLng(ll));
                                map.animateCamera(CameraUpdateFactory.zoomTo(16f));
                            }
                        });

                        break;

                    case R.id.digitaEndereco:
                        Toast.makeText(ctx, "Digitar endereço", Toast.LENGTH_SHORT).show();
                        digitarEnderecoEncontrado.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        enviaAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novoEncontradoActivity.AnuncioService anuncio = new novoEncontradoActivity.AnuncioService();
                anuncio.execute(tipo, sexo, racaAnimal.getText().toString(), corAnimal.getText().toString(),
                        observacoesAnimal.getText().toString(), nomeAnimal.getText().toString(), resgatadoString, porte);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPickImage(View view) {
        // Click on image button
        ImagePicker.pickImage(this, "Select your image:");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);

        // TODO do something with the bitmap

        File file = new File(getFilesDir().getPath() + "image" + bitmap.getByteCount() +".png");


        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmaps.add(bitmap);
        urlImg.add(file);
        PagerAdapter pAdapter = new CustomPagerAdapter(this, bitmaps);
        imgPager.setAdapter(pAdapter);

    }

    private class AnuncioService extends AsyncTask<String, Void, HttpResponse> {

        @Override
        protected HttpResponse doInBackground(String... params) {
            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/cadastro-encontrado");
            HttpResponse resposta = null;

            try {

                chamada.setHeader("Authorization", "Basic " + session.getToken());

                Long userId = session.getUserPrefs();

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.addPart("userId", new StringBody(String.valueOf(userId), ContentType.TEXT_PLAIN));

                if (!urlImg.isEmpty()) {
                    for (int i = 0; i < urlImg.size(); i++) {
                        entityBuilder.addPart("file", new FileBody(urlImg.get(i)));
                    }
                }


                entityBuilder.addPart("tipo", new StringBody((params[0]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("sexo", new StringBody((params[1]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("raca", new StringBody((params[2]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("cor", new StringBody((params[3]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("descricao", new StringBody((params[4]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("titulo", new StringBody((params[5]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("resgatado", new StringBody((params[6]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("porte", new StringBody((params[7]), ContentType.TEXT_PLAIN));


                //Falta a localização

                HttpEntity entity = entityBuilder.build();

                chamada.setEntity(entity);
                resposta = cliente.execute(chamada);

                System.out.println(resposta.getStatusLine().getStatusCode());
                System.out.println(resposta.getStatusLine().getReasonPhrase());


            } catch (IOException e) {
                e.printStackTrace();
            }

            return resposta;
        }

        @Override
        protected void onPostExecute(HttpResponse httpResponse) {

            Toast.makeText(getApplicationContext(), String.valueOf(httpResponse.getStatusLine().getStatusCode()), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(novoEncontradoActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
