package com.example.amand.projetointegrador;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.model.NumericRange;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.mvc.imagepicker.ImagePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FinalizaCadastroActivity extends AppCompatActivity {

    private ImageView imgFinalizaCadastro;
    private FloatingActionButton uploadImageButton;
    private EditText finalizaCadastroTelefone;
    private EditText finalizaCadastroCelular;
    private EditText finalizaCadastroFacebook;
    private EditText finalizaCadastroWhats;
    private Button finalizaCadastroBtn;
    private AwesomeValidation awesomeValidation;

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
        ImagePicker.setMinQuality(600, 600);
        awesomeValidation.addValidation(finalizaCadastroTelefone, RegexTemplate.TELEPHONE, "Telefone Inválido");
        awesomeValidation.addValidation(finalizaCadastroCelular, RegexTemplate.TELEPHONE, "Celular inválido");
        awesomeValidation.addValidation(finalizaCadastroWhats, RegexTemplate.TELEPHONE, "Numero inválido");

        //https://github.com/ArthurHub/Android-Image-Cropper/wiki/Pick-image-for-cropping-from-Camera-or-Gallery

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onPickImage(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);

        // TODO do something with the bitmap

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

}

