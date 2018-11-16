package com.lek.sororas.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lek.sororas.CreateActivity;
import com.lek.sororas.R;

public class FragmentCreateCategoty extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    CreateActivity main;
    Button advanceBtn;
    ListView listView;

    String[] categorias;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.create_category, container, false);
        } catch (InflateException e) {

        }

        categorias = getResources().getStringArray(R.array.categorias);

        context = inflater.getContext();

        main = (CreateActivity) context;

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.previusPage(v);
            }
        });

        advanceBtn = view.findViewById(R.id.advanceBtn);
        advanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.nextPage(v);
                main.fragmentCreateTags.setTags();

            }
        });

        listView = view.findViewById(R.id.listView);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(main,
                android.R.layout.simple_list_item_1, categorias){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);

                Typeface face = Typeface.createFromAsset(main.getAssets(), "roboto_light.ttf");
                text.setTypeface(face);

                return view;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for(int i = 0; i< categorias.length;i++)
                    listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));

                view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                main.anuncio.setCategoria(categorias[position]);
            }
        });


        return view;
    }

}