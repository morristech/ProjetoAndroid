package com.example.amand.projetointegrador.encontrado;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amand.projetointegrador.MainActivity;
import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.RegistroActivity;
import com.example.amand.projetointegrador.helpers.GPSTracker;
import com.example.amand.projetointegrador.helpers.Session;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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
import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.android.segmented.SegmentedGroup;

public class novoEncontradoActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private RadioGroup localizacaoEncontrado;
    private SegmentedGroup tipoAnimal;
    private SegmentedGroup sexoAnimal;
    private SegmentedGroup porteAnimal;
    private EditText racaAnimal;
    private EditText corAnimal;
    private EditText observacoesAnimal;
    private EditText titulo;
    private Button enviaAnuncio;
    private Switch resgatado;
    private View mapa;
    private TextView switchTxt;

    private String tipo = "";
    private String sexo = "";
    private String resgatadoString;
    private String porte = "";

    private CircleImageView img1;
    private CircleImageView img2;
    private CircleImageView img3;
    private CircleImageView img4;
    private CircleImageView img5;
    private CircleImageView img6;

    private ImageButton rmimg1;
    private ImageButton rmimg2;
    private ImageButton rmimg3;
    private ImageButton rmimg4;
    private ImageButton rmimg5;
    private ImageButton rmimg6;

    // Fragmento do AutoComplete
    PlaceAutocompleteFragment autocompleteFragment;
    private View autocompleteView;
    private static final String TAG = "novoEncontradoActivity";
    LatLng coordinates;

    Double lat;
    Double lng;

    private GoogleMap map;

    private FusedLocationProviderClient mFusedLocationClient;

    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<File> urlImg = new ArrayList<>();

    Context ctx;

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.amand.projetointegrador.R.layout.activity_novo_encontrado);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Novo animal Encontrado");

        autocompleteView = findViewById(R.id.place_autocomplete_fragment);
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteView.setVisibility(View.GONE);

        switchTxt = (TextView) findViewById(R.id.switchTxt);
        switchTxt.setText("Não");

        ctx = this;

        session = new Session(this);

        img1 = (CircleImageView) findViewById(R.id.addimage1);
        img2 = (CircleImageView) findViewById(R.id.addimage2);
        img3 = (CircleImageView) findViewById(R.id.addimage3);
        img4 = (CircleImageView) findViewById(R.id.addimage4);
        img5 = (CircleImageView) findViewById(R.id.addimage5);
        img6 = (CircleImageView) findViewById(R.id.addimage6);

        rmimg1 = (ImageButton) findViewById(R.id.rmimage1);
        rmimg2 = (ImageButton) findViewById(R.id.rmimage2);
        rmimg3 = (ImageButton) findViewById(R.id.rmimage3);
        rmimg4 = (ImageButton) findViewById(R.id.rmimage4);
        rmimg5 = (ImageButton) findViewById(R.id.rmimage5);
        rmimg6 = (ImageButton) findViewById(R.id.rmimage6);


        rmimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (File url : urlImg) {
                    if (url.getName().equals(img1.getTag())) {
                        urlImg.remove(url);
                        img1.setTag("addimage2");
                        img1.setImageResource(R.drawable.addimage2);
                        rmimg1.setVisibility(View.GONE);
                        System.out.println(urlImg.size() + "+++++++++");
                    }
                }
            }
        });

        rmimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (File url : urlImg) {
                    if (url.getName().equals(img2.getTag())) {
                        urlImg.remove(url);
                        img2.setTag("addimage2");
                        img2.setImageResource(R.drawable.addimage2);
                        rmimg2.setVisibility(View.GONE);
                    }
                }
            }
        });

        rmimg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (File url : urlImg) {
                    if (url.getName().equals(img3.getTag())) {
                        urlImg.remove(url);
                        img3.setTag("addimage2");
                        img3.setImageResource(R.drawable.addimage2);
                        rmimg3.setVisibility(View.GONE);
                    }
                }
            }
        });

        rmimg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (File url : urlImg) {
                    if (url.getName().equals(img4.getTag())) {
                        urlImg.remove(url);
                        img4.setTag("addimage2");
                        img4.setImageResource(R.drawable.addimage2);
                        rmimg4.setVisibility(View.GONE);
                    }
                }
            }
        });

        rmimg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (File url : urlImg) {
                    if (url.getName().equals(img5.getTag())) {
                        urlImg.remove(url);
                        img5.setTag("addimage2");
                        img5.setImageResource(R.drawable.addimage2);
                        rmimg5.setVisibility(View.GONE);
                    }
                }
            }
        });

        rmimg6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (File url : urlImg) {
                    if (url.getName().equals(img6.getTag())) {
                        urlImg.remove(url);
                        img6.setTag("addimage2");
                        img6.setImageResource(R.drawable.addimage2);
                        rmimg6.setVisibility(View.GONE);
                    }
                }
            }
        });

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);


        tipoAnimal = (SegmentedGroup) findViewById(R.id.tipoAnimal);
        sexoAnimal = (SegmentedGroup) findViewById(R.id.sexoAnimal);
        porteAnimal = (SegmentedGroup) findViewById(R.id.porteAnimal);
        corAnimal = (EditText) findViewById(R.id.corAnimal);
        observacoesAnimal = (EditText) findViewById(R.id.descricaoAnimal);
        enviaAnuncio = (Button) findViewById(R.id.enviaAnuncio);
        titulo = (EditText) findViewById(R.id.titulo);
        resgatado = (Switch) findViewById(R.id.switchResgatado);
        mapa = findViewById(R.id.mapaEncontrado);
        resgatadoString = "Não";

        localizacaoEncontrado = (RadioGroup) findViewById(R.id.localizacaoRadio);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaEncontrado);

        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(novoEncontradoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }
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
                    if (isChecked) {
                        resgatadoString = "True";
                        switchTxt.setText("Sim");
                        //do stuff when Switch is ON
                    } else {
                        resgatadoString = "False";
                        resgatado.setShowText(false);
                        switchTxt.setText("Não");
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
                        if (autocompleteView.getVisibility() == View.VISIBLE) {
                            autocompleteView.setVisibility(View.GONE);
                        }

                        Toast.makeText(ctx, "Localizacao Atual", Toast.LENGTH_SHORT).show();

                            LatLng ll = new LatLng(lat, lng);
                            map.addMarker(new MarkerOptions().position(ll).title("Você está aqui"));
                            map.moveCamera(CameraUpdateFactory.newLatLng(ll));
                            map.animateCamera(CameraUpdateFactory.zoomTo(16f));

                        break;

                    case R.id.digitaEndereco:
                        autocompleteView.setVisibility(View.VISIBLE);
                        //place_autocomplete_fragment
                        // Método executado quando um local é selecionado no GOoglePlaces Widget Input
                        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                            /**
                             * @param place local escolhido pela pessoa, através do GooglePlaces Widget Input
                             */
                            @Override
                            public void onPlaceSelected(Place place) {
                                // A pessoa escolheu um place. Latitude e longitude são salvas em "coordinates".
                                coordinates = place.getLatLng();
                                lat = place.getLatLng().latitude;
                                lng = place.getLatLng().longitude;
                                Log.i(TAG, "Place: " + place.getName());

                                // Se a pessoa escolheu um endereço, execute a lógica abaixo. Caso o contrário, não faça nada
                                if (coordinates != null) {
                                    // Limpa todos os markers
                                    map.clear();
                                    // Volta a visibilidade do fragmento
                                    mapa.setVisibility(View.VISIBLE);
                                    // Adiciona o marcador baseado no endereço digitado pela pessoa
                                    map.addMarker(new MarkerOptions().position(coordinates).title("Animalzinho"));
                                    // Move a câmera
                                    map.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                                    // Dá zoom
                                    map.animateCamera(CameraUpdateFactory.zoomTo(16f));
                                }

                            }

                            /**
                             * @param status mensagem de erro
                             */
                            @Override
                            public void onError(Status status) {
                                // TODO: Handle the error.
                                Log.i(TAG, "Aconteceu algum erro: " + status);
                            }
                        });
                        break;
                }
            }
        });

        enviaAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titulo.getText().toString().equals("") || titulo.getText().toString().isEmpty()) {
                    titulo.setError("Digite o titulo do anúncio");
                } else {
                    novoEncontradoActivity.AnuncioService anuncio = new novoEncontradoActivity.AnuncioService();
                    anuncio.execute(tipo, sexo, corAnimal.getText().toString(),
                            observacoesAnimal.getText().toString(), titulo.getText().toString(), resgatadoString, porte,
                            String.valueOf(lat), String.valueOf(lng));
                }
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
        if (resultCode != Activity.RESULT_CANCELED) {
            Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);

            // TODO do something with the bitmap

            File file = new File(getFilesDir().getPath() + "image" + bitmap.getByteCount() + ".png");


            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (img1.getTag().equals("addimage2")) {
                img1.setImageBitmap(bitmap);
                img1.setTag(file.getName());
                rmimg1.setVisibility(View.VISIBLE);
            } else if (img2.getTag().equals("addimage2")) {
                img2.setImageBitmap(bitmap);
                img2.setTag(file.getName());
                rmimg2.setVisibility(View.VISIBLE);
            } else if (img3.getTag().equals("addimage2")) {
                img3.setImageBitmap(bitmap);
                img3.setTag(file.getName());
                rmimg3.setVisibility(View.VISIBLE);
            } else if (img4.getTag().equals("addimage2")) {
                img4.setImageBitmap(bitmap);
                img4.setTag(file.getName());
                rmimg4.setVisibility(View.VISIBLE);
            } else if (img5.getTag().equals("addimage2")) {
                img5.setImageBitmap(bitmap);
                img5.setTag(file.getName());
                rmimg5.setVisibility(View.VISIBLE);
            } else if (img6.getTag().equals("addimage2")) {
                img6.setImageBitmap(bitmap);
                img6.setTag(file.getName());
                rmimg6.setVisibility(View.VISIBLE);
            }

            System.out.println(urlImg.size() + "++++++++++++");

            bitmaps.add(bitmap);
            urlImg.add(file);
        }

    }

    @Override
    public void onClick(View v) {
        onPickImage(v);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
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
                entityBuilder.addPart("cor", new StringBody((params[2]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("descricao", new StringBody((params[3]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("titulo", new StringBody((params[4]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("resgatado", new StringBody((params[5]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("porte", new StringBody((params[6]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("latitude", new StringBody((params[7]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("longitude", new StringBody((params[8]), ContentType.TEXT_PLAIN));


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
