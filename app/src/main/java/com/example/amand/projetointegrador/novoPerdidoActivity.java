package com.example.amand.projetointegrador;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.amand.projetointegrador.Adapters.CustomPagerAdapter;
import com.example.amand.projetointegrador.model.Anuncio;
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

public class novoPerdidoActivity extends AppCompatActivity {

    private EditText digitarEnderecoPerdido;
    private RadioGroup localizacaoPerdido;
    private RadioButton localizacaoAtual;
    private RadioButton digitaEndereco;

    private ViewPager imgPager;
    private FloatingActionButton addImgBtn;
    private SegmentedGroup tipoAnimal;
    private SegmentedGroup sexoAnimal;
    private EditText racaAnimal;
    private EditText corAnimal;
    private EditText observacoesAnimal;
    private Button enviaAnuncio;

    private String tipo;
    private String sexo;

    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<File> urlImg = new ArrayList<>();

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_perdido);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Novo animal perdido");

        session = new Session(this);

        imgPager = (ViewPager) findViewById(R.id.novoPerdidoPager);
        addImgBtn = (FloatingActionButton) findViewById(R.id.novoPerdidoImgBtn);
        tipoAnimal = (SegmentedGroup) findViewById(R.id.tipoAnimal);
        sexoAnimal = (SegmentedGroup) findViewById(R.id.sexoAnimal);
        racaAnimal = (EditText) findViewById(R.id.racaAnimal);
        corAnimal = (EditText) findViewById(R.id.corAnimal);
        observacoesAnimal = (EditText) findViewById(R.id.descricaoAnimal);
        enviaAnuncio = (Button) findViewById(R.id.enviaAnuncio);

        digitarEnderecoPerdido = (EditText) findViewById(R.id.digitarEnderecoPerdido);
        localizacaoPerdido = (RadioGroup) findViewById(R.id.localizacaoRadio);
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

        sexoAnimal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.btnFeminino:
                        sexo = "Fêmea";
                        break;
                    case R.id.btnMasculino:
                        sexo = "Macho";
                        break;
                }
            }
        });

        localizacaoPerdido.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.localizacaoAtual:
                        if(digitarEnderecoPerdido.getVisibility() == View.VISIBLE) {
                            digitarEnderecoPerdido.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.digitaEndereco:
                        digitarEnderecoPerdido.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        enviaAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnuncioService anuncio = new AnuncioService();
                anuncio.execute(tipo, sexo, racaAnimal.getText().toString(), corAnimal.getText().toString(),
                        observacoesAnimal.getText().toString());
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
            HttpPost chamada = new HttpPost(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/cadastro-perdido");
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
                entityBuilder.addPart("observacoes", new StringBody((params[4]), ContentType.TEXT_PLAIN));

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
        }
    }
}
