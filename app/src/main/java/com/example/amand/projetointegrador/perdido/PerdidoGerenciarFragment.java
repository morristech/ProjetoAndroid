package com.example.amand.projetointegrador.perdido;

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
import android.widget.ListView;

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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PerdidoGerenciarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PerdidoGerenciarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerdidoGerenciarFragment extends Fragment {
    private ListView listPerdidos;
    Context context;
    private Session session;
    final List<AnuncioPerdido> listAnuncio = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;


    private PerdidoFragment.OnFragmentInteractionListener mListener;

    public PerdidoGerenciarFragment() {
        // Required empty public constructor
    }

    public static PerdidoGerenciarFragment newInstance(String param1, String param2) {
        PerdidoGerenciarFragment fragment = new PerdidoGerenciarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getApplicationContext();
        session = new Session(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perdido_gerenciar, container, false);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

        listPerdidos = (ListView) view.findViewById(R.id.listGerenciaPerdido);
        GetService get = new GetService();
        get.execute();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GetService get2 = new GetService();
                get2.execute();
            }
        });


        System.out.println(session.getToken() + "*******************");

        listPerdidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(context, PerdidoDetalhesActivity.class);
                // Pass image index
                AnuncioPerdido ap = (AnuncioPerdido) listPerdidos.getAdapter().getItem(position);
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
                webAdd = RegistroActivity.ENDERECO_WEB + "/adotapet-servidor/api/anuncio/get-perdidos-usuario/"+session.getUserPrefs();

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
                        PerdidoGerenciarAdapter pa = new PerdidoGerenciarAdapter(context, listAnuncio, getActivity());
                        listPerdidos.setAdapter(pa);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
