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
import android.widget.TextView;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lek.sororas.Adapters.AnuncioRecyclerView;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Models.User;
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
    int countFavorites = 0;

    UserEvaluation userEvaluation;
//    FirebaseDatabase database;
//    DatabaseReference myRef;

    MaterialRatingBar materialRatingBar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ProgressBar progress;

    TextView count;


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

        progress = view.findViewById(R.id.progress);

        count = view.findViewById(R.id.count);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        count.setText("0 Favoritos");
        loadFavoritos();

    }


    public void loadFavoritos(){

        final User user = CurrentUser.getUser();
        favoritos.clear();

        if(user.getFavoritosIds()!=null){
            for(String favoriteId : user.getFavoritosIds()){

                DocumentReference docRef = main.db.collection("advertisement").document(favoriteId);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        Anuncio anuncio = task.getResult().toObject(Anuncio.class);
                        countFavorites++;

                        favoritos.add(anuncio);
                        if(countFavorites == user.getFavoritosIds().size()){

                            AnuncioRecyclerView adapter = new AnuncioRecyclerView(context,favoritos,anunciosIds,progress);
                            //AnuncioRecyclerView adapter = new AnuncioRecyclerView(context,anuncios,anunciosIds,progress);
                            mRecyclerView.setAdapter(adapter);

                            count.setText(countFavorites + " Favoritos");
                        }
                    }
                });
            }
        }
        else{
            AnuncioRecyclerView adapter = new AnuncioRecyclerView(context,favoritos,anunciosIds,progress);
            //AnuncioRecyclerView adapter = new AnuncioRecyclerView(context,anuncios,anunciosIds,progress);
            mRecyclerView.setAdapter(adapter);

            count.setText(countFavorites + " Favoritos");
        }
    }

}
