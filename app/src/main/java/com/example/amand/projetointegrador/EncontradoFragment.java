package com.example.amand.projetointegrador;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roughike.bottombar.BottomBar;


//FRAGMENTO DE MOSTRA DE ANUNCIOS DE ANIMAIS ENCONTRADOS COMO GRIDVIEW

public class EncontradoFragment extends Fragment {
    private BottomBar bottomBar;

    private OnFragmentInteractionListener mListener;

    public EncontradoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EncontradoFragment newInstance(String param1, String param2) {
        EncontradoFragment fragment = new EncontradoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isVisible()) {
            bottomBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_encontrado, container, false);

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
