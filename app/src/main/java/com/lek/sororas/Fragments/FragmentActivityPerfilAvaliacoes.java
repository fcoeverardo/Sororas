package com.lek.sororas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lek.sororas.Adapters.TrabalhoRecyclerView;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Models.Evaluation;
import com.lek.sororas.PerfilActivity;
import com.lek.sororas.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentActivityPerfilAvaliacoes extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    PerfilActivity main;

    ArrayList<Evaluation> evaluations;
//    FirebaseDatabase database;
//    DatabaseReference myRef;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.layout_perfilavaliacoes, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (PerfilActivity) context;

        //main.blankLayout.setVisibility(View.GONE);

        evaluations = new ArrayList<>();

//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();

        mRecyclerView =  view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);


        return view;
    }

    public void setEvaluations(ArrayList<Evaluation> avaliacoes){

//        AvalicaoesRecyclerView adapter = new AvalicaoesRecyclerView(context,avaliacoes);
//        mRecyclerView.setAdapter(adapter);

    }
}
