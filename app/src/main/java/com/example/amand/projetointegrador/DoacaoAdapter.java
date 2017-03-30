package com.example.amand.projetointegrador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.TextView;

/**
 * Created by amanda on 29/03/17.
 */

public class DoacaoAdapter extends BaseAdapter {

    GridView grid;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            //R.drawable.image
    };
    public String[] nomeStrings;

    private TextView nomeAnimal;

    private ImageSwitcher imageSwitcher;

    private Context mContext;

    // Constructor
    public DoacaoAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.activity_doacao_detalhes, null);

            nomeAnimal = (TextView) grid.findViewById(R.id.nomeAnimal);
            nomeAnimal.setText(nomeStrings[position]);

            imageSwitcher = (ImageSwitcher) grid.findViewById(R.id.imgSliderDoacao);
            imageSwitcher.setImageResource(mThumbIds[position]);

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
