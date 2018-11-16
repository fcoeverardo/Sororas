package com.lek.sororas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lek.sororas.CreateActivity;
import com.lek.sororas.R;

public class FragmentCreateType extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    CreateActivity main;
    FrameLayout servicos,produtos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.create_type, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (CreateActivity) context;

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        //main.setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.previusPage(v);
            }
        });

        servicos = view.findViewById(R.id.type_servicos);
        produtos = view.findViewById(R.id.type_produtos);

        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.anuncio.setTipo((String)v.getTag());
            }
        };

        //findViews(view);


        return view;
    }

}