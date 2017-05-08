package com.example.amand.projetointegrador.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.amand.projetointegrador.R;
import com.example.amand.projetointegrador.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amanda on 03/05/17.
 */

public class CustomPagerAdapter extends PagerAdapter {


    Context mContext;
    LayoutInflater mLayoutInflater;
    List<Bitmap> img = new ArrayList<>();
    Session s;


    public CustomPagerAdapter(Context context, List<Bitmap> imgs) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        s = new Session(mContext);
        img = imgs;
    }

    @Override
    public int getCount() {
        return img.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Long id = s.getUserPrefs();
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imgView = (ImageView) itemView.findViewById(R.id.imageView);
        imgView.setImageBitmap(img.get(position));
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
