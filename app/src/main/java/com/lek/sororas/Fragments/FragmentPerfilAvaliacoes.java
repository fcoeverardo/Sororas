package com.lek.sororas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Evaluation;
import com.lek.sororas.Models.UserEvaluation;
import com.lek.sororas.R;

import java.util.ArrayList;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FragmentPerfilAvaliacoes extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    MainActivity main;

    UserEvaluation userEvaluation;
//    FirebaseDatabase database;
//    DatabaseReference myRef;

    MaterialRatingBar materialRatingBar;

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
            view = inflater.inflate(R.layout.layout_avaliacoes, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (MainActivity) context;

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
