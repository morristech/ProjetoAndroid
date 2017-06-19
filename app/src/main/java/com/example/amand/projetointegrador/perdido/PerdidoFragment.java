package com.example.amand.projetointegrador.perdido;

import android.app.ProgressDialog;
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

import com.example.amand.projetointegrador.doacao.DoacaoDetalhesActivity;
import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.RegistroActivity;
import com.example.amand.projetointegrador.helpers.Session;
import com.example.amand.projetointegrador.model.AnuncioPerdido;
import com.example.amand.projetointegrador.model.PerfilUsuario;
import com.example.amand.projetointegrador.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;


//FRAGMENTO DE MOSTRA DE ANUNCIOS DE ANIMAIS PERDIDOS COMO GRIDVIEW

public class PerdidoFragment extends Fragment {

    private GridView gridPerdido;
    Context context;
    private Session session;
    final List<AnuncioPerdido> listAnuncio = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    private ProgressDialog mProgressDialog;


    private PerdidoFragment.OnFragmentInteractionListener mListener;

    public PerdidoFragment() {
        // Required empty public constructor
    }

    public static PerdidoFragment newInstance(String param1, String param2) {
        PerdidoFragment fragment = new PerdidoFragment();
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
        gridPerdido = (GridView) view.findViewById(R.id.gridDoacao);
        GetService get = new GetService();
        get.execute();

        mProgressDialog = new ProgressDialog(this.getActivity());
        mProgressDialog.setMessage("Signing........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Esse negocio tá me fodendo
                GetService get2 = new GetService();
                get2.execute();
            }
        });

        session = new Session(context);

        System.out.println(session.getToken() + "*******************");

        gridPerdido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(context, PerdidoDetalhesActivity.class);
                // Pass image index
                AnuncioPerdido ap = (AnuncioPerdido) gridPerdido.getAdapter().getItem(position);
                i.putExtra("perdido", ap.getId());
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
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/get-perdidos";

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
                    JSONArray array = new JSONArray(s);

                    listAnuncio.clear();

                    final int numberIterator = array.length();
                    for (int i = 0; i < numberIterator; i++) {
                        JSONObject obj = array.getJSONObject(i);

                        AnuncioPerdido ap = new AnuncioPerdido();

                        JSONArray imgs = obj.getJSONArray("imgAnuncio");

                        List<String> list = new ArrayList<String>();
                        if (imgs.length() > 0) {
                            for (int j = 0; j < imgs.length(); j++) {
                                list.add(imgs.get(j).toString());
                            }
                        }

                        ap.setId(obj.getLong("id"));
                        ap.setImgAnucio(list);
                        ap.setRaca(obj.getString("raca"));
                        ap.setNome(obj.getString("nome"));
                        ap.setCor(obj.getString("cor"));
                        ap.setDescricao(obj.getString("descricao"));
                        ap.setSexo(obj.getString("sexo"));
                        ap.setTipo(obj.getString("tipo"));
                        ap.setPorte(obj.getString("porte"));

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
                        ap.setUsuario(usuario);

                        Date date = new Date(obj.getLong("dataPublicacao"));

                        ap.setDataPublicacao(date);

                        listAnuncio.add(ap);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            PerdidoAdapter pa = new PerdidoAdapter(context, listAnuncio);
            gridPerdido.setAdapter(pa);
        }

    }

    private class GetFiltered extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient cliente = HttpClientBuilder.create().build();
            HttpGet chamada =
                    new HttpGet(RegistroActivity.ENDERECO_WEB +
                            "/adotapet-servidor/api/anuncio/get-perdidos-filtered?tipo="+params[0]+"&porte="
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

            if (s != null) {

                try {
                    JSONArray array = new JSONArray(s);

                    listAnuncio.clear();

                    final int numberIterator = array.length();
                    for (int i = 0; i < numberIterator; i++) {
                        JSONObject obj = array.getJSONObject(i);

                        AnuncioPerdido ap = new AnuncioPerdido();

                        JSONArray imgs = obj.getJSONArray("imgAnuncio");

                        List<String> list = new ArrayList<String>();
                        if (imgs.length() > 0) {
                            for (int j = 0; j < imgs.length(); j++) {
                                list.add(imgs.get(j).toString());
                            }
                        }

                        ap.setId(obj.getLong("id"));
                        ap.setImgAnucio(list);
                        ap.setRaca(obj.getString("raca"));
                        ap.setNome(obj.getString("nome"));
                        ap.setCor(obj.getString("cor"));
                        ap.setDescricao(obj.getString("descricao"));
                        ap.setSexo(obj.getString("sexo"));
                        ap.setTipo(obj.getString("tipo"));
                        ap.setPorte(obj.getString("porte"));

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
                        ap.setUsuario(usuario);

                        Date date = new Date(obj.getLong("dataPublicacao"));

                        ap.setDataPublicacao(date);

                        listAnuncio.add(ap);
                        PerdidoAdapter pa = new PerdidoAdapter(context, listAnuncio);
                        gridPerdido.setAdapter(pa);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getPerdidosFilter(String tipo, String porte, String sexo) {

        mProgressDialog.show();
        new GetFiltered().execute(tipo, porte, sexo);

    }
}
