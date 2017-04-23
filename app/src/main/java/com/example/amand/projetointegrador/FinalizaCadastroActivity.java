package com.example.amand.projetointegrador;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.mvc.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class FinalizaCadastroActivity extends AppCompatActivity {

    private ImageView imgFinalizaCadastro;
    private FloatingActionButton uploadImageButton;
    private EditText finalizaCadastroTelefone;
    private EditText finalizaCadastroCelular;
    private EditText finalizaCadastroFacebook;
    private EditText finalizaCadastroWhats;
    private Button finalizaCadastroBtn;
    private AwesomeValidation awesomeValidation;
    Session session;

    public static String file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza_cadastro);

        session = new Session(this);

        imgFinalizaCadastro = (ImageView) findViewById(R.id.imgFinalizaCadastro);
        uploadImageButton = (FloatingActionButton) findViewById(R.id.uploadImageButton);
        finalizaCadastroTelefone = (EditText) findViewById(R.id.finalizaCadastroTelefone);
        finalizaCadastroCelular = (EditText) findViewById(R.id.finalizaCadastroCelular);
        finalizaCadastroFacebook = (EditText) findViewById(R.id.finalizaCadastroFacebook);
        finalizaCadastroWhats = (EditText) findViewById(R.id.finalizaCadastroWhats);
        finalizaCadastroBtn = (Button) findViewById(R.id.finalizaCadastroBtn);
        ImagePicker.setMinQuality(600, 600);
        //awesomeValidation.addValidation(this, R.id.finalizaCadastroTelefone, RegexTemplate.TELEPHONE, "Telefone Inválido");
        //awesomeValidation.addValidation(finalizaCadastroCelular, RegexTemplate.TELEPHONE, "Celular inválido");
        //awesomeValidation.addValidation(finalizaCadastroWhats, RegexTemplate.TELEPHONE, "Numero inválido");

        //https://github.com/ArthurHub/Android-Image-Cropper/wiki/Pick-image-for-cropping-from-Camera-or-Gallery

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onPickImage(v);
            }
        });

        finalizaCadastroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    perfilService service = new perfilService(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/usuario/uploadteste");

                    service.execute(file, finalizaCadastroTelefone.getText().toString(), finalizaCadastroCelular.getText().toString(),
                            finalizaCadastroFacebook.getText().toString(), finalizaCadastroWhats.getText().toString());
                } catch (Exception e) {
                    System.out.println(e + "*******************************************");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);

        // TODO do something with the bitmap

        File file = new File(getFilesDir().getPath() + "image.png");


        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imgFinalizaCadastro.setImageBitmap(createSquaredBitmap(bitmap));

    }

    public void onPickImage(View view) {
        // Click on image button
        ImagePicker.pickImage(this, "Select your image:");
    }

    private static Bitmap createSquaredBitmap(Bitmap srcBmp) {
        int dim = Math.max(srcBmp.getWidth(), srcBmp.getHeight());
        Bitmap dstBmp = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(dstBmp);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(srcBmp, (dim - srcBmp.getWidth()) / 2, (dim - srcBmp.getHeight()) / 2, null);

        return dstBmp;
    }

    private class perfilService extends AsyncTask<String, Void, HttpResponse> {

        private String webAdd;

        private perfilService(String endereco) {
            webAdd = endereco;
        }

        @Override
        protected HttpResponse doInBackground(String... params) {
            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(webAdd);
            HttpResponse resposta = null;

            try {
                chamada.setHeader("Authorization", "Basic " + session.getToken());


                File file = new File(getFilesDir().getPath() + "image.png");

                Long userId = session.getUserPrefs();

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.addPart("userId", new StringBody(String.valueOf(userId), ContentType.TEXT_PLAIN));

                if (file.exists()) {
                    entityBuilder.addPart("file", new FileBody(file));
                }
                entityBuilder.addPart("telefone", new StringBody((params[1]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("celular", new StringBody((params[2]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("facebook", new StringBody((params[3]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("whats", new StringBody((params[4]), ContentType.TEXT_PLAIN));

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

