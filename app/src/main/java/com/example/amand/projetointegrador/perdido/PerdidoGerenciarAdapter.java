package com.example.amand.projetointegrador.perdido;

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
import com.example.amand.projetointegrador.encontrado.EncontradoGerenciarAdapter;
import com.example.amand.projetointegrador.helpers.Session;
import com.example.amand.projetointegrador.model.AnuncioPerdido;

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
 * Created by amanda on 13/06/17.
 */

public class PerdidoGerenciarAdapter extends BaseAdapter{

    ListView lista;


    private List<AnuncioPerdido> perdidos = new ArrayList<>();
    private int posicao;

    private ImageView imgView;

    private Context mContext;
    private TextView nomeAnimal;
    private TextView dataPublicacao;
    private Button btnDelete;
    Session s;

    // Constructor
    public PerdidoGerenciarAdapter(Context c, List<AnuncioPerdido> anuncios) {
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
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        View list;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        s = new Session(mContext);

        if (convertView == null) {

            list = new View(mContext);
            list = inflater.inflate(R.layout.item_gerenciar, null);

            posicao = position;
            nomeAnimal = (TextView) list.findViewById(R.id.nomeAnimal);
            nomeAnimal.setText(perdidos.get(position).getNome());

            dataPublicacao = (TextView) list.findViewById(R.id.dataPublicacao);

            btnDelete = (Button) list.findViewById(R.id.btnDelete);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            dataPublicacao.setText("Publicado em: "+ sdf.format(perdidos.get(position).getDataPublicacao()));

            imgView = (ImageView) list.findViewById(R.id.imgAnimal);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteService del = new DeleteService(posicao);
                    del.execute();
                }
            });

            if(!perdidos.get(position).getImgAnucio().isEmpty()) {
                new DownloadImageTask((ImageView) list.findViewById(R.id.imgAnimal)).execute(
                        RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/perdido/" + perdidos.get(position).getId() +
                                "/" + perdidos.get(position).getImgAnucio().get(0));
            }

        } else {
            list = (View) convertView;
        }

        return list;
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
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/delete-perdido/" + perdidos.get(pos).getId();
        AnuncioPerdido anuncio = perdidos.get(pos);

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
                perdidos.remove(pos);
                notifyDataSetChanged();

            } else {
                Toast.makeText(mContext, "Ocorreu um erro ao excluir anuncio", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
