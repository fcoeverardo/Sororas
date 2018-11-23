package com.lek.sororas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.lek.sororas.Adapters.AnuncioRecyclerView;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Models.UserEvaluation;
import com.lek.sororas.R;
import com.lek.sororas.Utils.CurrentUser;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FragmentPerfilFavoritos extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    MainActivity main;

    ArrayList<Anuncio> favoritos;
    ArrayList<String> anunciosIds;

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
            view = inflater.inflate(R.layout.layout_trabalhos, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (MainActivity) context;

        favoritos = new ArrayList<>();
        //anunciosIds = CurrentUser.getUser().getFavoritos();


//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();

        mRecyclerView =  view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(context,2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        loadFavoritos();

    }


    public void loadFavoritos(){

//        myRef.child("advertisement").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                favoritos.clear();
//                anunciosIds = CurrentUser.getUser().getFavoritos();
//
//                if(dataSnapshot.hasChildren()){
//
//                    for(DataSnapshot anuncioSnapshot : dataSnapshot.getChildren()){
//
//                        if(anunciosIds.contains(anuncioSnapshot.getKey())){
//                            Anuncio anuncio = anuncioSnapshot.getValue(Anuncio.class);
//                            favoritos.add(anuncio);
//                        }
//
//                    }
//                }
//
//                AnuncioRecyclerView adapter = new AnuncioRecyclerView(context,favoritos,anunciosIds);
//                mRecyclerView.setAdapter(adapter);
//
//                main.anunciosIds = anunciosIds;
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

}
