package com.example.amand.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class ChoiceActivity extends AppCompatActivity {

    private ImageButton novaDoacao;
    private ImageButton novoPerdido;
    private ImageButton novoEncontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        novaDoacao = (ImageButton) findViewById(R.id.btnDoacao);
        novoPerdido = (ImageButton) findViewById(R.id.btnPerdido);
        novoEncontrado = (ImageButton) findViewById(R.id.btnEncontrado);

        novaDoacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, novaDoacaoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        novoPerdido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, novoPerdidoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        novoEncontrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, novoEncontradoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Criar um novo anúncio");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
