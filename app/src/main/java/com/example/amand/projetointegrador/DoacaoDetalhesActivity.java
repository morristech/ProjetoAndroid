package com.example.amand.projetointegrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DoacaoDetalhesActivity extends AppCompatActivity {

    public String[] nomeStrings;
    public String[] racaStrings;
    public String[] sexoStrings;
    public Integer[] idadeInts;
    public String[] corStrings;
    public Boolean[] castradoBoolean;
    public Boolean[] vacinadoBoolean;
    public String[] observacaoStrings;

    private TextView nomeAnimal;
    private TextView racaAnimal;
    private TextView sexoAnimal;
    private TextView idadeAnimal;
    private TextView corAnimal;
    private TextView castradoAnimal;
    private TextView vacinadoAnimal;
    private TextView observacaoAnimal;

    private ImageSwitcher imageSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doacao_detalhes);

        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");
        //DoacaoAdapter doacaoAdapter = new DoacaoAdapter(this);
        // Get intent data


        imageSwitcher = (ImageSwitcher) findViewById(R.id.imgSliderDoacao);

        //imageSwitcher.setImageResource(doacaoAdapter.mThumbIds[position]);

        nomeAnimal = (TextView) findViewById(R.id.nomeAnimal);
        nomeAnimal.setText(nomeStrings[position]);

        racaAnimal = (TextView) findViewById(R.id.racaAnimal);
        racaAnimal.setText(racaStrings[position]);

        sexoAnimal = (TextView) findViewById(R.id.sexoAnimal);
        sexoAnimal.setText(sexoStrings[position]);

        idadeAnimal = (TextView) findViewById(R.id.idadeAnimal);
        idadeAnimal.setText(String.valueOf(idadeInts[position]));

        corAnimal = (TextView) findViewById(R.id.corAnimal);
        corAnimal.setText(corStrings[position]);

        castradoAnimal = (TextView) findViewById(R.id.castradoAnimal);
        castradoAnimal.setText(castradoBoolean[position] ? "Castrado: Sim" : "Castrado: Não");

        vacinadoAnimal = (TextView) findViewById(R.id.vacinadoAnimal);
        vacinadoAnimal.setText(vacinadoBoolean[position] ? "Vacinado: Sim" : "Vacinado: Não");

        observacaoAnimal = (TextView) findViewById(R.id.observacoesAnimal);
        observacaoAnimal.setText(observacaoStrings[position]);

    }
}
