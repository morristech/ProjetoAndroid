package com.example.amand.projetointegrador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amand.projetointegrador.helpers.Session;
import com.mvc.imagepicker.ImagePicker;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class AlterarDadosActivity extends AppCompatActivity {

    private EditText email;
    private EditText nome;
    //private EditText senha;
    private MaskedEditText dataNascimento;
    private MaskedEditText celular;
    private MaskedEditText telefone;
    private MaskedEditText whatsapp;

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
        dataNascimento = (MaskedEditText) findViewById(R.id.dataNascimento);
        celular = (MaskedEditText) findViewById(R.id.celular);
        telefone = (MaskedEditText) findViewById(R.id.telefone);
        whatsapp = (MaskedEditText) findViewById(R.id.whatsapp);

        imagem = (CircleImageView) findViewById(R.id.imgAlterarDados);
        btnUpload = (FloatingActionButton) findViewById(R.id.uploadImageButton);
        enviar = (Button) findViewById(R.id.enviaDados);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage(v);
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateService update = new UpdateService();
                update.execute(nome.getText().toString(),
                        dataNascimento.getText().toString(),
                        telefone.getText().toString(),
                        celular.getText().toString(),
                        whatsapp.getText().toString());
            }
        });

        s = new Session(this);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Alterar dados");


        ctx = this;
        emailShared = s.getUserEmail();
        idShared = s.getUserPrefs();

        GetDados getuserdata = new GetDados();
        getuserdata.execute();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AlterarDadosActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(AlterarDadosActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void onPickImage(View view) {
        // Click on image button
        ImagePicker.pickImage(this, "Select your image:");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            imagem.setImageBitmap(bitmap);

            // TODO do something with the bitmap

            File file = new File(getFilesDir().getPath() + "image.png");

            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
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

    private class UpdateService extends AsyncTask<String, Void, String> {

        private String webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/usuario/edita-usuario/";

        @Override
        protected String doInBackground(String... params) {
            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(webAdd);
            HttpResponse resposta = null;
            String stringRes = "";

            try {
                chamada.setHeader("Authorization", "Basic " + s.getToken());

                Long userId = s.getUserPrefs();

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.addPart("userId", new StringBody(String.valueOf(userId), ContentType.TEXT_PLAIN));

                File file = new File(getFilesDir().getPath() + "image.png");

                if (file.exists()) {
                    entityBuilder.addPart("file", new FileBody(file));
                }

                entityBuilder.addPart("nome", new StringBody((params[0]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("dataNasc", new StringBody((params[1]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("telefone", new StringBody((params[2]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("celular", new StringBody((params[3]), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("whats", new StringBody((params[4]), ContentType.TEXT_PLAIN));

                HttpEntity entity = entityBuilder.build();

                chamada.setEntity(entity);
                resposta = cliente.execute(chamada);
                stringRes = EntityUtils.toString(resposta.getEntity());

                System.out.println(resposta.getStatusLine().getStatusCode());
                System.out.println(resposta.getStatusLine().getReasonPhrase());


            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringRes;
        }

        @Override
        protected void onPostExecute(String s) {

                if(s.equals("Sucesso!")) {

                    Toast.makeText(ctx, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AlterarDadosActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(ctx, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                }

        }
    }
}
