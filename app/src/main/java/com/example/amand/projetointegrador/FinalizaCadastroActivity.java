package com.example.amand.projetointegrador;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class FinalizaCadastroActivity extends AppCompatActivity {

    private ImageView imgFinalizaCadastro;
    private FloatingActionButton uploadImageButton;
    private EditText finalizaCadastroTelefone;
    private EditText finalizaCadastroCelular;
    private EditText finalizaCadastroFacebook;
    private EditText finalizaCadastroWhats;
    private Button finalizaCadastroBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza_cadastro);

        imgFinalizaCadastro = (ImageView) findViewById(R.id.imgFinalizaCadastro);
        uploadImageButton = (FloatingActionButton) findViewById(R.id.uploadImageButton);
        finalizaCadastroTelefone = (EditText) findViewById(R.id.finalizaCadastroTelefone);
        finalizaCadastroCelular = (EditText) findViewById(R.id.finalizaCadastroCelular);
        finalizaCadastroFacebook = (EditText) findViewById(R.id.finalizaCadastroFacebook);
        finalizaCadastroWhats = (EditText) findViewById(R.id.finalizaCadastroWhats);
        finalizaCadastroBtn = (Button) findViewById(R.id.finalizaCadastroBtn);

        //https://github.com/ArthurHub/Android-Image-Cropper/wiki/Pick-image-for-cropping-from-Camera-or-Gallery

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

}
