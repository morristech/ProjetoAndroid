package com.example.amand.projetointegrador.perdido;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.RegistroActivity;
import com.example.amand.projetointegrador.adapters.CustomPagerAdapter;
import com.example.amand.projetointegrador.doacao.DoacaoDetalhesActivity;
import com.example.amand.projetointegrador.helpers.Session;
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

public class PerdidoDetalhesActivity extends AppCompatActivity implements View.OnClickListener {

    private final List<String> images = new ArrayList<>();
    private final List<Bitmap> bitmaps = new ArrayList<>();

    private Long id;
    private Double lat;
    private Double lng;
    private TextView nomeAnimal;
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

    private Button btnContato;
    private Dialog contatoDialog;

    private String nomeUsuario = "";
    private String[] emailUsuario;
    private String telefoneUsuario = "";
    private String celularUsuario = "";
    private String whatsUsuario = "";

    private Button btnFechar, btnWhatsApp, btnEmail, btnTelefone, btnSms, getBtnWhatsApp, btnFacebook, btnCelular;
    private TextView nomeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perdido_detalhes);
        s = new Session(this);

        contatoDialog = new Dialog(this);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Detalhes de animal perdido");

        // custom dialog

        contatoDialog.setContentView(R.layout.dialog_contato);
        contatoDialog.setTitle("Title...");

        ctx = this;
        Intent i = getIntent();

        id = i.getLongExtra("perdido", 0);

        //DoacaoAdapter doacaoAdapter = new DoacaoAdapter(this);
        // Get intent data
        mapa = findViewById(R.id.mapaDetalhesPerdido);

        indicator = (CircleIndicator) findViewById(R.id.indicator);

        viewPager = (ViewPager) findViewById(R.id.imgSliderPerdido);

        nomeAnimal = (TextView) findViewById(R.id.nomeAnimal);

        racaAnimal = (TextView) findViewById(R.id.racaAnimal);

        sexoAnimal = (TextView) findViewById(R.id.sexoAnimal);

        corAnimal = (TextView) findViewById(R.id.corAnimal);

        porteAnimal = (TextView) findViewById(R.id.porteAnimal);

        observacaoAnimal = (TextView) findViewById(R.id.observacoesAnimal);

        nomeDialog = (TextView) contatoDialog.findViewById(R.id.nomeDialog);

        btnContato = (Button) findViewById(R.id.btnContato);

        btnFechar = (Button) contatoDialog.findViewById(R.id.btnFechar);
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contatoDialog.dismiss();
            }
        });

        btnCelular = (Button) contatoDialog.findViewById(R.id.btnCelular);
        btnCelular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contatoDialog.dismiss();
                String phone = celularUsuario;
                dialPhoneNumber(phone);
            }
        });

        btnWhatsApp = (Button) contatoDialog.findViewById(R.id.btnWhatsApp);
        btnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contatoDialog.dismiss();
                String nome = nomeUsuario;
                String phoneWhatsApp = whatsUsuario; // Não colocar o prefixo "+"
                String body = "Vi seu anúncio no Lucky Pets!";
                //openWhatsApp(phoneWhatsApp, body);
                insertContact(nome, phoneWhatsApp);
            }
        });

        btnEmail = (Button) contatoDialog.findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contatoDialog.dismiss();
                String[] address = emailUsuario;
                String subject = "Estou interessado no " +nomeAnimal.getText().toString();
                String body = "Olá, vi o seu anúncio pelo aplicativo Lucky Pets e fiquei interessado.";
                composeEmail(address, subject, body);
            }
        });

        btnTelefone = (Button) contatoDialog.findViewById(R.id.btnTelefone);
        btnTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contatoDialog.dismiss();
                String phone = telefoneUsuario;
                dialPhoneNumber(phone);
            }
        });

        btnSms = (Button) contatoDialog.findViewById(R.id.btnSms);
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contatoDialog.dismiss();
                String numero = celularUsuario;
                String body = "Olá, vi o seu anúncio pelo aplicativo Lucky Pets e fiquei interessado.";
                composeSms(numero, body);
            }
        });

        /*Button btnFacebook = (Button) contatoDialog.findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contatoDialog.dismiss();
                String idFacebook = "100001265208996";
                //getOpenFacebookIntent(context, idFacebook);
                newFacebookIntent(idFacebook);
            }
        });

        Button btnGooglePlus = (Button) contatoDialog.findViewById(R.id.btnGooglePlus);
        btnGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contatoDialog.dismiss();
                String idGPlus = "110537512799846453017";
                openGPlus(idGPlus);
            }
        });*/

        btnContato.setOnClickListener(this);

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

    /**
     * Intent para abrir a discagem (ligação)
     * @param phoneNumber número de telefone. Formatos válidos: "2125551212" ou "(212) 555 1212"
     */
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber)); // Apenas apps de discagem
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Ocorreu algum erro ao tentar usar o aplicativo de ligações.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Intent para abrir o serviço de e-mail (qualquer um)
     * @param address destinatários
     * @param subject assunto
     * @param body corpo do e-mail (texto base, citando o animal e etc)
     */
    public void composeEmail(String[] address, String subject, String body) {
        // se não funcionar, o correto seria "String[] addresses"
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // Apenas apps de e-mail
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Instale algum aplicativo de e-mail para acessar essa função", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Intent para abrir o Google Plus no perfil selecionado. Necessário passar o ID
     * @param profile passa o ID do perfil
     */
    public void openGPlus(String profile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", profile);
            startActivity(intent);
        } catch(ActivityNotFoundException e) {
            // Se não estiver instalado, abre no navegador
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/"+profile+"/posts")));
        }
    }

    /**
     * Intent para enviar uma mensagem de texto (sms)
     * @param numero numero do celular
     * @param message mensagem inicial
     */
    public void composeSms(String numero, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.setData(Uri.parse("smsto:" + numero));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Intent para inserir um contato automaticamente. Usado no lugar do WhatsApp
     * @param name
     * @param phone
     */
    public void insertContact(String name, String phone) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(ctx, "Ops! Algo deu errado!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Intent alternativa do Facebook
     * @param idFacebook The full URL to the Facebook page or profile.
     */
    public void newFacebookIntent(String idFacebook) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + idFacebook)));
    }

    @Override
    public void onClick(View v) {
        contatoDialog.show();
    }

    private class GetService extends AsyncTask<String, Void, String> {

        private String
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/get-perdido/" +id;

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
                JSONObject usuario = o.getJSONObject("usuario");
                JSONObject perfil = usuario.getJSONObject("perfil");

                /* Pega dados do usuário para popular o botão de contato */
                nomeUsuario = usuario.getString("nome");
                emailUsuario = new String[] {usuario.getString("email")};
                telefoneUsuario = perfil.getString("telefone");
                celularUsuario = perfil.getString("celular");
                whatsUsuario = perfil.getString("whatsapp");

                nomeDialog.setText(nomeUsuario);

                if(telefoneUsuario.equals("")) {
                    btnTelefone.setVisibility(View.GONE);
                }

                if(celularUsuario.equals("")){
                    btnSms.setVisibility(View.GONE);
                    btnCelular.setVisibility(View.GONE);
                }

                if(whatsUsuario.equals("")) {
                    btnWhatsApp.setVisibility(View.GONE);
                }

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

                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaDetalhesPerdido);

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
                    down.execute(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/perdido/" + id + "/" + str);
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
