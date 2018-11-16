package com.lek.sororas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lek.sororas.CreateActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.R;

public class FragmentCreateTitle extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    CreateActivity main;
    EditText title;
    Button advanceBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.create_title, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (CreateActivity) context;

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.previusPage(v);
            }
        });

        title = view.findViewById(R.id.title);

        advanceBtn = view.findViewById(R.id.advanceBtn);
        advanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!title.getText().toString().equals("")){
                    main.anuncio.setTitulo(title.getText().toString());
                    main.nextPage(v);
                }

                else{
                    Toast.makeText(context,"Você deve escrover um título",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}