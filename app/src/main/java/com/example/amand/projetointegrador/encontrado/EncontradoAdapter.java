package com.example.amand.projetointegrador.encontrado;

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
import com.example.amand.projetointegrador.model.AnuncioEncontrado;
import com.example.amand.projetointegrador.model.AnuncioPerdido;
import com.example.amand.projetointegrador.perdido.PerdidoAdapter;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amanda on 02/06/17.
 */

public class EncontradoAdapter extends BaseAdapter {

    GridView grid;

    private List<AnuncioEncontrado> encontrados = new ArrayList<>();

    private ImageView imgView;
    private TextView tituloAnuncio;

    private Context mContext;

    // Constructor
    public EncontradoAdapter(Context c, List<AnuncioEncontrado> anuncios) {
        this.mContext = c;
        this.encontrados = anuncios;

    }

    public int getCount() {
        return encontrados.size();
    }

    public Object getItem(int position) {
        return encontrados.get(position);
    }

    public long getItemId(int position) {
        return encontrados.indexOf(encontrados.get(position));
    }

    public class Holder {
        private TextView tituloAnuncio;
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
            convertView = inflater.inflate(R.layout.item, null);
            holder = new Holder();

            holder.tituloAnuncio = (TextView) convertView.findViewById(R.id.nomeAnimal);

            holder.imgView = (ImageView) convertView.findViewById(R.id.itemImg);

            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tituloAnuncio.setText(encontrados.get(position).getTitulo());

        if (!encontrados.get(position).getImgAnucio().isEmpty()) {
            Picasso.with(mContext).load(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/encontrado/" + encontrados.get(position).getId() +
                    "/" + encontrados.get(position).getImgAnucio().get(0)).placeholder(R.drawable.imgplaceholder).into(holder.imgView);
        }

        return convertView;
    }
}
