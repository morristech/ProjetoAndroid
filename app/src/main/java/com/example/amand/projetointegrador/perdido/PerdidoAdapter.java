package com.example.amand.projetointegrador.perdido;

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
import com.example.amand.projetointegrador.model.AnuncioPerdido;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PerdidoAdapter extends BaseAdapter {

    GridView grid;

    private List<AnuncioPerdido> perdidos = new ArrayList<>();

    private ImageView imgView;
    private TextView nomeAnimal;

    private Context mContext;

    // Constructor
    public PerdidoAdapter(Context c, List<AnuncioPerdido> anuncios) {
        this.mContext = c;
        this.perdidos = anuncios;

    }

    public int getCount() {
        return perdidos.size();
    }

    public Object getItem(int position) {
        return perdidos.get(position);
    }

    public long getItemId(int position) {
        return perdidos.indexOf(perdidos.get(position));
    }

    public class Holder {
        private TextView nomeAnimal;
        private ImageView imgView;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

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

        holder.nomeAnimal.setText(perdidos.get(position).getNome());

        if (!perdidos.get(position).getImgAnucio().isEmpty()) {
            Picasso.with(mContext).load(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/perdido/" + perdidos.get(position).getId() +
                    "/" + perdidos.get(position).getImgAnucio().get(0)).placeholder(R.drawable.imgplaceholder).into(holder.imgView);
        }

        return convertView;
    }
}
