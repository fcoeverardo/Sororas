package com.lek.sororas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lek.sororas.Adapters.AvalicaoesRecyclerView;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Evaluation;
import com.lek.sororas.Models.User;
import com.lek.sororas.Models.UserEvaluation;
import com.lek.sororas.R;
import com.lek.sororas.Utils.CurrentUser;

import java.util.ArrayList;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FragmentPerfilAvaliacoes extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    MainActivity main;

    UserEvaluation userEvaluation;

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

        mRecyclerView =  view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);


        loadEvaluations();

        return view;
    }


    public void setEvaluations(ArrayList<Evaluation> avaliacoes){

        AvalicaoesRecyclerView adapter = new AvalicaoesRecyclerView(context,avaliacoes);
        mRecyclerView.setAdapter(adapter);

    }

    public void loadEvaluations(){

        DocumentReference docRef = main.db.collection("evaluations").document(main.mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        userEvaluation = document.toObject(UserEvaluation.class);
                        setEvaluations(userEvaluation.getAvalicaoes());


                    }

                } else {

                    Log.d("getUser", "get failed with ", task.getException());
                }
            }
        });
    }

}
