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
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.lek.sororas.Adapters.AnuncioRecyclerView;
import com.lek.sororas.Adapters.TrabalhoRecyclerView;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.R;

import java.util.ArrayList;

public class FragmentPerfilTrabalhos extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    MainActivity main;
//    FirebaseDatabase database;
//    DatabaseReference myRef;
    ArrayList<Anuncio> trabalhos;
    ArrayList<String> anuncioIds;

    ProgressBar progress;

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

        main.blankLayout.setVisibility(View.GONE);

        trabalhos = new ArrayList<>();
        anuncioIds = new ArrayList<>();

//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();

        mRecyclerView =  view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(context,2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);

        progress = view.findViewById(R.id.progress);

        loadTrablhos();

        return view;
    }


    public void loadTrablhos(){


        CollectionReference advertisement = main.db.collection("advertisement");

        Query query = advertisement.whereEqualTo("proprietaria",main.db.collection("users")
                .document(main.mAuth.getCurrentUser().getUid()));

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot document : queryDocumentSnapshots) {

                    Anuncio anuncio = document.toObject(Anuncio.class);
                    trabalhos.add(anuncio);

                    AnuncioRecyclerView adapter = new AnuncioRecyclerView(context,trabalhos,anuncioIds,progress);
                    mRecyclerView.setAdapter(adapter);

                    System.out.println(document.getId());
                }

            }
        });
//        myRef.child("advertisement").orderByChild("proprietaria").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                       trabalhos.clear();
//                       anuncioIds.clear();
//                        if(dataSnapshot.hasChildren()){
//
//                            for(DataSnapshot anuncioSnapshot : dataSnapshot.getChildren()){
//
//                                Anuncio trabalho = anuncioSnapshot.getValue(Anuncio.class);
//                                trabalhos.add(trabalho);
//                                anuncioIds.add(anuncioSnapshot.getKey());
//
//                            }
//
//                            TrabalhoRecyclerView adapter = new TrabalhoRecyclerView(context,trabalhos,anuncioIds);
//                            mRecyclerView.setAdapter(adapter);
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

    }
}
