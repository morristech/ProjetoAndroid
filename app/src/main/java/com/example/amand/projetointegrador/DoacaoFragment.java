package com.example.amand.projetointegrador;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.amand.projetointegrador.model.AnuncioDoacao;
import com.example.amand.projetointegrador.model.PerfilUsuario;
import com.example.amand.projetointegrador.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

//FRAGMENTO DE MOSTRA DE ANUNCIOS DE ANIMAIS PARA DOAÇÃO COMO GRIDVIEW


public class DoacaoFragment extends Fragment {

    private GridView gridDoacao;
    Context context;
    private Session session;

    private OnFragmentInteractionListener mListener;

    public DoacaoFragment() {
        // Required empty public constructor
    }

    public static DoacaoFragment newInstance(String param1, String param2) {
        DoacaoFragment fragment = new DoacaoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doacao, container, false);

        context = this.getActivity().getApplicationContext();
        gridDoacao = (GridView) view.findViewById(R.id.gridDoacao);

        GetService get = new GetService();
        get.execute();

        session = new Session(context);

        gridDoacao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(context, DoacaoDetalhesActivity.class);
                // Pass image index
                i.putExtra("id", position);
                startActivity(i);
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class GetService extends AsyncTask<String, Void, String> {

        private String
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/get-doacoes";

        @Override
        protected String doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpGet chamada = new HttpGet(webAdd);
            HttpResponse resposta = null;
            String systemRes = "";

            try {

                chamada.setHeader("Authorization", "Basic " + session.getToken());

                resposta = cliente.execute(chamada);
                systemRes = EntityUtils.toString(resposta.getEntity());

                System.out.println(resposta.getStatusLine().getStatusCode());
                System.out.println(resposta.getStatusLine().getReasonPhrase());


            } catch (IOException e) {
                e.printStackTrace();
            }

            return systemRes;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s != null) {

                List<AnuncioDoacao> listAnuncio = new ArrayList<>();

                try {
                    JSONArray array = new JSONArray(s);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);

                        AnuncioDoacao ad = new AnuncioDoacao();

                        JSONArray imgs = obj.getJSONArray("imgAnuncio");
                        List<String> list = new ArrayList<String>();
                        for (int j = 0; j < imgs.length(); j++) {
                            list.add(imgs.getJSONObject(i).getString("imgAnuncio"));
                        }

                        ad.setId(obj.getLong("id"));
                        ad.setImgAnucio(list);
                        ad.setCastrado(obj.getBoolean("castrado"));
                        ad.setDeficiencia(obj.getBoolean("deficiente"));
                        ad.setIdade(obj.getInt("idade"));
                        ad.setRaca(obj.getString("raca"));
                        ad.setNome(obj.getString("nome"));
                        ad.setCor(obj.getString("cor"));
                        ad.setObservacoes(obj.getString("observacoes"));
                        ad.setSexo(obj.getString("sexo"));
                        ad.setTipo(obj.getString("tipo"));

                        JSONObject user = obj.getJSONObject("usuario");
                        Usuario usuario = new Usuario();
                        usuario.setId(user.getLong("id"));
                        usuario.setEmail(user.getString("email"));
                        usuario.setNome(user.getString("nome"));

                        PerfilUsuario perfil = new PerfilUsuario();
                        JSONObject objPerfil = obj.getJSONObject("perfil");
                        perfil.setId(objPerfil.getLong("id"));
                        perfil.setTelefone(objPerfil.getString("telefone"));
                        perfil.setFaceUser(objPerfil.getString("faceUser"));
                        perfil.setWhatsapp(objPerfil.getString("whatsapp"));
                        perfil.setCelular(objPerfil.getString("celular"));

                        usuario.setPerfil(perfil);
                        ad.setUsuario(usuario);

                        Date date = new Date(obj.getLong("dataPublicacao"));

                        ad.setDataPublicacao(date);

                        listAnuncio.add(ad);
                    }

                    gridDoacao.setAdapter(new DoacaoAdapter(context, listAnuncio));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
