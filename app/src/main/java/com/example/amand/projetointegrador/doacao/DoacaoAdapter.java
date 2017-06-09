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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amanda on 29/03/17.
 */

public class DoacaoAdapter extends BaseAdapter {

    GridView grid;


    private List<AnuncioDoacao> doacoes = new ArrayList<>();

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
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.item, null);

            nomeAnimal = (TextView) grid.findViewById(R.id.nomeAnimal);
            nomeAnimal.setText(doacoes.get(position).getNome());

            imgView = (ImageView) grid.findViewById(R.id.itemImg);

            if(!doacoes.get(position).getImgAnucio().isEmpty()) {
                new DownloadImageTask((ImageView) grid.findViewById(R.id.itemImg)).execute(
                        RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/doacao/" + doacoes.get(position).getId() +
                                "/" + doacoes.get(position).getImgAnucio().get(0));
            }

        } else {
            grid = (View) convertView;
        }

        return grid;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
