package com.example.amand.projetointegrador.doacao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.RegistroActivity;
import com.example.amand.projetointegrador.helpers.Session;
import com.example.amand.projetointegrador.model.Anuncio;
import com.example.amand.projetointegrador.model.AnuncioDoacao;
import com.example.amand.projetointegrador.model.PerfilUsuario;
import com.example.amand.projetointegrador.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

//FRAGMENTO DE MOSTRA DE ANUNCIOS DE ANIMAIS PARA DOAÇÃO COMO GRIDVIEW


public class DoacaoFragment extends Fragment {

    private GridView gridDoacao;
    Context context;
    private Session session;
    final List<AnuncioDoacao> listAnuncio = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    private ProgressDialog mProgressDialog;


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
        GetService get = new GetService();
        get.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doacao, container, false);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        context = this.getActivity().getApplicationContext();
        gridDoacao = (GridView) view.findViewById(R.id.gridDoacao);

        mProgressDialog = new ProgressDialog(this.getActivity());
        mProgressDialog.setMessage("Filtrando...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GetService get2 = new GetService();
                get2.execute();
            }
        });

        session = new Session(context);

        System.out.println(session.getToken() + "*******************");

        gridDoacao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(context, DoacaoDetalhesActivity.class);
                // Pass image index
                AnuncioDoacao ad = (AnuncioDoacao) gridDoacao.getAdapter().getItem(position);
                i.putExtra("doacao", ad.getId());
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

            if (swipeRefresh.isRefreshing()) {
                swipeRefresh.setRefreshing(false);
            }

            if (s != null) {

                try {

                    listAnuncio.clear();

                    JSONArray array = new JSONArray(s);

                    final int numberIterator = array.length();
                    for (int i = 0; i < numberIterator; i++) {
                        JSONObject obj = array.getJSONObject(i);

                        AnuncioDoacao ad = new AnuncioDoacao();

                        JSONArray imgs = obj.getJSONArray("imgAnuncio");

                        List<String> list = new ArrayList<String>();
                        if (imgs.length() > 0) {
                            for (int j = 0; j < imgs.length(); j++) {
                                list.add(imgs.get(j).toString());
                            }
                        }

                        ad.setNome(obj.getString("nome"));
                        ad.setId(obj.getLong("id"));
                        ad.setImgAnucio(list);
                        ad.setCastrado(obj.getBoolean("castrado"));
                        ad.setDeficiencia(obj.getBoolean("deficiencia"));
                        ad.setIdade(obj.getInt("idade"));
                        ad.setRaca(obj.getString("raca"));
                        ad.setCor(obj.getString("cor"));
                        ad.setDescricao(obj.getString("descricao"));
                        ad.setSexo(obj.getString("sexo"));
                        ad.setTipo(obj.getString("tipo"));
                        ad.setPorte(obj.getString("porte"));

                        JSONObject user = obj.getJSONObject("usuario");
                        Usuario usuario = new Usuario();
                        usuario.setId(user.getLong("id"));
                        usuario.setEmail(user.getString("email"));
                        usuario.setNome(user.getString("nome"));

                        PerfilUsuario perfil = new PerfilUsuario();
                        JSONObject objPerfil = user.getJSONObject("perfil");
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            DoacaoAdapter da = new DoacaoAdapter(context, listAnuncio);
            gridDoacao.setAdapter(da);
        }

    }

    private class GetFiltered extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpGet chamada =
                    new HttpGet(RegistroActivity.ENDERECO_WEB +
                            "/adotapet-servidor/api/anuncio/get-doacao-filtered?tipo="+params[0]+"&porte="
                            +params[1]+"&sexo="+params[2]);
            HttpResponse resposta = null;
            String systemRes = "";

            try {

                chamada.setHeader("Authorization", "Basic " + session.getToken());

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

            mProgressDialog.dismiss();

            if (swipeRefresh.isRefreshing()) {
                swipeRefresh.setRefreshing(false);
            }

            if (s != null) {

                try {
                    JSONArray array = new JSONArray(s);

                    listAnuncio.clear();

                    final int numberIterator = array.length();
                    for (int i = 0; i < numberIterator; i++) {
                        JSONObject obj = array.getJSONObject(i);

                        AnuncioDoacao ad = new AnuncioDoacao();

                        JSONArray imgs = obj.getJSONArray("imgAnuncio");

                        List<String> list = new ArrayList<String>();
                        if (imgs.length() > 0) {
                            for (int j = 0; j < imgs.length(); j++) {
                                list.add(imgs.get(j).toString());
                            }
                        }

                        ad.setId(obj.getLong("id"));
                        ad.setImgAnucio(list);
                        ad.setCastrado(obj.getBoolean("castrado"));
                        ad.setDeficiencia(obj.getBoolean("deficiencia"));
                        ad.setIdade(obj.getInt("idade"));
                        ad.setRaca(obj.getString("raca"));
                        ad.setNome(obj.getString("nome"));
                        ad.setCor(obj.getString("cor"));
                        ad.setDescricao(obj.getString("descricao"));
                        ad.setSexo(obj.getString("sexo"));
                        ad.setTipo(obj.getString("tipo"));
                        ad.setPorte(obj.getString("porte"));

                        JSONObject user = obj.getJSONObject("usuario");
                        Usuario usuario = new Usuario();
                        usuario.setId(user.getLong("id"));
                        usuario.setEmail(user.getString("email"));
                        usuario.setNome(user.getString("nome"));

                        PerfilUsuario perfil = new PerfilUsuario();
                        JSONObject objPerfil = user.getJSONObject("perfil");
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
                        DoacaoAdapter da = new DoacaoAdapter(context, listAnuncio);
                        gridDoacao.setAdapter(da);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void getDoacoesFilter(String tipo, String porte, String sexo) {

        new GetFiltered().execute(tipo, porte, sexo);
        mProgressDialog.show();

    }
}
