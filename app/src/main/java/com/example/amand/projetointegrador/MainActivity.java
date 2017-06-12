package com.example.amand.projetointegrador;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.amand.projetointegrador.helpers.AlertDialogFragment;
import com.example.amand.projetointegrador.helpers.Session;
import com.google.android.gms.maps.model.Circle;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Session session;
    private CircleImageView barUserImg;
    private TextView barUserName;
    private TextView barUserEmail;
    private Context ctx;

    private BottomBar bb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        barUserImg = (CircleImageView) findViewById(R.id.barUserImg);
        barUserName = (TextView) findViewById(R.id.barUserName);
        barUserEmail = (TextView) findViewById(R.id.barUserEmail);

        ctx = this;
        session = new Session(this);

        toolbar.setTitle("Adota Pet");


        bb = (BottomBar) findViewById(R.id.bottomBar);
        bb.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_doacoes:
                        FragmentTransaction tx1 = getSupportFragmentManager().beginTransaction();
                        tx1.replace(R.id.flAnuncios, Fragment.instantiate(MainActivity.this, "com.example.amand.projetointegrador.doacao.DoacaoFragment"));
                        tx1.addToBackStack(null);
                        tx1.commit();
                        getSupportActionBar().setTitle("Doações");
                        break;

                    case R.id.tab_encontrados:
                        FragmentTransaction tx2 = getSupportFragmentManager().beginTransaction();
                        tx2.replace(R.id.flAnuncios, Fragment.instantiate(MainActivity.this, "com.example.amand.projetointegrador.encontrado.EncontradoFragment"));
                        tx2.addToBackStack(null);
                        tx2.commit();
                        getSupportActionBar().setTitle("Animais encontrados");
                        break;

                    case R.id.tab_perdidos:
                        FragmentTransaction tx3 = getSupportFragmentManager().beginTransaction();
                        tx3.replace(R.id.flAnuncios, Fragment.instantiate(MainActivity.this, "com.example.amand.projetointegrador.perdido.PerdidoFragment"));
                        tx3.addToBackStack(null);
                        tx3.commit();
                        getSupportActionBar().setTitle("Animais perdidos");
                        break;
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        barUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.barUserEmail);
        barUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.barUserName);

        GetDados get = new GetDados();
        get.execute();

        new DownloadImageTask((CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.barUserImg))
                .execute(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/" + session.getUserPrefs() + "/" + session.getUserImg());

        System.out.print(session.getUserImg());

        if(session.getUserEmail() != null && session.getUserName() != null) {
            barUserEmail.setText(session.getUserEmail());
            barUserName.setText(session.getUserName());
        }

    }


    private class GetDados extends AsyncTask<String, Void, String> {

        private String
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/usuario/getuserdata/"+session.getUserEmail();

        @Override
        protected String doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpGet chamada = new HttpGet(webAdd);
            HttpResponse resposta = null;
            String systemRes = "";

            try {

                chamada.setHeader("Authorization", "Basic " + session.getToken());

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

                session.setUserImg(perfil.getString("fotoPerfil"));
                session.setUserName(obj.getString("nome"));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.filtrarAnuncios) {
            // custom dialog
            final Dialog dialog = new Dialog(ctx);
            dialog.setContentView(R.layout.filtros);

            Spinner tipo = (Spinner) dialog.findViewById(R.id.spinnertipo);
            Spinner porte = (Spinner) dialog.findViewById(R.id.spinnerporte);
            Spinner sexo = (Spinner) dialog.findViewById(R.id.spinnersexo);

            String tipoStr = tipo.getSelectedItem().toString();
            String porteStr = porte.getSelectedItem().toString();
            String sexoStr = sexo.getSelectedItem().toString();

            Button dialogButton = (Button) dialog.findViewById(R.id.btnFiltrar);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bb.getCurrentTabId() == R.id.tab_doacoes) {

                    } else if(bb.getCurrentTabId() == R.id.tab_encontrados) {

                    } if(bb.getCurrentTabId() == R.id.tab_perdidos) {

                    }
                }
            });

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_novoanuncio) {
            Intent intent = new Intent(MainActivity.this, ChoiceActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_meusanuncios) {
            Intent mine = new Intent(MainActivity.this, GerenciarAnunciosActivity.class);
            startActivity(mine);

        } else if (id == R.id.nav_vizanuncios) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flAnuncios, Fragment.instantiate(MainActivity.this, "com.example.amand.projetointegrador.doacao.DoacaoFragment"));
            tx.addToBackStack(null);
            tx.commit();
        } else if (id == R.id.nav_editprofile) {
            Intent i = new Intent(MainActivity.this, AlterarDadosActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_sobre) {

        } else if (id == R.id.nav_logout){
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        session.setLoggedin(false);
        session.setToken("");
        session.setUserEmail("");
        session.setUserImg("");
        session.setUserName("");
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
}
