package com.example.amand.projetointegrador.doacao;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.amand.projetointegrador.MainActivity;
import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.RegistroActivity;
import com.example.amand.projetointegrador.helpers.Session;
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

public class novaDoacaoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nomeAnimal;
    private SegmentedGroup tipoAnimal;
    private SegmentedGroup sexoAnimal;
    private EditText racaAnimal;
    private EditText corAnimal;
    private EditText observacoesAnimal;
    private Button enviaAnuncio;

    private RadioGroup radioDef;
    private RadioGroup radioCas;

    private RadioButton btnMasculino;

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


    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<File> urlImg = new ArrayList<>();

    private String nome = "", raca = "", cor = "", observacoes = "", sexo = "", tipo = "", castrado = "", deficiente = "";

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_doacao);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Nova doação");

        session = new Session(this);

        nomeAnimal = (EditText) findViewById(R.id.nomeAnimal);
        racaAnimal = (EditText) findViewById(R.id.racaAnimal);
        corAnimal = (EditText) findViewById(R.id.corAnimal);
        observacoesAnimal = (EditText) findViewById(R.id.descricaoAnimal);
        btnMasculino = (RadioButton) findViewById(R.id.btnMasculino);

        enviaAnuncio = (Button) findViewById(R.id.enviaAnuncio);

        tipoAnimal = (SegmentedGroup) findViewById(R.id.tipoAnimal);
        sexoAnimal = (SegmentedGroup) findViewById(R.id.sexoAnimal);
        radioDef = (RadioGroup) findViewById(R.id.radioDeficiencia);
        radioCas = (RadioGroup) findViewById(R.id.radioCastrado);

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
                    if(url.getName().equals(img1.getTag())) {
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
                    if(url.getName().equals(img2.getTag())) {
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
                    if(url.getName().equals(img3.getTag())) {
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
                    if(url.getName().equals(img4.getTag())) {
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
                    if(url.getName().equals(img5.getTag())) {
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
                    if(url.getName().equals(img6.getTag())) {
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

                if(nome.equals("") || nome.isEmpty()) {
                    nomeAnimal.setError("Digite o nome do animal");
                }

                else {
                    AnuncioService newAnuncio = new AnuncioService();
                    newAnuncio.execute(nome, tipo, sexo, raca, cor, observacoes, deficiente, castrado);
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
                entityBuilder.addPart("descricao", new StringBody((params[5]), ContentType.TEXT_PLAIN));
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

            //Toast.makeText(getApplicationContext(), String.valueOf(httpResponse.getStatusLine().getStatusCode()), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(novaDoacaoActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

}
