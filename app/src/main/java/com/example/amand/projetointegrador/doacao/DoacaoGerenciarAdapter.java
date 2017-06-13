package com.example.amand.projetointegrador.doacao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.RegistroActivity;
import com.example.amand.projetointegrador.helpers.Session;
import com.example.amand.projetointegrador.model.Anuncio;
import com.example.amand.projetointegrador.model.AnuncioDoacao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by amanda on 12/06/17.
 */

public class DoacaoGerenciarAdapter extends BaseAdapter {

    ListView lista;

    private List<AnuncioDoacao> doacoes = new ArrayList<>();

    private ImageView imgView;
    private TextView nomeAnimal;
    private TextView dataPublicacao;
    private Button btnDelete;
    Session s;

    private int posicao;
    private Context mContext;

    // Constructor
    public DoacaoGerenciarAdapter(Context c, List<AnuncioDoacao> anuncios) {
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

        s = new Session(mContext);

        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.item_gerenciar, null);

            posicao = position;
            nomeAnimal = (TextView) grid.findViewById(R.id.nomeAnimal);
            nomeAnimal.setText(doacoes.get(position).getNome());

            dataPublicacao = (TextView) grid.findViewById(R.id.dataPublicacao);

            btnDelete = (Button) grid.findViewById(R.id.btnDelete);
            btnDelete.setTag(position);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            dataPublicacao.setText("Publicado em: "+ sdf.format(doacoes.get(position).getDataPublicacao()));

            imgView = (ImageView) grid.findViewById(R.id.imgAnimal);


            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer index = (Integer) btnDelete.getTag();


                    DeleteService del = new DeleteService(index);
                    del.execute();
                }
            });

            if(!doacoes.get(position).getImgAnucio().isEmpty()) {
                new DownloadImageTask((ImageView) grid.findViewById(R.id.imgAnimal)).execute(
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

    private class DeleteService extends AsyncTask<String, Void, String> {

        private int pos;

        public DeleteService(int position) {
            this.pos = position;
        }

        private String
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/delete-doacao/" + doacoes.get(pos).getId();
        @Override
        protected String doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpGet chamada = new HttpGet(webAdd);
            HttpResponse resposta = null;
            String systemRes = "";

            try {
                chamada.setHeader("Authorization", "Basic " + s.getToken());

                resposta = cliente.execute(chamada);
                systemRes = EntityUtils.toString(resposta.getEntity(), StandardCharsets.UTF_8);

                System.out.println(resposta.getStatusLine().getStatusCode());
                System.out.println(resposta.getStatusLine().getReasonPhrase());


            } catch (IOException e) {
                e.printStackTrace();
            }

            return systemRes;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("Sucesso")) {
                doacoes.remove(pos);
                notifyDataSetChanged();
            } else {
                Toast.makeText(mContext, "Ocorreu um erro ao excluir anuncio", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
