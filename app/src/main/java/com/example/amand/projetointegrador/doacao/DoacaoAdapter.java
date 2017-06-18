package com.example.amand.projetointegrador.doacao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.RegistroActivity;
import com.example.amand.projetointegrador.model.AnuncioDoacao;
import com.facebook.internal.Utility;
import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amanda on 29/03/17.
 */

public class DoacaoAdapter extends BaseAdapter {

    GridView grid;

    private List<AnuncioDoacao> doacoes;

    private ImageView imgView;
    private TextView nomeAnimal;

    private Context mContext;

    // Constructor
    public DoacaoAdapter(Context c, List<AnuncioDoacao> anuncios) {
        this.mContext = c;
        this.doacoes = anuncios;
    }

    public int getCount() {
        return doacoes.size();
    }

    public Object getItem(int position) {
        return doacoes.get(position);
    }

    public long getItemId(int position) {
        return doacoes.indexOf(doacoes.get(position));
    }

    public class Holder {
        private TextView nomeAnimal;
        private ImageView imgView;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        View grid;
        Holder holder = null;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);

            holder = new Holder();

            holder.nomeAnimal = (TextView) convertView.findViewById(R.id.nomeAnimal);
            holder.imgView = (ImageView) convertView.findViewById(R.id.itemImg);

            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.nomeAnimal.setText(doacoes.get(position).getNome());

        if (!doacoes.get(position).getImgAnucio().isEmpty()) {
            Picasso.with(mContext).load(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/doacao/" + doacoes.get(position).getId() +
                    "/" + doacoes.get(position).getImgAnucio().get(0)).placeholder(R.drawable.imgplaceholder).into(holder.imgView);
        }

        return convertView;
    }

}
