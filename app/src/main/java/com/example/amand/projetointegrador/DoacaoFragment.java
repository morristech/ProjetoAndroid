package com.example.amand.projetointegrador;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

//FRAGMENTO DE MOSTRA DE ANUNCIOS DE ANIMAIS PARA DOAÇÃO COMO GRIDVIEW


public class DoacaoFragment extends Fragment {

    private GridView gridDoacao;
    Context context;

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
        View view =  inflater.inflate(R.layout.fragment_doacao, container, false);

        context = this.getActivity().getApplicationContext();
        gridDoacao = (GridView) view.findViewById(R.id.gridDoacao);
        gridDoacao.setAdapter(new DoacaoAdapter(context));

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
}
