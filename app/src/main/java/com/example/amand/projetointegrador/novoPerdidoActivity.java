package com.example.amand.projetointegrador;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class novoPerdidoActivity extends AppCompatActivity {

    private EditText digitarEnderecoPerdido;
    private RadioGroup localizacaoPerdido;
    private RadioButton localizacaoAtual;
    private RadioButton digitaEndereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_perdido);

        digitarEnderecoPerdido = (EditText) findViewById(R.id.digitarEnderecoPerdido);
        localizacaoPerdido = (RadioGroup) findViewById(R.id.localizacaoRadio);
        localizacaoAtual = (RadioButton) findViewById(R.id.localizacaoAtual);
        digitaEndereco = (RadioButton) findViewById(R.id.digitaEndereco);

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

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Novo animal perdido");
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
