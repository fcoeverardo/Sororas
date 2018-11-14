package com.lek.sororas.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.R;

import java.util.ArrayList;

public class FragmentHome extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    MainActivity main;
//    FirebaseDatabase database;
//    DatabaseReference myRef;
    ArrayList<Anuncio> anuncios;
    ArrayList<String> anunciosIds;
    ProgressBar progress;
    TextView searchbar;

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
            view = inflater.inflate(R.layout.fragment_home, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();
        main = (MainActivity) context;

//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();

        anuncios = new ArrayList<>();
        anunciosIds = new ArrayList<>();
        main.blankLayout.setVisibility(View.GONE);

        progress = view.findViewById(R.id.progress);

        mRecyclerView =  view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(context,2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);

        progress.setVisibility(View.VISIBLE);
        loadAnuncios();

        searchbar = view.findViewById(R.id.searchbar);
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickSearch();
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();


    }

    public void loadAnuncios(){

//        myRef.child("advertisement").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                anuncios.clear();
//                anunciosIds.clear();
//
//                if(dataSnapshot.hasChildren()){
//
//                    for(DataSnapshot anuncioSnapshot : dataSnapshot.getChildren()){
//
//                        Anuncio anuncio = anuncioSnapshot.getValue(Anuncio.class);
//                        anuncios.add(anuncio);
//
//                        anunciosIds.add(anuncioSnapshot.getKey());
//
//                    }
//                }
//
//                Collections.reverse(anuncios);
//                Collections.reverse(anunciosIds);
//                main.anunciosIds = anunciosIds;
//
//                AnuncioRecyclerView adapter = new AnuncioRecyclerView(context,anuncios,anunciosIds,progress);
//                mRecyclerView.setAdapter(adapter);
//
//                progress.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }


    public void clickSearch(){

//        Intent i = new Intent(context, SearchActivity.class);
//        context.startActivity(i);
    }

}