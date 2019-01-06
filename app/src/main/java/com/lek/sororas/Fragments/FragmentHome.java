package com.lek.sororas.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lek.sororas.Adapters.AnuncioRecyclerView;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Models.User;
import com.lek.sororas.R;
import com.lek.sororas.SearchActivity;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentHome extends BasicFragment {


    ArrayList<Anuncio> anuncios;
    ArrayList<String> anunciosIds;
    ProgressBar progress;
    TextView searchbar;

    User currentProprietaria;

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

        initFirebase();

        context = inflater.getContext();
        main = (MainActivity) context;

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

       CollectionReference docRef = db.collection("advertisement");
       docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d("loadingAnuncio", document.getId() + " => " + document.getData());

                        Anuncio anuncio = document.toObject(Anuncio.class);
                        anuncios.add(anuncio);
                        anuncio.id = document.getId();

//                        DocumentReference ref = anuncio.getProprietaria();
//                        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                currentProprietaria = documentSnapshot.toObject(User.class);
//                                Log.d("loadingAnuncio", "Error getting documents: ");
//                            }
//                        });


                    }


                    Collections.reverse(anuncios);
                    Collections.reverse(anunciosIds);
                    main.anunciosIds = anunciosIds;

                    AnuncioRecyclerView adapter = new AnuncioRecyclerView(context,anuncios,anunciosIds,progress);
                    //AnuncioRecyclerView adapter = new AnuncioRecyclerView(context,anuncios,anunciosIds,progress);
                    mRecyclerView.setAdapter(adapter);

                    //progress.setVisibility(View.GONE);


                } else {
                    Log.d("loadingAnuncio", "Error getting documents: ", task.getException());
                }
            }
        });

    }


    public void clickSearch(){

        Intent i = new Intent(context, SearchActivity.class);
        context.startActivity(i);
    }

}
