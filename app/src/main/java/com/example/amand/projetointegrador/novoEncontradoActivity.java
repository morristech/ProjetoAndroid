package com.example.amand.projetointegrador;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class novoEncontradoActivity extends AppCompatActivity {

    private RadioGroup radioLocalizacao;
    private RadioButton localizacaoAtual;
    private RadioGroup digitarLocalizacaoEncontrado;
    private EditText digitaEndereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_encontrado);

        radioLocalizacao = (RadioGroup) findViewById(R.id.radioLocalizacao);
        digitaEndereco = (EditText) findViewById(R.id.digitaEndereco);

        radioLocalizacao.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.localizacaoAtual:
                        if(digitaEndereco.getVisibility() == View.VISIBLE) {
                            digitaEndereco.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.digitarEnderecoEncontrado:
                        digitaEndereco.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Novo animal encontrado");
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
