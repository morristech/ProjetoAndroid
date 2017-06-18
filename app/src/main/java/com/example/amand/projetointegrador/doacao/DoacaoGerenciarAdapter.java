package com.example.amand.projetointegrador.doacao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.squareup.picasso.Picasso;

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

    private List<AnuncioDoacao> doacoes;

    Session s;

    private int posicao;
    private Context mContext;
    private LayoutInflater inflater = null;
    private Activity activity;

    // Constructor
    public DoacaoGerenciarAdapter(Context c, List<AnuncioDoacao> anuncios, Activity activity) {
        this.mContext = c;
        this.doacoes = anuncios;
        this.activity = activity;
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
        private ImageView imgView;
        private TextView nomeAnimal;
        private TextView dataPublicacao;
        private Button btnDelete;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        s = new Session(mContext);

        if (convertView == null) {

            this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_gerenciar, parent, false);
            holder = new Holder();

            posicao = position;
            holder.nomeAnimal = (TextView) convertView.findViewById(R.id.nomeAnimal);

            holder.dataPublicacao = (TextView) convertView.findViewById(R.id.dataPublicacao);
            holder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);

            holder.imgView = (ImageView) convertView.findViewById(R.id.imgAnimal);
            convertView.setTag(holder);

        } else {

            holder = (Holder) convertView.getTag();
        }

        if (!doacoes.get(position).getImgAnucio().isEmpty()) {
            Picasso.with(mContext).load(RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/file/doacao/" + doacoes.get(position).getId() +
                    "/" + doacoes.get(position).getImgAnucio().get(0)).placeholder(R.drawable.imgplaceholder).into(holder.imgView);
        }

        holder.nomeAnimal.setText(doacoes.get(position).getNome());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        holder.dataPublicacao.setText("Publicado em: " + sdf.format(doacoes.get(position).getDataPublicacao()));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setTitle("Deseja excluir o anúncio?");
                builder.setMessage("Esta operação não poderá ser desfeita");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        DeleteService del = new DeleteService(position);
                        del.execute();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return convertView;
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
