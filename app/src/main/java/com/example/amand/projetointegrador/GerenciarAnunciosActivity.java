package com.example.amand.projetointegrador;

import android.app.TaskStackBuilder;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class GerenciarAnunciosActivity extends AppCompatActivity {

    private BottomBar bb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_anuncios);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bb = (BottomBar) findViewById(R.id.bottomBar);
        bb.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_doacoes:
                        FragmentTransaction tx1 = getSupportFragmentManager().beginTransaction();
                        tx1.replace(R.id.flAnuncios, Fragment.instantiate(GerenciarAnunciosActivity.this, "com.example.amand.projetointegrador.doacao.DoacaoGerenciarFragment"));
                        tx1.addToBackStack(null);
                        tx1.commit();
                        getSupportActionBar().setTitle("Minhas doações");
                        break;

                    case R.id.tab_encontrados:
                        FragmentTransaction tx2 = getSupportFragmentManager().beginTransaction();
                        tx2.replace(R.id.flAnuncios, Fragment.instantiate(GerenciarAnunciosActivity.this, "com.example.amand.projetointegrador.encontrado.EncontradoGerenciarFragment"));
                        tx2.addToBackStack(null);
                        tx2.commit();
                        getSupportActionBar().setTitle("Meus animais encontrados");
                        break;

                    case R.id.tab_perdidos:
                        FragmentTransaction tx3 = getSupportFragmentManager().beginTransaction();
                        tx3.replace(R.id.flAnuncios, Fragment.instantiate(GerenciarAnunciosActivity.this, "com.example.amand.projetointegrador.perdido.PerdidoGerenciarFragment"));
                        tx3.addToBackStack(null);
                        tx3.commit();
                        getSupportActionBar().setTitle("Meus animais perdidos");
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
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
}
