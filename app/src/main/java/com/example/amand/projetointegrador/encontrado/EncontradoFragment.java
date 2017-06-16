package com.example.amand.projetointegrador.encontrado;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.RegistroActivity;
import com.example.amand.projetointegrador.doacao.DoacaoDetalhesActivity;
import com.example.amand.projetointegrador.helpers.Session;
import com.example.amand.projetointegrador.model.AnuncioEncontrado;
import com.example.amand.projetointegrador.model.PerfilUsuario;
import com.example.amand.projetointegrador.model.Usuario;
import com.example.amand.projetointegrador.encontrado.EncontradoAdapter;
import com.example.amand.projetointegrador.encontrado.EncontradoFragment;
import com.roughike.bottombar.BottomBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;


/** FRAGMENTO DE MOSTRA DE ANUNCIOS DE ANIMAIS ENCONTRADOS COMO GRIDVIEW */

public class EncontradoFragment extends Fragment {
    private GridView gridEncontrado;
    Context context;
    private Session session;
    final List<AnuncioEncontrado> listAnuncio = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;


    private EncontradoFragment.OnFragmentInteractionListener mListener;

    public EncontradoFragment() {
        // Required empty public constructor
    }

    public static EncontradoFragment newInstance(String param1, String param2) {
        EncontradoFragment fragment = new EncontradoFragment();
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
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        context = this.getActivity().getApplicationContext();
        gridEncontrado = (GridView) view.findViewById(R.id.gridDoacao);
        GetService get = new GetService();
        get.execute();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Esse negocio tá me fodendo
                EncontradoFragment.GetService get2 = new EncontradoFragment.GetService();
                get2.execute();
            }
        });

        session = new Session(context);

        System.out.println(session.getToken() + "*******************");

        gridEncontrado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(context, EncontradoDetalhesActivity.class);
                // Pass image index
                AnuncioEncontrado ap = (AnuncioEncontrado) gridEncontrado.getAdapter().getItem(position);
                i.putExtra("encontrado", ap.getId());
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
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/get-encontrados";

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

                        AnuncioEncontrado ae = new AnuncioEncontrado();

                        JSONArray imgs = obj.getJSONArray("imgAnuncio");

                        List<String> list = new ArrayList<String>();
                        if (imgs.length() > 0) {
                            for (int j = 0; j < imgs.length(); j++) {
                                list.add(imgs.get(j).toString());
                            }
                        }

                        ae.setId(obj.getLong("id"));
                        ae.setImgAnucio(list);
                        ae.setTitulo(obj.getString("titulo"));
                        ae.setCor(obj.getString("cor"));
                        ae.setDescricao(obj.getString("descricao"));
                        ae.setSexo(obj.getString("sexo"));
                        ae.setTipo(obj.getString("tipo"));
                        ae.setResgatado(obj.getBoolean("resgatado"));
                        ae.setPorte(obj.getString("porte"));

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
                        ae.setUsuario(usuario);

                        Date date = new Date(obj.getLong("dataPublicacao"));

                        ae.setDataPublicacao(date);

                        listAnuncio.add(ae);
                        EncontradoAdapter ea = new EncontradoAdapter(context, listAnuncio);
                        gridEncontrado.setAdapter(ea);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class GetFiltered extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpGet chamada = new HttpGet(RegistroActivity.ENDERECO_WEB +
                    "/adotapet-servidor/api/anuncio/get-encontrados-filtered?tipo="+params[0]+"&porte="
                    +params[1]+"&sexo="+params[2]);
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

                try {
                    JSONArray array = new JSONArray(s);

                    listAnuncio.clear();

                    final int numberIterator = array.length();
                    for (int i = 0; i < numberIterator; i++) {
                        JSONObject obj = array.getJSONObject(i);

                        AnuncioEncontrado ae = new AnuncioEncontrado();

                        JSONArray imgs = obj.getJSONArray("imgAnuncio");

                        List<String> list = new ArrayList<String>();
                        if (imgs.length() > 0) {
                            for (int j = 0; j < imgs.length(); j++) {
                                list.add(imgs.get(j).toString());
                            }
                        }

                        ae.setId(obj.getLong("id"));
                        ae.setImgAnucio(list);
                        ae.setTitulo(obj.getString("titulo"));
                        ae.setCor(obj.getString("cor"));
                        ae.setDescricao(obj.getString("descricao"));
                        ae.setSexo(obj.getString("sexo"));
                        ae.setTipo(obj.getString("tipo"));
                        ae.setResgatado(obj.getBoolean("resgatado"));
                        ae.setPorte(obj.getString("porte"));

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
                        ae.setUsuario(usuario);

                        Date date = new Date(obj.getLong("dataPublicacao"));

                        ae.setDataPublicacao(date);

                        listAnuncio.add(ae);
                        EncontradoAdapter ea = new EncontradoAdapter(context, listAnuncio);
                        gridEncontrado.setAdapter(ea);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void getEncontradosFilter(String tipo, String porte, String sexo) {

        new GetFiltered().execute(tipo, porte, sexo);

    }
}
