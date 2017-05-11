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
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.amand.projetointegrador.Adapters.CustomPagerAdapter;
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

public class novaDoacaoActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private FloatingActionButton addImages;
    private EditText nomeAnimal;
    private SegmentedGroup tipoAnimal;
    private RadioButton cachorro;
    private RadioButton gato;
    private SegmentedGroup sexoAnimal;
    private RadioButton feminino;
    private RadioButton masculino;
    private EditText racaAnimal;
    private EditText corAnimal;
    private EditText observacoesAnimal;
    private RadioButton deficieciaSim;
    private RadioButton deficienciaNao;
    private RadioButton castradoSim;
    private RadioButton castradoNao;
    private Button enviaAnuncio;

    private RadioGroup radioDef;
    private RadioGroup radioCas;

    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<File> urlImg = new ArrayList<>();

    private String nome, raca, cor, observacoes, sexo, tipo, castrado, deficiente;

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_doacao);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Nova doação");

        session = new Session(this);

        viewPager = (ViewPager) findViewById(R.id.novaDoacaoPager);
        addImages = (FloatingActionButton) findViewById(R.id.novaDoacaoImgBtn);
        nomeAnimal = (EditText) findViewById(R.id.nomeAnimal);
        racaAnimal = (EditText) findViewById(R.id.racaAnimal);
        corAnimal = (EditText) findViewById(R.id.corAnimal);
        observacoesAnimal = (EditText) findViewById(R.id.descricaoAnimal);

        enviaAnuncio = (Button) findViewById(R.id.enviaAnuncio);

        addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage(v);
            }
        });

        tipoAnimal = (SegmentedGroup) findViewById(R.id.tipoAnimal);
        sexoAnimal = (SegmentedGroup) findViewById(R.id.sexoAnimal);
        radioDef = (RadioGroup) findViewById(R.id.radioDeficiencia);
        radioCas = (RadioGroup) findViewById(R.id.radioCastrado);

        radioDef.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.deficienciaSim:
                        deficiente = "true";
                        break;
                    case R.id.deficienciaNao:
                        deficiente = "false";
                        break;
                }
            }
        });

        radioCas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.castradoSim:
                        castrado = "true";
                        break;
                    case R.id.castradoNao:
                        castrado = "false";
                        break;
                }
            }
        });

        tipoAnimal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.cachorroTipo:
                        tipo = "Cachorro";
                        break;
                    case R.id.gatoTipo:
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

        enviaAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = nomeAnimal.getText().toString();
                raca = racaAnimal.getText().toString();
                cor = corAnimal.getText().toString();
                observacoes = observacoesAnimal.getText().toString();

                AnuncioService newAnuncio = new AnuncioService();
                newAnuncio.execute(nome, tipo, sexo, raca, cor, observacoes, deficiente, castrado);
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
        viewPager.setAdapter(pAdapter);

    }

    private class AnuncioService extends AsyncTask<String, Void, HttpResponse> {

        @Override
        protected HttpResponse doInBackground(String... params) {
            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/cadastro-doacao");
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

                entityBuilder.addPart("nome", new StringBody((params[0]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("tipo", new StringBody((params[1]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("sexo", new StringBody((params[2]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("raca", new StringBody((params[3]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("cor", new StringBody((params[4]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("observacoes  ", new StringBody((params[5]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("deficiencia", new StringBody((params[6]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("castrado", new StringBody((params[7]), ContentType.TEXT_PLAIN));

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
